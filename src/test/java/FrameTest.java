import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrameTest {

    private static final String FRAME = "mce_0_ifr";
    private static final String CUSTOM_TEXT = "Hello world!";
    private static final By ACTIVE_ELEMENT = By.id("tinymce");

    private WebDriver driver;

    @BeforeEach
    public void init() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/iframe");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @Test
    public void iframeAddedTextIsDisplayedTest() {
        driver.switchTo().frame(FRAME);
        WebElement activeElement = driver.findElement(ACTIVE_ELEMENT);
        activeElement.clear();
        activeElement.sendKeys(CUSTOM_TEXT);
        String actualValueFromFrame = activeElement.getText();
        assertEquals(CUSTOM_TEXT, actualValueFromFrame, String.format("Expected text: %n\nActual text: %n", CUSTOM_TEXT, actualValueFromFrame));
    }

    @AfterEach
    public void tearDown() {
        driver.close();
    }
}
