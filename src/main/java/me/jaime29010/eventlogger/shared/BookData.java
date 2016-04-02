package me.jaime29010.eventlogger.shared;

import java.util.List;

public class BookData {
    private final LocationData location;
    private final String title;
    private final String author;
    private final List<String> pages;
    private final LocationData chestLocation;

    public BookData(LocationData location, String title, String author, List<String> pages, LocationData chestLocation) {
        this.location = location;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.chestLocation = chestLocation;
    }

    public LocationData getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getPages() {
        return pages;
    }

    public LocationData getChestLocation() {
        return chestLocation;
    }
}
