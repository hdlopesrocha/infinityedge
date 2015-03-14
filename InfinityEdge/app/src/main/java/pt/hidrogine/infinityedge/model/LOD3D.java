package pt.hidrogine.infinityedge.model;

import java.util.List;

import hidrogine.math.BoundingSphere;
import hidrogine.math.Camera;
import hidrogine.math.MathHelper;
import hidrogine.math.Matrix;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.util.ShaderProgram;


public class LOD3D extends AndroidModel3D {

    AndroidModel3D[] models;

    public LOD3D(final AndroidModel3D... models) {
        this.models = models;
    }


    public void draw(ShaderProgram shader, Camera camera, Matrix matrix) {
        float distance = (float) camera.getPosition().distance(matrix.getTranslation());
        float levelSize = camera.getFar()/models.length;


        int model = MathHelper.clamp((int) Math.floor(distance / levelSize), 0, models.length - 1);
        models[model].draw(shader,camera,matrix);
    }

    @Override
    public List<Vector3> getLights() {
        return null;
    }

    @Override
    public BoundingSphere getContainer() {
        return models[0].getContainer();
    }
}
