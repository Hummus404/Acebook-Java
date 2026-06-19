package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LikeAPostTest{
    WebDriver driver;
    Faker faker;
    WebDriverWait wait;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    public void setupLikeAPost() {
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
    public void successfulMakingAPost() throws InterruptedException {

        String dummyInputText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque vel lorem cursus quam gravida sagittis. Cras ac placerat erat. Donec efficitur venenatis augue in fermentum. Cras finibus condimentum nunc, in ornare elit malesuada eu. Fusce sagittis mollis justo eget vehicula. Nullam tristique luctus sapien nec bibendum. Vivamus pretium mauris eget.";
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("input-content-field")
                )
        );
        driver.findElement(By.className("input-content-field")).sendKeys(dummyInputText);
        driver.findElement(By.className("submit-btn")).click();
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("input-content-field")
                )
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("body")
                )
        );
        String pageText = driver.findElement(By.tagName("body")).getText();
        assertTrue(pageText.contains(dummyInputText));

    }

    @Test
    public void successfulLikeOnAPost() {
        driver.get("http://localhost:8081/posts");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(driver -> driver.findElement(By.className("like-count")));
        WebElement likeCountText = driver.findElement(By.className("like-count"));
        wait.until(driver -> driver.findElement(By.id("like-status")));
        String  likeInteractionButton = driver.findElement(By.id("like-status")).getText();
        WebElement likeButton = driver.findElement(By.className("like-status"));

//        assertEquals("0 likes",likeCountText.getText());
        assertTrue(likeInteractionButton.contains("Like"));

        likeButton.click();

//        assertEquals("1", likeCountText.getText());
        assertTrue(likeInteractionButton.contains("Unlike"));
    }

}
