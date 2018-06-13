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

    private WebDriver driver;
    private int cycle;
    private int round;
    private int turn;
    private int loops;


    public CookieClicker() {

        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("http://orteil.dashnet.org/cookieclicker/");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {


        CookieClicker cC = new CookieClicker();
        cC.setUp();

        // declare how much time each turn should take, in number of for loops
        cC.turn = 1000;
        // declare how many loops to go through before buying all upgrades
        cC.loops = 3;

        while (true) {
            cC.run();
        }
    }

    // actual run of programs using 2 two loops
    // buys one upgrade and one building at end of each loop
    // buys buildings until no money left at the end of all loops
    private void run() throws InterruptedException {

        cycle++;
        System.out.println("\nCycle nr: " + cycle + ":\n");
        for (int times = 0; times < loops; times++) {
            round++;
            for (int i = 0; i < turn; i++) {
                driver.findElement(By.cssSelector("#bigCookie")).click();
                if (isElementPresent(By.cssSelector(".shimmer"))) {
                    Thread.sleep(800);
                    driver.findElement(By.cssSelector(".shimmer")).click();
                    System.out.println("\n!! Golden cookie found !!\n");
                }
            }
            if (isElementPresent(By.cssSelector(".unlocked.enabled"))) {
                String bought = matchProduct(driver.findElements(By.cssSelector(".unlocked.enabled"))
                        .get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1));
                driver.findElements(By.cssSelector(".unlocked.enabled"))
                        .get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1).click();
                System.out.println("    + Round " + round + ". Bought a Building: " + bought);
            }
            System.out.println();
            save();
        }
        System.out.println("\nBuying items.");

        if (isElementPresent(By.cssSelector(".upgrade.enabled"))) {
            Actions move = new Actions(driver);
            move.moveToElement(driver.findElement(By.cssSelector(".upgrade.enabled"))).build().perform();
            System.out.println(" * Bought Upgrade!\n" + driver.findElement(By.cssSelector(".name")).getText()
                    + ": " + driver.findElement(By.cssSelector(".description")).getText() + "\n");
            driver.findElement(By.cssSelector(".upgrade.enabled")).click();
        }
        while (isElementPresent(By.cssSelector(".unlocked.enabled"))) {
            String bought = matchProduct(driver.findElements(By.cssSelector(".unlocked.enabled"))
                    .get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1));
            driver.findElements(By.cssSelector(".unlocked.enabled")).get(driver.findElements(By.cssSelector(".unlocked.enabled")).size() - 1).click();
            System.out.println("    + Bought a Building: " + bought);
        }
        System.out.println("\nCycle " + cycle + " ended.");
        round = 0;
    }

    // importing save and disabling resource intensive things
    private void setUp() {
        System.out.println("Setup Sequence!");
        driver.findElement(By.id("prefsButton")).click();
        driver.findElement(By.linkText("Import save")).click();

        read();

        driver.findElement(By.linkText("Load")).click();

        try {
            driver.findElement(By.linkText("Fancy graphics ON")).click();
        } catch (Exception e) {
            System.out.println("Fancy graphics button already depressed");
        }
        try {
            driver.findElement(By.linkText("Particles ON")).click();
        } catch (Exception e) {
            System.out.println("Particles button already depressed");
        }
        try {
            driver.findElement(By.linkText("Numbers ON")).click();
        } catch (Exception e) {
            System.out.println("Number button already depressed");
        }
        try {
            driver.findElement(By.linkText("Milk ON")).click();
        } catch (Exception e) {
            System.out.println("Milk button already depressed");
        }
        try {
            driver.findElement(By.linkText("Cursors ON")).click();
        } catch (Exception e) {
            System.out.println("Cursors button already depressed");
        }
        try {
            driver.findElement(By.linkText("Wobbly cookie ON")).click();
        } catch (Exception e) {
            System.out.println("Wobbly cookie button already depressed");
        }
        try {
            driver.findElement(By.linkText("Alt cookie sound ON")).click();
        } catch (Exception e) {
            System.out.println("Alt cookie sound button already depressed");
        }
        try {
            driver.findElement(By.linkText("Defocus OFF")).click();
        } catch (Exception e) {
            System.out.println("Defocus button already pressed");
        }

        driver.findElement(By.cssSelector(".cc_btn_accept_all")).click();
        round = 0;
        cycle = 0;

    }

    // reads save code from txt file
    private void read() {

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

    // saves game to txt file
    private void save() {

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
        System.out.println(">> Game Saved! @ " + ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS)
                + "\n");
    }

    // finds name of bought building
    private String matchProduct(WebElement building) {
        String name = "";
        if (building.equals(driver.findElement(By.id("product10"))))
            name = "";
        else if (building.equals(driver.findElement(By.id("product9"))))
            name = "";
        else if (building.equals(driver.findElement(By.id("product8"))))
            name = "Shipment";
        else if (building.equals(driver.findElement(By.id("product7"))))
            name = "Wizard tower";
        else if (building.equals(driver.findElement(By.id("product6"))))
            name = "Temple";
        else if (building.equals(driver.findElement(By.id("product5"))))
            name = "Bank";
        else if (building.equals(driver.findElement(By.id("product4"))))
            name = "Factory";
        else if (building.equals(driver.findElement(By.id("product3"))))
            name = "Mine";
        else if (building.equals(driver.findElement(By.id("product2"))))
            name = "Farm";
        else if (building.equals(driver.findElement(By.id("product1"))))
            name = "Grandma";
        else if (building.equals(driver.findElement(By.id("product0"))))
            name = "Cursor";
        else name = "big ass fail";

        return name;
    }

    // verifies element is present
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}