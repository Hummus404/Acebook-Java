package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class FriendSearchBarTest {
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
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void searchbarIsPresent() {
        driver.get("http://localhost:8081/posts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("userSearch")));
        assertTrue(driver.findElement(By.name("userSearch")).isDisplayed());

    }
    @Test
    public void searchWithNoInputNotAllowed() {
        driver.get("http://localhost:8081/posts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("userSearch")));
        String preSearch = driver.getCurrentUrl();
        driver.findElement(By.name("userSearch")).sendKeys("");
        driver.findElement(By.className("search-btn")).click();
        String postSearch = driver.getCurrentUrl();
        assertEquals(preSearch, postSearch);

    }
    @Test
    public void searchWithLessThanThreeCharsNotAllowed() {
        driver.get("http://localhost:8081/posts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("userSearch")));
        String preSearch = driver.getCurrentUrl();
        driver.findElement(By.name("userSearch")).sendKeys("UY");
        driver.findElement(By.className("search-btn")).click();
        String postSearch = driver.getCurrentUrl();
        assertEquals(preSearch, postSearch);
    }

    @Test
    public void searchForExistingUserReturnsExistingUserOnResultsPAge() {
        driver.get("http://localhost:8081/posts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("userSearch")));
        String searchText = "Adrian";
        driver.findElement(By.name("userSearch")).sendKeys(searchText);
        driver.findElement(By.className("search-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("name-tag")));
        String result = driver.findElement(By.className("name-tag")).getText();
        assertTrue(result.contains(searchText));
    }
    @Test
    public void searchForNonExistingUserReturnsNoUsersFound() {
        driver.get("http://localhost:8081/posts");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("userSearch")));
        String searchText = "Supreme leader of the group";
        driver.findElement(By.name("userSearch")).sendKeys(searchText);
        driver.findElement(By.className("search-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("no-result")));
        String result = driver.findElement(By.className("no-result")).getText();
        String noResultMsg = "No users found...";
        assertEquals(noResultMsg, result);

    }
}
