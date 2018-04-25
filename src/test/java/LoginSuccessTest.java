import dataProviders.loginDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class loginSuccessTest {

    private static final By USERNAME_INPUT = By.id("Username");
    private static final By PASSWORD_INPUT = By.name("Password");
    private static final By LOGIN_BUTTON = By.className("submit-button");
    private static final By REMEMBER_ME_CHECKBOX = By.xpath("//div[@class='remember-chBox']/label");
    private static final By SKYPE_DATA_IMAGE = By.cssSelector(".skype-container img");
    private static final By ISSOFT_SOLUTIONS_LINK = By.linkText("ISsoft Solutions");
    private static final By OFFICE_TAB = By.id("officeMenu");
    private static final By SEARCH_BY_OFFICE_INPUT = By.id("input-search");
    private static final By WEX_HEALTH_CLOUD_LINK = By.partialLinkText("WEX Health Cloud");
    private static final By INDIVIDUAL_IMAGE = By.cssSelector("img[alt='Individual image']");
    private static final By LOGOUT_LINK = By.cssSelector(".sign-out-span > a");

    private WebDriver driver;

    @BeforeEach
    public void init() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://192.168.0.100/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @ParameterizedTest
    @ArgumentsSource(loginDataProvider.class)
    public void loginTest(String username, String password, boolean rememberMe) throws InterruptedException {
        String expectedSkype = "csi.alexandersavenok";

        populateLogin(username, password, rememberMe);
        clickSubmitButton();

        //Add explicit waiter for login test, which will wait until Sign out link appears (after login).
        WebDriverWait waiter = new WebDriverWait(driver, 10);
        waiter.until(ExpectedConditions.visibilityOf(driver.findElement(LOGOUT_LINK)));

        Thread.sleep(5000); //it's like implicitly wait

        WebElement skypeDataImage = driver.findElement(SKYPE_DATA_IMAGE);
        String skypeDataImageAttribute = skypeDataImage.getAttribute("alt");

        WebElement isSoftSolutionsLink = driver.findElement(ISSOFT_SOLUTIONS_LINK);
        WebElement wexHealthCloudLink = driver.findElement(WEX_HEALTH_CLOUD_LINK);
        WebElement individualImage = driver.findElement(INDIVIDUAL_IMAGE);
        assertEquals(expectedSkype, skypeDataImageAttribute, String.format("Expected skype value: %s\nActual skype value: %s", expectedSkype, skypeDataImageAttribute));
        assertTrue(isSoftSolutionsLink.isDisplayed(), "IsSoft Solutions link does not displayed");
        assertTrue(wexHealthCloudLink.isDisplayed(), "Wex Health Cloud link does not displayed");
        assertTrue(individualImage.isDisplayed(), "Individual image does not displayed");
    }

    @Test
    public void officeTabIsDisplayedTest() {
        boolean rememberMe = false;
        String username = "AlexanderSavenok";
        String password = "*********";
        WebDriverWait waiter = new WebDriverWait(driver, 10);

        //a) Go to RMSys login page, RMSys login page should appear.
        waiter.until(ExpectedConditions.titleIs("RMSys - Sign In"));

        //b) Enter correct username and password, both inputs should be filled in.
        populateLogin(username, password, rememberMe);
        waiter.until(ExpectedConditions.textToBePresentInElementLocated(USERNAME_INPUT, username));
        waiter.until(ExpectedConditions.textToBePresentInElementLocated(PASSWORD_INPUT, password));

        //c) Click Submit button, RMSys home page should appear.
        clickSubmitButton();
        waiter.until(ExpectedConditions.titleIs("RMSys - Home"));

        //d) Go to office tab, wait for "Search by office" input to appear (wait 15 seconds, polling frequence - 2,7 seconds).
        driver.findElement(OFFICE_TAB).click();
        new FluentWait(driver).withTimeout(15, TimeUnit.SECONDS).pollingEvery((long) 2.7, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOf(driver.findElement(SEARCH_BY_OFFICE_INPUT)));
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    private void populateLogin(String username, String password, boolean rememberMe) {
        driver.findElement(USERNAME_INPUT).sendKeys(username);
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
        selectRememberMeCheckbox(rememberMe);
    }

    private void clickSubmitButton() {
        driver.findElement(LOGIN_BUTTON).click();
    }

    private void selectRememberMeCheckbox(boolean select) {
        WebElement rememberMeCheckbox = driver.findElement(REMEMBER_ME_CHECKBOX);
        if(!rememberMeCheckbox.isSelected() && select) {
            rememberMeCheckbox.click();
        }
    }
}
