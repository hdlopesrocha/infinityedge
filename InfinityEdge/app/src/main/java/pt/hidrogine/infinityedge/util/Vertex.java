package pt.hidrogine.infinityedge.util;


import hidrogine.math.Vector2;
import hidrogine.math.Vector3;

public class Vertex {
    private Vector3 position;
    private Vector3 normal;
    private Vector2 texCoord;

    public Vertex(Vector3 p, Vector3 n, Vector2 t) {
        position = p;
        normal = n;
        texCoord = t;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public Vector2 getTexCoord() {
        return texCoord;
    }
}
