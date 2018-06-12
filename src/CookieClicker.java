import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


public class CookieClicker {

    //private WebDriver driver;
    //private String url;

    /*
    CookieClicker {

         System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
         System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
         System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
         driver = new FirefoxDriver();
         driver.manage().window().maximize();
         url = "http://orteil.dashnet.org/cookieclicker/";
         driver.get(url);
         Thread.sleep(2000);

     }

 */
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
        WebDriver driver;
        driver = new FirefoxDriver();
        driver.manage().window().maximize();

        driver.get("http://orteil.dashnet.org/cookieclicker/");
        Thread.sleep(2000);

        setUp(driver);
        int count = 0;
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        int round = 0;
        while (true) {
            count++;
            System.out.println("\nCycle nr: " + count + ":\n");
            for (int times = 0; times < 3 ; times++) {
                round++;
                for (int i = 0; i < 1200; i++) {
                    driver.findElement(By.cssSelector("#bigCookie")).click();
                    if (isElementPresent(By.cssSelector(".shimmer"), driver)) {
                        driver.findElement(By.cssSelector(".shimmer")).click();
                        System.out.println("\n!! Golden cookie found !!\n");
                    }
                }
                if (isElementPresent(By.cssSelector(".upgrade.enabled"), driver)) {
                    Actions move = new Actions(driver);
                    move.moveToElement(driver.findElement(By.cssSelector(".upgrade.enabled"))).build().perform();
                    System.out.println(" * Bought Upgrade!\n" + driver.findElement(By.cssSelector(".name")).getText()
                            + ": " + driver.findElement(By.cssSelector(".description")).getText() + "\n");
                    driver.findElement(By.cssSelector(".upgrade.enabled")).click();
                }
                if (isElementPresent(By.cssSelector(".unlocked.enabled"), driver)) {
                    String bought = matchProduct(driver, driver.findElements(By.cssSelector(".unlocked.enabled"))
                            .get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1));
                    driver.findElements(By.cssSelector(".unlocked.enabled"))
                            .get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1).click();
                    System.out.println("    + Round " + round + ". Bought an unit of: " + bought);
                }
                System.out.println();
                save(driver);
            }
            System.out.println("\nBuying items.");
            while (isElementPresent(By.cssSelector(".unlocked.enabled"), driver)) {
                String bought = matchProduct(driver, driver.findElements(By.cssSelector(".unlocked.enabled"))
                        .get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1));
                driver.findElements(By.cssSelector(".unlocked.enabled")).get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1).click();
                System.out.println("    + Bought an unit of: " + bought);
            }
            System.out.println("Cycle " + count + " ended.");
            round = 0;
        }

    }

    private static void run() {

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


        driver.findElement(By.cssSelector(".cc_btn_accept_all")).click();

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
        } catch (IOException io) {
            System.out.println(io.getMessage());

        }
        driver.findElement(By.linkText("All done!")).click();
        System.out.println("\n>> Game Saved! @ " + ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS)
                + "\n");
    }

    private static String matchProduct(WebDriver driver, WebElement upgrade) {
        String name = "";
        if (upgrade.equals(driver.findElement(By.id("product10"))))
            name = "";
        else if (upgrade.equals(driver.findElement(By.id("product9"))))
            name = "";
        else if (upgrade.equals(driver.findElement(By.id("product8"))))
            name = "Shipment";
        else if (upgrade.equals(driver.findElement(By.id("product7"))))
            name = "Wizard tower";
        else if (upgrade.equals(driver.findElement(By.id("product6"))))
            name = "Temple";
        else if (upgrade.equals(driver.findElement(By.id("product5"))))
            name = "Bank";
        else if (upgrade.equals(driver.findElement(By.id("product4"))))
            name = "Factory";
        else if (upgrade.equals(driver.findElement(By.id("product3"))))
            name = "Mine";
        else if (upgrade.equals(driver.findElement(By.id("product2"))))
            name = "Farm";
        else if (upgrade.equals(driver.findElement(By.id("product1"))))
            name = "Grandma";
        else if (upgrade.equals(driver.findElement(By.id("product0"))))
            name = "Cursor";
        else name = "big ass fail";

        return name;
    }
}