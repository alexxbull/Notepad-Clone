/**
* Name: Bull, Alexx
* Project: 2
* Due: 02/12/2018
* Course: cs-245-01-w18
*
* Description:
* A simple demo of different fonts styles.
*/

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class JFontChooser
{
    private boolean clickedOK;
    private Color defaultColor;
    private Integer[] sizeArray;
    private JButton fontColorButton, okButton, cancelButton;
    private JDialog dialog;
    private JLabel fontNameLabel, fontStyleNameLabel, fontSizeValueLabel, sampleText;
    private JList<String> fontList;
    private JList<Integer> fontStyleList, fontSizeList;
    private JPanel buttonsPanel, fontPanel, fontStylePanel, fontSizePanel;
    private String[] systemFonts;

    public JFontChooser()
    {
        clickedOK = false;
        defaultColor = Color.BLACK;
        Font headerFont = new Font("Courier New", Font.BOLD, 16);         // default font for header labels
        Font labelsFont = new Font("Courier New", Font.PLAIN, 18);         // default font for labels
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);  // default border to be used on labels

        // make font dialog's components
        buttonsPanel = new JPanel();
        fontPanel = new JPanel(new GridLayout(3, 0));
        fontStylePanel = new JPanel(new GridLayout(3, 0));
        fontSizePanel = new JPanel(new GridLayout(3, 0));

        fontColorButton = new JButton("Font Color");
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        sampleText = new JLabel("Sample");

        // make font panel components
        JLabel fontLabel = new JLabel("Font:");
        fontLabel.setFont(headerFont);
        fontLabel.setHorizontalAlignment(JLabel.LEFT);
        fontLabel.setVerticalAlignment(JLabel.BOTTOM);

        systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontList = new JList(systemFonts); // list of fonts on system
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.setSelectedIndex(0);
        fontList.ensureIndexIsVisible(0);
        fontList.setVisibleRowCount(4);

        fontNameLabel = new JLabel(fontList.getSelectedValue());
        fontNameLabel.setFont(labelsFont);
        fontNameLabel.setBackground(Color.WHITE);
        fontNameLabel.setOpaque(true);
        fontNameLabel.setBorder(border);
        fontNameLabel.setDisplayedMnemonic('F');

        fontList.addListSelectionListener(lse ->
        {
            if (!lse.getValueIsAdjusting())
                setFont();
        });

        // add components to it's respective panel
        fontPanel.add(fontLabel);
        fontPanel.add(fontNameLabel);
        fontPanel.add(new JScrollPane(fontList));

        // make font style list
        JLabel fontStyleLabel = new JLabel("Font Style:");
        fontStyleLabel.setFont(headerFont);
        fontStyleLabel.setHorizontalAlignment(JLabel.LEFT);
        fontStyleLabel.setVerticalAlignment(JLabel.BOTTOM);

        fontStyleList = new JList(new String[]{ "Bold", "Italic", "Regular"}); // list of font styles
        fontStyleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontStyleList.setSelectedIndex(2);
        fontStyleList.ensureIndexIsVisible(2);
        fontStyleList.setVisibleRowCount(4);

        fontStyleNameLabel = new JLabel("" + fontStyleList.getSelectedValue());
        fontStyleNameLabel.setFont(labelsFont);
        fontStyleNameLabel.setBackground(Color.WHITE);
        fontStyleNameLabel.setOpaque(true);
        fontStyleNameLabel.setBorder(border);

        fontStyleList.addListSelectionListener(lse ->
        {
            if (!lse.getValueIsAdjusting())
                setFont();
        });

        // add components to it's respective panel
        fontStylePanel.add(fontStyleLabel);
        fontStylePanel.add(fontStyleNameLabel);
        fontStylePanel.add(new JScrollPane(fontStyleList));

        // make font size list
        JLabel fontSizeLabel = new JLabel("Font Size:");
        fontSizeLabel.setFont(headerFont);
        fontSizeLabel.setHorizontalAlignment(JLabel.LEFT);
        fontSizeLabel.setVerticalAlignment(JLabel.BOTTOM);

        sizeArray = new Integer[]{8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};

        fontSizeList = new JList(sizeArray); // list of fonts sizes
        fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontSizeList.setSelectedIndex(10);
        fontSizeList.ensureIndexIsVisible(10);
        fontSizeList.setVisibleRowCount(4);

        fontSizeValueLabel = new JLabel("" + fontSizeList.getSelectedValue());
        fontSizeValueLabel.setFont(labelsFont);
        fontSizeValueLabel.setBackground(Color.WHITE);
        fontSizeValueLabel.setOpaque(true);
        fontSizeValueLabel.setBorder(border);

        fontSizeList.addListSelectionListener(lse ->
        {
            if (!lse.getValueIsAdjusting())
                setFont();
        });

        // add components to it's respective panel
        fontSizePanel.add(fontSizeLabel);
        fontSizePanel.add(fontSizeValueLabel);
        fontSizePanel.add(new JScrollPane(fontSizeList));

        // add components to buttonsPanel
        buttonsPanel.add(fontColorButton);
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.setSize(5, 5);

        // set sample text default properties
        sampleText.setFont(new Font("" + fontList.getSelectedValue(), Font.PLAIN, 24));
        sampleText.setHorizontalAlignment(JLabel.LEFT);
        sampleText.setVerticalAlignment(JLabel.CENTER);
    }

    // update each font category with the last selected value
    private void setFont()
    {
        int fontStyle = 0;
        switch (fontStyleList.getSelectedIndex())
        {
            case 0:
                fontStyle = Font.BOLD;
                break;
            case 1:
                fontStyle = Font.ITALIC;
                break;
            default:
                fontStyle = Font.PLAIN;
                break;
        }

        fontNameLabel.setText("" + fontList.getSelectedValue());
        fontStyleNameLabel.setText("" + fontStyleList.getSelectedValue());
        fontSizeValueLabel.setText("" + fontSizeList.getSelectedValue());

        fontList.ensureIndexIsVisible(fontList.getSelectedIndex());
        fontStyleList.ensureIndexIsVisible(fontStyleList.getSelectedIndex());
        fontSizeList.ensureIndexIsVisible(fontSizeList.getSelectedIndex());

        sampleText.setFont(new Font(fontList.getSelectedValue(), fontStyle, fontSizeList.getSelectedValue()));
    }

    public boolean showDialog(JFrame parent)
    {
        dialog = new JDialog(parent, "Font", true);
        dialog.setLayout(new GridLayout(0, 3));
        dialog.setSize(600, 400);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // add components to dialog
        dialog.add(fontPanel);
        dialog.add(fontStylePanel);
        dialog.add(fontSizePanel);
        dialog.add(buttonsPanel);
        dialog.add(new JPanel().add(sampleText));
        dialog.setLocationRelativeTo(parent);   // center dialog in parent's frame

        // set font color button properties
        fontColorButton.setSize(10,10);
        fontColorButton.addActionListener(ae ->
        {
            Color newColor = JColorChooser.showDialog(parent, "Font Color", defaultColor);
            if (newColor != null)
            {
                sampleText.setForeground(newColor);
                defaultColor = newColor;
            }
        });

        // change clickedOK to true if Ok button is clicked
        clickedOK = false;
        okButton.setSize(10,10);
        okButton.addActionListener(ae ->
        {
            clickedOK = true;
            dialog.dispose();
        });

        cancelButton.addActionListener(ae -> dialog.dispose());
        cancelButton.setSize(10,10);

        //dialog.pack();
        dialog.setVisible(true);

        return clickedOK;
    }

    public void setDefault(Font f)
    {
        int fontIndex = 0;
        // find the index of the the given in the systemFonts array
        for (int i = 0; i < systemFonts.length; i++)
        {
            if (systemFonts[i].equals(f.getName()))
            {
                fontIndex = i;
                break;
            }
        }

        // find the style of the given font
        int styleIndex;
        switch (f.getStyle())
        {
            case Font.BOLD:
                styleIndex = 0;
                break;
            case Font.ITALIC:
                styleIndex = 1;
                break;
            default:
                styleIndex = 2;
                break;
        }

        // find the index of the give font size in the fontSize list
        int sizeIndex = 0;
        for (int i = 0; i < sizeArray.length; i++)
        {
            if (sizeArray[i] == f.getSize())
            {
                sizeIndex = i;
                break;
            }
        }

        fontList.setSelectedIndex(fontIndex);
        fontStyleList.setSelectedIndex(styleIndex);
        fontSizeList.setSelectedIndex(sizeIndex);

        setFont();
    }

    public void setDefault(Color c){defaultColor = c;}
    public Color getColor(){return defaultColor;}

    public Font getFont()
    {
        int fontStyle = 0;
        switch (fontStyleList.getSelectedIndex())
        {
            case 0:
                fontStyle = Font.BOLD;
                break;
            case 1:
                fontStyle = Font.ITALIC;
                break;
            default:
                fontStyle = Font.PLAIN;
                break;
        }

        return new Font(fontList.getSelectedValue(), fontStyle, fontSizeList.getSelectedValue());
    }
}
