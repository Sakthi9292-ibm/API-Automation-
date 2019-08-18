package restAPITesting;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.junit.Assert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.*;

public class testUtils {
	
	/* 
	 * Call Get Method 
	 * Call Post Method 
	 * Create Request templeate for Json Request
	 * Set values to the rquest template before calling POST
	 * Find the size of an array in JSON
	 * Check tag present 
	 * Read the tag
	 * assert the tag value  
	 * assert the status code 
	 */ 

	public static Response callGetMethod(String baseURI, String path, HashMap<String, String> header) {
		Response res;
		RestAssured.baseURI = baseURI;
		if (header == null) {
			res = RestAssured.given().get(path);

		} else {

			res = RestAssured.given().when().headers(header).get(path);
		}

		return res;

	}

	public static Response callPostMethod(String baseURI, String path, String ct, HashMap<String, String> header,
			String Request) {

		RestAssured.baseURI = baseURI;

		String ctcase=null;
		if (ct!=null) {
			ctcase = ct.toLowerCase();
		}

		Response res;
		if (header == null) {
			if (ctcase!=null && ctcase.contains("json")) {
				
				res = RestAssured.given().body(Request).when().contentType(ContentType.JSON).post(path);

			} else if (ctcase!=null && ctcase.contains("xml")) {
				res = RestAssured.given().body(Request).when().contentType(ContentType.XML).post(path);

			} else {
				res = RestAssured.given().body(Request).when().post(path);

			}
		} else {
			if (ctcase!=null && ctcase.contains("json")) {
				System.out.println("Inside head not null and JSON");
				res = RestAssured.given().body(Request).when().contentType(ContentType.JSON).headers(header).post(path);
			} else if (ctcase!=null && ctcase.contains("xml")) {

				res = RestAssured.given().body(Request).when().contentType(ContentType.XML).headers(header).post(path);

			} else {

				res = RestAssured.given().body(Request).when().contentType(ContentType.TEXT).headers(header).post(path);

			}

		}

		return res;
	}

	public static String setRequestTemplateforJson(String filepath) throws IOException {

		BufferedReader bf = new BufferedReader(new FileReader(filepath));

		StringBuilder sb = new StringBuilder();

		String line = bf.readLine();

		while (line != null) {

			sb.append(line);
			sb.append(System.lineSeparator());
			line = bf.readLine();
		}

		String template = sb.toString();

		return template;

	}

	public static String SetValuetoTemplateJson(String Json, String path, String value) {

		DocumentContext doc = com.jayway.jsonpath.JsonPath.parse(Json).set("$." + path, value);
		
		//System.out.println();
		//String newJson = new Gson().toJson(doc.read("$"));

		return doc.jsonString();
	}
	
	public static String SetValuestoTemplateJson(String Json, HashMap<String, Object> values) {

		Set<String> keys = values.keySet();

		String jsonstring = Json;
		for (String string : keys) {

			DocumentContext doc = com.jayway.jsonpath.JsonPath.parse(jsonstring).set("$." + string, values.get(string));

			jsonstring = doc.jsonString();

		}

		return jsonstring;
	}
	
	
	

	public static Integer findSize(Response res, String path) {

		int i = 0;

		i = res.getBody().jsonPath().getList(path).size();

		return i;

	}

	public static void isTagPresent(Response response, String path) {

		Assert.assertNotNull(response.getBody().jsonPath().get(path));
		System.out.println("Assert for " + path + " --->" + response.getBody().jsonPath().get(path));

	}

	public static Object getData(Response res, String path) {

		Object data = res.getBody().jsonPath().get(path);

		return data;
	}

	public static void assertTagValue(Response res, String path, Object expected) {

		assertEquals(expected.toString(), res.getBody().jsonPath().get(path).toString());

	}

	public static void isTagPresent(Response response, String path, String path2) {

		int j = response.getBody().jsonPath().getList(path).size();

		String pa = path + "." + path2;

		assertEquals(response.getBody().jsonPath().getList(pa).size(), j);
		System.out.println("lenght of path - " + path + " is " + j + " and " + path2 + " is present "
				+ response.getBody().jsonPath().getList(pa).size() + " times");

	}
	
	public static HashMap<String,Object> jSONtoMap(String json){
		
		
		HashMap<String,Object> map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>(){}.getType());
		
		return map ;
	}
	

	
	public static void assertStatus(Response res,Object code) {
		
		
		assertEquals(Integer.parseInt((String) code), res.getStatusCode());
		
		
	}
}
