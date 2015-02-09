package pt.hidrogine.infinityedge.model;


import pt.hidrogine.infinityedge.util.Camera;
import pt.hidrogine.infinityedge.util.ShaderProgram;

public abstract class Model {

    public abstract void draw(ShaderProgram shader, Camera camera);


}
