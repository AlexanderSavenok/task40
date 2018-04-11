import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class alertsTest {

    private static final By CLICK_FOR_JS_ALERT = By.cssSelector("ul > li:first-child > button");
    private static final By CLICK_FOR_JS_CONFIRM = By.cssSelector("ul > li:nth-child(2) > button");
    private static final By CLICK_FOR_JS_PROMPT = By.cssSelector("ul > li:last-child > button");
    private static final By RESULT_LABEL = By.id("result");

    private WebDriver driver;

    @BeforeEach
    public void init() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @Test
    public void clickForJSAlertTest() {
        String resultMessage = "You successfuly clicked an alert";
        String expectedMessageFromAlert = "I am a JS Alert";

        clickByJSButton(CLICK_FOR_JS_ALERT);
        String textFromAlert = getTextFromAlertAndAccept();

        assertEquals(expectedMessageFromAlert, textFromAlert, String.format("Expected alert text: %n\nActual alert text: %n", expectedMessageFromAlert, textFromAlert));
        assertEquals(resultMessage, driver.findElement(RESULT_LABEL).getText(), String.format("Expected result text: %n\nActual result text: %n", expectedMessageFromAlert, textFromAlert));
    }

    @Test
    public void clickForJSConfirmTest() {
        String resultMessage = "You clicked: Ok";
        String expectedMessageFromAlert = "I am a JS Confirm";

        clickByJSButton(CLICK_FOR_JS_CONFIRM);
        String textFromAlert = getTextFromAlertAndAccept();

        assertEquals(expectedMessageFromAlert, textFromAlert, String.format("Expected alert text: %n\nActual alert text: %n", expectedMessageFromAlert, textFromAlert));
        assertEquals(resultMessage, driver.findElement(RESULT_LABEL).getText(), String.format("Expected result text: %n\nActual result text: %n", expectedMessageFromAlert, textFromAlert));
    }

    @Test
    public void clickForJSPromptTest() {
        String textForInput = "Test text";

        clickByJSButton(CLICK_FOR_JS_PROMPT);
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(textForInput);
        alert.accept();
        String expectedResultMessage = String.format("You entered: %s", textForInput);
        String resultMessage = driver.findElement(RESULT_LABEL).getText();

        assertEquals(expectedResultMessage, resultMessage, String.format("Expected result text: %n\nActual result text: %n", expectedResultMessage, resultMessage));
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }

    private void clickByJSButton(By locator) {
        driver.findElement(locator).click();
    }

    private String getTextFromAlertAndAccept() {
        String textFromAlert;
        Alert alert = driver.switchTo().alert();
        textFromAlert = alert.getText();
        alert.accept();

        return textFromAlert;
    }
}
