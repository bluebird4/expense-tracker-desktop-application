package ui.options;

import model.MonthlyRecord;
import ui.TrackerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Option allowing user to switch to a different record to track
public class SwitchRecord extends Option {
    private MonthlyRecord newRecord;

    public SwitchRecord(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
        newRecord = null;
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Track a Different Month");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new SwitchRecord.SwitchRecordClickHandler());
    }

    private class SwitchRecordClickHandler implements ActionListener {

        // EFFECTS: sets active option to switch record
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            trackerApp.setActiveOption(SwitchRecord.this);
        }
    }

}
