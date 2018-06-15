import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


class CookieClicker {

    private WebDriver driver;
    private By goal;
    private String goalName;
    private boolean started = false;
    private int cycleLength;
    private int buildings;


    CookieClicker() {

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

    // actual run of program
    void run() throws InterruptedException {
        if (!started)
            System.out.println("\n    >>  Cycle Starting\n");
        else
            System.out.println("    >> New Cycle Starting");

        for (int clicks = 0; clicks < cycleLength; clicks++) {
            driver.findElement(By.cssSelector("#bigCookie")).click();
            if (isElementPresent(By.cssSelector(".shimmer"))) {
                Thread.sleep(800);
                driver.findElement(By.cssSelector(".shimmer")).click();
                System.out.println("\n!! Golden cookie found !!\n");
            }
        }
        if (isElementPresent(goal)) {
            driver.findElement(goal).click();
            System.out.println("\n++ " + goalName + " has been bought.\n");
            getGoal();
        } else
            System.out.println("\n" + goalName + " was not found. Restarting..");

        save();
        started = true;
/*
        if (isElementPresent(By.cssSelector(".upgrade.enabled"))) {
            Actions move = new Actions(driver);
            move.moveToElement(driver.findElement(By.cssSelector(".upgrade.enabled"))).build().perform();
            System.out.println(" * Bought Upgrade!\n" + driver.findElement(By.cssSelector(".name")).getText()
                    + ": " + driver.findElement(By.cssSelector(".description")).getText() + "\n");
            driver.findElement(By.cssSelector(".upgrade.enabled")).click();
*/
    }

    // importing save and disabling resource intensive things
    void setUp() {
        System.out.println("Setup Sequence!\n");
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
        System.out.println("\nSetup Complete!\n");
    }

    // pulls efficiency of each building
    void getGoal() {
        Actions move = new Actions(driver);
        BigDecimal producing;
        int index = 0;
        long min = Integer.MAX_VALUE;
        long eff;
        //if (!isElementPresent(By.cssSelector(".pieTimer"))) {
        System.out.println("Calculating goal..\n");
        try {
            for (int i = 0; i < buildings; i++) {
                move.moveToElement(driver.findElement(By.cssSelector("#product" + i))).build().perform();
                Thread.sleep(700);
                producing = new BigDecimal(driver.findElement(By.cssSelector("div.data b")).getText()
                        .replaceAll("\\D+", ""));
                System.out.print("Price for a " + driver.findElement(By.id("productName" + i)).getText() +
                        " is: " +
                        new BigDecimal(driver.findElement(By.id("productPrice" + i)).getText().replaceAll
                                ("\\D+", ""))
                        + ", and it produces: " + producing);
                eff = (new BigDecimal(driver.findElement(By.id("productPrice" + i)).getText().replaceAll
                        ("\\D+", "")).divide(producing, 0, RoundingMode.DOWN)).longValue();
                System.out.println(", with an effiency of: " + eff);
                if (eff == Math.min(eff, min)) {
                    min = eff;
                    index = i;
                }
            }
        } catch (Exception e) {
            System.out.println("\n//// Failed to get a goddamn number.. ");
        }

        goal = By.cssSelector("#product" + index + ".enabled");
        goalName = driver.findElement(By.id("productName" + index)).getText();
        System.out.println("\n* Goal has been set to buy: " + goalName);

    }

    // reads save code from txt file
    private void read() {

        System.out.println("\n-- Importing Save! --\n");
        try {
            File txt = new File("CookieSave.txt");
            Scanner sc = new Scanner(txt);
            sc.useDelimiter("\\Z");
            driver.findElement(By.id("textareaPrompt")).sendKeys(sc.next());
            System.out.println("\n-- Save game imported --\n");
        } catch (Exception e) {
            System.out.println("\n !! -- Savegame could not be imported! -- !!\n");
        }

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
        System.out.println("\n>> Game Saved! @ " + ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit
                .SECONDS)
                + "\n");
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

    void setBuildings(int buildings) {
        this.buildings = buildings;
    }

    void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }


}


