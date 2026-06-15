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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class PostWithImageTest {


    WebDriver driver;
    Faker faker;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    // Add test where:
    // 1. 'add image' button is clicked and an image is added
    // 2. submit button is clicked
    // 3. post is now rendered with the image showing
    @Test
    public void successfulImageUpload() {
        System.out.println(System.getProperty("user.dir"));
        String email = faker.name().username() + "@email.com";
        driver.get("http://localhost:8081/");
        driver.findElement(By.name("username")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        //int before = driver.findElements(By.cssSelector("li img")).size();

        int before = wait.until(driver -> driver.findElements(By.cssSelector("li img")).size());


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
        int after = driver.findElements(By.cssSelector("li img")).size();
        assertTrue(after > before);

    }

    @Test
    public void successfulContentUpload() {
        System.out.println(System.getProperty("user.dir"));
        String email = faker.name().username() + "@email.com";
        driver.get("http://localhost:8081/");
        driver.findElement(By.name("username")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();
        String dummyInputText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque vel lorem cursus quam gravida sagittis. Cras ac placerat erat. Donec efficitur venenatis augue in fermentum. Cras finibus condimentum nunc, in ornare elit malesuada eu. Fusce sagittis mollis justo eget vehicula. Nullam tristique luctus sapien nec bibendum. Vivamus pretium mauris eget.";
        driver.findElement(By.className("input-content-field")).sendKeys(dummyInputText);
        driver.findElement(By.className("submit-btn")).click();
        String pageText = driver.findElement(By.tagName("body")).getText();
        assertTrue(pageText.contains(dummyInputText));

    }

    @Test
    public void successfulImageAndContentUpload() {
        System.out.println(System.getProperty("user.dir"));
        String email = faker.name().username() + "@email.com";
        driver.get("http://localhost:8081/");
        driver.findElement(By.name("username")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();
        String dummyInputText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque vel lorem cursus quam gravida sagittis. Cras ac placerat erat. Donec efficitur venenatis augue in fermentum. Cras finibus condimentum nunc, in ornare elit malesuada eu. Fusce sagittis mollis justo eget vehicula. Nullam tristique luctus sapien nec bibendum. Vivamus pretium mauris eget.";

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
        driver.findElement(By.className("input-content-field")).sendKeys(dummyInputText);
        int before = driver.findElements(By.cssSelector("li img")).size();
        driver.findElement(By.className("submit-btn")).click();
        int after = driver.findElements(By.cssSelector("li img")).size();
        String pageText = driver.findElement(By.tagName("body")).getText();
        assertTrue(pageText.contains(dummyInputText));
        assertTrue(after > before);

    }


    @Test
    public void emptyPostIsNotSubmitted() {
        System.out.println(System.getProperty("user.dir"));
        String email = faker.name().username() + "@email.com";
        driver.get("http://localhost:8081/");
        driver.findElement(By.name("username")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.className("submit-btn"))
        );
        submitBtn.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Both image and content fields cannot be empty", alert.getText());
        alert.accept();

    }


}