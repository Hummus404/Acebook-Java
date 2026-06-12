package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class PostWithImageTest {


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
        wait.until(driver -> driver.findElement(By.name("username")));
        wait.until(driver -> driver.findElement(By.name("password")));
        driver.findElement(By.name("username")).sendKeys("test1234@email.com");
        driver.findElement(By.name("password")).sendKeys(" -6G3b5!(dOcJ[c')^&>8M5(w");
        driver.findElement(By.name("action")).click();

        wait.until(driver -> driver.findElement(By.id("greeting")));

    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    @Test
    public void successfulImageUpload() {

        wait.until(driver -> driver.findElements(By.cssSelector("li img")));
        int before = driver.findElements(By.cssSelector("li img")).size();

        Path imagePath = Paths.get(
                System.getProperty("user.dir"),
                "src",
                "test",
                "resources",
                "images",
                "test-image.jpg"
        );

        driver.findElement(By.name("imageFile"))
                .sendKeys(imagePath.toString());

        driver.findElement(By.className("submit-btn")).click();

        wait.until(driver -> driver.findElements(By.cssSelector("li img")));
        int after = driver.findElements(By.cssSelector("li img")).size();
        assertTrue(after > before);

    }

    @Test
    public void successfulContentUpload() throws InterruptedException {

        String dummyInputText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque vel lorem cursus quam gravida sagittis. Cras ac placerat erat. Donec efficitur venenatis augue in fermentum. Cras finibus condimentum nunc, in ornare elit malesuada eu. Fusce sagittis mollis justo eget vehicula. Nullam tristique luctus sapien nec bibendum. Vivamus pretium mauris eget.";
        wait.until(driver -> driver.findElement(By.className("input-content-field")));
        driver.findElement(By.className("input-content-field")).sendKeys(dummyInputText);
        driver.findElement(By.className("submit-btn")).click();
        wait.until(driver -> driver.findElement(By.className("input-content-field")));
        wait.until(driver -> driver.findElement(By.tagName("body")));
        String pageText = driver.findElement(By.tagName("body")).getText();
        assertTrue(pageText.contains(dummyInputText));

    }

    @Test
    public void successfulImageAndContentUpload() {

        String dummyInputText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque vel lorem cursus quam gravida sagittis. Cras ac placerat erat. Donec efficitur venenatis augue in fermentum. Cras finibus condimentum nunc, in ornare elit malesuada eu. Fusce sagittis mollis justo eget vehicula. Nullam tristique luctus sapien nec bibendum. Vivamus pretium mauris eget.";

        Path imagePath = Paths.get(
                System.getProperty("user.dir"),
                "src",
                "test",
                "resources",
                "images",
                "test-image.jpg"
        );

        wait.until(driver -> driver.findElement(By.name("imageFile")));

        driver.findElement(By.name("imageFile"))
                .sendKeys(imagePath.toString());
        driver.findElement(By.className("input-content-field")).sendKeys(dummyInputText);
        int before = driver.findElements(By.cssSelector("li img")).size();
        driver.findElement(By.className("submit-btn")).click();

        wait.until(driver -> driver.findElement(By.cssSelector("li img")));
        int after = driver.findElements(By.cssSelector("li img")).size();
        wait.until(driver -> driver.findElement(By.className("input-content-field")));
        String pageText = driver.findElement(By.tagName("body")).getText();
        assertTrue(pageText.contains(dummyInputText));
        assertTrue(after > before);

    }


    @Test
    public void emptyPostIsNotSubmitted() {
        wait.until(driver -> driver.findElement(By.className("submit-btn"))).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Both image and content fields cannot be empty", alert.getText());
        alert.accept();

    }


}