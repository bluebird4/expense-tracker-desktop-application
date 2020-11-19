package ui.options;

import model.MonthlyRecord;
import ui.TrackerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Option allowing user to view currently selected monthly record
public class ViewRecord extends Option {
    MonthlyRecord record;

    public ViewRecord(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        record = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("View Current Record");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new ViewRecord.ViewRecordClickHandler());
    }

    private class ViewRecordClickHandler implements ActionListener {

        // EFFECTS: sets active option to view record
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            trackerApp.setActiveOption(ViewRecord.this);
        }
    }

}
