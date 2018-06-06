import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
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
        //driver.manage().window().maximize();


        driver.get("http://orteil.dashnet.org/cookieclicker/");
        Thread.sleep(2000);

        setUp(driver);

        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        while (true) {
            for (int i = 0; i < 250; i++) {
            driver.findElement(By.cssSelector("#bigCookie")).click();
            if (isElementPresent(By.cssSelector(".shimmer"), driver))
            driver.findElement(By.cssSelector(".shimmer")).click();}
            //Thread.sleep(60000);
            while (isElementPresent(By.cssSelector(".upgrade.enabled"), driver))
            driver.findElement(By.cssSelector(".upgrade.enabled")).click();
            while (isElementPresent(By.cssSelector(".unlocked.enabled"), driver)) {
                driver.findElements(By.cssSelector(".unlocked.enabled"))
                        .get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1).click();
            }
            save(driver);
        }
        //save(driver);

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

        read(driver);
        try {
            driver.findElement(By.linkText("Load")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Fancy graphics ON")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Particles ON")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Numbers ON")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Milk ON")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Cursors ON")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Wobbly cookie ON")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Alt cookie sound ON")).click();
        } catch (Exception e) {

        }
        try {
            driver.findElement(By.linkText("Defocus OFF")).click();
        } catch (Exception e) {

        }


        //driver.findElement(By.id("prefsButton")).click();

    }

    private static void read(WebDriver driver) {

        File txt = new File("CookieSave.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(txt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sc.useDelimiter("\\Z");
        driver.findElement(By.id("textareaPrompt")).sendKeys(sc.next());

    }

    private static void save(WebDriver driver) {

        driver.findElement(By.linkText("Export save")).click();

        File txt = new File("CookieSave.txt");
        PrintWriter saveCookies;
        try {
            saveCookies = new PrintWriter(txt);
            saveCookies.println(driver.findElement(By.id("textareaPrompt")).getText());
            saveCookies.close();
            //ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "CookieSave.txt");
            //pb.start();
        } catch (IOException io) {
            System.out.println(io.getMessage());

        }
        driver.findElement(By.linkText("All done!")).click();
    }
}