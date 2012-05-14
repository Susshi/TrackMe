package de.androidlab.trackme.map;

public class RouteListEntry {
    public int image;
    public String name;
    public boolean checked;
    
    public RouteListEntry(int image, String name, boolean checked) {
        this.image = image;
        this.name = name;
        this.checked = checked;
    }
}
