package pt.hidrogine.infinityedge.model;


import java.util.List;

import hidrogine.math.Camera;
import hidrogine.math.Vector3;
import hidrogine.math.Matrix;
import pt.hidrogine.infinityedge.util.ShaderProgram;


public abstract class Model {

    public abstract void draw(ShaderProgram shader, Camera camera, Matrix matrix);
    public abstract List<Vector3> getLights();
}
