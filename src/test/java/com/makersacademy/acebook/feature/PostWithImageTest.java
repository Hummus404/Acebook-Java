package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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
//        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("username")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();

        int before = driver.findElements(By.cssSelector("li img")).size();

        driver.findElement(By.name("imageFile")).sendKeys("/Users/MakersAdmin/Documents/Projects/Acebook-Java/images/1781088986755_how-could-they-466cd61395.jpg");

        driver.findElement(By.name("submit-btn")).click();

        int after = driver.findElements(By.cssSelector("li img")).size();

        assertTrue(after > before);

    }

    @Test
    public void successfulContentUpload() {
        System.out.println(System.getProperty("user.dir"));
        String email = faker.name().username() + "@email.com";

        driver.get("http://localhost:8081/");
//        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("username")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();

        driver.findElement(By.name("input-content-field")).sendKeys("This is a content");

        driver.findElement(By.name("submit-btn")).click();

        WebElement contentText = driver.findElement(By.tagName("p"));

        assertEquals("This is a content", contentText.getText());

    }


}