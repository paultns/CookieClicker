import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    WebDriver driver;
    private By goal;
    private String goalName;
    private String upgradeText;
    private int cycleLength;
    private int buildings;
    private double minutes;
    private BigDecimal cps;
    private boolean newBuildingFound;
    private boolean buyNewBuilding; // user decides if he wishes to save for the unlocked building
    private boolean upgrade; // user decides if he wishes to save for next upgrade
    private volatile boolean loop; // user decides if to loop or end program
    private volatile boolean buy; // user decides if the program will ony click or if it will buy also
    private volatile boolean fullConsole; //user decides if he wishes to see all the details in the console
    private int failCount;

    CookieClicker(WebDriver browser) {

        driver = browser;
        driver.manage().window().maximize();
        driver.get("http://orteil.dashnet.org/cookieclicker/");

        cycleLength = 1;
        loop = true;
        buy = true;
        minutes = 1;
        upgrade = false;
        upgradeText = "";
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

    // importing save from file and disabling resource intensive things
    void setUp(boolean newgame) {

        System.out.println("\nSetup Sequence!\n");

        if (!newgame) read();

        try {
            driver.findElement(By.linkText("Fancy graphics ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Fancy graphics button already depressed");
        }
        try {
            driver.findElement(By.linkText("Particles ON")).click();
        } catch (Exception e) {
            if (fullConsole)
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
            if (fullConsole)
                System.out.println("Milk button already depressed");
        }
        try {
            driver.findElement(By.linkText("Cursors ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Cursors button already depressed");
        }
        try {
            driver.findElement(By.linkText("Wobbly cookie ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Wobbly cookie button already depressed");
        }
        try {
            driver.findElement(By.linkText("Alt cookie sound ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Alt cookie sound button already depressed");
        }
        try {
            driver.findElement(By.linkText("Short numbers ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Short numbers button already depressed");
        }
        try {
            driver.findElement(By.linkText("Fast notes OFF")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Fast notes button already pressed");
        }
        try {
            driver.findElement(By.linkText("Defocus OFF")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Defocus button already pressed");
        }
        System.out.println();
        driver.findElement(By.cssSelector(".cc_btn_accept_all")).click();

        CookieFrame.toggleStart(true);
        CookieFrame.toggleShutdown(true);
        //driver.findElement(By.id("storeBulk10")).click();
    }

    // updates the goal of the game before starting the run of the game
    // was needed to be implemented as a standalone method
    // because user might pause, interact with the game, and unpause
    // in which case the game would be stuck on the old gola
    void update() {
        updateCps();
        newBuilding();
        if (buildings == 1) {
            goal = By.cssSelector("#product0" + ".enabled");
            goalName = driver.findElement(By.id("productName0")).getText();
        } else if (!buyNewBuilding && buildings != 0)
            getGoal();
    }

    // importing save from text and disabling resource intensive things
    void setUp(String savegame) {

        System.out.println("\nSetup Sequence!\n");

        read(savegame);

        try {
            driver.findElement(By.linkText("Fancy graphics ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Fancy graphics button already depressed");
        }
        try {
            driver.findElement(By.linkText("Particles ON")).click();
        } catch (Exception e) {
            if (fullConsole)
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
            if (fullConsole)
                System.out.println("Milk button already depressed");
        }
        try {
            driver.findElement(By.linkText("Cursors ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Cursors button already depressed");
        }
        try {
            driver.findElement(By.linkText("Wobbly cookie ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Wobbly cookie button already depressed");
        }
        try {
            driver.findElement(By.linkText("Alt cookie sound ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Alt cookie sound button already depressed");
        }
        try {
            driver.findElement(By.linkText("Short numbers ON")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Short numbers button already depressed");
        }
        try {
            driver.findElement(By.linkText("Fast notes OFF")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Fast notes button already pressed");
        }
        try {
            driver.findElement(By.linkText("Defocus OFF")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Defocus button already pressed");
        }

        driver.findElement(By.cssSelector(".cc_btn_accept_all")).click();
        //update();
        CookieFrame.toggleStart(true);
        CookieFrame.toggleShutdown(true);
        //driver.findElement(By.id("storeBulk10")).click();
    }

    // actual run of program
    void cookieRobot() {
        if (fullConsole)
            if (buy) {
                System.out.print("\n>> New Cycle Starting. ");
                if (upgrade)
                    System.out.println("Will buy a new upgrade.\n");
                else if (buyNewBuilding)
                    System.out.println("Will buy a new building.\n");
                else
                    System.out.println("Will buy the most efficient building.\n");
            }
        for (int loops = 0; loops < cycleLength; loops++)
            for (int clicks = 0; clicks < 15; clicks++) {
                try {
                    driver.findElement(By.cssSelector("#bigCookie")).click();
                    failCount = 0;
                } catch (Exception er) {
                    if (fullConsole)
                        System.out.println(er.getMessage() + "\n");
                    if (failCount > 10) {
                        System.out.println("Program will end after current cycle finishes..\n");
                        CookieFrame.setStart("Start");
                        setLoop(false);
                        CookieFrame.toggleGame(false);
                        CookieFrame.toggleStart(false);
                        CookieFrame.toggleEnd(false);
                        break;
                    }
                    failCount++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Thread Sleep Fail");
                        e.printStackTrace();
                    }
                    System.out.print("Big Cookie cannot be found");
                    for (int i = 0; i < failCount; i++)
                        System.out.print(".");
                    System.out.println();

                }
                if (isElementPresent(By.cssSelector(".shimmer"))) goldenCookie();
            }
        if (buy)
            buy();

        if (!isLoop()) {
            System.out.println("Automation ended. Please choose another action or close window to end program");
            CookieFrame.toggleStart(true);
            CookieFrame.toggleShutdown(true);
            save();
        }
    }

    // clicks golden cookies and displays it's text
    private void goldenCookie() {
        while (true) {
            try {
                driver.findElement(By.cssSelector(".shimmer")).click();
                break;
            } catch (Exception e) {
                if (fullConsole)
                    System.out.println("Golden cookie fail. Retrying...");
            }
        }
        System.out.println("!! Golden cookie found !!\n");

        while (true) {
            try {
                System.out.println(driver.findElement(By.id("particle0")).getText() + "\n");
                break;
            } catch (Exception e) {
                if (fullConsole)
                    System.out.println("Golden cookie text fail. Retrying...");
            }
        }
    }

    // buys the current set goal if available, and finds, updates the cps, and finds the next goal
    // increases duration of next turn if goal can not be afforded
    private void buy() {
        if (isElementPresent(goal)) {
            driver.findElement(goal).click();
            System.out.println("  ++ " + goalName + " has been bought.\n");
            if (upgrade) {
                System.out.println(upgradeText + "\n");
                upgrade = false;
            } else if (buyNewBuilding) {
                buyNewBuilding = false;
                newBuilding();
            }
            if (cycleLength > 1) {
                cycleLength--;
                if (fullConsole)
                    System.out.println(" Decreasing number of turns per cycle to " + cycleLength + "\n");
            }
            updateCps();
            getGoal();
        } else {
            cycleLength++;
            if (fullConsole)
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
                if (fullConsole)
                    System.out.println(" -price for next upgrade has been updated (" + upgradePrice + " cookies)\n");
                break;
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",
                        driver.findElement(By.cssSelector("#upgrade0")));
                if (fullConsole)
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
                Thread.sleep(300);
                goalName = driver.findElement(By.cssSelector("#tooltip .name")).getAttribute("textContent");
                upgradeText = driver.findElement(By.cssSelector("#tooltip .description")).getAttribute("textContent");
                if (!upgradeText.isEmpty() && !goalName.isEmpty()) {
                    if (fullConsole)
                        System.out.println(" -upgrade text pull succeeded.\n");
                    break;
                }
                if (fullConsole)
                    System.out.println(" -upgrade pull error. retrying..");
            } catch (Exception e) {
                if (fullConsole)
                    System.out.println(" -upgrade pull error. retrying..");
            }
    }

    // pulls number of already owned buildings and the next new unlocked building
    private void newBuilding() {
        int count = 0;
        for (int index = 0; index < 20; index++) {
            if (!isElementPresent(By.id("product" + index)))
                break;
            if (driver.findElement(By.id("productOwned" + index)).getText().isEmpty()) {
                newBuildingFound = true;
                if (fullConsole)
                    System.out.print("\nNew unlocked building found: " + driver.findElement(By.id
                            ("productName" + index)).getAttribute("textContent"));
                break;
            } else count++;
        }
        buildings = count;
        if (count > 0)
            minutes = count * 2.25;
        if (!newBuildingFound) {
            if (fullConsole)
                System.out.print("No new building found");
        }
        if (fullConsole)
            System.out.println(". Number of already bought buildings: " + count + "\n");
        if (buildings == 0) {
            if (fullConsole)
                System.out.println("\nNo pre-owned buildings found. Saving for the next new building\n");
            buyNewBuilding = true;
            goal = By.id("product0");
            goalName = driver.findElement(By.id("productName0")).getAttribute("textContent");
            if (fullConsole)
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
                if (fullConsole)
                    System.out.println("\nCalculating goal..\n");
                for (int i = 0; i < buildings; i++) {
                    try {
                        move.moveToElement(driver.findElement(By.cssSelector("#product" + i))).build().perform();
                        Thread.sleep(100);
                        producing = new BigDecimal(driver.findElement(By.cssSelector("div.data b")).getAttribute
                                ("textContent").replaceAll("\\D+", ""));
                        if (fullConsole)
                            System.out.print("Price for a " + driver.findElement(By.id("productName" + i)).getText()
                                    + " " +
                                    "is: " +
                                    new BigDecimal(driver.findElement(By.id("productPrice" + i)).getText().replaceAll
                                            ("\\D+", "")) + ", and it produces: " + producing);
                        eff = (new BigDecimal(driver.findElement(By.id("productPrice" + i)).getText().replaceAll
                                ("\\D+", "")).divide(producing, 0, RoundingMode.DOWN)).longValue();
                        if (fullConsole)
                            System.out.println(", with an efficiency of: " + eff);
                        if (eff == Math.min(eff, min)) {
                            min = eff;
                            index = i;
                        }
                    } catch (Exception e) {
                        if (fullConsole)
                            System.out.println("//// Failed to get number of producing cookies for " + driver
                                    .findElement(By.id("productName" + i)).getText());

                    }
                }

                goal = By.cssSelector("#product" + index + ".enabled");
                goalName = driver.findElement(By.id("productName" + index)).getText();
                System.out.println("* Goal has been set to buy: " + goalName + " \n");
            }
    }

    // algorithm to decide if it's worth it to buy a new building or upgrade
    private boolean evolve() {
        if (getUpgradePrice().compareTo(new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1)))
                .getText().replaceAll("\\D+", ""))) < 0) {
            upgrade = true;
            getUpgrade();
            goal = By.cssSelector("#upgrade0.enabled");
            if (fullConsole)
                System.out.println("Profitable to buy a new upgrade. Setting goal for " + goalName + "\n");
            else
                System.out.println("* Goal has been set to buy: " + goalName + " \n");
            return true;
        }
        try {
            double minutesTo;
            minutesTo = (new BigDecimal(driver.findElement(By.id("productPrice" + buildings)).getText()
                    .replaceAll("\\D+", "")).divide(cps, 2, RoundingMode.UP))
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.UP).doubleValue();
            if (fullConsole)
                System.out.println(" > Next building can be afforded in " + minutesTo + " minutes. Must be under " +
                        minutes + " minutes. Price for next building: " +
                        driver.findElement(By.id("productPrice" + buildings)).getText().replaceAll("\\D+", ""));


            if (minutesTo < minutes) {
                goal = By.cssSelector("#product" + buildings + ".enabled");
                goalName = driver.findElement(By.id("productName" + buildings))
                        .getAttribute("textContent");
                if (fullConsole)
                    System.out.println("Profitable to buy a new unlocked building. Setting goal for " + goalName +
                            "\n");
                else
                    System.out.println("* Goal has been set to buy: " + goalName + " \n");
                buyNewBuilding = true;
                return true;
            }
        } catch (ArithmeticException e) {
            if (fullConsole)
                System.out.println("ERROR!! Skipping new building calculation. Following error has been " +
                        "encountered: \"" + e.getMessage() + "\"");
        }
        return false;
    }

    // updates the cookies per second
    private void updateCps() {
        if (isElementPresent(By.cssSelector(".pieTimer"))) {
            if (fullConsole)
                System.out.println("> Cookie multiplier underway. Not updating cps");
        } else while (true) {
            try {
                cps = new BigDecimal(driver.findElement(By.cssSelector("#cookies div")).getAttribute("textContent")
                        .replaceAll("[^\\d.]", ""));
                if (fullConsole)
                    System.out.println(" -cps updated:" + cps + "\n");
                break;
            } catch (Exception e) {
                if (fullConsole)
                    System.out.println(" -cps error! retrying.");
            }
        }
    }

    // reads save code from txt file
    private void read() {

        System.out.println("-- Importing Save! --\n");
        try {
            if (!isElementPresent(By.id("prefsButton")))
                driver.findElement(By.id("prefsButton")).click();
            driver.findElement(By.linkText("Import save")).click();
            File txt = new File("CookieSave");
            Scanner sc = new Scanner(txt);
            sc.useDelimiter("\\Z");
            driver.findElement(By.id("textareaPrompt")).sendKeys(sc.next());
            driver.findElement(By.linkText("Load")).click();
            System.out.println("\n-- Save game imported! --\n");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("\n !! -- Savegame could not be imported! Check that file is present-- !!\n");
        }
    }

    // reads a save code from the input box
    private void read(String saveGame) {
        System.out.println("-- Importing Save! --\n");
        try {
            if (!isElementPresent(By.id("prefsButton")))
                driver.findElement(By.id("prefsButton")).click();
            driver.findElement(By.linkText("Import save")).click();
            driver.findElement(By.id("textareaPrompt")).sendKeys(saveGame);
            driver.findElement(By.linkText("Load")).click();
            System.out.println("\n-- Save game imported! --\n");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("\n !! -- Savegame could not be imported! Check that the code is valid !!\n");
        }
    }

    // saves game to txt file
    private void save() {
        try {
            if (!isElementPresent(By.linkText("Save")))
                driver.findElement(By.id("prefsButton")).click();
            driver.findElement(By.linkText("Save")).click();
            File txt = new File("CookieSave");
            PrintWriter saveCookies;
            saveCookies = new PrintWriter(txt);
            driver.findElement(By.linkText("Export save")).click();
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

    void setBuy(boolean buy) {
        this.buy = buy;
    }

    void setCycleLength(int turns) {
        cycleLength = turns;
    }

    void setConsole(boolean onoff) {
        fullConsole = onoff;
    }
}