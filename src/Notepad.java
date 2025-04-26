import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Notepad extends JFrame implements ActionListener {
    JTextArea padArea;
    JScrollPane scrollPane;
    JLabel fontLabel;
    JSpinner fontSizeSpinner;
    JButton fontColorButton;
    JComboBox<String> fontBox;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem, saveItem, exitItem;

    Notepad() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Untitled - Notepad");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // Text Area
        padArea = new JTextArea();
        padArea.setFont(new Font("Arial", Font.PLAIN, 20));
        padArea.setLineWrap(true);
        padArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(padArea);
        this.add(scrollPane, BorderLayout.CENTER);

        // === Toolbar Panel for Font Options ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fontLabel = new JLabel("Font: ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                padArea.setFont(new Font(padArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
            }
        });

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox<>(fonts);
        fontBox.setSelectedItem("Arial");
        fontBox.addActionListener(this);

        topPanel.add(fontLabel);
        topPanel.add(fontBox);
        topPanel.add(new JLabel("Size: "));
        topPanel.add(fontSizeSpinner);
        topPanel.add(fontColorButton);

        this.add(topPanel, BorderLayout.NORTH);

        // === Menu Bar ===
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");

        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fontColorButton) {
            Color color = JColorChooser.showDialog(null, "Choose Text Color", padArea.getForeground());
            if (color != null) padArea.setForeground(color);
        }

        if (e.getSource() == fontBox) {
            padArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, padArea.getFont().getSize()));
        }

        if (e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int response = fileChooser.showOpenDialog(this);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (Scanner scanner = new Scanner(file)) {
                    padArea.setText("");
                    while (scanner.hasNextLine()) {
                        padArea.append(scanner.nextLine() + "\n");
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (e.getSource() == saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showSaveDialog(this);

            if (response == JFileChooser.APPROVE_OPTION) {
                try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                    writer.print(padArea.getText());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }

    }