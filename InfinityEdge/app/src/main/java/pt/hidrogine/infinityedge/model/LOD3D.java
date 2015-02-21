package pt.hidrogine.infinityedge.model;

import java.util.List;

import hidrogine.math.Camera;
import hidrogine.math.IBoundingSphere;
import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Matrix;
import pt.hidrogine.infinityedge.util.MathHelper;
import pt.hidrogine.infinityedge.util.ShaderProgram;


public class LOD3D extends Model implements IModel3D{

    Model3D [] models;

    public LOD3D(final Model3D ... models) {
        this.models = models;
    }


    public void draw(ShaderProgram shader, Camera camera, Matrix matrix) {
        float distance = (float) camera.getPosition().distance(matrix.getTranslation());
        float levelSize = camera.getFar()/models.length;


        int model = MathHelper.clamp((int) Math.floor(distance/ levelSize),0,models.length-1);
        models[model].draw(shader,camera,matrix);
    }

    @Override
    public List<IVector3> getLights() {
        return null;
    }

    @Override
    public IBoundingSphere getContainer() {
        return models[0].getContainer();
    }
}
