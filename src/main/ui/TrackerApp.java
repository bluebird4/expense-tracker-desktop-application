package ui;

import model.MonthlyRecord;
import model.TrackedRecords;
import ui.options.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// GUI implementation of expense tracker application
// based off of DrawingEditor class in DrawingEditor
public class TrackerApp extends JFrame {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;

    private List<Option> options;
    private Option activeOption;

    private TrackedRecords trackedRecords;
    private MonthlyRecord currentRecord;

    // constructor taken and repurposed from DrawingEditor
    public TrackerApp() {
        super("Expense Tracker Application");
        initializeFields();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where this TrackerApp will operate
    // method taken and repurposed from DrawingEditor
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        initializeMenuButtons();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS:  sets currentRecord to null, instantiates new list of TrackedRecords, and initializes writer and reader
    // method taken and repurposed from DrawingEditor
    private void initializeFields() {
        currentRecord = null;
        trackedRecords = new TrackedRecords();
        options = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS:  a helper method which declares and instantiates app options
    // method taken and repurposed from DrawingEditor
    private void initializeMenuButtons() {
        JPanel menuArea = new JPanel();
        menuArea.setLayout(new GridLayout(8, 1));
        menuArea.setSize(new Dimension(0, 0));
        add(menuArea, BorderLayout.WEST);

        JLabel topLabel = new JLabel("\t\tWelcome! Please choose an option from below:");
        menuArea.add(topLabel);

        AddExpense addExpense = new AddExpense(this, menuArea);
        options.add(addExpense);

        DeleteExpense deleteExpense = new DeleteExpense(this, menuArea);
        options.add(deleteExpense);

        ChangeBudget changeBudget = new ChangeBudget(this, menuArea);
        options.add(changeBudget);

        ViewRecord viewRecord = new ViewRecord(this, menuArea);
        options.add(viewRecord);

        SwitchRecord switchRecord = new SwitchRecord(this, menuArea);
        options.add(switchRecord);

        SaveRecord saveRecord = new SaveRecord(this, menuArea);
        options.add(saveRecord);

        LoadRecord loadRecord = new LoadRecord(this, menuArea);
        options.add(loadRecord);
    }

    // MODIFIES: this
    // EFFECTS:  sets the given option as the activeOption
    public void setActiveOption(Option option) {
        activeOption.activate();
        activeOption = option;
    }

}
