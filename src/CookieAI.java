import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class CookieAI {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");


        CookieFrame cC = new CookieFrame();
        //CookieClicker cC = new CookieClicker(new FirefoxDriver());

        //while (cC.isLoop()) cC.run();



    }
}