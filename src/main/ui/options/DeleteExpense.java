package ui.options;

import model.*;
import model.exception.RecordAlreadyExistsException;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.TrackerApp;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Option allowing user to delete an expense
public class DeleteExpense extends Option {
    private Expense expense;

    public DeleteExpense(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        expense = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Delete Expense");
        button.setActionCommand("openDelete");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new DeleteExpense.DeleteExpenseClickHandler());
    }

    private class DeleteExpenseClickHandler extends JFrame implements ActionListener {
        private static final int WIDTH = 600;
        private static final int HEIGHT = 500;

        private JFrame expenseWindow;
        private JPanel panel;
        private JFormattedTextField givenYear;
        private JLabel infoLabel;
        private JScrollPane display;
        private JTextArea expenseView;
        private JTextArea toDelete;

        private Month selectedMonth;
        private int selectedYear;

        private JsonWriter writer;
        private JsonReader reader;

        // EFFECTS: sets active option to delete expense
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("openDelete")) {
                initializeNewWindow();

                if (trackerApp.currentRecord == null) {
                    promptUser();
                } else {
                    initializeExpenseGraphics();
                }
            } else if (command.equals("confirm")) {
                selectedYear = Integer.parseInt(givenYear.getText());
                initializeRecord();
                panel.removeAll();
                initializeExpenseGraphics();
            } else if (command.equals("delete")) {
                deleteExpense();
                confirmDeleted();
            }
        }

        // EFFECTS: adds a short delay after delete button is clicked before closing window
        private void confirmDeleted() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                System.out.println("InterruptedException thrown");
            }
            expenseWindow.dispatchEvent(new WindowEvent(expenseWindow, WindowEvent.WINDOW_CLOSING));
        }

        // MODIFIES: expenseWindow, trackerApp.currentRecord
        // EFFECTS:  deletes an expense from trackerApp.currentRecord
        private void deleteExpense() {
            int index = Integer.parseInt(toDelete.getText()) - 1;
            expense = trackerApp.currentRecord.getExpense(index);
            trackerApp.currentRecord.removeExpense(expense);
            saveRecord();
        }

        // MODIFIES: expenseWindow
        // EFFECTS:  draws the related graphics required to delete an expense
        private void initializeExpenseGraphics() {
            JPanel inputArea = new JPanel();
            JButton deleteExpenseButton = new JButton("Delete Expense");
            expenseView = new JTextArea();
            display = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            infoLabel = new JLabel("Please enter the number of the expense you would like to delete.");

            deleteExpenseButton.addActionListener(this);
            deleteExpenseButton.setActionCommand("delete");
            infoLabel.setHorizontalAlignment(JLabel.CENTER);
            expenseView.setEditable(false);

            displayExpenses();
            setUpPanel(inputArea, deleteExpenseButton);
        }

        // MODIFIES: expenseWindow
        // EFFECTS:  loads all expenses in trackerApp.currentRecord into text field
        private void displayExpenses() {
            StringBuilder s = new StringBuilder(new StringBuilder());
            int count = 1;

            for (Expense e : trackerApp.currentRecord.getExpenses()) {
                String cost = " (" + e.toDollars() + ")";
                String counter = "[" + count + "] ";
                s.append(counter).append(e.getCategory()).append(": ").append(e.getLabel()).append(cost).append("\n");
                count++;
            }

            expenseView.setText(s.toString());
            display.setViewportView(expenseView);
        }

        // MODIFIES: expenseWindow, panel
        // EFFECTS:  sets up panel options
        private void setUpPanel(JPanel inputArea, JButton deleteExpenseButton) {
            JLabel deleteLabel = new JLabel("Expense to Delete:");
            toDelete = new JTextArea();
            deleteLabel.setHorizontalAlignment(JLabel.CENTER);

            panel.setLayout(new BorderLayout());
            inputArea.setLayout(new GridLayout(1, 3));

            inputArea.add(deleteLabel);
            inputArea.add(toDelete);
            inputArea.add(deleteExpenseButton);
            panel.add(infoLabel, BorderLayout.NORTH);
            panel.add(display);
            panel.add(inputArea, BorderLayout.SOUTH);

            expenseWindow.add(panel);
            expenseWindow.setVisible(true);
            panel.setVisible(true);
        }

        // MODIFIES: expenseWindow
        // EFFECTS:  draws the JFrame window where this TrackerApp will operate
        // method taken and repurposed from DrawingEditor
        private void initializeNewWindow() {
            expenseWindow = new JFrame("New Expense");
            panel = new JPanel();

            expenseWindow.setLayout(new BorderLayout());
            expenseWindow.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            expenseWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            expenseWindow.setLocationRelativeTo(null);
            expenseWindow.setVisible(true);
        }

        // MODIFIES: expenseWindow
        // EFFECTS:  prompts the user to track a new record
        private void promptUser() {
            JPanel inputField = new JPanel();
            JComboBox monthList = new JComboBox(Month.values());
            JButton confirmButton = new JButton("Confirm");
            infoLabel = new JLabel("<html>Please select the month and year you would<br>"
                    + "like to delete an expense from.</html>");
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

            expenseWindow.add(panel);
            expenseWindow.setVisible(true);
            panel.setVisible(true);
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
            writer = new JsonWriter(trackerApp.trackedRecords.generateFilePath(month, year));
            try {
                trackerApp.currentRecord = reader.read();
            } catch (IOException ioException) {
                System.out.println("Error: file could not be read.");
            }
        }

        // EFFECTS: saves stored record in trackerApp.currentRecord to file
        private void saveRecord() {
            writer = new JsonWriter(trackerApp.trackedRecords.generateFilePath(trackerApp.currentRecord.getMonth(),
                    trackerApp.currentRecord.getYear()));
            try {
                writer.open();
            } catch (FileNotFoundException e) {
                System.out.println("There was an error trying to save the file.");
            }
            writer.write(trackerApp.currentRecord);
            System.out.println("Record saved to file.");
            writer.close();
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
