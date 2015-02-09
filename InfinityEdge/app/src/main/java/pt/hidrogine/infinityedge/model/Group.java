package pt.hidrogine.infinityedge.model;

import java.util.ArrayList;

public class Group {
    public String name;
    public ArrayList<BufferObject> subGroups = new ArrayList<BufferObject>();
    public float maxY = Float.MIN_VALUE;

    public Group() {

    }
}
