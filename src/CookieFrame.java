import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class CookieFrame extends JFrame implements ActionListener {

    private JRadioButton firefox;
    private JRadioButton explorer;
    private JRadioButton chrome;
    private JRadioButton loadGame;
    private JRadioButton continueGame;
    private JRadioButton loopYes;
    private JRadioButton loopNo;
    private JRadioButton clickOnly;
    private JRadioButton clickBuy;
    private JButton start;
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

        start = new JButton("Run");
        start.addActionListener(this);

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

        final ButtonGroup loopGame = new ButtonGroup();
        loopYes = new JRadioButton("Loop");
        loopNo = new JRadioButton("End after current cycle");
        loopGame.add(loopYes);
        loopGame.add(loopNo);
        loopYes.setSelected(true);

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

        System.out.println(" !! In case of loading a savegame, remove this line and insert savegame here.");
    }

    private void start() {

        if (!setup) {
            output.setText("");
            setup = true;
            output.setEditable(false);
            panel.setLayout(new GridLayout(0, 3, 0, 0));
            panel.removeAll();
            panel.add(new JLabel("Game Mode:"));
            panel.add(clickOnly);
            panel.add(clickBuy);
            panel.add(new JLabel("Use to play or pause:"));
            panel.add(loopYes);
            panel.add(loopNo);
            panel.add(start);
            start.setText("Start");
            pack();

            if (firefox.isSelected())
                cC = new CookieClicker(new FirefoxDriver());
            else if (chrome.isSelected())
                cC = new CookieClicker(new ChromeDriver());
            else if (explorer.isSelected())
                cC = new CookieClicker(new InternetExplorerDriver());
            else cC = new CookieClicker(new FirefoxDriver());
            output.setText("");
            cC.setUp();
            if (loadGame.isSelected())
                cC.read(output.getText());
            else if (continueGame.isSelected()) {
                cC.read();
            }
            System.out.println("Setup Complete! Please select game mode! (can be also updated on the go) " +
                    "Game starting at: " + ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.SECONDS) + "\n");
        } else {
            if (!started) {
                if (loopYes.isSelected()) {
                    start.setText("Update game");
                    started = true;
                    cC.setLoop(true);
                    System.out.println("Program will loop until user decides to stop it\n");
                } else {
                    cC.setLoop(false);
                    started = false;
                    System.out.println("Program will only run for one cycle\n");
                }
                if (clickOnly.isSelected()) {
                    cC.setBuy(false);
                    cC.setCycleLength(20);
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
                cC.newBuilding();
                cC.getGoal();
                while (cC.isLoop()) cC.cookieRobot();
            } else {
                if (loopNo.isSelected()) {
                    System.out.println("Program will end after current cycle finishes..\n");
                    cC.setLoop(false);
                    started = false;
                    start.setText("Start");
                    JButton shutdown = new JButton("close window");
                    panel.add(shutdown);
                    shutdown.addActionListener((ActionEvent e) -> {
                            cC.driver.quit();
                            setVisible(false); //you can't see me!
                            dispose(); //Destroy the JFrame object
                    });
                }
                else {
                    if (clickOnly.isSelected()) {
                        cC.setBuy(false);
                        cC.setCycleLength(20);
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
}