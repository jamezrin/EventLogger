package me.jaime29010.eventlogger.shared;

public class SignData {
    private final LocationData location;
    private final String[] lines;

    public SignData(LocationData location, String[] lines) {
        this.location = location;
        this.lines = lines;
    }

    public LocationData getLocation() {
        return location;
    }

    public String[] getLines() {
        return lines;
    }
}
