package RestAssuredProject.Assignments;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class Test_Create_New_Pet{
	JSONParser parser;
	Object obj;
	FileInputStream fis;
	Properties prop;
	JSONObject payload;
	RequestSpecification requestSpec;
	RequestSpecBuilder requestSpecBuild;
	
	@BeforeClass
	  public void SetUp() throws IOException, ParseException{
		fis = new FileInputStream("C://Users//mumar//workspace//Assignments//Data_Files//Pets_Constants.properties");
	    prop = new Properties();
	    prop.load(fis);
	    
	    parser = new JSONParser();
		obj = parser.parse(new FileReader(prop.getProperty("JSONPayload_FilePath")));
		payload = (JSONObject) obj;
		
	    requestSpecBuild = new RequestSpecBuilder();
		requestSpecBuild.setBaseUri (prop.getProperty("BaseUrl"));	    
	    requestSpec = requestSpecBuild.build();
	  }
	
	//Create new Pet using JSON Payload
	@Test
	public void Create_New_PET() throws IOException, ParseException{		
		Response response = RestAssured.given()
			.spec(requestSpec)
			.header("Content-Type", "application/json")
			.body(payload)
		.when()
			.post(prop.getProperty("EndPoint"))
		.then()
			.contentType(ContentType.JSON)
            .extract().response();
		
		//Extract Status code
		int status_code = response.statusCode();
		System.out.println("Status code of Create Pet request is : " + status_code);
		
		//Verify Status code
		Assertions.assertThat(status_code).isEqualTo(200);
		
		//Response Body
		String response_Body = response.asString();
		System.out.println("Response of Post request is :  " + response_Body);
		
		//Extract Pet Category, Pet Name, Pet Status
		String pet_category_name = response.jsonPath().getString("category.name");
		String pet_name = response.jsonPath().getString("name");
		String pet_status = response.jsonPath().getString("status");
		
		//Verify Pet Category, Pet Name, Pet Status with expected values stored in Properties
		Assertions.assertThat(pet_category_name).isEqualTo(prop.getProperty("NewPetCategoryName"));
		Assertions.assertThat(pet_name).isEqualTo(prop.getProperty("NewPetName"));
		Assertions.assertThat(pet_status).isEqualTo(prop.getProperty("NewPetStatus"));		
	}
}