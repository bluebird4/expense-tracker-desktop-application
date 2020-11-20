package ui.options;

import ui.TrackerApp;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

// Abstract class representing a clickable menu option in GUI application
// methods and base of abstract class taken and repurposed from DrawingEditor
public abstract class Option {
    protected JButton button;
    protected TrackerApp trackerApp;

    public Option(TrackerApp trackerApp, JComponent parent) {
        this.trackerApp = trackerApp;
        createButton(parent);
        addToParent(parent);
        addListener();
    }

    // EFFECTS: creates button to activate Option
    protected abstract void createButton(JComponent parent);

    // EFFECTS: adds a listener for this Option
    protected abstract void addListener();

    // MODIFIES: parent
    // EFFECTS:  adds the given button to the parent component
    public void addToParent(JComponent parent) {
        parent.add(button);
    }

    // EFFECTS: plays a sound from a file
    // method from http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
    public void playSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

}
