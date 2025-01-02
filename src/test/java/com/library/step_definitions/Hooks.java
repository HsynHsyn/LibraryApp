package com.library.step_definitions;

import com.library.utilities.ConfigurationReader;
import com.library.utilities.DB_Util;
import com.library.utilities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.time.Duration;

public class Hooks {



    @Before // The io.cucumber.java.Before annotation is specific to Cucumber and is triggered only for Cucumber scenarios. //Runs before every scenario defined in a Gherkin feature.
    public void setBaseURI(){
        System.out.println("-----Setting BaseURI");
        RestAssured.baseURI= ConfigurationReader.getProperty("library.baseUri");
    }

    @After
    public void endScenario(Scenario scenario){
        System.out.println("Test result for " + scenario.getName() + " " + scenario.getStatus());
    }

    @Before("@db")
    public void dbHook(){
        System.out.println("----- creating database connection");
        DB_Util.createConnection();
    }

    @After("@db")
    public void afterHook(){
        System.out.println("----- closing database connection");
        DB_Util.destroy();
    }

    @Before("@ui")
    public void setUp(){
        Driver.get().get(ConfigurationReader.getProperty("library_url"));
        Driver.get().manage().window().maximize();
        Driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @After("@ui")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png","screenshot");
        }
        Driver.closeDriver();
    }


}
