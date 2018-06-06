import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

        //setUp(driver);
        driver.findElement(By.id("prefsButton")).click();

        save(driver);

        /*while (true) {
            for (int i = 0; i < 100; i++)
                driver.findElement(By.cssSelector("#bigCookie")).click();
            if (isElementPresent(By.cssSelector("div[class*='upgrade enabled']"), driver))
                driver.findElement(By.cssSelector("div[class*='upgrade enabled']")).click();
            else if (isElementPresent(By.cssSelector("div[class*='unlocked enabled']"), driver))
                driver.findElement(By.cssSelector("div[class*='unlocked enabled']")).click();


        } */
    }


    private static boolean isElementPresent(By by, WebDriver driver) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private static void setUp(WebDriver driver) {
        driver.findElement(By.id("prefsButton")).click();
        driver.findElement(By.linkText("Import save")).click();
        driver.findElement(By.id("textareaPrompt")).sendKeys("Mi4wMTA2fHwxNTI4Mjg1OTAzMTA5OzE1MjgyODU5MDMxMDk7MTUyODI4NTkxMzA1MDtDb29raWUgTW9uc3RlcjthZGtzbnwxMTExMTEwMTEwMDEwMDEwMHwwOzA7MDswOzA7MDswOzA7MDswOzA7MDswOzA7MDswOzA7MDswOzA7MDswOzswOzA7MDswOzA7MDswOy0xOy0xOy0xOy0xOy0xOzA7MDswOzA7NTA7MDswOy0xOy0xOzE1MjgyODU5MDMxMDM7MDswO3wwLDAsMCwwOzAsMCwwLDA7MCwwLDAsMDswLDAsMCwwOzAsMCwwLDA7MCwwLDAsMDswLDAsMCwwOzAsMCwwLDA7MCwwLDAsMDswLDAsMCwwOzAsMCwwLDA7MCwwLDAsMDswLDAsMCwwOzAsMCwwLDA7MCwwLDAsMDt8MDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwfDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMTAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwfA%3D%3D%21END%21");
        driver.findElement(By.linkText("Load")).click();
        driver.findElement(By.linkText("Fancy graphics ON")).click();
        driver.findElement(By.linkText("Particles ON")).click();
        driver.findElement(By.linkText("Numbers ON")).click();
        driver.findElement(By.linkText("Milk ON")).click();
        driver.findElement(By.linkText("Cursors ON")).click();
        driver.findElement(By.linkText("Wobbly cookie ON")).click();
        driver.findElement(By.linkText("Alt cookie sound ON")).click();
        driver.findElement(By.linkText("Fast notes OFF")).click();
        driver.findElement(By.linkText("Defocus OFF")).click();


        //driver.findElement(By.id("prefsButton")).click();

    }

    private static void save (WebDriver driver) {

        driver.findElement(By.linkText("Export save")).click();
        System.out.println(driver.findElement(By.id("textareaPrompt")).getText());
        driver.findElement(By.linkText("All done!")).click();

        File txt = new File("hours.txt");
        PrintWriter writeHours;
        try {
            writeHours = new PrintWriter(txt);
            for (int i = 0; i < this.hours.length; i++)
                writeHours.println(this.projects[i] + this.hours[i] * 0.25);
            writeHours.close();
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "hours.txt");
            pb.start();
        } catch (IOException io) {
            System.out.println(io.getMessage());

    }
}