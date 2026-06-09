package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
        String email = faker.name().username() + "@email.com";

        driver.get("http://localhost:8081/");
//        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("username")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();
        //driver.findElement(By.name("image")).sendKeys("/Users/MakersAdmin/Documents/Projects/Acebook-Java/images/random-img.jpg");
//        driver.findElement(By.name("image")).sendKeys("Acebook-Java/images/TestImage.jpg");
//        driver.findElement(By.name("action")).click();

    }


}