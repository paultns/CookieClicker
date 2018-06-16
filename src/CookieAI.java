import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CookieAI {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");

        CookieClicker cC = new CookieClicker(new FirefoxDriver());

        // set number of unlocked buildings

        // set number of loops between building buys

        while (cC.isLoop())             cC.run();



    }
}



// calculate worth by comparing with previous unit price also
// fix upgrade text

// method to capture next upgrade price.. check if price returned is empty or not
// calculate worth of buying upgrade also



// build for repeated try to find an element until succeed (by)

