package com.otus.javaqa;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SauceLabsGridTest {
    private WebDriver driver;

    @BeforeEach
    public void setup(TestInfo testInfo) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("username", Constants.USERNAME);
        capabilities.setCapability("accessKey", Constants.ACCESS_KEY);
        capabilities.setCapability("platform", "Windows 10");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("version", "75.0");
        capabilities.setCapability("name", testInfo.getDisplayName());

        driver = new RemoteWebDriver(new URL("http://ondemand.eu-central-1.saucelabs.com/wd/hub"), capabilities);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Otus Homepage Title Check")
    public void homePageTitleTest() {
        driver.navigate().to(Constants.OTUS_HOMEPAGE);
        boolean result = driver.getTitle().equals("OTUS - Онлайн-образование");
        sendTestResultToSauceLabs(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Presence of Java QA Automation Course on Operations Courses Page")
    public void qaJavaCourseIsPresentTest() {
        driver.navigate().to(Constants.OTUS_OPERATIONS_COURSES_PAGE);
        List<WebElement> listOfCourses = driver.findElements(By.xpath("//div[@class='lessons']/a"));
        boolean isCourseFound = false;

        for (WebElement course : listOfCourses) {
            String courseTitle = course.findElement(By.xpath(".//div[contains(@class, 'lesson-title')]")).getAttribute("innerText").trim();
            if (courseTitle.equals("Java QA Automation Engineer")) {
                isCourseFound = true;
                break;
            }
        }
        sendTestResultToSauceLabs(isCourseFound);
        assertTrue(isCourseFound);
    }

    private void sendTestResultToSauceLabs(boolean result) {
        if (result){
            ((JavascriptExecutor)driver).executeScript("sauce:job-result=passed");
        }
        else {
            ((JavascriptExecutor)driver).executeScript("sauce:job-result=failed");
        }
    }
}