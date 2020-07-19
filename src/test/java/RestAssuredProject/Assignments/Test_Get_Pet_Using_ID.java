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


public class Test_Get_Pet_Using_ID {
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
	public void get_pet_by_id() throws IOException{	
		Response response = RestAssured.given()
			.spec(requestSpec)
			.header("Content-Type", "application/json")
		.when()
			.get(prop.getProperty("EndPoint") +"/"+ prop.getProperty("PetID"))
		.then()
			.contentType(ContentType.JSON)
			.extract().response();
		
		//Extract and Print status code
		int status_code = response.statusCode();
		System.out.println("Status Code of Get PET using ID is : " + status_code);
		
		//Verify Status Code
		Assertions.assertThat(status_code).isEqualTo(200);
		
		//Response Body
		String response_Body = response.asString();
		System.out.println("Response of Post request is :  " + response_Body);
		
		//Extract Pet Category, Pet Name, Pet Status
		String pet_category_name = response.jsonPath().getString("category.name");
		String pet_name = response.jsonPath().getString("name");
		String pet_status = response.jsonPath().getString("status");
		
		//Verify Pet Category, Pet Name, Pet Status
		Assertions.assertThat(pet_category_name).isEqualTo(prop.getProperty("PetCategoryName"));
		Assertions.assertThat(pet_name).isEqualTo(prop.getProperty("PetName"));
		Assertions.assertThat(pet_status).isEqualTo(prop.getProperty("PetStatus"));
	}
}
