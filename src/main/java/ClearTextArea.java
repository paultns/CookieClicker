import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ClearTextArea implements FocusListener {


    @Override
    public void focusGained(FocusEvent e) {

        ((JTextComponent) e.getSource()).setText("");
    }

    @Override
    public void focusLost(FocusEvent e) {


    }
}

