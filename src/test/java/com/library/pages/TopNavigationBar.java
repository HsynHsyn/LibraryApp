package com.library.pages;

import com.library.step_definitions.LibraryApp_StepDefs;
import com.library.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopNavigationBar {


    public TopNavigationBar(){
        PageFactory.initElements(Driver.get(),this);
    }


    //-----------------------Top Navigation Bar Locators-----------------------------//
    @FindBy(xpath = "//span[.='Test Librarian 10']")
    public WebElement userProfileButton;

    @FindBy(xpath = "//a[.='Log Out']")
    public WebElement logOutButton;

    //p[contains(.,'Thank you')]
    @FindBy(id= "navbarDropdown")
    public WebElement userProfileName;

// //a[id= 'navbarDropdown']

    //-----------------------DashBoard Page Locators-----------------------------//

    @FindBy(xpath = "//span[.='Dashboard']")
    public WebElement dashboardButton;

    @FindBy(id="user_count")
    public WebElement usersNumber;

    @FindBy(id="book_count")
    public WebElement bookNumber;

    @FindBy(id="borrowed_books")
    public WebElement borrowBooksNumber;

    //-----------------------Users Page Locators-----------------------------//
    @FindBy(xpath = "//span[.='Users']")
    public WebElement usersButton;

    @FindBy(xpath = "(//table[@id='tbl_users']//tbody//tr//td)[4]")
    public WebElement usersEmail;

    @FindBy(xpath = "(//table[@id='tbl_users']//tbody//tr//td)[3]")
    public WebElement usersFullName;


    //-----------------------Users /Add User Page Locators-----------------------------//

//    @FindBy(xpath = "(//td[.='"+ +"']")
//    public WebElement isbn;


    //-----------------------Books Page Locators-----------------------------//

    @FindBy(xpath = "//span[.='Books']")
    public WebElement booksButton;

    @FindBy(css = "input[type='search']")
    public WebElement searchButton;

    //(//table[@id='tbl_books']//tbody//td)[2]
    @FindBy(xpath = "(//table[@id='tbl_books']//tbody//tr//td)[2]")
    public WebElement bookIsbn;







}
