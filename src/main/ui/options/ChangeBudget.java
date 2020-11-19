package ui.options;

import model.MonthlyRecord;
import ui.TrackerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Option allowing user to change the monthly budget of the current record
public class ChangeBudget extends Option {
    private MonthlyRecord record;

    public ChangeBudget(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        record = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Change Monthly Budget");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new ChangeBudget.ChangeBudgetClickHandler());
    }

    private class ChangeBudgetClickHandler implements ActionListener {

        // EFFECTS: sets active option to change budget
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            trackerApp.setActiveOption(ChangeBudget.this);
        }
    }

}
