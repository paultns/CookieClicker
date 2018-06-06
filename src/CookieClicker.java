import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class CookieClicker {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");

        WebDriver driver;
        driver = new FirefoxDriver();
        //driver = new ChromeDriver();
        //driver = new InternetExplorerDriver();
        driver.manage().window().maximize();

        //driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.get("http://orteil.dashnet.org/cookieclicker/");
        Thread.sleep(2000);
        while (true) {
            if (isElementPresent(By.cssSelector("div[class*='upgrade enabled']"), driver))
                driver.findElement(By.cssSelector("div[class*='upgrade enabled']")).click();
            else if (isElementPresent(By.cssSelector("div[class*='unlocked enabled']"), driver))
                driver.findElement(By.cssSelector("div[class*='unlocked enabled']")).click();
            driver.findElement(By.cssSelector("#bigCookie")).click();
        }
    }


    private static boolean isElementPresent(By by, WebDriver driver) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}