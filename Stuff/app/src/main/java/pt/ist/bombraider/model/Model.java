package pt.ist.bombraider.model;

import pt.ist.bombraider.util.Camera;
import pt.ist.bombraider.util.ShaderProgram;

public abstract class Model {

    public abstract void draw(ShaderProgram shader, Camera camera);


}
