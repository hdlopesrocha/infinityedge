package pt.hidrogine.infinityedge.scene;


import android.opengl.GLES20;
import android.os.Debug;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.IVector3;
import hidrogine.math.Matrix;
import hidrogine.math.Quaternion;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.dto.Asteroid;
import pt.hidrogine.infinityedge.dto.Object3D;
import pt.hidrogine.infinityedge.dto.SpaceShip;
import pt.hidrogine.infinityedge.util.ShaderProgram;

public class Demo extends Scene {

    private Random random = new Random();
    private SpaceShip fighter;
    public Demo(){
        fighter = new SpaceShip(new Vector3(0,0,0), Renderer.fighter);
        fighter.insert(space);
    //    Debug.startMethodTracing("myapp");
        int size = 1024;
        for(int i =0; i < 10000 ; ++i) {
            new Asteroid(new Vector3(getRandom()*size, getRandom()*size, getRandom()*size), Renderer.asteroid1).insert(space);
            new Asteroid(new Vector3(getRandom()*size, getRandom()*size, getRandom()*size), Renderer.asteroid2).insert(space);
            if(i%100==0)
            System.out.println("i="+i);
        }
    //    Debug.stopMethodTracing();

    }

    float getRandom(){
        return random.nextFloat()-0.5f;
    }

    @Override
    public void update(float delta_t){
        controlObject(fighter,delta_t);
    }

    private void controlObject(SpaceShip obj, float delta_t){
      //  obj._aceleration = _object._ray.Direction * _object._physics._acelerationMax * _control.analogAcel.Value;
        boolean axis_x = false;
        boolean axis_y = false;
        float sense = 0.03f;
        float distance= 256;

        Quaternion quat = new Quaternion().createFromYawPitchRoll(  (float) (Renderer.analogX * (axis_x ? 1 : -1) * sense * Math.PI / 2),
                                                                    (float) (Renderer.analogY * (axis_y ? 1 : -1) * sense * Math.PI / 2), 0);
        obj.getRotation().multiply(quat);
        Quaternion after = obj.getRotation();


        Vector3 up = new Vector3(0,1,0);
        Vector3 dir = new Vector3(0,0,1);

        up.transform(after);
        dir.transform(after);

        obj.aceleration.set(0,0,0).addMultiply(dir,Renderer.accel*obj.maxAcceleration);
        obj.move(delta_t);
        obj.update(space);

        IVector3 pos = new Vector3(obj.getPosition()).addMultiply(dir,-4).addMultiply(up, 1);
        Renderer.camera.lookAt(pos ,new Vector3(obj.getPosition()).addMultiply(dir,distance),up);

    }


    private void setCamera() {
      /*  float angle = (float) Math.PI / 24;
        boolean axis_x = true;
        boolean axis_y = true;
        float analog_x = _control.analogStick.Value.X * angle;
        float analog_y = _control.analogStick.Value.Y * angle;
        float distance = 256;
        Quaternion ypr = new Quaternion().createFromYawPitchRoll((axis_x ? -1 : 1) * analog_x, (axis_y ? -1 : 1) * analog_y + (float) Math.PI / 16f, 0);
        Quaternion quat = new Quaternion(fighter.getRotation()).multiply(ypr);
        IVector3 dir = (new Matrix().createTranslation(new Vector3(0, 0, 1)).multiply(new Matrix().createFromQuaternion(quat))).getTranslation().multiply(-6);
        IVector3 pos = new Vector3(dir).add(fighter.getCenter());
        IVector3 lookAt = new Vector3(fighter.getCenter()).addMultiply(fighter.getDirection(), distance);
        IVector3 direction = new Vector3(lookAt).subtract(pos).normalize();
        IVector3 side = new Vector3(direction).cross(fighter.getDirection());
        IVector3 up = new Vector3(new Vector3(side).cross(direction).normalize()).reflect(fighter.getSide());
        */
    }

    @Override
    public void draw(final ShaderProgram shader){
        shader.applyCamera(Renderer.camera,new Matrix().identity());
        drawSky(shader);
        super.draw(shader);

    }

    @Override
    public void end() {
       Renderer.currentScene = new Background();

    }


    public void drawSky(ShaderProgram shader) {
        GLES20.glDisable(GL10.GL_CULL_FACE);
        GLES20.glDisable(GL10.GL_DEPTH_TEST);
        shader.disableLight();
        Matrix mat = new Matrix().createTranslation(Renderer.camera.getPosition());
        shader.applyCamera(Renderer.camera, mat);
        Renderer.sky.draw(shader, Renderer.camera);
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
    }


}
