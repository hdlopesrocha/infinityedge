package pt.hidrogine.infinityedge.model;

import android.content.Context;

import hidrogine.math.Camera;
import hidrogine.math.Group;
import hidrogine.math.IBufferBuilder;
import hidrogine.math.IBufferObject;
import hidrogine.math.ITextureLoader;
import hidrogine.math.Material;
import hidrogine.math.Matrix;
import hidrogine.math.Model3D;
import hidrogine.math.Quaternion;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.util.ShaderProgram;
import pt.hidrogine.infinityedge.util.TextureLoader;


public class AndroidModel3D extends Model3D {


    public AndroidModel3D(final Context context, final TextureLoader loader, final int resource, final float scale) {
        this(context,loader,resource,scale,null);
    }

    public AndroidModel3D(){

    }


    public AndroidModel3D(final Context context, final TextureLoader loader, final int resource, final float scale, final Quaternion rot) {
        super(context.getResources().openRawResource(resource),scale,new IBufferBuilder() {
            @Override
            public IBufferObject build() {

                return new BufferObject();
            }
        },rot);


        loadTextures(loader);



    }


    private void loadTextures(final TextureLoader loader){
        for(final Material m : materials.values()){
            m.load(new ITextureLoader() {
                @Override
                public int load() {
                    String mapKd = m.filename.split("\\.")[0];

                    return loader.load(mapKd );
                }
            });

        }
    }

    public void draw(ShaderProgram shader, Camera camera, Matrix matrix) {
        shader.applyCamera(Renderer.camera, matrix);
        for (Group g : groups) {
            for (IBufferObject ib : g.getBuffers()) {

                BufferObject b = (BufferObject) ib;
                b.draw(shader);
            }
        }
    }

}
