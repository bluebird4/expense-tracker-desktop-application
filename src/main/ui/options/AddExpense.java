package ui.options;

import model.Expense;
import model.MonthlyRecord;
import ui.TrackerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Option allowing the user to add a new expense to the current record
public class AddExpense extends Option {
    private Expense expense;
    private MonthlyRecord record;

    public AddExpense(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        expense = null;
        record = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Add New Expense");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new AddExpenseClickHandler());
    }

    private class AddExpenseClickHandler implements ActionListener {

        // EFFECTS: sets active option to add expense
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            trackerApp.setActiveOption(AddExpense.this);
        }
    }

}
