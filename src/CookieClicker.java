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
    private volatile String goalName;
    private volatile String upgradeText;
    private int cycleLength;
    private int buildings;
    private double minutes;
    private BigDecimal cps;
    private boolean saveForNewBuilding; // user decides if he wishes to save for the unlocked building
    private boolean upgrade; // user decides if he wishes to save for next upgrade
    private volatile boolean fullConsole; //user decides if he wishes to see all the details in the console
    private volatile boolean buyBuildings; // user decides if the program will buy buildings
    private volatile boolean buyUpgrades; // user decides if the program shall buy upgrades
    private volatile boolean buyNewBuildings; // user decides if the program shall a buy a new building
    private volatile boolean clickGoldenCookies; // user decides if the program shall click golden cookies
    private volatile boolean autoSave; // user decides whether the game will be automatically saved
    private volatile boolean stop;
    private int failCount;

    CookieClicker(WebDriver browser) {

        driver = browser;
        driver.manage().window().maximize();
        driver.get("http://orteil.dashnet.org/cookieclicker/");
        fullConsole = true;
        cycleLength = 1;
        minutes = 1;
        upgrade = false;
        upgradeText = "";
        saveForNewBuilding = false;
        cps = new BigDecimal(0);
        try {
            Thread.sleep(3000);
            driver.findElement(By.id("prefsButton")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //stop = false;
    }


    // initial game setup
    // will import save from code, from file, or start new game
    // according to the input
    void setUp(String savegame, String gameType) {

        System.out.println("\nSetup Sequence!\n");

        if (gameType.equals("CONTINUE GAME"))
            read(null);
        else if (gameType.equals("LOAD GAME"))
            read(savegame);
        if (!isElementPresent(By.linkText("Save")))
            driver.findElement(By.id("prefsButton")).click();
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
        driver.findElement(By.id("prefsButton")).click();
        driver.findElement(By.cssSelector(".cc_btn_accept_all")).click();
        update();
        CookieFrame.changeState("START");
        System.out.println();
        //driver.findElement(By.id("storeBulk10")).click();
    }

    // actual run of program
    void cookieRobot() {

        while (true) {

            for (int clicks = 0; clicks < 15 * cycleLength; clicks++) {
                if (stop) return;
                try {
                    driver.findElement(By.cssSelector("#bigCookie")).click();
                    failCount = 0;
                } catch (Exception er) {
                        /*
                        if (fullConsole)
                            System.out.println(er.getMessage() + "\n");
                            */
                    if (failCount > 10) {
                        System.out.println("Program will end after current cycle finishes..\n");
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
                if (clickGoldenCookies && isElementPresent(By.cssSelector(".shimmer"))) goldenCookie();
            }
            if (buyUpgrades || buyBuildings || buyNewBuildings)
                if (goal == null) {
                    if (getGoal())
                        buy();
                } else
                    buy();
            if (autoSave)
                save();
        }
    }

    // updates the goal of the game before starting the run of the game
    // was needed to be implemented as a standalone method
    // because user might pause, interact with the game, and unpause
    // in which case the game would be stuck on the old gola
    private void update() {
        updateCps();
        newBuilding();
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
            } else if (saveForNewBuilding) {
                saveForNewBuilding = false;
                newBuilding();
            }
            if (cycleLength > 1) {
                cycleLength--;
                if (fullConsole)
                    System.out.println(" Decreasing number of turns per cycle to " + cycleLength + "\n");
            }
            clearGoals();
            updateCps();
            if (!getGoal())
                return;
        } else {
            cycleLength++;
            if (fullConsole)
                System.out.println(goalName + " could not be afforded. Trying again " + "next cycle..." +
                        " Increasing number of turns per cycle to " + cycleLength + "\n");
        }
    }

    // pulls number of already owned buildings and the next new unlocked building
    private void newBuilding() {
        int count = 0;

        for (int index = 0; index < 15; index++) {
            if (!isElementPresent(By.id("product" + index)))
                break;
            if (driver.findElement(By.id("productOwned" + index)).getText().isEmpty()) {
                if (fullConsole)
                    System.out.println("New unlocked building found: " + driver.findElement(By.id
                            ("productName" + index)).getAttribute("textContent"));
                break;
            } else count++;
        }
        buildings = count;
        if (count > 0)
            minutes = count * 4.5;
        if (fullConsole)
            System.out.println("Number of already bought buildings: " + count + "\n");
        if (buildings == 0) {
            if (fullConsole)
                System.out.println("No pre-owned buildings found. Saving for the next new building\n");
            saveForNewBuilding = true;
            goal = By.id("product0");
            goalName = driver.findElement(By.id("productName0")).getAttribute("textContent");
            if (fullConsole)
                System.out.println("No buildings detected. Current goal is Cursor");
            //upgrade = false;
        } else if (buildings == 15 && fullConsole)
            System.out.print("No new building found");
    }

    // retrieves the next upgrade's price
    private BigDecimal getUpgradePrice() {
        BigDecimal upgradePrice = null;
        while (upgradePrice == null)
            try {
                Actions move = new Actions(driver);
                move.moveToElement(driver.findElement(By.cssSelector("#upgrade0"))).build().perform();
                Thread.sleep(200);
                upgradePrice = new BigDecimal(driver.findElement(By.cssSelector("#tooltip .price")).getAttribute
                        ("textContent").replaceAll("[^\\d.]", ""));
                if (fullConsole)
                    System.out.println(" -price for next upgrade has been updated (" + upgradePrice + " cookies)\n");
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",
                        driver.findElement(By.cssSelector("#upgrade0")));
                if (fullConsole)
                    System.out.println(" -upgrade price pull error. retrying..");
                if (stop) break;
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
                upgrade = true;
                while (goalName == null)
                    goalName = driver.findElement(By.cssSelector("#tooltip .name")).getAttribute("textContent");
                while (goal == null)
                    goal = By.cssSelector("#upgrade0.enabled");
                upgradeText = driver.findElement(By.cssSelector("#tooltip .description")).getAttribute("textContent");
                if (!upgradeText.isEmpty() && !goalName.isEmpty()) {
                    if (fullConsole)
                        System.out.println("Profitable to buy a new upgrade. Setting goal for " + goalName + "\n");
                    else
                        System.out.println("* Goal has been set to buy: " + goalName + " \n");
                    break;
                }

                if (fullConsole)
                    System.out.println(" -upgrade pull error. retrying..");
            } catch (Exception e) {
                if (fullConsole)
                    System.out.println(" -upgrade pull error. retrying..");
            }
    }

    // pulls efficiency of each building   /// must edit, will not continue if no new buildings are found
    private boolean getGoal() {

        if (buyUpgrades) {
            if (!buyBuildings && !buyNewBuildings) {
                getUpgrade();
                return true;
            }
            System.out.println("The last building price is: " + new BigDecimal(driver.findElement(By.id
                    ("productPrice" + (buildings - 1)))
                    .getText().replaceAll("[^\\d.]", "")));
            if (getUpgradePrice().compareTo(new BigDecimal(driver.findElement(By.id("productPrice" + (buildings - 1)))
                    .getText().replaceAll("[^\\d.]", ""))) < 0) {
                System.out.println("Its Cheaper!");
                getUpgrade();
                return true;
            }
        }
        // checks if goal si to be set on buying a new building
        if (buyNewBuildings) {
            if (!buyBuildings && buildings < 15) {
                setGoalNewBuilding();
                return true;
            }
            if (buildings == 15) {
                System.out.println("No new unlocked buildings found, cannot buy new buildings");
                CookieFrame.buyNewBuildings.setSelected(false);
                if (!buyBuildings) return false;
            } else {
                try {
                    double minutesTo;
                    minutesTo = (new BigDecimal(driver.findElement(By.id("productPrice" + buildings)).getText()
                            .replaceAll("\\D+", "")).divide(cps, 2, RoundingMode.UP))
                            .divide(BigDecimal.valueOf(60), 2, RoundingMode.UP).doubleValue();
                    if (fullConsole)
                        System.out.println(" > Next building can be afforded in " + minutesTo + " minutes. Must be " +
                                "under " +
                                minutes + " minutes. Price for next building: " +
                                driver.findElement(By.id("productPrice" + buildings)).getText().replaceAll("\\D+", ""));
                    if (minutesTo < minutes) {
                        setGoalNewBuilding();
                        return true;
                    }
                } catch (ArithmeticException e) {
                    if (fullConsole)
                        System.out.println("ERROR!! Skipping new building calculation. Following error has been " +
                                "encountered: \"" + e.getMessage() + "\"");
                }
            }
        }
        // calculates the most efficient building
        if (buyBuildings) {
            Actions move = new Actions(driver);
            BigDecimal producing;
            int index = 0;
            long min = Integer.MAX_VALUE;
            long eff;
            if (fullConsole)
                System.out.println("\nCalculating goal..\n");
            for (int i = 0; i < buildings; i++) {
                while (true) {
                    try {
                        move.moveToElement(driver.findElement(By.cssSelector("#product" + i))).build().perform();
                        Thread.sleep(100);
                        producing = new BigDecimal(driver.findElement(By.cssSelector("div.data b")).getAttribute
                                ("textContent").replaceAll("\\D+", ""));
                        if (fullConsole)
                            System.out.print("Price for a " + driver.findElement(By.id("productName" + i))
                                    .getAttribute
                                            ("textContent") + " " + "is: " + new BigDecimal(driver.findElement(By.id
                                    ("productPrice" + i)).getAttribute
                                    ("textContent").replaceAll("\\D+", "")) + ", and it produces: " + producing);
                        eff = (new BigDecimal(driver.findElement(By.id("productPrice" + i)).getAttribute
                                ("textContent").replaceAll("\\D+", "")).divide(producing, 0, RoundingMode.DOWN))
                                .longValue();
                        if (fullConsole)
                            System.out.println(", with an efficiency of: " + eff);
                        if (eff == Math.min(eff, min)) {
                            min = eff;
                            index = i;
                        }
                        break;
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",
                                driver.findElement(By.cssSelector("#product" + i)));
                        if (fullConsole)
                            System.out.println("//// Failed to get number of producing cookies for " +
                                    driver.findElement(By.id("productName" + i)).getText());
                        if (stop)
                            return false;
                    }
                }
            }
            goal = By.cssSelector("#product" + index + ".enabled");
            goalName = driver.findElement(By.id("productName" + index)).getText();
            System.out.println("* Goal has been set to buy: " + goalName + " \n");
            return true;
        }
        return false;
    }

    // will set goal to buy a new unlocked building
    private void setGoalNewBuilding() {
        goal = By.cssSelector("#product" + buildings + ".enabled");
        goalName = driver.findElement(By.id("productName" + buildings)).getAttribute("textContent");
        if (fullConsole)
            System.out.println("Profitable to buy a new unlocked building. Setting goal for " + goalName +
                    "\n");
        else
            System.out.println("* Goal has been set to buy: " + goalName + " \n");
        saveForNewBuilding = true;
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


    // reads a savegame from or from file
    void read(String saveGame) {
        System.out.println("-- Importing Save! --\n");
        if (!isElementPresent(By.linkText("Save")))
            driver.findElement(By.id("prefsButton")).click();
        try {
            if (!isElementPresent(By.id("prefsButton")))
                driver.findElement(By.id("prefsButton")).click();
            driver.findElement(By.linkText("Import save")).click();
            if (saveGame != null)
                driver.findElement(By.id("textareaPrompt")).sendKeys(saveGame);
            else {
                File txt = new File("CookieSave");
                Scanner sc = new Scanner(txt);
                sc.useDelimiter("\\Z");
                driver.findElement(By.id("textareaPrompt")).sendKeys(sc.next());
            }
            driver.findElement(By.linkText("Load")).click();
            driver.findElement(By.id("prefsButton")).click();
            System.out.println("\n-- Save game imported! --\n");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("\n !! -- Savegame could not be imported! Check that the code is valid and file is " +
                    "present!!\n");
        }
    }

    // saves game to txt file
    void save() {
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
            driver.findElement(By.id("prefsButton")).click();
            System.out.println("      >> Game Saved! @ " + ZonedDateTime.now().toLocalTime
                    ().truncatedTo(ChronoUnit.SECONDS));
        } catch (Exception e) {
            System.out.println("      >> Game could not be saved at this time!\n");
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

    // verifies element is present
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // getter and setters

    void setBuyingBuildings(boolean statement) {
        buyBuildings = statement;
    }

    void setBuyingUpgrades(Boolean statement) {
        buyUpgrades = statement;
    }

    void setBuyingNewBuildings(Boolean statement) {
        buyNewBuildings = statement;
    }

    void setClickGoldenCookies(Boolean statement) {
        clickGoldenCookies = statement;
    }

    void setAutoSave(Boolean statement) {
        autoSave = statement;
    }

    void setFullConsole(Boolean statement) {
        fullConsole = statement;
    }

    void stop(Boolean statement) {
        stop = statement;
    }

    void clearGoals() {
        goal = null;
        goalName = null;
    }

}