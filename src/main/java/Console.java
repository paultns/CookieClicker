import java.io.OutputStream;
import javax.swing.JTextArea;

// This class extends from OutputStream to redirect output to a JTextArrea

class Console extends OutputStream {
    private JTextArea textArea;

    Console(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        // redirects data to the text area
        textArea.append(String.valueOf((char) b));

        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}