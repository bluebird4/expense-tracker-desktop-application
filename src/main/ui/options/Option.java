package ui.options;

import ui.TrackerApp;

import javax.swing.*;

// Abstract class representing a clickable menu option in GUI application
// methods and base of abstract class taken and repurposed from DrawingEditor
public abstract class Option {
    protected JButton button;
    protected TrackerApp trackerApp;
    private boolean active;

    public Option(TrackerApp trackerApp, JComponent parent) {
        this.trackerApp = trackerApp;
        createButton(parent);
        addToParent(parent);
        active = false;
        addListener();
    }

    // EFFECTS: creates button to activate Option
    protected abstract void createButton(JComponent parent);

    // EFFECTS: adds a listener for this Option
    protected abstract void addListener();

    public boolean isActive() {
        return active;
    }

    // EFFECTS: sets this Option's active field to true
    public void activate() {
        active = true;
    }

    // EFFECTS: sets this Option's active field to false
    public void deactivate() {
        active = false;
    }

    // MODIFIES: parent
    // EFFECTS:  adds the given button to the parent component
    public void addToParent(JComponent parent) {
        parent.add(button);
    }

}
