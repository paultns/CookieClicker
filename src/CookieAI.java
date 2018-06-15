import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CookieAI {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");

        CookieClicker cC = new CookieClicker(new ChromeDriver());

        // set number of unlocked buildings
        cC.setBuildings(10);

        // set number of loops between building buys
        cC.setCycleLength(10);

        cC.setUp();

        cC.getGoal();

        while (cC.isLoop())
            cC.run();


    }
}


// create method to generate number of buildings
// find way to set goal for building with no units
