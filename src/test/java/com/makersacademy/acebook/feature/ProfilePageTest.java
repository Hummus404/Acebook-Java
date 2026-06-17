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
    WebDriver driver;
    Faker faker;
    WebDriverWait wait;
    String expectedUserName;
    String defaultProfilePic;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        expectedUserName = "adrian_woz_here";

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

    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void rendersPage() {

        wait.until(ExpectedConditions.urlContains("/posts"));

        Actions action = new Actions(driver);

        WebElement userIcon = driver.findElement(By.className("user-menu"));

        action.moveToElement(userIcon).perform();

        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile-link")));

        profileLink.click();

        wait.until(ExpectedConditions.urlContains("/profile/" + expectedUserName));

    }

    @Test
    public void showsUsernameAndProfilePic() {

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

        String defaultProfilePic = "http://localhost:8081/images/defaultProfileAvatar.jpeg";

        assertEquals(expectedUserName, username.getText());
        assertTrue(Objects.requireNonNull(profilePic.getAttribute("src")).contains(defaultProfilePic));
    }
}
