package project.common;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DigitOnlyTextField extends JTextField {

    public DigitOnlyTextField() {
        super();

        // Apply digit-only filter
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new DigitFilter());

        // Set default value if empty on focus lost
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().trim().isEmpty()) {
                    setText("10");
                }
            }
        });
    }

    private static class DigitFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d+")) {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder newText = new StringBuilder(currentText);
                newText.insert(offset, string);

                // Remove leading zero if followed by digits
                if (newText.length() > 1 && newText.charAt(0) == '0') {
                    newText.deleteCharAt(0);
                }

                fb.replace(0, fb.getDocument().getLength(), newText.toString(), attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder newText = new StringBuilder(currentText);
                newText.replace(offset, offset + length, string);

                if (newText.length() > 1 && newText.charAt(0) == '0') {
                    newText.deleteCharAt(0);
                }

                fb.replace(0, fb.getDocument().getLength(), newText.toString(), attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            fb.remove(offset, length);
        }
    }
}
