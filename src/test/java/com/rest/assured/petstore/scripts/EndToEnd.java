package com.rest.assured.petstore.scripts;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static com.rest.assured.petstore.scripts.TestConstants.*;

import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EndToEnd {

	@Test(priority=1)
	public void test_status_available() {

		Response res = given().queryParam(KEY_STATUS, VALUE_STATUS_AVAILABLE).when().accept(ContentType.JSON).get(URI + "/findByStatus");
		
		assertEquals(STATUS_CODE_OK, res.getStatusCode());
		assertEquals(STATUS_LINE_OK, res.getStatusLine());
		
		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		List<String> statuses = jsonPath.getList(KEY_STATUS);
		for (String status: statuses) {
			assertEquals(VALUE_STATUS_AVAILABLE, status);
		}
	}

	@Test(priority=2)
	public void test_status_pending() {

		Response res = given().queryParam(KEY_STATUS, VALUE_STATUS_PENDING).when().accept(ContentType.JSON).get(URI + "/findByStatus");
		
		assertEquals(STATUS_CODE_OK, res.getStatusCode());
		assertEquals(STATUS_LINE_OK, res.getStatusLine());
		
		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		List<String> statuses = jsonPath.getList(KEY_STATUS);
		for (String status: statuses) {
			assertEquals(VALUE_STATUS_PENDING, status);
		}
	}

	@Test(priority=3)
	public void test_status_sold() {

		Response res = given().queryParam(KEY_STATUS, VALUE_STATUS_SOLD).when().accept(ContentType.JSON).get(URI + "/findByStatus");
		
		assertEquals(STATUS_CODE_OK, res.getStatusCode());
		assertEquals(STATUS_LINE_OK, res.getStatusLine());
		
		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		List<String> statuses = jsonPath.getList(KEY_STATUS);
		for (String status: statuses) {
			assertEquals(VALUE_STATUS_SOLD, status);
		}
	}
	
	@Test(priority=4)
	public void test_add() {

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
		assertEquals(VALUE_NAME, jsonPath.get(KEY_NAME));
		assertEquals(VALUE_STATUS_AVAILABLE, jsonPath.get(KEY_STATUS));
		
		id = jsonPath.get(KEY_ID);
		assertNotEquals(0, id, "Id should not be Zero");
	}
	
	@Test(priority=5)
	public void test_getById_after_add() throws InterruptedException
	{
		Thread.sleep(2000);
		
		Response res = given()
				.when().contentType(ContentType.JSON).accept(ContentType.JSON)
				.get(URI + "/" + id)
				.then().statusCode(STATUS_CODE_OK).statusLine(STATUS_LINE_OK)
				.log().body().extract().response();
		
		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		
		assertEquals(id, jsonPath.get(KEY_ID));
		assertEquals(VALUE_NAME, jsonPath.get(KEY_NAME));
	}
	
	@Test(priority=6)
	public void test_Update() throws InterruptedException
	{
		Thread.sleep(2000);
		
		JSONObject jsonObject = TestUtil.getJsonObject(id, VALUE_UPDATED_NAME, VALUE_STATUS_AVAILABLE);
		
		Response res = given()
				.when().contentType(ContentType.JSON).accept(ContentType.JSON).body(jsonObject.toString()).when()
				.put(URI)
				.then().statusCode(STATUS_CODE_OK).statusLine(STATUS_LINE_OK).body(KEY_STATUS, equalTo(VALUE_STATUS_AVAILABLE))
				.log().body().extract().response();
		
		
		assertEquals(STATUS_CODE_OK, res.getStatusCode());
		assertEquals(STATUS_LINE_OK, res.getStatusLine());

		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		assertEquals(VALUE_STATUS_AVAILABLE, jsonPath.get(KEY_STATUS));
		assertEquals(id, jsonPath.get(KEY_ID));
		assertEquals(VALUE_UPDATED_NAME, jsonPath.get(KEY_NAME));
	}
	
	@Test(priority=7)
	public void test_getById_after_update() throws InterruptedException
	{
		Thread.sleep(2000);
		
		Response res = given()
				.when().contentType(ContentType.JSON).accept(ContentType.JSON)
				.get(URI + "/" + id)
				.then().statusCode(STATUS_CODE_OK).statusLine(STATUS_LINE_OK)
				.log().body().extract().response();
		
		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		assertEquals(id, jsonPath.get(KEY_ID));
		assertEquals(VALUE_UPDATED_NAME, jsonPath.get(KEY_NAME));
	}
	
	@Test(priority=8)
	public void test_Delete() {
		Response res = given().when().contentType(ContentType.JSON).accept(ContentType.JSON)
		.delete(URI + "/" + id).then().statusCode(STATUS_CODE_OK)
		.statusLine(STATUS_LINE_OK).log().body().extract().response();
		
		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		assertEquals(STATUS_CODE_OK, (int)jsonPath.get(KEY_CODE));
		assertEquals("" + id, jsonPath.get(KEY_MESSAGE));

	}
	
	@Test(priority=9)
	public void test_getById_after_delete() throws InterruptedException
	{
		Thread.sleep(2000);
		
		Response res = given()
				.when().contentType(ContentType.JSON).accept(ContentType.JSON)
				.get(URI + "/" + id)
				.then().statusCode(STATUS_CODE_NOT_FOUND).statusLine(STATUS_LINE_NOT_FOUND)
				.log().body().extract().response();
		
		String jsonString = res.asString();
		JsonPath jsonPath = new JsonPath(jsonString);
		assertEquals(GET_ERROR_CODE, (int)jsonPath.get(KEY_CODE));
		assertEquals(GET_ERROR_MESSAGE, jsonPath.get(KEY_MESSAGE));
	}
}
