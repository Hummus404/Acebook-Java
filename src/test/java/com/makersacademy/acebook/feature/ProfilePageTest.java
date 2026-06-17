package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfilePageTest {
    Faker faker;
    WebDriver driver;
    WebDriverWait wait;
    String expectedUserName = "adrian_woz_here";
    String defaultProfilePic;


    public String signUp() {
        String email = faker.name().username() + "@email.com";

        driver.get("http://localhost:8081/");
        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();

        driver.findElement(By.id("username-input")).sendKeys(email);
        driver.findElement(By.id("first-name-input")).sendKeys("Random");
        driver.findElement(By.id("surname-input")).sendKeys("user");
        driver.findElement(By.id("submit-btn")).click();

        wait.until(driver -> driver.findElement(By.id("greeting")));
//        String greetingText = driver.findElement(By.id("greeting")).getText();
//        assertTrue(greetingText.startsWith("Signed in as " + email));

        return email;

    }

    public void signIn() {
        driver.get("http://localhost:8081/");
        WebElement username = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username"))
        );

        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("password"))
        );

//        username.clear();
//        password.clear();

        username.sendKeys("test1234@email.com");
        password.sendKeys(" -6G3b5!(dOcJ[c')^&>8M5(w");
        wait.until(
                ExpectedConditions.elementToBeClickable(By.name("action"))
        ).click();

        wait.until(ExpectedConditions.urlContains("/posts"));


    }



    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));





    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void rendersPage() {

        signIn();

//        wait.until(ExpectedConditions.urlContains("/posts"));

        Actions action = new Actions(driver);

        WebElement userIcon = driver.findElement(By.className("user-menu"));

        action.moveToElement(userIcon).perform();

        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile-link")));

        profileLink.click();

        wait.until(ExpectedConditions.urlContains("/profile/" + expectedUserName));

    }

    @Test
    public void showsUsernameAndProfilePic() {

        signIn();

        wait.until(ExpectedConditions.urlContains("/posts"));

        Actions action = new Actions(driver);

        WebElement userIcon = driver.findElement(By.className("user-menu"));

        action.moveToElement(userIcon).perform();

        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile-link")));

        profileLink.click();

        wait.until(ExpectedConditions.urlContains("/profile/" + expectedUserName));

        WebElement username = driver.findElement(By.id("username-text"));
        WebElement profilePic = driver.findElement(By.id("profile-pic"));

        assertTrue(username.isDisplayed());
        assertTrue(profilePic.isDisplayed());

        defaultProfilePic = "http://localhost:8081/images/defaultProfileAvatar.jpeg";

        assertEquals(expectedUserName, username.getText());
        assertTrue(Objects.requireNonNull(profilePic.getAttribute("src")).contains(defaultProfilePic));
    }

    @Test
    public void showOnlyLoggedInUsersPosts() {
       String email = signUp();

    }
}
