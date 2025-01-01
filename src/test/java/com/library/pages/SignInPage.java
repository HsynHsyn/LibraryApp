package com.library.pages;

import com.library.utilities.BrowserUtils;
import com.library.utilities.ConfigurationReader;
import com.library.utilities.Driver;
import com.library.utilities.LibUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.Map;

public class SignInPage {

    public SignInPage(){
        PageFactory.initElements(Driver.get(),this);
    }


    @FindBy(id = "inputEmail")
    public WebElement userNameField;

    @FindBy(id = "inputPassword")
    public WebElement passwordField;

    @FindBy(xpath = "//button[.='Sign in']" )
    public WebElement signInButton;


    public void login(String role) {

        // Get Credentials
        Map<String, String> roleCredentials = LibUtils.returnCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        // login
        login(email,password);

    }

    public void login(String email,String password) {

        userNameField.sendKeys(email);
        passwordField.sendKeys(password);
        BrowserUtils.waitFor(1);
        signInButton.click();

    }

}
