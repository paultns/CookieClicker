
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class CookieFrame extends JFrame implements ActionListener {

    private JButton start;
    ;
    private JRadioButton loopYes;
    private JRadioButton loopNo;
    private JRadioButton firefox;
    private JRadioButton explorer;
    private JRadioButton chrome;
    private JRadioButton loadGame;
    private JRadioButton newGame;
    private JRadioButton continueGame;
    private JTextField loadSave;
    private JPanel panel;
    private JPanel panel2;
    //private JLabel text;
    private boolean started;

    CookieFrame() {

        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");

        started = false;
        setTitle("CookieAutoClicker");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        Container frame = getContentPane();
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3, 0, 10));
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(0, 1));

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

        final ButtonGroup game = new ButtonGroup();
        newGame = new JRadioButton("New Game");
        loadGame = new JRadioButton("Load");
        continueGame = new JRadioButton("Continue");
        game.add(loadGame);
        game.add(newGame);
        game.add(continueGame);

        final ButtonGroup loopGame = new ButtonGroup();
        loopYes = new JRadioButton("Loop");
        loopNo = new JRadioButton("End after current cycle");
        loopGame.add(loopYes);
        loopGame.add(loopNo);

        loadSave = new JTextField();
        loadSave.setEditable(false);

        panel.add(chrome);
        panel.add(firefox);
        panel.add(explorer);

        panel.add(loadSave);

        panel.add(newGame);
        panel.add(loadGame);
        panel.add(continueGame);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(loadSave, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.SOUTH);

        JTextArea output = new JTextArea(50, 10);
        output.setEditable(false);
        PrintStream printStream = new PrintStream(new Console(output));
        System.setOut(printStream);
        System.setErr(printStream);

        frame.setPreferredSize(new Dimension(350, 300));
        panel2.setPreferredSize(new Dimension(0, 100));

        panel2.add(start);
        panel2.add(output);
        setVisible(true);
        pack();

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start && ((!newGame.isSelected() && !loadGame.isSelected() && !continueGame.isSelected())
                || (!firefox.isSelected() && !chrome.isSelected() && !explorer.isSelected()))) {
            if (!newGame.isSelected() && !loadGame.isSelected() && !continueGame.isSelected())
                System.out.println("Please choose whether you would like to start a newgame, load a savegame, " +
                        "or continue..");
            if (!firefox.isSelected() && !chrome.isSelected() && !explorer.isSelected())
                System.out.println("Please choose browser");
        } else {
            if (started)
                System.out.println("Game already started");
            else {
                started = true;
                panel2.setLayout(new GridLayout(0, 3, 2, 5));
                start.setText("Update Game");
                panel2.add(loopYes);
                panel2.add(loopNo);
                panel2.add(start);
                pack();

                try {
                    Thread.sleep(2000);
                    CookieClicker cC = new CookieClicker(new FirefoxDriver());
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}