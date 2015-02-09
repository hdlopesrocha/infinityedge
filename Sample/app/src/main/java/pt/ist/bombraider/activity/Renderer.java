package pt.ist.bombraider.activity;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.BoundingFrustum;
import hidrogine.math.Matrix;
import pt.ist.bombraider.R;
import pt.ist.bombraider.dto.BoxDto;
import pt.ist.bombraider.model.Model3D;
import pt.ist.bombraider.util.Camera;
import pt.ist.bombraider.util.ShaderProgram;



public class Renderer implements GLSurfaceView.Renderer {
    private ShaderProgram shader;
    private Camera camera = new Camera();
    private Game game;
    private Model3D box;


    public void init(Game activity) {
        game = activity;

        box = new Model3D(activity, R.raw.box, 0.8f);
        camera.lookAt(16, 4, 0, 0, 0);

    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        init(Game.activity);
        shader = new ShaderProgram(game);
        shader.applyCamera(camera);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        shader.updateViewPort(width, height);
    }



    @Override
    public void onDrawFrame(GL10 glUnused) {


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        shader.setIdentity();

        BoundingFrustum frustum = new BoundingFrustum(new Matrix(camera.getViewMatrix()).multiply(camera.getProjectionMatrix()));


        camera.lookAt(4, 4, 0, 0, 0);

        shader.updateCamera(camera);

        BoxDto box = new BoxDto(0l, 0f, 0f);

        draw(box);


    }

    float angle = 0;

    public void draw(BoxDto obj) {
        shader.pushMatrix();
        shader.rotate(angle,0,0);
        shader.translate(obj.getX(), obj.getY(), obj.getZ());
        box.draw(shader,camera);
        shader.popMatrix();
        angle += 0.01f;
    }



}
