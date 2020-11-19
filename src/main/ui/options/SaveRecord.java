package ui.options;

import ui.TrackerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Option allowing user to save current record to file
public class SaveRecord extends Option {

    public SaveRecord(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Save Record to File");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new SaveRecord.SaveRecordClickHandler());
    }

    private class SaveRecordClickHandler implements ActionListener {

        // EFFECTS: sets active option to save record
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            trackerApp.setActiveOption(SaveRecord.this);
        }
    }

}
