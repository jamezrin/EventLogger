package me.jaime29010.eventlogger.shared;

import org.apache.commons.lang3.text.WordUtils;

public class RenameData {
    private final LocationData location;
    private final String type;
    private final String name;
    public RenameData(LocationData location, String type, String name) {
        this.location = location;
        this.type = type;
        this.name = name;
    }

    public LocationData getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTypeCaps() {
        return WordUtils.capitalizeFully(type.replace('_', ' '));
    }
}
