package pt.hidrogine.infinityedge.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henrique on 21/02/2015.
 */
public class Team {
    private String name;
    private List<Object3D> objects = new ArrayList<Object3D>();

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Object3D> getObjects() {
        return objects;
    }
}
