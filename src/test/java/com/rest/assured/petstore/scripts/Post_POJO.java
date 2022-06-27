package com.rest.assured.petstore.scripts;

import static com.rest.assured.petstore.scripts.TestConstants.KEY_STATUS;
import static com.rest.assured.petstore.scripts.TestConstants.STATUS_CODE_OK;
import static com.rest.assured.petstore.scripts.TestConstants.STATUS_LINE_OK;
import static com.rest.assured.petstore.scripts.TestConstants.URI;
import static com.rest.assured.petstore.scripts.TestConstants.VALUE_NAME;
import static com.rest.assured.petstore.scripts.TestConstants.VALUE_STATUS_AVAILABLE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Post_POJO {

	@Test
	public void test_addNewPet() {
		
		JSONObject jsonObject = TestUtil.getJsonObject(0, VALUE_NAME, VALUE_STATUS_AVAILABLE);

		Response res = given()
				.when().contentType(ContentType.JSON).accept(ContentType.JSON).body(jsonObject.toString()).when()
				.post(URI)
				.then().statusCode(STATUS_CODE_OK).statusLine(STATUS_LINE_OK).body(KEY_STATUS, equalTo(VALUE_STATUS_AVAILABLE))
				.log().body().extract().response();
		
		
		assertEquals(STATUS_CODE_OK, res.getStatusCode());
		assertEquals(STATUS_LINE_OK, res.getStatusLine());

		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		assertEquals(VALUE_STATUS_AVAILABLE, jsonPath.get(KEY_STATUS));

	}
}
