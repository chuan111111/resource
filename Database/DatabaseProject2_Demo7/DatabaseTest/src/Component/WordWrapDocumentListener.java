package Component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Utilities;

public class WordWrapDocumentListener implements DocumentListener {
    private int maxWordsPerLine;

    private JTextArea jTextArea;

    public WordWrapDocumentListener(JTextArea jTextArea, int maxWordsPerLine) {
        this.maxWordsPerLine = maxWordsPerLine;
        this.jTextArea = jTextArea;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    private void handleDocumentEvent(DocumentEvent e) {
        Document document = e.getDocument();
        try {
            int caretPosition = e.getOffset();
            int startLine = Utilities.getRowStart(jTextArea, caretPosition);
            int endLine = Utilities.getRowEnd(jTextArea, caretPosition);
            String text = document.getText(startLine, endLine - startLine);
            String[] words = text.split("."); // 按空格分割单词
            if (words.length > maxWordsPerLine) {
                String newText = "\n" + text.trim(); // 在行尾添加换行符
                document.insertString(endLine, newText, null);
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
}



