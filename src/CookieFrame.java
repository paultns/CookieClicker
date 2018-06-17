
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        started = false;
        setTitle("CookieAutoClicker");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        Container frame = getContentPane();
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3));
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(0,1));

        //panel.setBackground(Color.blue);

        start = new JButton("Run");
        start.addActionListener(this);

        final ButtonGroup browsers = new ButtonGroup();
        chrome = new JRadioButton("Open in Chrome");
        firefox = new JRadioButton("Open in Firefox");
        explorer = new JRadioButton("Open in InternetExplorer");
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

        panel.add(chrome);
        panel.add(firefox);
        panel.add(explorer);

        panel.add(loadSave);

        panel.add(newGame);
        panel.add(loadGame);
        panel.add(continueGame);

        frame.add(panel,BorderLayout.NORTH);
        frame.add(loadSave, BorderLayout.CENTER);
        frame.add(panel2,BorderLayout.SOUTH);
        setSize(1000, 3000);
        //panel.add(loopYes);
        //panel.add(loopNo);


        panel2.add(start);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            if (!newGame.isSelected() && !loadGame.isSelected() && !continueGame.isSelected())
                System.out.println("choose game error");
            if (!firefox.isSelected() && !chrome.isSelected() && !explorer.isSelected())
                System.out.println("choose browser error");
            if (started)
                System.out.println("Game already started");
            else {
                started = true;
                start.setText("Update Game");
                panel.add(loopYes);
                panel.add(loopNo);
                //panel.add(start);
                pack();
            }
        }
    }
}