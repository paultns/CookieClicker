import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class CookieFrame extends JFrame implements ActionListener {

    private JRadioButton firefox;
    private JRadioButton explorer;
    private JRadioButton chrome;
    private JRadioButton loadGame;
    private JRadioButton continueGame;
    private JRadioButton clickOnly;
    private JRadioButton clickBuy;
    private static JButton start;
    private static JButton shutdown;
    private static JButton end;
    private JTextArea output;
    private JPanel panel;
    private boolean started;
    private boolean setup;
    private CookieClicker cC;

    CookieFrame() {

        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");

        started = false;
        setup = false;
        setTitle("CookieAutoClicker");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 7, 0, 20));

        //panel.setBackground(Color.blue);
        end = new JButton("Save and Stop");
        end.addActionListener((ActionEvent e) -> {
            System.out.println("Program will end after current cycle finishes..\n");
            start.setText("Start");
            cC.setLoop(false);
            started = false;
            //shutdown.setEnabled(false);
            start.setEnabled(false);
            end.setEnabled(false);
        });
        end.setEnabled(false);
        start = new JButton("Run");
        start.addActionListener(this);
        shutdown = new JButton("Shutdown");
        shutdown.addActionListener((ActionEvent e) -> {
            cC.driver.quit();
            setVisible(false); //you can't see me!
            dispose(); //Destroy the JFrame object
        });

        final ButtonGroup browsers = new ButtonGroup();
        chrome = new JRadioButton("Chrome");
        firefox = new JRadioButton("Firefox");
        explorer = new JRadioButton("InternetExplorer");
        browsers.add(firefox);
        browsers.add(chrome);
        browsers.add(explorer);
        firefox.setSelected(true);

        final ButtonGroup game = new ButtonGroup();
        JRadioButton newGame = new JRadioButton("New Game");
        loadGame = new JRadioButton("Load");
        continueGame = new JRadioButton("Continue");
        game.add(loadGame);
        game.add(newGame);
        game.add(continueGame);
        continueGame.setSelected(true);

        final ButtonGroup gameMode = new ButtonGroup();
        clickOnly = new JRadioButton("Click Only");
        clickBuy = new JRadioButton("Click and Buy");
        gameMode.add(clickOnly);
        gameMode.add(clickBuy);
        clickBuy.setSelected(true);

        panel.add(chrome);
        panel.add(firefox);
        panel.add(explorer);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(newGame);
        panel.add(loadGame);
        panel.add(continueGame);

        // creates a text area which will work as an input area for the save game
        // and as a console after the program has started
        output = new JTextArea(50, 10);
        PrintStream printStream = new PrintStream(new Console(output));
        System.setOut(printStream);
        System.setErr(printStream);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
        add(start, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(700, 800));
        start.setPreferredSize(new Dimension(0, 50));

        setVisible(true);
        pack();

        System.out.println(" !! To load a savegame, copy savegame in console, select Load and click run");
    }

    private void start() {
        if (continueGame.isSelected() && !saveFound())
            System.out.println("Save file not found, either load a game or select new game.\n");
        else if (!setup) {
            String console = output.getText().split(" ")[output.getText().split(" ").length - 1];
            output.setText("");
            setup = true;
            output.setEditable(false);
            panel.setLayout(new GridLayout(0, 3, 0, 0));
            panel.removeAll();
            panel.add(new JLabel("Game Mode:"));
            panel.add(clickOnly);
            panel.add(clickBuy);
            panel.add(start);
            start.setText("Start");
            start.setEnabled(false);
            panel.add(end);
            panel.add(shutdown);
            shutdown.setEnabled(false);
            pack();

            if (firefox.isSelected())
                cC = new CookieClicker(new FirefoxDriver());
            else if (chrome.isSelected())
                cC = new CookieClicker(new ChromeDriver());
            else if (explorer.isSelected())
                cC = new CookieClicker(new InternetExplorerDriver());
            else cC = new CookieClicker(new FirefoxDriver());
            output.setText("");
            if (loadGame.isSelected())
                cC.setUp(console.split("\n")[console.split("\n").length - 1]);
            else if (continueGame.isSelected()) {
                cC.setUp();
            }
            System.out.println("Setup Complete! Please select game mode! (can be also updated on the go) " +
                    "Game starting at: " + ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS) + "\n");
        } else {
            if (!started) {
                start.setText("Update game");
                shutdown.setEnabled(false);
                end.setEnabled(true);
                started = true;
                cC.setLoop(true);
                System.out.println("Program will loop until user decides to stop it\n");
                if (clickOnly.isSelected()) {
                    cC.setBuy(false);
                    cC.setCycleLength(1);
                    System.out.println("\n>> New Cycle Starting.\nProgram will only click cookie and golden cookies " +
                            "without buying buildings / upgrades");

                } else {
                    cC.setBuy(true);
                    cC.setCycleLength(1);
                    System.out.println("Program will click cookie and golden cookies, and buy upgrades / " +
                            "buildings when feasible");
                }
                while (cC.isLoop()) cC.cookieRobot();
            } else {
                if (clickOnly.isSelected()) {
                    cC.setBuy(false);
                    cC.setCycleLength(1);
                    System.out.println("Program will only click cookie and golden cookies without buying " +
                            "buildings / upgrades");
                    System.out.println("Changes will take effect after current cycle ends\n");
                } else {
                    cC.setBuy(true);
                    cC.setCycleLength(1);
                    System.out.println("Program will click cookie and golden cookies, and buy upgrades / " +
                            "buildings when feasible");
                    System.out.println("Changes will take effect after current cycle ends\n");
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                start();
                return null;
            }
        };
        worker.execute();
    }

    static void enableStart() {
        start.setEnabled(true);
    }

    static void enableShutdown() {
        shutdown.setEnabled(true);
    }

    private boolean saveFound() {
        File f = new File("CookieSave");
        return f.exists();
    }
}