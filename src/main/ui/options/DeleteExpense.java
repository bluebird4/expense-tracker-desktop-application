package ui.options;

import model.Expense;
import model.MonthlyRecord;
import ui.TrackerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Option allowing user to delete an expense
public class DeleteExpense extends Option {
    private Expense expense;
    private MonthlyRecord record;

    public DeleteExpense(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        expense = null;
        record = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Delete Expense");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new DeleteExpense.DeleteExpenseClickHandler());
    }

    private class DeleteExpenseClickHandler implements ActionListener {

        // EFFECTS: sets active option to delete expense
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            trackerApp.setActiveOption(DeleteExpense.this);
        }
    }

}
