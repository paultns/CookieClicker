/*
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CookieFrame extends JFrame implements ActionListener {

    private JButton start;;
    private JRadioButton loopYes;
    private JRadioButton loopNo;
    private JRadioButton upgrades;
    private JRadioButton buildings;
    private JRadioButton saveFor;
    private JButton = newGame;
    private JButton = continueGame;
    private JLabel text;

    CookieFrame() {
        this.setTitle("CookieAutoClicker");
        //this.setSize(1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        Container panel = this.getContentPane();
        panel.setLayout(new GridBagLayout());
        //panel.setBackground(Color.blue);
        start = new JButton("Run");
        start.addActionListener(this);
        loopYes = new JRadioButton("Loop");
         loopNo = new JRadioButton("End after current cycle");
         buildings = new JRadioButton("Buy Buildings");
         Upgrades = new JRadioButton ("Buy Upgrades");
         saveFor = new JRadioButton ("Save to buy new building");

       this.text = new JLabel("Hours:  " + String.valueOf(this.num));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.weightx = 0.5;
        c.gridx = GridBagConstraints.RELATIVE;
        panel.add(this.text, c);
        c.gridy = 1;
        panel.add(this.minus1, c);
        panel.add(this.plus1, c);
        c.gridy = 2;
        panel.add(this.minus25, c);
        panel.add(this.plus25, c);
        final ButtonGroup streams = new ButtonGroup();
        streams.add(this.email);
        streams.add(this.ola);
        streams.add(this.sites);
        c.gridy = 3;
        panel.add(this.email, c);
        panel.add(this.sites, c);
        panel.add(this.ola, c);
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridy = 4;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(this.calculate, c);

        this.pack();
        this.setVisible(true);

    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.minus25 && this.num > 0.24)
            this.num = this.num - 0.25;
        else if (e.getSource() == this.plus25)
            this.num = this.num + 0.25;
        else if (e.getSource() == this.minus1 && this.num > 0.99)
            this.num--;
        else if (e.getSource() == this.plus1)
            this.num++;
        else if (e.getSource() == this.calculate && (email.isSelected() || sites.isSelected() || ola
                .isSelected()) && this.num > 0)
            Output();
        else
            this.text.setText("Error!");
        this.text.setText("Hours:  " + String.valueOf(this.num));  //refreshes the value
    }
}
*/