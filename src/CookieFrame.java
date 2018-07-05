import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class CookieFrame extends JFrame implements ActionListener {

    static JCheckBox buyNewBuildings;
    private static JButton start;
    private static JButton shutdown;
    private static JButton save;
    private static JButton reset;
    private static JButton load;
    private static JRadioButton firefox;
    private static JRadioButton explorer;
    private static JRadioButton chrome;
    private static JRadioButton loadGame;
    private static JRadioButton continueGame;
    private static JRadioButton newGame;
    private static JRadioButton consoleOn;
    private static JRadioButton consoleOff;
    private static JCheckBox autoSave;
    private static JCheckBox clickGolden;
    private static JCheckBox buyBuildings;
    private static JCheckBox buyUpgrades;
    private static String gameState;
    private JPanel panel;
    private static JButton openSave;
    private static JTextArea output;
    private CookieClicker cC;
    private static ClearTextArea focusClearer;
    private static SwingWorker<Void, Void> worker;

    CookieFrame() {

        // setup of the browser driver paths
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");

        // initializing all buttons
        setTitle("CookieAutoClicker v1.5.1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        //panel.setBackground(Color.blue);
        panel = new JPanel();
        start = new JButton("Run");
        start.addActionListener(this);
        shutdown = new JButton("Shutdown");
        shutdown.addActionListener((ActionEvent e) -> {
            try {
                cC.driver.quit();
            }
            catch (Exception ex) {
                System.out.println("\nWindows was already closed.\n");
            }
            setVisible(false); //you can't see me!
            dispose(); //Destroy the JFrame object
        });
        save = new JButton("Save Game");
        save.addActionListener((ActionEvent e) -> cC.save());
        load = new JButton("Load Save File");
        load.addActionListener((ActionEvent e) -> cC.read(null));
        openSave = new JButton("Open Save File");
        openSave.addActionListener((ActionEvent open) ->
                showSave()
        );
        reset = new JButton("Reset Game");
        reset.addActionListener((ActionEvent e) -> {
            changeState("RESET");
            cC.driver.quit();
            cC = null;
        });
        // browser chooser. with firefox as default
        final ButtonGroup browsers = new ButtonGroup();
        chrome = new JRadioButton("Chrome");
        firefox = new JRadioButton("Firefox");
        explorer = new JRadioButton("InternetExplorer");
        browsers.add(firefox);
        browsers.add(chrome);
        browsers.add(explorer);
        firefox.setSelected(true);
        // console updates selector, with minimal updates as default
        final ButtonGroup toggleConsole = new ButtonGroup();
        consoleOn = new JRadioButton("Detailed Console");
        consoleOff = new JRadioButton("Minimal Console Updates");
        toggleConsole.add(consoleOff);
        toggleConsole.add(consoleOn);
        consoleOn.setSelected(true);
        // game mode selector, with continue game as default
        final ButtonGroup game = new ButtonGroup();
        newGame = new JRadioButton("New Game");
        loadGame = new JRadioButton("Load");
        continueGame = new JRadioButton("Continue");
        game.add(loadGame);
        game.add(newGame);
        game.add(continueGame);
        continueGame.setSelected(true);

        // initializing checkboxes
        buyBuildings = new JCheckBox("Buy Buildings");
        buyBuildings.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                cC.setBuyingBuildings(false);
                System.out.println("# Will not buy buildings.");
                cC.clearGoals();
            } else {
                cC.setBuyingBuildings(true);
                System.out.println("# Will also buy buildings");
            }
        });
        buyUpgrades = new JCheckBox("Buy Upgrades");
        buyUpgrades.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                cC.setBuyingUpgrades(false);
                cC.clearGoals();
                System.out.println("# Will not buy upgrades.");
            } else {
                cC.setBuyingUpgrades(true);
                System.out.println("# Will also buy upgrades.");
            }
        });
        buyNewBuildings = new JCheckBox("Buy New Buildings");
        buyNewBuildings.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                System.out.println("# Will not buy new unlocked buildings");
                cC.setBuyingNewBuildings(false);
                cC.clearGoals();
            } else {
                cC.setBuyingNewBuildings(true);
                System.out.println("# Will also buy new buildings.");
            }
        });
        clickGolden = new JCheckBox("Click Golden Cookies");
        clickGolden.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                cC.setClickGoldenCookies(false);
                System.out.println("# Golden Cookies will not be clicked.");
            } else {
                cC.setClickGoldenCookies(true);
                System.out.println("# Golden Cookies will be automatically clicked.");
            }
        });
        autoSave = new JCheckBox("Save Automatically");
        autoSave.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                cC.setAutoSave(false);
                System.out.println("# Game will not be automatically saved");
            } else {
                cC.setAutoSave(true);
                System.out.println("# Game will be automatically saved");
            }
        });

        // creates a text area which will work as an input area for the save game
        // and as a console after the program has started
        output = new JTextArea(50, 10);
        PrintStream printStream = new PrintStream(new Console(output));
        System.setOut(printStream);
        System.setErr(printStream);
        focusClearer = new ClearTextArea();
        output.addFocusListener(focusClearer);
        //initializing variables


        gameState = "DRIVER OFF";
        System.out.println(" !! To load a savegame, insert savegame here, select Load and click run...");
        panelSetup();
    }

    // initial setup of the panel on opening the program
    private void panelSetup() {

        panel.setLayout(new GridLayout(0, 6, 0, 0));
        start.setPreferredSize(new Dimension(0, 50));
        //panel.add(new JLabel("Choose Browser:"));
        panel.add(explorer);
        panel.add(chrome);
        panel.add(firefox);
        panel.add(newGame);
        panel.add(loadGame);
        panel.add(continueGame);
        panel.add(buyBuildings);
        buyBuildings.setEnabled(false);
        panel.add(buyUpgrades);
        buyUpgrades.setEnabled(false);
        panel.add(buyNewBuildings);
        buyNewBuildings.setEnabled(false);
        panel.add(clickGolden);
        clickGolden.setEnabled(false);
        panel.add(start);
        panel.add(openSave);
        panel.add(save);
        save.setEnabled(false);
        panel.add(load);
        load.setEnabled(false);

        panel.add(autoSave);
        autoSave.setEnabled(false);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
        JPanel panel2 = new JPanel();
        panel2.add(reset);
        panel2.add(shutdown);
        shutdown.setEnabled(false);
        reset.setEnabled(false);
        panel2.add(consoleOn);
        consoleOn.setEnabled(false);
        panel2.add(consoleOff);
        consoleOff.setEnabled(false);
        add(panel2, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(750, 850));


        setVisible(true);
        pack();
    }

    // starting driver based on chosen browser
    private void startDriver() {
        if (continueGame.isSelected() && !fileFound("CookieSave"))
            System.out.println("Save file not found, either load a game or select new game.\n");
        else if (firefox.isSelected()) {
            if (fileFound("geckodriver.exe"))
                setup(new FirefoxDriver(), output.getText());
            else
                System.out.println("ERROR!! Firefox driver file not found!");
        } else if (chrome.isSelected()) {
            if (fileFound("chromedriver.exe"))
                setup(new ChromeDriver(), output.getText());
            else
                System.out.println("ERROR!! Chrome driver file not found!");
        } else if (explorer.isSelected()) {
            if (fileFound("IEDriverServer.exe"))
                setup(new InternetExplorerDriver(), output.getText());
            else
                System.out.println("ERROR!! Internet Explorer driver file not found");
        } else setup(new FirefoxDriver(), output.getText());

    }

    // game setup
    private void setup(WebDriver driver, String loadedGame) {

        lock();
        cC = new CookieClicker(driver);
        if (loadGame.isSelected())
            cC.setUp(loadedGame, "LOAD GAME");
        else if (continueGame.isSelected()) {
            cC.setUp(null, "CONTINUE GAME");
        } else cC.setUp(null, "NEW GAME");
        //output.setText("");
        System.out.println("Setup Complete! Please select game mode! (can be also updated on the go) " +
                "Game starting at: " + ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS) + "\n");

        consoleOff.addItemListener((ItemEvent ie) -> {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                cC.setFullConsole(false);
                System.out.println("# Console will only display important updates");
            }
        });
        consoleOn.addItemListener((ItemEvent ie) -> {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                cC.setFullConsole((true));
                System.out.println("# Console will display full information");
            }
        });
    }

    // modifies the GUI on program run and rest
    static void changeState(String state) {
        if (state.equals("START")) {
            gameState = "GAME PAUSED";
            openSave.setEnabled(true);
            chrome.setEnabled(false);
            firefox.setEnabled(false);
            explorer.setEnabled(false);
            newGame.setEnabled(false);
            loadGame.setEnabled(false);
            continueGame.setEnabled(false);
            start.setText("Start");
            start.setEnabled(true);
            buyBuildings.setEnabled(true);
            save.setEnabled(true);
            buyUpgrades.setEnabled(true);
            buyNewBuildings.setEnabled(true);
            clickGolden.setEnabled(true);
            autoSave.setEnabled(true);
            consoleOn.setEnabled(true);
            consoleOff.setEnabled(true);
            reset.setEnabled(true);
            buyBuildings.setSelected(false);
            buyUpgrades.setSelected(false);
            buyNewBuildings.setSelected(false);
            clickGolden.setSelected(false);
            shutdown.setEnabled(true);
            load.setEnabled(true);
        } else if (state.equals("RESET")) {
            gameState = "DRIVER OFF";
            chrome.setEnabled(true);
            firefox.setEnabled(true);
            explorer.setEnabled(true);
            newGame.setEnabled(true);
            loadGame.setEnabled(true);
            continueGame.setEnabled(true);
            start.setText("Run");
            buyBuildings.setEnabled(false);
            save.setEnabled(false);
            buyUpgrades.setEnabled(false);
            buyNewBuildings.setEnabled(false);
            clickGolden.setEnabled(false);
            autoSave.setEnabled(false);
            consoleOn.setEnabled(false);
            consoleOff.setEnabled(false);
            reset.setEnabled(false);
            shutdown.setEnabled(false);
            output.addFocusListener(focusClearer);
            output.setEditable(true);
        }
    }

    // locks the console and the whole GUI
    private void lock() {
        Component[] comps = panel.getComponents();
        for (Component comp : comps)
            comp.setEnabled(false);
        output.removeFocusListener(focusClearer);
        output.setEditable(false);
    }

    // program startup on clicking the run button // need to edit.
    public void actionPerformed(ActionEvent e) {

        switch (gameState) {
            case "DRIVER OFF": {
                gameState = "GAME PAUSED";
                worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        startDriver();
                        return null;
                    }
                };
                worker.execute();
                break;
            }
            case "GAME PAUSED": {
                cC.stop(false);
                start.setText("Pause");
                shutdown.setEnabled(false);
                reset.setEnabled(false);
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        cC.cookieRobot();
                        return null;
                    }
                };
                worker.execute();
                gameState = "GAME STARTED";
                break;
            }
            case "GAME STARTED": {
                start.setText("Start");
                shutdown.setEnabled(true);
                reset.setEnabled(true);
                gameState = "GAME PAUSED";
                cC.stop(true);
                cC.clearGoals();

            }
            default:
                break;
        }
    }

    //opens a notepad with the saved game
    private void showSave() {

        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "CookieSave");
        try {
            pb.start();
        } catch (IOException e) {
            System.out.println("Could not open save file. Error: " + e.getMessage());
        }

    }

    // checks whether a file exists in the folder or not
    private boolean fileFound(String file) {
        File f = new File(file);
        return f.exists();
    }

}
