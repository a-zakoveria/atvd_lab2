package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class AppTest {
    private WebDriver driver; // WebDriver for browser control
    private WebDriverWait wait; // Object for explicit waits
    private static final String TARGET_URL = "https://www.wikipedia.org/"; // Target URL for testing
    private static final int TIMEOUT = 15; // Timeout for waiting (in seconds)

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup(); // Setup driver for Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*"); // Add necessary options
        driver = new ChromeDriver(options); // Initialize driver with options
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2)); // Implicit wait (fallback timeout)
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT)); // Initialize explicit wait
        driver.manage().window().setSize(new Dimension(1920, 1080)); // Set window size (more stable than maximize())
    }

    @Test
    public void testOpenPageAndVerifyTitle() {
        driver.get(TARGET_URL); // Open target page
        wait.until(ExpectedConditions.titleContains("Wikipedia")); // Wait until page title contains "Wikipedia"
        Assert.assertTrue(driver.getTitle().contains("Wikipedia"), "Title does not contain 'Wikipedia'"); // Verify title contains "Wikipedia"
    }

    @Test
    public void testClickElement() {
        driver.get(TARGET_URL); // Open target page
        WebElement englishLink = waitForElement(By.id("js-link-box-en")); // Wait for and find element by ID
        englishLink.click(); // Click element
        wait.until(ExpectedConditions.titleContains("Wikipedia, the free encyclopedia")); // Wait for title change
        Assert.assertTrue(driver.getTitle().contains("Wikipedia, the free encyclopedia"), "Title does not contain 'Wikipedia, the free encyclopedia'"); // Verify new title
    }

    @Test
    public void testInputDataAndVerifyField() {
        driver.get(TARGET_URL); // Open target page
        WebElement searchBox = waitForElement(By.name("search")); // Wait for and find search box
        searchBox.sendKeys("Selenium WebDriver"); // Enter text in search box
        Assert.assertEquals(searchBox.getAttribute("value"), "Selenium WebDriver", "Search box does not contain 'Selenium WebDriver'"); // Verify entered text
    }

    @Test
    public void testFindElementUsingIndirectXPath() {
        driver.get(TARGET_URL); // Open target page
        // Find element using indirect XPath (class or ID)
        WebElement searchBox = waitForElement(By.xpath("//input[contains(@class, 'search-input') or contains(@id, 'searchInput')]"));
        Assert.assertNotNull(searchBox, "Search box not found using indirect XPath"); // Verify element is found
    }

    @Test
    public void testConditionVerification() {
        driver.get(TARGET_URL); // Open target page
        WebElement searchBox = waitForElement(By.name("search")); // Wait for and find search box
        searchBox.sendKeys("Selenium WebDriver"); // Enter text in search box
        WebElement searchButton = waitForElement(By.xpath("//button[@type='submit']")); // Wait for and find search button
        searchButton.click(); // Click search button
        WebElement results = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading"))); // Wait for search results
        Assert.assertTrue(results.isDisplayed(), "Search results are not displayed"); // Verify results are displayed
    }

    // Helper method to wait for and find element
    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close browser after tests
        }
    }
}
