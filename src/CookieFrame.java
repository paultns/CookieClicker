/*
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CookieFrame extends JFrame implements ActionListener {

    private double num;
    private int[] hours;
    private String[] projects;
    private JButton plus25;
    private JButton minus25;
    private JButton minus1;
    private JButton plus1;
    private JRadioButton email;
    private JRadioButton sites;
    private JRadioButton ola;
    private JButton calculate;
    private JLabel text;

    CookieFrame() {
        this.setTitle("OpenAir Tool");
        //this.setSize(1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        Container panel = this.getContentPane();
        panel.setLayout(new GridBagLayout());
        //panel.setBackground(Color.blue);
        this.minus25 = new JButton("-0.25h");
        this.minus25.addActionListener(this);
        this.plus25 = new JButton("+0.25h");
        this.plus25.addActionListener(this);
        this.minus1 = new JButton("-1h");
        this.minus1.addActionListener(this);
        this.plus1 = new JButton("+1h");
        this.plus1.addActionListener(this);
        this.calculate = new JButton("Split!");
        this.calculate.addActionListener(this);
        this.email = new JRadioButton("Email Stream");
        this.sites = new JRadioButton("Sites Stream");
        this.ola = new JRadioButton("OLA Stream");
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
        else if (e.getSource() == this.calculate && (email.isSelected() || sites.isSelected() || ola.isSelected()) && this.num > 0)
            Output();
        else
            this.text.setText("Error!");
        this.text.setText("Hours:  " + String.valueOf(this.num));  //refreshes the value
    }
}
*/