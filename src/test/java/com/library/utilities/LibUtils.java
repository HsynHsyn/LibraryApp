package com.library.utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Calendar;
import org.junit.Assert;

public class LibUtils {


    public static String getToken(String email, String password) {

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
                email = ConfigurationReader.getProperty("librarian_username");
                password = ConfigurationReader.getProperty("librarian_password");
                //email = System.getenv("LIBRARIAN_USERNAME");
                //password = System.getenv("LIBRARIAN_PASSWORD");
                break;

            case "student":
                email = ConfigurationReader.getProperty("student_username");
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
        String name = "hsyn" + faker.book().title();
        String isbn = "hsyn" + faker.number().digits(13);
        Integer year = faker.number().numberBetween(1900, 2024);
        String author = faker.book().author();
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


    public static Map<String, Object> createRandomUser() {

        // Create a Faker instance
        Faker faker = new Faker();
        // Create a LinkedHashMap
        Map<String, Object> userMap = new LinkedHashMap<>();

        // Generate dynamic dates as strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Create a random start date as a string (future date within 30 days)
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.add(Calendar.DAY_OF_YEAR, faker.number().numberBetween(1, 30));
        String startDate = dateFormat.format(startDateCalendar.getTime());

        // Create an end date as a string (1 year after the start date)
        Calendar endDateCalendar = (Calendar) startDateCalendar.clone();
        endDateCalendar.add(Calendar.YEAR, 1);
        String endDate = dateFormat.format(endDateCalendar.getTime());
        String password = faker.lorem().characters(10, true, true);

        int randomNumber = faker.number().numberBetween(1, 500);
        String email = "libraryuser" + randomNumber + "@library";

        // Dynamically populate the map using Faker
        userMap.put("full_name", faker.name().fullName()); // Full name
        userMap.put("email", email); // Email
        userMap.put("password", password); // Password as a UUID without hyphens
        userMap.put("user_group_id", faker.number().numberBetween(2, 3)); // Random user group ID
        userMap.put("status", "ACTIVE"); // Fixed status
        userMap.put("start_date", startDate); // Fixed start date
        userMap.put("end_date", endDate); // Fixed end date
        userMap.put("address", faker.address().fullAddress()); // Full address

        return userMap;
    }

    //Compare DB and API values
    public static void AssertAllDBElement(Map<String, Object> randomDataMap, Map<String, String> dataMap) {

        Assert.assertTrue("Keys are different", dataMap.keySet().equals(randomDataMap.keySet()));

        // Compare values for each key
        for (String key : dataMap.keySet()) {

            if (key.equalsIgnoreCase("password")) {
                continue;
            }

            // Get the expected value from the database map (String type)
            String expectedValue = dataMap.get(key);

            // Get the actual value from the API map (Object type)
            Object actualValueObj = randomDataMap.get(key);

            // Convert the actual value to a String if it's not null
            String actualValue = (actualValueObj != null) ? actualValueObj.toString() : null;


            // Compare the expected value and the actual value
            Assert.assertEquals("Values do not match for key: " + key, expectedValue, actualValue);
        }

    }
}