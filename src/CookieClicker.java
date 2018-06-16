import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
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
    private int cycleLength;
    private int buildings;
    private double minutes;
    private BigDecimal cps;
    private BigDecimal upgradePrice;
    private boolean newBuildingFound;
    private boolean buyNewBuilding; // user decides if he wishes to save for the unlocked building
    private boolean upgrade; // user decides if he wishes to save for next upgrade
    private boolean newGame;
    private boolean loop; // user decides if to loop or end program


    CookieClicker(WebDriver browser) {

        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
        driver = browser;
        driver.manage().window().maximize();
        driver.get("http://orteil.dashnet.org/cookieclicker/");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cycleLength = 1;
        loop = true;
        minutes = 1;
        upgrade = false;
        buyNewBuilding = false;
        newGame = true;
        newBuildingFound = false;
        cps = new BigDecimal(0);
        upgradePrice = new BigDecimal(0);

        setUp();
    }

    // importing save and disabling resource intensive things
    private void setUp() {

        System.out.println("\nSetup Sequence!\n");
        driver.findElement(By.id("prefsButton")).click();

        if (!newGame)
            read();

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
        try {
            driver.findElement(By.linkText("Fast notes OFF")).click();
        } catch (Exception e) {
            System.out.println("Fast notes button already pressed");
        }


        driver.findElement(By.cssSelector(".cc_btn_accept_all")).click();

        newBuilding();
        if (buildings == 1) {
            goal = By.cssSelector("#product0" + ".enabled");
            goalName = driver.findElement(By.id("productName0")).getText();
        } else if (!buyNewBuilding && buildings != 0)
            getGoal();

        //driver.findElement(By.id("storeBulk10")).click();

        System.out.println("\nSetup Complete!\n");

        //driver.manage().window().setPosition(new Point(0, -2000));
    }

    // actual run of program
    void run() throws InterruptedException {
        System.out.print((char) 27 + "[34m\n>> New Cycle Starting. ");
        //System.out.print("\n>> New Cycle Starting. ");
        if (upgrade)
            System.out.println("Will buy a new upgrade.\n" + (char) 27 + "[0m");
        else if (buyNewBuilding)
            System.out.println("Will buy a new building.\n" + (char) 27 + "[0m");
        else
            System.out.println("Will buy the most efficient building.\n" + (char) 27 + "[0m");

        for (int loops = 0; loops < cycleLength; loops++)
            for (int clicks = 0; clicks < 20; clicks++) {
                driver.findElement(By.cssSelector("#bigCookie")).click();
                if (isElementPresent(By.cssSelector(".shimmer"))) {
                    goldenCookie();
                }
            }

        if (upgrade)
            buyUpgrade();
        else
            buy();

        save();

    }

    //golden cookie
    private void goldenCookie() throws InterruptedException {
        Thread.sleep(800);
        driver.findElement(By.cssSelector(".shimmer")).click();
        Thread.sleep(500);
        System.out.println((char) 27 + "[33m\n!! Golden cookie found !!\n");
        System.out.println(driver.findElement(By.id("particle0")).getText() + (char) 27 + "[0m");
        //System.out.println(driver.findElement(By.cssSelector("#particle0 div")).getText() + (char) 27 + "[0m");
    }

    // default buy most effiecient building
    private void buy() {
        if (isElementPresent(goal)) {
            driver.findElement(goal).click();
            //System.out.println("\n++ " + goalName + " has been bought.\n");
            System.out.println((char) 27 + "[32m\n   ++  " + goalName + " has been bought.\n" + (char) 27 + "[0m");
            getGoal();
            updateCps();
            if (cycleLength > 1) {
                cycleLength--;
                System.out.println(" Decreasing cycle duration.\n");
            }
            if (buyNewBuilding) {
                buyNewBuilding = false;
                newBuilding();
                minutes += 3;
            }
        } else {
            cycleLength++;
            System.out.println((char) 27 + "[31m \n" + goalName + " could not be afforded. Trying again " +
                    "next cycle..." + (char) 27 + "[0m Increasing cycle duration.");
        }

    }

    // buys upgrade
    private void buyUpgrade() {
        if (isElementPresent(goal)) {
            System.out.println(" * Bought Upgrade!");
            try {
                Actions move = new Actions(driver);
                move.moveToElement(driver.findElement(By.cssSelector(".upgrade.enabled"))).build()
                        .perform();
                Thread.sleep(1000);
                System.out.println("\n" + driver.findElement(By.cssSelector(".name"))
                        .getText()
                        + ": " + driver.findElement(By.cssSelector(".description")).getText() + "\n");
            } catch (Exception e) {
                System.out.println("Description could not be retrieved at this moment :(");
            }
            driver.findElement(goal).click();
            upgrade = false;
            getGoal();
        } else
            System.out.println(" * No unlocked upgrade found at this time.\n");
        //upgrade = false;
    }

    // retrieves the next upgrade's price and text
    private void getUpgrade() {

    }

    // pulls number of already owned buildings and the next new unlocked building
    private void newBuilding() {
        int count = 0;
        for (int index = 0; index < 20; index++) {
            if (!isElementPresent(By.id("product" + index)))
                break;
            if (driver.findElement(By.id("productOwned" + index)).getText().isEmpty()) {
                newBuildingFound = true;
                System.out.print("\nNew unlocked building found: " + (char) 27 + "[32m" + driver.findElement(By.id
                        ("productName" + index)).getAttribute("textContent") + (char) 27 + "[0m");
                break;
            } else count++;
        }
        buildings = count;
        if (!newBuildingFound) {
            System.out.print("No new building found");
        }

        System.out.println(". Number of already bought buildings: " + count);
        if (buildings == 0) {
            System.out.println("\nNo pre-owned buildings found. Saving for the next new building\n");
            buyNewBuilding = true;
            goal = By.id("product0");
            goalName = driver.findElement(By.id("productName0")).getAttribute("textContent");
            System.out.println("No buildings detected. Current goal is Cursor");
            upgrade = false;
        }

    }

    // pulls efficiency of each building   /// must edit, will not continue if no new buildings are found
    private void getGoal() {
        if (buildings > 0 && newBuildingFound)
            if (!evolve()) {
                Actions move = new Actions(driver);
                BigDecimal producing;
                int index = 0;
                long min = Integer.MAX_VALUE;
                long eff;
                //if (!isElementPresent(By.cssSelector(".pieTimer"))) {
                System.out.println("\nCalculating goal..\n");
                for (int i = 0; i < buildings; i++) {
                    try {
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
                    } catch (Exception e) {

                        //System.out.println("//// Failed to get number of producing cookies for " + driver
                        //        .findElement(By.id("productName" + i)).getText());

                        System.out.println((char) 27 + "[31m//// Failed to get number of producing cookies for " +
                                driver.findElement(By.id("productName" + i)).getText() + (char) 27 + "[0m");
                    }
                }

                goal = By.cssSelector("#product" + index + ".enabled");
                goalName = driver.findElement(By.id("productName" + index)).getText();
                System.out.println("\n* Goal has been set to buy: " + (char) 27 + "[32m" + goalName + (char) 27 +
                        "[0m\n");
            }
    }


    // algorhtym to decide if it's worth it to buy a new building
    private boolean evolve() {
        if (upgradePrice.compareTo(new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1))).getText()
                .replaceAll("\\D+", ""))) < 0) {
            upgrade = true;
            goal = By.cssSelector(".upgrade.enabled");
            return true;
        }
        if (new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1))).getText().replaceAll("\\D+", ""))
                .compareTo(new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1))).getText()
                        .replaceAll("\\D+", ""))) > 0) {
            goal = By.cssSelector("#product" + buildings + ".enabled");
            goalName = driver.findElement(By.id("productName" + buildings))
                    .getAttribute("textContent");
            System.out.println("\nProfitable to buy a new unlocked building. Setting goal for " +
                    (char) 27 + "[32m" + goalName + (char) 27 + "[0m\n");
            buyNewBuilding = true;
            return true;
        }
        int minutesTo;
        minutesTo = (new BigDecimal(driver.findElement(By.id("productPrice" + buildings)).getText()
                .replaceAll("\\D+", ""))
                .divide(cps, 0, RoundingMode.UP))
                .divide(BigDecimal.valueOf(60), 0, RoundingMode.UP).intValue();
        System.out.println((char) 27 + "[36m" + "\n > Next building can be afforded in " + minutesTo + (char) 27 +
                "[0m " +
                "Must be under " + minutes + " minutes. Price for next building: " +
                driver.findElement(By.id("productPrice" + buildings)).getText()
                        .replaceAll("\\D+", ""));

        if (minutesTo < minutes) {
            goal = By.cssSelector("#product" + buildings + ".enabled");
            goalName = driver.findElement(By.id("productName" + buildings))
                    .getAttribute("textContent");
            System.out.println("\nProfitable to buy a new unlocked building. Setting goal for " +
                    (char) 27 + "[32m" + goalName + (char) 27 + "[0m \n");
            buyNewBuilding = true;
            return true;
        }
        return false;

    }

    // updates the cookies per second
    private void updateCps() {
        if (isElementPresent(By.cssSelector(".pieTimer"))) {
            System.out.println((char) 27 + "[31m> Cookie multiplier underway. Not updating cps" + (char) 27 + "[0m");
        }
        while (true) {
            try {
                cps = new BigDecimal(driver.findElement(By.cssSelector("#cookies div")).getText()
                        .replaceAll("[^\\d.]", ""));
                System.out.println((char) 27 + "[36m -cps updated:" + (char) 27 + "[0m " + cps + "\n");
                break;
            } catch (Exception e) {
                System.out.println((char) 27 + "[31m -cps error! retrying." + (char) 27 + "[0m");
            }
        }
    }

    // reads save code from txt file
    private void read() {

        System.out.println("\n-- Importing Save! --\n");
        try {
            driver.findElement(By.linkText("Import save")).click();
            File txt = new File("CookieSave.txt");
            Scanner sc = new Scanner(txt);
            sc.useDelimiter("\\Z");
            driver.findElement(By.id("textareaPrompt")).sendKeys(sc.next());
            driver.findElement(By.linkText("Load")).click();
            try {
                driver.findElement(By.cssSelector("#bigCookie")).click();
            } catch (Exception e) {
                driver.findElement(By.linkText("Nevermind")).click();
                System.out.println((char) 27 + "[31m\n   !!! Corrupted Save code !!!\n" + (char) 27 + "[0m");
            }
            System.out.println("\n-- Save game imported --\n");
        } catch (Exception e) {
            e.getMessage();
            driver.findElement(By.linkText("Nevermind")).click();
            System.out.println((char) 27 + "[31m\n !! -- Savegame could not be imported! Check that file " +
                    "is present-- !!\n" + (char) 27 + "[0m");
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
        } catch (Exception io) {
            System.out.println(io.getMessage());

        }
        driver.findElement(By.linkText("All done!")).click();
        //System.out.println("\n      >> Game Saved! @ " + ZonedDateTime.now().toLocalTime().truncatedTo
        //        (ChronoUnit.SECONDS) + "\n");
        System.out.println((char) 27 + "[35m \n      >> Game Saved! @ " + ZonedDateTime.now().toLocalTime
                ().truncatedTo(ChronoUnit.SECONDS) + (char) 27 + "[0m");
    }

    // foolproof method of clicking sometbhing
    private void cClick(By element) {

    }

    // verifies element is present
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    // getter for the loop bool
    public boolean isLoop() {
        return loop;
    }

    // setter for the loop bool
    public void setLoop(boolean bool) {
        this.loop = bool;
    }

    // returns if the user wants to buy an upgrade or not
    public boolean isUpgrade() {
        return upgrade;
    }

    // sets the decision to buy or not upgrades
    public void setUpgrade(boolean bool) {
        this.upgrade = bool;
    }

    // returns if the user wants to buy an upgrade or not
    public boolean isBuyNewBuilding() {
        return buyNewBuilding;
    }

    // sets the decision to buy or not upgrades
    public void setBuyNewBuilding(boolean bool) {
        this.buyNewBuilding = bool;
    }
}


