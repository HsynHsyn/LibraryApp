package com.library.utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LibUtils {


    public static String getToken(String email,String password){

        JsonPath jp = RestAssured.given().log().uri()
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC) // Datatype that I am sending to API
                .formParam("email", email)
                .formParam("password", password)
                .when().post("/login")
                .then().statusCode(200)
                .extract().jsonPath();

        String token = jp.getString("token");

        return token;
    }

    public static String generateTokenByRole(String role) {

        Map<String, String> roleCredentials = returnCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        return getToken(email, password);

    }

    public static Map<String, String> returnCredentials(String role) {
        String email = "";
        String password = "";

        switch (role) {
            case "librarian":
                email = ConfigurationReader.getProperty("librarian_username") ;
                password = ConfigurationReader.getProperty("librarian_password") ;
                //email = System.getenv("LIBRARIAN_USERNAME");
                //password = System.getenv("LIBRARIAN_PASSWORD");
                break;

            case "student":
                email = ConfigurationReader.getProperty("student_username") ;
                password = ConfigurationReader.getProperty("student_password");
                //email = System.getenv("STUDENT_USERNAME");
                //password = System.getenv("STUDENT_PASSWORD");
                break;

            default:
                throw new RuntimeException("Invalid Role Entry :\n>> " + role + " <<");
        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return credentials;

    }

    public static Map<String, Object> createRandomBook() {

        Faker faker = new Faker();
        Map<String, Object> bookMap = new LinkedHashMap<>();
        String name = "hsyn "+faker.book().title();
        String isbn = "hsyn "+faker.number().digits(13);
        Integer year = faker.number().numberBetween(1900, 2024);
        String author= faker.book().author();
        Integer book_category_id = faker.number().numberBetween(1, 10);
        String description = faker.lorem().sentence();

        bookMap.put("name", name);
        bookMap.put("isbn", isbn);
        bookMap.put("year", year);
        bookMap.put("author", author);
        bookMap.put("book_category_id", book_category_id);
        bookMap.put("description", description);

        return bookMap;
    }




    public static Map<String, Object> createRandomTeam() {

        Faker faker = new Faker();
        Map<String, Object> teamMap = new LinkedHashMap<>();



        teamMap.put("campus-location", "VA");
        teamMap.put("batch-number", "36");
        teamMap.put("team-name", faker.team().name());

        // Fill the required fields

        return teamMap;
    }
}
