package persistence;

import org.json.JSONObject;

// Interface taken and repurposed from JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
