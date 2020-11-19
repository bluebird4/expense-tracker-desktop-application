package ui.options;

import ui.TrackerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Option allowing user to load a record from file
public class LoadRecord extends Option {

    public LoadRecord(TrackerApp trackerApp, JComponent parent) {
        super(trackerApp, parent);
    }

    @Override
    protected void createButton(JComponent parent) {
        button = new JButton("Load Record From File");
        addToParent(parent);
    }

    // MODIFIES: this
    // EFFECTS:  constructs a new listener object which is added to the JButton
    @Override
    protected void addListener() {
        button.addActionListener(new LoadRecord.LoadRecordClickHandler());
    }

    private class LoadRecordClickHandler implements ActionListener {

        // EFFECTS: sets active option to load record
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            trackerApp.setActiveOption(LoadRecord.this);
        }
    }
}
