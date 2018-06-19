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

    public WebDriver driver;
    private By goal;
    private String goalName;
    private String upgradeName;
    private int cycleLength;
    private int buildings;
    private double minutes;
    private BigDecimal cps;
    private boolean newBuildingFound;
    private boolean buyNewBuilding; // user decides if he wishes to save for the unlocked building
    private boolean upgrade; // user decides if he wishes to save for next upgrade
    private volatile boolean loop; // user decides if to loop or end program
    private volatile boolean buy; // user decides if the program will ony click or if it will buy also


    CookieClicker(WebDriver browser) {

        driver = browser;
        driver.manage().window().maximize();
        driver.get("http://orteil.dashnet.org/cookieclicker/");

        cycleLength = 1;
        loop = true;
        buy = true;
        minutes = 1;
        upgrade = false;
        upgradeName = "";
        buyNewBuilding = false;
        newBuildingFound = false;
        cps = new BigDecimal(0);
        try {
            Thread.sleep(3000);
            driver.findElement(By.id("prefsButton")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // importing save and disabling resource intensive things
    void setUp() {

        System.out.println("\nSetup Sequence!\n");

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
        /*
        try {
            driver.findElement(By.linkText("Numbers ON")).click();
        } catch (Exception e) {
            System.out.println("Number button already depressed");
        }
        */
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
            driver.findElement(By.linkText("Short numbers ON")).click();
        } catch (Exception e) {
            System.out.println("Short numbers button already depressed");
        }
        try {
            driver.findElement(By.linkText("Fast notes OFF")).click();
        } catch (Exception e) {
            System.out.println("Fast notes button already pressed");
        }
        try {
            driver.findElement(By.linkText("Defocus OFF")).click();
        } catch (Exception e) {
            System.out.println("Defocus button already pressed");
        }

        driver.findElement(By.cssSelector(".cc_btn_accept_all")).click();

        newBuilding();
        if (buildings == 1) {
            goal = By.cssSelector("#product0" + ".enabled");
            goalName = driver.findElement(By.id("productName0")).getText();
        } else if (!buyNewBuilding && buildings != 0)
            getGoal();

        //driver.findElement(By.id("storeBulk10")).click();


    }

    // actual run of program
    void cookieRobot() throws InterruptedException {
        System.out.print("\n>> New Cycle Starting. ");
        if (buy) {
            if (upgrade)
                System.out.println("Will buy a new upgrade.\n");
            else if (buyNewBuilding)
                System.out.println("Will buy a new building.\n");
            else
                System.out.println("Will buy the most efficient building.\n");
        } else System.out.println("Only clicking.");
        for (int loops = 0; loops < cycleLength; loops++)
            for (int clicks = 0; clicks < 15; clicks++) {
                driver.findElement(By.cssSelector("#bigCookie")).click();
                if (isElementPresent(By.cssSelector(".shimmer"))) goldenCookie();

            }
        if (isBuy())
            buy();

        save();

        if (!isLoop())
            System.out.println("Automation ended. Please choose another action or close window to end program");
    }

    // golden cookie
    private void goldenCookie() throws InterruptedException {
        while (true) {
            try {
                driver.findElement(By.cssSelector(".shimmer")).click();
                break;
            } catch (Exception e) {
                System.out.println("Golden cookie fail. Retrying...");
            }
        }
        System.out.println("!! Golden cookie found !!\n");

        while (true) {
            try {
                System.out.println(driver.findElement(By.id("particle0")).getText() + "\n");
                break;
            } catch (Exception e) {
                System.out.println("Golden cookie text fail. Retrying...");
            }
        }
    }

    // buying method
    private void buy() {
        if (isElementPresent(goal)) {
            driver.findElement(goal).click();
            System.out.println("  ++ " + goalName + " has been bought.\n");
            if (upgrade) {
                System.out.println(upgradeName + "\n");
                upgrade = false;
            }
            if (cycleLength > 1) {
                cycleLength--;
                System.out.println(" Decreasing number of turns per cycle to " + cycleLength + "\n");
            }
            if (buyNewBuilding) {
                buyNewBuilding = false;
                newBuilding();
            }
            updateCps();
            getGoal();
        } else {
            cycleLength++;
            System.out.println(goalName + " could not be afforded. Trying again " + "next cycle..." +
                    " Increasing number of turns per cycle to " + cycleLength + "\n");
        }
    }

    // retrieves the next upgrade's price
    private BigDecimal getUpgradePrice() {
        BigDecimal upgradePrice;
        while (true)
            try {
                Actions move = new Actions(driver);
                move.moveToElement(driver.findElement(By.cssSelector("#upgrade0"))).build().perform();
                Thread.sleep(200);
                upgradePrice = new BigDecimal(driver.findElement(By.cssSelector("#tooltip .price")).getAttribute
                        ("textContent").replaceAll("[^\\d]", ""));
                if (upgradePrice != null) {
                    System.out.println(" -upgrade price pull succeeded " + upgradePrice + "\n");
                    break;
                }
                System.out.println(" -upgrade price pull error. retrying..");
            } catch (Exception e) {
                System.out.println(" -upgrade price pull error. retrying..");
            }
        return upgradePrice;
    }

    // retrieves the next upgrade's text
    private void getUpgrade() {
        while (true)
            try {
                Actions move = new Actions(driver);
                move.moveToElement(driver.findElement(By.cssSelector("#upgrade0"))).build().perform();
                Thread.sleep(500);
                goalName = driver.findElement(By.cssSelector("#tooltip .name")).getAttribute("textContent");
                upgradeName = driver.findElement(By.cssSelector("#tooltip .description")).getAttribute("textContent");
                if (!upgradeName.isEmpty() && !goalName.isEmpty()) {
                    System.out.println(" -upgrade text pull succeeded.\n");
                    break;
                }

                System.out.println(" -upgrade pull error. retrying..");
            } catch (Exception e) {
                System.out.println(" -upgrade pull error. retrying..");
            }
    }

    // pulls number of already owned buildings and the next new unlocked building
    void newBuilding() {
        int count = 0;
        for (int index = 0; index < 20; index++) {
            if (!isElementPresent(By.id("product" + index)))
                break;
            if (driver.findElement(By.id("productOwned" + index)).getText().isEmpty()) {
                newBuildingFound = true;
                System.out.print("\nNew unlocked building found: " + driver.findElement(By.id
                        ("productName" + index)).getAttribute("textContent"));
                break;
            } else count++;
        }
        buildings = count;
        if (count > 0)
            minutes = count * 2.25;
        if (!newBuildingFound) {
            System.out.print("No new building found");
        }

        System.out.println(". Number of already bought buildings: " + count + "\n");
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
    void getGoal() {
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
                        Thread.sleep(100);
                        producing = new BigDecimal(driver.findElement(By.cssSelector("div.data b")).getAttribute
                                ("textContent").replaceAll("\\D+", ""));
                        System.out.print("Price for a " + driver.findElement(By.id("productName" + i)).getText() + " " +
                                "is: " +
                                new BigDecimal(driver.findElement(By.id("productPrice" + i)).getText().replaceAll
                                        ("\\D+", "")) + ", and it produces: " + producing);
                        eff = (new BigDecimal(driver.findElement(By.id("productPrice" + i)).getText().replaceAll
                                ("\\D+", "")).divide(producing, 0, RoundingMode.DOWN)).longValue();
                        System.out.println(", with an effiency of: " + eff);
                        if (eff == Math.min(eff, min)) {
                            min = eff;
                            index = i;
                        }
                    } catch (Exception e) {

                        System.out.println("//// Failed to get number of producing cookies for " + driver
                                .findElement(By.id("productName" + i)).getText());

                    }
                }

                goal = By.cssSelector("#product" + index + ".enabled");
                goalName = driver.findElement(By.id("productName" + index)).getText();
                System.out.println("\n* Goal has been set to buy: " + goalName + " \n");
            }
    }

    // algorhtym to decide if it's worth it to buy a new building or upgrade
    private boolean evolve() {
        if (getUpgradePrice().compareTo(new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1)))
                .getText()
                .replaceAll("\\D+", ""))) < 0) {
            upgrade = true;
            getUpgrade();
            goal = By.cssSelector("#upgrade0.enabled");
            System.out.println("Profitable to buy a new upgrade. Setting goal for " + goalName + "\n");
            return true;
        }
 /*
            if (new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1))).getText().replaceAll
            ("\\D+", ""))
                    .compareTo(new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1))).getText()
                            .replaceAll("\\D+", ""))) > 0) {
                goal = By.cssSelector("#product" + buildings + ".enabled");
                goalName = driver.findElement(By.id("productName" + buildings))
                        .getAttribute("textContent");
                System.out.println("Profitable to buy a new unlocked building. Setting goal for " + goalName + "\n");
                buyNewBuilding = true;
                return true;
            }
*/
        try {
            double minutesTo;
            minutesTo = (new BigDecimal(driver.findElement(By.id("productPrice" + buildings)).getText()
                    .replaceAll("\\D+", "")).divide(cps, 2, RoundingMode.UP))
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.UP).doubleValue();
            System.out.println(" > Next building can be afforded in " + minutesTo + " minutes. Must be under " +
                    minutes + " minutes. Price for next building: " +
                    driver.findElement(By.id("productPrice" + buildings)).getText().replaceAll("\\D+", ""));


            if (minutesTo < minutes) {
                goal = By.cssSelector("#product" + buildings + ".enabled");
                goalName = driver.findElement(By.id("productName" + buildings))
                        .getAttribute("textContent");
                System.out.println("Profitable to buy a new unlocked building. Setting goal for " + goalName + "\n");
                buyNewBuilding = true;
                return true;
            }
        } catch (ArithmeticException e) {
            System.out.println("ERROR!! Skipping new building calculation. Following error has been " +
                    "encountered: \"" + e.getMessage() + "\"");
        }

        return false;

    }

    // updates the cookies per second
    private void updateCps() {
        if (isElementPresent(By.cssSelector(".pieTimer"))) {
            System.out.println((char) 27 + "[31m> Cookie multiplier underway. Not updating cps\n");
        } else while (true) {
            try {
                cps = new BigDecimal(driver.findElement(By.cssSelector("#cookies div")).getAttribute("textContent")
                        .replaceAll("[^\\d.]", ""));
                System.out.println(" -cps updated:" + cps + "\n");
                break;
            } catch (Exception e) {
                System.out.println(" -cps error! retrying.");
            }
        }
    }

    // reads save code from txt file
    void read() {

        System.out.println("-- Importing Save! --\n");
        try {
            driver.findElement(By.linkText("Import save")).click();
            File txt = new File("CookieSave");
            Scanner sc = new Scanner(txt);
            sc.useDelimiter("\\Z");
            driver.findElement(By.id("textareaPrompt")).sendKeys(sc.next());
            driver.findElement(By.linkText("Load")).click();
            System.out.println("\n-- Save game imported --\n");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("\n !! -- Savegame could not be imported! Check that file is present-- !!\n");
        }
    }

    // reads a save code from the input box
    void read(String saveGame) {
        System.out.println("-- Importing Save! --\n");
        try {
            driver.findElement(By.linkText("Import save")).click();
            driver.findElement(By.id("textareaPrompt")).sendKeys(saveGame);
            driver.findElement(By.linkText("Load")).click();
            System.out.println("\n-- Save game imported --\n");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("\n !! -- Savegame could not be imported! Check that the code is valid !!\n");
        }
    }

    // saves game to txt file
    private void save() {
        try {
            driver.findElement(By.linkText("Save")).click();
            driver.findElement(By.linkText("Export save")).click();
            File txt = new File("CookieSave");
            PrintWriter saveCookies;
            saveCookies = new PrintWriter(txt);
            saveCookies.println(driver.findElement(By.id("textareaPrompt")).getText());
            saveCookies.close();
            driver.findElement(By.linkText("All done!")).click();
            System.out.println("      >> Game Saved! @ " + ZonedDateTime.now().toLocalTime
                    ().truncatedTo(ChronoUnit.SECONDS));
        } catch (Exception e) {
            System.out.println("      >> Game could not be saved at this time!\n");
        }
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

    // getter for the loop bool
    boolean isLoop() {
        return loop;
    }

    // setter for the loop bool
    void setLoop(boolean bool) {
        this.loop = bool;
    }

    private boolean isBuy() {
        return buy;
    }

    void setBuy(boolean buy) {
        this.buy = buy;
    }

    void setCycleLength(int turns) {
        cycleLength = turns;
    }
}