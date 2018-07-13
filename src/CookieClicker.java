import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.PrintWriter;
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
    private String cps;
    private boolean saveForNewBuilding; // user decides if he wishes to save for the unlocked building
    private boolean upgrade; // user decides if he wishes to save for next upgrade
    private volatile boolean fullConsole; //user decides if he wishes to see all the details in the console
    volatile boolean buy;
    volatile boolean clickGoldenCookie;
    volatile boolean autoSave;
    volatile boolean stop;
    private int failcount = 0;
    private long timeStart;


    CookieClicker(WebDriver browser) {

        autoSave = false;
        clickGoldenCookie = false;
        buy = false;
        stop = false;
        driver = browser;
        driver.manage().window().maximize();
        driver.get("http://orteil.dashnet.org/cookieclicker/");
        fullConsole = true;
        cycleLength = 1;
        minutes = 1;
        upgrade = false;
        upgradeText = "";
        saveForNewBuilding = false;
        cps = "0";
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
            driver.findElement(By.linkText("Short numbers OFF")).click();
        } catch (Exception e) {
            if (fullConsole)
                System.out.println("Short numbers button already pressed");
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
    void runner() throws InterruptedException {
        for (int i = 0; i < cycleLength; i++) {
            if (stop)
                return;
            if (clickGoldenCookie)
                goldenCookie();
            try {
                driver.findElement(By.cssSelector("#bigCookie")).click();
                failcount = 0;
            } catch (Exception e) {
                failcount++;
                System.out.println("Big cookie cannot be clicked.. " + failcount);
                if (failcount == 10) {
                    CookieFrame.loop = false;
                    CookieFrame.start.doClick();
                }
                Thread.sleep(1000);
            }
        }
        if (buy) {
            System.out.println("Checkpoint @ " + ZonedDateTime.now().toLocalTime
                    ().truncatedTo(ChronoUnit.SECONDS));
            System.out.println("Seconds elapsed: " + ((System.currentTimeMillis() - timeStart) / 1000));
            if (goal == null || goalName == null)
                getGoal();
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
                clearGoals();
                updateCps();
                getGoal();
            } else {
                System.out.println(goalName + " could not be afforded. Trying again @ ");
                getGoal();
            }
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
            } else {
                count++;
                try {
                    System.out.println(driver.findElement(By.id("productName" + index)).getText());
                    Buildings.convert(driver.findElement(By.id("productPrice" + index)).getAttribute
                            ("textContent"));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
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
    private String getUpgradePrice() {
        String upgradePrice = null;
        while (upgradePrice == null)
            try {
                Actions move = new Actions(driver);
                move.moveToElement(driver.findElement(By.cssSelector("#upgrade0"))).build().perform();
                Thread.sleep(200);
                upgradePrice = driver.findElement(By.cssSelector("#tooltip .price")).getAttribute("textContent");
                //  if (fullConsole)
                //      System.out.println(" -price for next upgrade has been updated (" + upgradePrice + " cookies)\n");
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

    // pulls efficiency of each building
    private void getGoal() {
        boolean found = false;
        String goalPrice;
        //System.out.println("The last building price is: " + new BigDecimal(driver.findElement(By.id
        //        ("productPrice" + (buildings - 1)))
        //        .getText().replaceAll("[^\\d.]", "")));
        goalPrice = getUpgradePrice();
        if (Buildings.compare(goalPrice, driver.findElement(By.id("productPrice" + (buildings - 1))).getText()) < 0) {
            getUpgrade();
            found = true;
        }

        // checks if goal is to be set on buying a new building
        if (!found) {
            if (buildings == 15)
                System.out.println("No new unlocked buildings found, cannot buy new buildings");
            else {
                try {
                    goalPrice = driver.findElement(By.id("productPrice" + buildings)).getText();

                    double minutesTo;
                    minutesTo = Buildings.divide(goalPrice, cps);

                    if (fullConsole)
                        System.out.println(" > Next building can be afforded in " + minutesTo + " minutes. Must be " +
                                "under " + minutes + " minutes. Price for next building: " + goalPrice);
                    if (minutesTo < minutes) {
                        setGoalNewBuilding();
                        found = true;
                    }
                } catch (ArithmeticException e) {
                    if (fullConsole)
                        System.out.println("ERROR!! Skipping new building calculation. Following error has been " +
                                "encountered: \"" + e.getMessage() + "\"");
                }
            }
        }

        // calculates the most efficient building
        if (!found) {
            Actions move = new Actions(driver);
            String producing;
            String price;
            int index = 0;
            double min = Integer.MAX_VALUE;
            double eff;
            if (fullConsole)
                System.out.println("\nCalculating goal..\n");
            for (int i = 0; i < buildings; i++) {
                while (true) {
                    try {
                        move.moveToElement(driver.findElement(By.cssSelector("#product" + i))).build().perform();
                        //Thread.sleep(800);
                        producing = driver.findElement(By.cssSelector("div.data b")).getAttribute("textContent");
                        price = driver.findElement(By.id("productPrice" + i)).getAttribute("textContent");
                        if (fullConsole)
                            System.out.print("Price for a " + driver.findElement(By.id("productName" + i)).getAttribute
                                            ("textContent") + " " + "is: " + price + ", and it produces: " + producing);
                        eff = Buildings.divide(price, producing);
                        if (fullConsole)
                            System.out.println(", with an efficiency of: " + eff);
                        if (eff == Math.min(eff, min)) {
                            min = eff;
                            index = i;
                            goalPrice = driver.findElement(By.id("productPrice" + i)).getAttribute("textContent");
                        }
                        break;
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",
                                driver.findElement(By.cssSelector("#product" + i)));
                        if (fullConsole)
                            System.out.println("//// Failed to get number of producing cookies for " +
                                    driver.findElement(By.id("productName" + i)).getText());
                    }
                }
            }
            goal = By.cssSelector("#product" + index + ".enabled");
            goalName = driver.findElement(By.id("productName" + index)).getText();
            System.out.println("* Goal has been set to buy: " + goalName + " \n");
        }
        String cookies = null;
        while (cookies == null) {
            try {
                cookies = driver.findElement(By.id("cookies")).getText().split("cookies")[0];
            } catch (Exception e) {
                System.out.println("- could not get number of cookies");
            }
        }
        if (Buildings.compare(cookies, goalPrice) > 0) {
            System.out.println("Goal can be bought, setting cycle length to 1");
            cycleLength = 1;
        } else
            try {
                cycleLength = (int) Buildings.divide(Buildings.substract(goalPrice, cookies), cps);
                //cycleLength = goalPrice.subtract(cookies).divide(cps, 0, RoundingMode.UP).intValue();
                System.out.println(" >> Goale price: " + goalPrice + "\n>> Cookies: " + cookies + "\n>> CPS: " + cps +
                        "\n>> Cycle length has been set to " + cycleLength + "~ seconds @ " + ZonedDateTime.now().toLocalTime
                        ().truncatedTo(ChronoUnit.SECONDS));
                timeStart = System.currentTimeMillis();
                cycleLength = (int) (cycleLength * 3.5);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


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
                cps = driver.findElement(By.cssSelector("#cookies div")).getAttribute("textContent").split(": ")[1];
                if (fullConsole)
                    System.out.println(" -cps updated: " + cps + "\n");
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
        if (isElementPresent(By.cssSelector(".shimmer"))) {
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

    void clearGoals() {
        goal = null;
        goalName = null;
    }

    void setFullConsole(Boolean statement) {
        fullConsole = statement;
    }


}