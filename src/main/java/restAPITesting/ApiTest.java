package restAPITesting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.testng.annotations.DataProvider;
//import org.junit.Test;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.assertion.Assertion;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;

import jxl.read.biff.BiffException;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException; 

public class ApiTest extends testUtils{
	
	@DataProvider(name ="TestData")
	public static String[][] datapr() throws BiffException, IOException {
		
		String[][] da = DataProviderclass.read();
		
		return da;
			
	}
	
	@Test(dataProvider = "TestData")
	public void apitest(String Base, String URI, String Template, String Input, String RecordType,
			String ExpectedResult) throws IOException {

		String Requesttemp = setRequestTemplateforJson(Template);

		HashMap<String, String> head = new HashMap<String, String>();

		head.put("Authorization", "Bearer L41uyhzxoig9roHjTyRHFvBT48K2GjCMjbVC");

		HashMap<String, Object> testdata = jSONtoMap(Input);

		String Request = SetValuestoTemplateJson(Requesttemp, testdata);

		System.out.println(Request);

		Response postresponse = callPostMethod(Base, URI, "json", head, Request);

		System.out.println("Post Response--\n" + postresponse.getBody().asString());

		String id = getData(postresponse, "result.id").toString();

		System.out.println("id--" + id);

		HashMap<String, Object> validateResponse = jSONtoMap(ExpectedResult);

		Response getresponse = callGetMethod(Base, URI + "/" + id, head);

		System.out.println("Get Response--\n" + getresponse.getBody().asString());

		
		for (String key : validateResponse.keySet()) {

			if (RecordType == "New") {
				assertTagValue(getresponse, "result.id", id);
				
				assertTagValue(getresponse, key, validateResponse.get(key).toString());
			} else {

				assertTagValue(postresponse, key, validateResponse.get(key).toString());

			}
		}

	}
	
}
