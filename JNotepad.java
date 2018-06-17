/**
* Name: Bull, Alexx
* Project: 3
* Due: 03/12/2018
* Course: CS-245-01-w18
*
* Description:
* A notepad text editor capable of saving and editing text files.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.NumberFormatException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

public class JNotepad
{
    private boolean wrapWord;
    private Font defaultFont;
    private Highlighter highlighter;
    private HighlightPainter paint;
    private JTextArea jta;
    private JFrame win;

    public static void main(String[] args)
    {
        try
        {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            // revert to Java Look and Feel();
            try
            {
                // Set cross-platform Java L&F (also called "Metal")
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            catch (Exception ex)
            {
                // if this fails too, then do nothing I guess except outputing the error
                JOptionPane.showMessageDialog(null, ex.getMessage());
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> new JNotepad());
    }

    public JNotepad()
    {
        wrapWord = false;
        defaultFont = new Font("Courier New", Font.PLAIN, 12);
        FileActionListener fal = new FileActionListener();

        win = new JFrame("Untitled - JNotepad");
        win.setSize(800, 500);
        win.setLayout(new GridLayout(0, 1));
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setIconImage(new ImageIcon("JNotepad.png").getImage());
        win.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                fal.confirmSave();
            }
        });

        // make a JTextArea for typing
        jta = new JTextArea();
        highlighter = jta.getHighlighter();
        paint = new DefaultHighlightPainter(Color.CYAN);
        jta.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                highlighter.removeAllHighlights();
            }
        });
        win.add(jta);

        // build the bar menu
        JMenuBar bar = new JMenuBar();
        bar.add(makeFileMenu(fal));
        bar.add(makeEditMenu());
        bar.add(makeFormatMenu());
        bar.add(makeViewMenu());
        bar.add(makeHelpMenu());

        // build Popup menu
        makePopupMenu();

        win.setJMenuBar(bar);
		win.setLocationRelativeTo(null);
        win.setVisible(true);
    }

    // make the layout and components of the File menu and return it
    public JMenu makeFileMenu(FileActionListener fal)
    {
        JMenu file = new JMenu("File");
        file.setMnemonic('F');

        JMenuItem fileNew = new JMenuItem("New", 'N');
        fileNew.setAccelerator(KeyStroke.getKeyStroke("control N"));
        fileNew.setActionCommand("New");
        fileNew.addActionListener(fal);
        file.add(fileNew);

        JMenuItem open = new JMenuItem("Open...");
        open.setAccelerator(KeyStroke.getKeyStroke("control O"));
        open.setActionCommand("Open");
        open.addActionListener(fal);
        file.add(open);

        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke("control S"));
        save.setActionCommand("Save");
        save.addActionListener(fal);
        file.add(save);

        JMenuItem saveAs = new JMenuItem("Save As...", 'S');
        saveAs.setActionCommand("Save As");
        saveAs.addActionListener(fal);
        file.add(saveAs);
        file.addSeparator();

        file.add(new JMenuItem("Page Setup...", 'u'));
        file.add(new JMenuItem("Print"));
        file.addSeparator();

        JMenuItem exit = new JMenuItem("Exit", 'x');
        exit.setActionCommand("Exit");
        exit.addActionListener(fal);
        file.add(exit);

        return file;
    }

    // make the layout and components of the Edit menu and return it
    public JMenu makeEditMenu()
    {
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic('E');
        EditActionListener eal = new EditActionListener();

        edit.add(new JMenuItem("Undo"));
        edit.addSeparator();

        JMenuItem cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.setActionCommand("Cut");
        cut.addActionListener(eal);
        edit.add(cut);

        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.setActionCommand("Copy");
        copy.addActionListener(eal);
        edit.add(copy);

        JMenuItem paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.setActionCommand("Paste");
        paste.addActionListener(eal);
        edit.add(paste);

        JMenuItem delete = new JMenuItem("Delete");
        delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        delete.setActionCommand("Delete");
        delete.addActionListener(eal);
        edit.add(delete);
        edit.addSeparator();

        JMenuItem find = new JMenuItem("Find...");
        find.setAccelerator(KeyStroke.getKeyStroke("control F"));
        find.setActionCommand("Find");
        find.addActionListener(eal);
        edit.add(find);

        JMenuItem findNext = new JMenuItem("Find Next");
        findNext.setActionCommand("Find Next");
        findNext.addActionListener(eal);
        edit.add(findNext);

        JMenuItem replace = new JMenuItem("Replace...");
        replace.setAccelerator(KeyStroke.getKeyStroke("control H"));
        replace.setActionCommand("Replace");
        replace.addActionListener(eal);
        edit.add(replace);

        JMenuItem gotoMenu = new JMenuItem("Go To...");
        gotoMenu.setAccelerator(KeyStroke.getKeyStroke("control G"));
        gotoMenu.setActionCommand("Go To");
        gotoMenu.addActionListener(eal);
        edit.add(gotoMenu);
        edit.addSeparator();

        JMenuItem select = new JMenuItem("Select");
        select.setAccelerator(KeyStroke.getKeyStroke("control A"));
        select.setActionCommand("Select");
        select.addActionListener(eal);
        edit.add(select);

        JMenuItem timestamp = new JMenuItem("Time/Date");
        timestamp.setAccelerator(KeyStroke.getKeyStroke("F5"));
        timestamp.setActionCommand("Timestamp");
        timestamp.addActionListener(eal);
        edit.add(timestamp);

        return edit;
    }

    // make the layout and components of the Format menu and return it
    public JMenu makeFormatMenu()
    {
        JMenu format = new JMenu("Format");
        format.setMnemonic('O');

        JCheckBoxMenuItem wordWrap = new JCheckBoxMenuItem("Word Wrap");
        wordWrap.addActionListener(ae ->
        {
            wrapWord = !wrapWord;
            jta.setLineWrap(wrapWord);
            jta.setWrapStyleWord(wrapWord);
        });
        format.add(wordWrap);

        JMenuItem font = new JMenuItem("Font", 'F');

        font.addActionListener(ae ->
        {
            JFontChooser jfontc = new JFontChooser();
            jfontc.setDefault(defaultFont);

            if (jfontc.showDialog(win))
            {
                jta.setFont(jfontc.getFont());
                jta.setForeground(jfontc.getColor());
            }
        });
        format.add(font);

        return format;
    }

    // make the layout and components of the View menu and return it
    public JMenu makeViewMenu()
    {
        JMenu view = new JMenu("View");
        view.setMnemonic('V');
        view.add(new JMenuItem("Status Bar", 'S'));

        JMenu color = new JMenu("Color");
        JMenuItem bg = new JMenuItem("Background Color");
        JMenuItem fg = new JMenuItem("Foreground Color");

        color.add(bg);
        color.add(fg);

        bg.addActionListener(ae ->
        {
            Color newColor = JColorChooser.showDialog(win, "Backgroung Color", Color.WHITE);
            if (newColor != null)
                jta.setBackground(newColor);
        });

        fg.addActionListener(ae ->
        {
            Color newColor = JColorChooser.showDialog(win, "Foreground Color", Color.BLACK);
            if (newColor != null)
                jta.setForeground(newColor);
        });

        view.add(color);

        return view;
    }

    // make the layout and components of the Help menu and return it
    public JMenu makeHelpMenu()
    {
        JMenu help = new JMenu("Help");
        help.setMnemonic('H');
        help.add(new JMenuItem("View Help", 'H'));
        help.addSeparator();

        JMenuItem about = new JMenuItem("About JNotepad");
        about.addActionListener(ae ->
        {
            JDialog jd = new JDialog(win, "About", true);
            jd.setLayout(new FlowLayout());
            jd.setSize(200, 200);
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            jd.setLocationRelativeTo(win);

            JLabel name = new JLabel("(c) Alexx Bull");
            name.setFont(new Font("Courier New", Font.BOLD, 18));

            jd.add(name);
            jd.pack();
            jd.setVisible(true);
        });
        help.add(about);

        return help;
    }

    // make the layout and components of the Popup menu
    public JPopupMenu makePopupMenu()
    {
        JPopupMenu popup = new JPopupMenu("Popup Menu");
        EditActionListener eal = new EditActionListener();

        JMenuItem cutPopup = new JMenuItem("Cut");
        cutPopup.setActionCommand("Cut");
        cutPopup.addActionListener(eal);
        popup.add(cutPopup);

        JMenuItem copyPopup = new JMenuItem("Copy");
        copyPopup.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copyPopup.setActionCommand("Copy");
        copyPopup.addActionListener(eal);
        popup.add(copyPopup);

        JMenuItem pastePopup = new JMenuItem("Paste");
        pastePopup.setAccelerator(KeyStroke.getKeyStroke("control V"));
        pastePopup.setActionCommand("Paste");
        pastePopup.addActionListener(eal);
        popup.add(pastePopup);

        jta.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent me)
            {
                // show popup menu if right-click mouse button is clicked
                if (me.getButton() == MouseEvent.BUTTON3)
                {
                    popup.show(win, me.getX(), me.getY());
                    popup.setVisible(true);
                }
            }

            // hide popup menu if left-click mouse button is clicked
            public void mouseClicked(MouseEvent me)
            {
                if (me.getButton() == MouseEvent.BUTTON1)
                    popup.setVisible(false);
            }
        });

        return popup;
    }

    class FileActionListener implements ActionListener
    {
        private JFileChooser fc;
        private File currentFile;
        private String originalFileText;

        public FileActionListener()
        {
            fc = new JFileChooser();

            // setup file filters for file chooser
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Text Files (.txt)", "txt"));
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Java Files (.java)", "java"));

            originalFileText = "";
        }

        public void actionPerformed(ActionEvent ae)
        {
            switch (ae.getActionCommand())
            {
                case "New":
                    confirmSave();
                    jta.replaceRange("", 0, jta.getText().length());
                    win.setTitle("Untitled - JNotepad");
                    currentFile = null;
                    break;
                case "Open":
                    confirmSave();
                    loadFile();
                    break;
                case "Save":
                    if (currentFile == null)
                        saveFile(true);
                    else
                        saveFile(false);
                    break;
                case "Save As":
                    saveFile(true);
                    break;
                case "Exit":
                    confirmSave();
                    System.exit(0);
            }
            fc.setAcceptAllFileFilterUsed(true);    // show all file type
        }

        // confirm is the user wants to save the current file
        public void confirmSave()
        {
            if (jta.getText().length() > 0 && !originalFileText.equals(jta.getText()))
            {
                int ans = JOptionPane.showConfirmDialog(win, "Do you want to save changes?");
                if (ans == JOptionPane.YES_OPTION)
                {
                    if (currentFile == null)
                        saveFile(true);
                    else
                        saveFile(false);
                }
            }
        }

        // save a file
        // if saveDialog is true, use the JFileChooser dialog menu
        // else if false save to current file
        public void saveFile(boolean saveDialog)
        {
            if (saveDialog)
            {
                if (fc.showSaveDialog(win) == JFileChooser.APPROVE_OPTION)
                    currentFile = fc.getSelectedFile();
                else
                    return;
            }
            try{
                jta.write(new FileWriter(currentFile));
                win.setTitle(currentFile.getName() + " - JNotepad");
                originalFileText = jta.getText();
            }
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(win, currentFile.getName() + " not found", "Alert", JOptionPane.WARNING_MESSAGE);
            }
        }

        // load a file
        public void loadFile()
        {
            fc.setAcceptAllFileFilterUsed(false);   // only show files that are of a type in the filters list

            int ans = fc.showOpenDialog(win);
            if (ans == JFileChooser.APPROVE_OPTION)
            {
                currentFile = fc.getSelectedFile();

                try{
                    jta.read(new FileReader(currentFile), null);
                    win.setTitle(currentFile.getName() + " - JNotepad");
                    originalFileText = jta.getText();
                }
                catch(IOException e)
                {
                    JOptionPane.showMessageDialog(win, currentFile.getName() + " not found", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    class EditActionListener implements ActionListener
    {
        private JTextField findTextField, gotoTextField, replaceTextField;

        public EditActionListener()
        {
            findTextField = new JTextField(20);
            gotoTextField = new JTextField("1", 20);
            replaceTextField = new JTextField(20);
        }

        public void actionPerformed(ActionEvent ae)
        {
            switch (ae.getActionCommand())
            {
                case "Copy":
                    jta.copy();
                    break;
                case "Cut":
                    jta.cut();
                    break;
                case "Delete":
                    jta.replaceSelection("");
                    break;
                case "Paste":
                    jta.paste();
                    break;
                case "Find":
                    find();
                    break;
                case "Find Next":
                    findWord();
                    break;
                case "Replace":
                    replace();
                    break;
                case "Go To":
                    goTo();
                    break;
                case "Select":
                    jta.selectAll();
                    break;
                case "Timestamp":
                    printTimestamp();
                    break;
            }
        }

        public void find()
        {
            JDialog jd = new JDialog(win, "Find", false);
            jd.setLayout(new FlowLayout());
            jd.setSize(400, 300);
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            jd.setLocationRelativeTo(win);

            JLabel findLabel = new JLabel("Find what:");
            findLabel.setFont(defaultFont);
            jd.add(findLabel);

            ActionListener findAction = ae -> findWord();

            findTextField.addActionListener(findAction);
            jd.add(findTextField);

            JButton findButton = new JButton("Find Next");
            findButton.addActionListener(findAction);
            jd.add(findButton);

            JButton cancelButton= new JButton("Cancel");
            cancelButton.addActionListener(ae -> jd.dispose());
            jd.add(cancelButton);

            jd.pack();
            jd.setVisible(true);
        }

        public void findWord()
        {
            String word = findTextField.getText();
            String text = "";
            try
            {
                text = jta.getDocument().getText(0, jta.getDocument().getLength());
                text = text.substring(jta.getCaretPosition());

                if (text.contains(word))
                {
                    int wordIndex = text.indexOf(word) + jta.getCaretPosition();
                    jta.setCaretPosition(wordIndex + word.length());

                    // highlight word
                    highlighter.removeAllHighlights();
                    highlighter.addHighlight(wordIndex, wordIndex + word.length(), paint);
                }
                else
                    throw new BadLocationException("Word not found", jta.getCaretPosition());

            }
            catch(BadLocationException e)
            {
                JOptionPane.showMessageDialog(win, String.format("Cannot find \"%s\"", word), "Alert", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        public void replace()
        {
            JDialog jd = new JDialog(win, "Replace", false);
            jd.setLayout(new GridLayout(0, 1));
            jd.setSize(400, 300);
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            jd.setLocationRelativeTo(win);

            JPanel findPanel = new JPanel();

            JLabel findLabel = new JLabel("Find what:");
            findLabel.setFont(defaultFont);
            findPanel.add(findLabel);

            ActionListener findAction = ae -> findWord();

            findTextField.addActionListener(findAction);
            findPanel.add(findTextField);

            JButton findButton = new JButton("Find Next");
            findButton.addActionListener(findAction);
            findPanel.add(findButton);

            JPanel replacePanel = new JPanel();

            JLabel replaceLabel = new JLabel("Replace with:");
            replaceLabel.setFont(defaultFont);
            replacePanel.add(replaceLabel);

            replaceTextField.addActionListener(ae -> replaceWord(false));
            replacePanel.add(replaceTextField);

            JButton replaceButton = new JButton("Replace");
            replaceButton.addActionListener(ae -> replaceWord(false));
            replacePanel.add(replaceButton);

            JButton replaceAllButton = new JButton("Replace All");
            replaceAllButton.addActionListener(ae -> replaceWord(true));
            replacePanel.add(replaceAllButton);


            JButton cancelButton= new JButton("Cancel");
            cancelButton.addActionListener(ae -> jd.dispose());

            jd.add(findPanel);
            jd.add(replacePanel);
            jd.add(new JPanel().add(cancelButton));

            jd.pack();
            jd.setVisible(true);
        }

        public void replaceWord(boolean replaceAll)
        {
            String word = findTextField.getText();
            String text;

            try
            {
                text = jta.getDocument().getText(0, jta.getDocument().getLength());
                if (!replaceAll)
                    text = text.substring(jta.getCaretPosition());

                if (text.contains(word))
                {

                    if (replaceAll)
                        jta.setText(text.replaceAll(word, replaceTextField.getText()));
                    else
                    {
                        // remove any highlights
                        highlighter.removeAllHighlights();

                        int wordIndex = text.indexOf(word) + jta.getCaretPosition();
                        jta.setCaretPosition(wordIndex + word.length());

                        StringBuilder newText = new StringBuilder(text);
                        newText.replace(wordIndex, wordIndex + word.length(), replaceTextField.getText());

                        jta.setText(newText.toString());
                        jta.setCaretPosition(0);
                    }

                }
                else
                    throw new BadLocationException("Word not found", jta.getCaretPosition());

            }
            catch(BadLocationException e)
            {
                JOptionPane.showMessageDialog(win, String.format("Cannot find \"%s\"", word), "Alert", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        public void goTo()
        {
            JDialog jd = new JDialog(win, "Go To Line", false);
            jd.setLayout(new FlowLayout());
            jd.setPreferredSize(new Dimension(250, 125));
            jd.setMinimumSize(jd.getPreferredSize());
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            jd.setLocationRelativeTo(win);

            JLabel gotoLabel = new JLabel("Line number:");
            gotoLabel.setFont(defaultFont);
            jd.add(gotoLabel);

            ActionListener gotoAction = ae ->
            {
                try
                {
                    int line = Integer.parseInt(gotoTextField.getText());
                    jta.setCaretPosition(jta.getDocument().getDefaultRootElement().getElement(line-1).getStartOffset());
                    jd.dispose();
                }
                catch(NumberFormatException  e)
                {
                    JOptionPane.showMessageDialog(win, "Only numbers are acceptable", "Alert", JOptionPane.WARNING_MESSAGE);
                }
                catch (NullPointerException e)
                {
                    JOptionPane.showMessageDialog(win, String.format("\"%s\" is beyond the total number of lines", gotoTextField.getText()), "Alert", JOptionPane.WARNING_MESSAGE);
                    gotoTextField.setText("1");
                }
            };

            gotoTextField.addActionListener(gotoAction);
            jd.add(gotoTextField);

            JButton gotoButton = new JButton("Go To");
            gotoButton.addActionListener(gotoAction);
            jd.add(gotoButton);

            JButton cancelButton= new JButton("Cancel");
            cancelButton.addActionListener(ae -> jd.dispose());
            jd.add(cancelButton);

            jd.pack();
            jd.setVisible(true);

        }

        public void printTimestamp()
        {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a MM/dd/YYYY");
            String timestamp = sdf.format(new Date());

            jta.insert(timestamp, jta.getCaretPosition());
        }
    }
}
