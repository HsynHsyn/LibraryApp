package com.library.step_definitions;

import com.library.pages.SignInPage;
import com.library.pages.TopNavigationBar;
import com.library.utilities.BrowserUtils;
import com.library.utilities.ConfigurationReader;
import com.library.utilities.DB_Util;
import com.library.utilities.LibUtils;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;


import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.assertj.core.api.SoftAssertions;


public class LibraryApp_StepDefs{


    RequestSpecification givenPart = RestAssured.given().log().all();
    Response response;
    JsonPath jp;
    ValidatableResponse thenPart;
    private String pathParam;
    Map<String,Object> randomDataMap;
    SignInPage signInPage = new SignInPage();
    TopNavigationBar topNavigationBar= new TopNavigationBar();
    String token;

    SoftAssertions softly = new SoftAssertions();
    //-----------------------US-1----------------------------------

    @Given("I logged Library api as a {string}")
    public void i_logged_in_bookit_api_as_a(String role) {
        givenPart.header("x-library-token", LibUtils.generateTokenByRole(role));

    }
    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);

    }
    @When("I send GET request to {string} endpoint")
    public void iSendGETRequestToEndpoint(String endpoint) {
        response = givenPart.when().get(endpoint);
        jp = response.jsonPath();
        thenPart= response.then();
        response.prettyPrint();
    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {

        thenPart.statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);
        //soft assertion
        softly.assertThat(thenPart.contentType(contentType));

    }
    @Then("Each {string} field should not be null")
    public void field_should_not_be_null(String path) {
        List<Object> idList = jp.getList(path);
        for (Object eachId : idList) {
            //Assert.assertFalse(eachId == null);
            Assert.assertNotNull(eachId);
            //soft assertion
            softly.assertThat(eachId).isNotEqualTo(null);
        }
    }

    @And("{string} field should not be null")
    public void fieldShouldNotBeNull(String path) {
        thenPart.body(path,is(notNullValue()));
        System.out.println("path = " + path);
    }
    //-----------------------US-2----------------------------------
    @Given("Path param is {string}")
    public void path_param_is(String pathParam) {
       this.pathParam = pathParam;
        givenPart.pathParam("id", pathParam);
    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String actualID) {
        String expectedID =pathParam;
        System.out.println("expectedID = " + expectedID);

        actualID = jp.getString("id");
        Assert.assertEquals(expectedID,actualID);
        //soft assertion
        softly.assertThat(expectedID).isEqualTo(actualID);
    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fields) {
        //option1
        for (String eachFields : fields) {
            Assert.assertNotNull(eachFields);
        }
        //option2
        assertThat(fields,everyItem(notNullValue()));
    }
   //-----------------------US-3----------------------------------
    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentType) {
        givenPart.contentType(contentType);
    }
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String randomDataType) {
        switch (randomDataType){
            case "book":
                randomDataMap = LibUtils.createRandomBook();
                break;
            case "user":
                randomDataMap = LibUtils.createRandomUser();
                break;
            default:
                throw new RuntimeException("Wrong data type is provide");
        }
        System.out.println("randomDataMap = " + randomDataMap);
        givenPart.formParams(randomDataMap);
    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response = givenPart.when().post(endpoint);
        jp = response.jsonPath();
        thenPart = response.then();

    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String messageField, String expectedValue) {
        String actualValue = jp.getString(messageField);
        Assert.assertEquals(expectedValue,actualValue);
    }
    //-----------------------US-3-1----------------------------------


    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String role) {
        signInPage.login(role);
    }

    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String page) {
        topNavigationBar.booksButton.click();
        BrowserUtils.waitFor(2);
    }
    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {
        //GET DATA FROM API
        String expectedAPI = jp.getString("book_id");
        System.out.println("expectedAPI = " + expectedAPI);
        /*//option-1 to get the query
        /*GET DATA FROM DATABASE
         String query = "SELECT isbn FROM books WHERE isbn = '" + expectedAPI + "'";
        DB_Util.runQuery(query);
        String actualDB = DB_Util.getFirstRowFirstColumn();
        System.out.println("actualDB = " + actualDB);*///option-1
        // Write a query
        String query = "SELECT * FROM books WHERE id = '" + expectedAPI + "'";
        DB_Util.runQuery(query);
        //Get the DB one row info
        Map<String, String > dataMap = DB_Util.getRowMap(1);
        System.out.println("dataMap = " + dataMap);
        //Assertion for API and DB
        Assert.assertEquals(expectedAPI,dataMap.get("id"));
        //GET DATA FROM UI
        //filter by using book name
        topNavigationBar.searchButton.sendKeys((String) randomDataMap.get("name"));
        BrowserUtils.waitFor(2);
        //Get the isbn value` text
        String value = (String) randomDataMap.get("isbn");
        String actualUI = BrowserUtils.tableDynamicElementFinder(value);;

       //Assertion for DB and UI
        Assert.assertEquals(dataMap.get("isbn"),actualUI);
    }
    //-----------------------US-4----------------------------------
    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
        //GET DATA FROM API
        String user_id = jp.getString("user_id");
        DB_Util.runQuery(ConfigurationReader.getProperty("query") + "'" + user_id + "'"+";");

        //Get the DB one row info as a MAP
        Map<String, String> dataMap = DB_Util.getRowMap(1);
        Assert.assertEquals(randomDataMap.get("email"), dataMap.get("email"));

        LibUtils.AssertAllDBElement(randomDataMap,dataMap);

    }
    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {
        //Get credential from API randomDataMap
        String username = (String) randomDataMap.get("email");
        String password = (String) randomDataMap.get("password");
        //Created user login
        signInPage.login(username,password);
        //Assertion for checking
        BrowserUtils.verifyElementDisplayed(topNavigationBar.userProfileName);
    }
    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {
        //Navigate to Users page
        topNavigationBar.usersButton.click();
        BrowserUtils.waitFor(2);
        //Send the name to search button to filter
        topNavigationBar.searchButton.sendKeys(topNavigationBar.usersEmail.getText());
        BrowserUtils.waitFor(2);
        //Assertion for displaying name
        BrowserUtils.verifyElementDisplayed(topNavigationBar.usersFullName);

    }
  //-----------------------US-5----------------------------------------------------
   @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {
        signInPage.login(email, password);
        //get the token
       token = LibUtils.getToken(email, password);
       System.out.println("token = " + token);

   }
    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {
        //Convert JWT token to object data
        // Token sends to request body
        givenPart.formParams("token", token);

    }
   }





