package pt.hidrogine.infinityedge.model;


import hidrogine.math.Matrix;
import pt.hidrogine.infinityedge.util.ShaderProgram;
import hidrogine.math.Camera;


public abstract class Model {

    public abstract void draw(ShaderProgram shader, Camera camera, Matrix matrix);


}
