package pt.hidrogine.infinityedge.model;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import hidrogine.math.BoundingSphere;
import hidrogine.math.Camera;
import hidrogine.math.IBoundingSphere;
import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Matrix;
import hidrogine.math.Quaternion;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.util.Material;
import pt.hidrogine.infinityedge.util.MathHelper;
import pt.hidrogine.infinityedge.util.ShaderProgram;
import pt.hidrogine.infinityedge.util.TextureLoader;


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
    public IBoundingSphere getContainer() {
        return models[0].getContainer();
    }
}
