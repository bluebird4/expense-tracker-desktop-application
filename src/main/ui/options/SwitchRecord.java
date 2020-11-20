package ui.options;

import model.Month;
import model.MonthlyRecord;
import model.exception.RecordAlreadyExistsException;
import persistence.JsonReader;
import ui.TrackerApp;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

// Option allowing user to switch to a different record to track
public class SwitchRecord extends Option {

    public SwitchRecord(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Track a Different Month");
        button.setActionCommand("switch");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new SwitchRecord.SwitchRecordClickHandler());
    }

    private class SwitchRecordClickHandler extends JFrame implements ActionListener {
        private static final int WIDTH = 500;
        private static final int HEIGHT = 400;

        private JFrame switchWindow;
        private JPanel panel;
        private JFormattedTextField givenYear;
        private JLabel infoLabel;

        private Month selectedMonth;
        private int selectedYear;

        private JsonReader reader;

        // EFFECTS: sets active option to switch record
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("switch")) {
                initializeNewWindow();
                promptUser();
            } else if (command.equals("confirm")) {
                selectedYear = Integer.parseInt(givenYear.getText());
                initializeRecord();
                panel.removeAll();
                confirmSwitch();
            }
        }

        // MODIFIES: viewWindow
        // EFFECTS:  prompts the user to track a new record
        private void promptUser() {
            JPanel inputField = new JPanel();
            JComboBox monthList = new JComboBox(Month.values());
            JButton confirmButton = new JButton("Confirm");
            infoLabel = new JLabel("<html>Please select the month and year of the record<br>"
                    + "you would like to switch to.</html>");
            givenYear = new JFormattedTextField(createFormatter());

            selectedMonth = Month.JANUARY;
            monthList.addActionListener(this);
            confirmButton.addActionListener(this);
            confirmButton.setActionCommand("confirm");
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            processMonth(monthList);

            inputField.setLayout(new GridLayout(1, 2));
            panel.setLayout(new GridLayout(3, 1));

            inputField.add(monthList);
            inputField.add(givenYear);
            panel.add(infoLabel);
            panel.add(inputField);
            panel.add(confirmButton);

            switchWindow.add(panel);
            switchWindow.setVisible(true);
            panel.setVisible(true);
        }

        // EFFECTS: adds a short delay after add button is clicked before closing window
        private void confirmSwitch() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                System.out.println("InterruptedException thrown");
            }
            switchWindow.dispatchEvent(new WindowEvent(switchWindow, WindowEvent.WINDOW_CLOSING));
        }

        // MODIFIES: trackerApp.currentRecord, trackerApp.trackedRecords
        // EFFECTS:  initializes new record from user input, loads from file if record already exists
        //           otherwise, creates new record
        private void initializeRecord() {
            trackerApp.currentRecord = new MonthlyRecord(selectedMonth, selectedYear);
            try {
                trackerApp.trackedRecords.addRecord(trackerApp.currentRecord);
            } catch (RecordAlreadyExistsException e) {
                loadRecord(selectedMonth, selectedYear);
            }
        }

        // MODIFIES: trackerApp.currentRecord
        // EFFECTS:  loads record from file and stores it in trackerApp.currentRecord
        private void loadRecord(Month month, int year) {
            reader = new JsonReader(trackerApp.trackedRecords.generateFilePath(month, year));
            try {
                trackerApp.currentRecord = reader.read();
            } catch (IOException ioException) {
                System.out.println("Error: file could not be read.");
            }
        }

        // MODIFIES: viewWindow
        // EFFECTS:  draws the JFrame window where this TrackerApp will operate
        private void initializeNewWindow() {
            switchWindow = new JFrame("Switch Record");
            panel = new JPanel();

            switchWindow.setLayout(new BorderLayout());
            switchWindow.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            switchWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            switchWindow.setLocationRelativeTo(null);
            switchWindow.setVisible(true);
        }

        // MODIFIES: selectedMonth
        // EFFECTS:  adds an ActionListener to monthList to change value of selectedMonth to user selection
        private void processMonth(JComboBox monthList) {
            monthList.addActionListener(e -> selectedMonth = (Month) monthList.getSelectedItem());
        }

        // EFFECTS: creates a MaskFormatter for use in JFormattedTextField givenYear
        // method taken from https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
        private MaskFormatter createFormatter() {
            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter("20##");
            } catch (java.text.ParseException exc) {
                System.err.println("formatter is bad: " + exc.getMessage());
                System.exit(-1);
            }
            return formatter;
        }

    }

}
