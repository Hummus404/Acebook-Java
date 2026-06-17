package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FriendRequestTest {

    WebDriver driver;
    Faker faker;
    WebDriverWait wait;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8081/");
        WebElement username = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username"))
        );

        WebElement password = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("password"))
        );

        username.clear();
        password.clear();

        username.sendKeys("test1234@email.com");
        password.sendKeys(" -6G3b5!(dOcJ[c')^&>8M5(w");
        wait.until(
                ExpectedConditions.elementToBeClickable(By.name("action"))
        ).click();

        wait.until(ExpectedConditions.urlContains("/posts"));


        wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/friends']"))
        ).click();
        wait.until(ExpectedConditions.urlContains("/friends"));

    }

//    @AfterEach
//    public void tearDown() {
//        driver.close();
//    }

    @Test
    public void userVisitsFriendsBarAndReturnsFriendsPage(){
        assertTrue(driver.getCurrentUrl().contains("/friends"));
    }

    @Test
    public void userVisitsFriendsPageAndCanSeeContent(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        assertTrue(driver.findElement(By.tagName("h1")).getText().contains("My Friends"));
    }

    @Test
    public void userVisitsFriendsPageAndCanViewFriendsTab(){
        wait.until(
                ExpectedConditions.elementToBeClickable(By.id("btn-friends"))
        ).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tab-friends")));
        assertTrue(driver.findElement(By.id("tab-friends")).isDisplayed());
    }

    @Test
    public void userVisitsFriendsPageAndCanViewIncomingFriendRequestTab(){
        wait.until(
                ExpectedConditions.elementToBeClickable(By.id("btn-incoming"))
        ).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tab-incoming")));
        assertTrue(driver.findElement(By.id("tab-incoming")).isDisplayed());
    }

    @Test
    public void userVisitsFriendsPageAndCanViewSentFriendRequestsTab(){
        wait.until(
                ExpectedConditions.elementToBeClickable(By.id("btn-sent"))
        ).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tab-sent")));
        assertTrue(driver.findElement(By.id("tab-sent")).isDisplayed());
    }

    @Test
    public void userOneCanSendFriendRequestFromUserTwosProfile(){
        driver.get("http://localhost:8081/profile/adrian_woz_here");
        wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(".profile-actions button[type='submit']"))
        ).click();
        wait.until(
                ExpectedConditions.urlContains("/profile/adrian_woz_here")
        );
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".profile-actions button:disabled"))
        );
        assertTrue(driver.findElement(By.cssSelector(".profile-actions button:disabled")).isDisplayed());
    }
}
