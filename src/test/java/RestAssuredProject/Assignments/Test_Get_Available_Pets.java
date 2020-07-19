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


public class Test_Get_Available_Pets {
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
	public void get_all_available_pets() throws IOException{	
		Response response = RestAssured.given()
			.spec(requestSpec)
			.queryParam("status", "available")
		.when()
			.get(prop.getProperty("EndPoint") + prop.getProperty("SearchByStatus"))
		.then()
			.contentType(ContentType.JSON).
            extract().response();
		
		//Extract Status code
		int status_code = response.statusCode();
		System.out.println("Status Code of Get available pets request is : " + status_code);		
		
		//Verify Status Code
		Assertions.assertThat(status_code).isEqualTo(200);
		
		//Response Body
		String response_Body = response.asString();
		System.out.println("Response of Post request is :  " + response_Body);
		
		//Extract and Print PET Status
		String pet_status = response.jsonPath().getString("status[0]");
		System.out.println("Status of First pet in a List is :  " + pet_status);
		
		Assertions.assertThat(pet_status).isEqualTo(prop.getProperty("PetStatus"));
	}
	
	
}
