package RestAssuredProject.Assignments;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.assertj.core.api.Assertions;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class Test_Delete_PET {
	
	FileInputStream fis;
	Properties prop;
	RequestSpecification requestSpec;
	RequestSpecBuilder requestSpecBuild;
	
	@BeforeClass
	  public void SetUp() throws IOException, ParseException{
		fis = new FileInputStream("C://Users//mumar//workspace//Assignments//Data_Files//Pets_Constants.properties");
	    prop = new Properties();
	    prop.load(fis);
		
	    requestSpecBuild = new RequestSpecBuilder();
		requestSpecBuild.setBaseUri (prop.getProperty("BaseUrl"));	    
	    requestSpec = requestSpecBuild.build();
	  }
	
	@Test
	public void Delete_PET() throws IOException, ParseException{		
		Response response = RestAssured.given()
			.spec(requestSpec)
			.header("Content-Type", "application/json")
		.when()
			.delete(prop.getProperty("EndPoint") + "/" + prop.getProperty("DeletePETID"))
		.then()
			.contentType(ContentType.JSON)
            .extract().response();
		
		//Status Code
		int status_code = response.statusCode();
		System.out.println("Status code of DELETE pet Request is : " + status_code);
		
		//Verify Status code
		Assertions.assertThat(status_code).isEqualTo(200);
		
		//Response Body data
		String response_Body = response.asString();
		System.out.println("Response of DELETE request is :  " + response_Body);		
	}
}
