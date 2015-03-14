package pt.hidrogine.infinityedge.scene;


import android.content.Context;
import android.opengl.GLES20;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.MathHelper;
import hidrogine.math.Matrix;
import hidrogine.math.Quaternion;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.object.Bullet;
import pt.hidrogine.infinityedge.object.Properties;
import pt.hidrogine.infinityedge.object.SpaceShip;
import pt.hidrogine.infinityedge.util.ShaderProgram;

public class Demo extends Scene {

    private Random random = new Random();
    private SpaceShip fighter;
    private LinkedList<Bullet> bullets = new LinkedList<Bullet>();


    public Demo(Context context){
        Properties properties = new Properties(10, 0.1f, 35, 35, 100, 1, "fighter1");



        fighter = new SpaceShip(new Vector3(0,0,0),properties);
        fighter.insert(space);
      //  Debug.startMethodTracing("test.trace");
        load(context,"map/ctf/easy/01.json");
      //  Debug.stopMethodTracing();

    }

    float getRandom(){
        return random.nextFloat()-0.5f;
    }

    @Override
    public void update(float delta_t) {
        super.update(delta_t);
        controlObject(fighter, delta_t);
        if (Renderer.fire){
            Bullet bullet = new Bullet(fighter, Renderer.bullet);
            bullet.insert(space);
            bullets.add(bullet);
        }
        Iterator<Bullet> it = bullets.iterator();

        while (it.hasNext()){
            Bullet b = it.next();
            b.move(delta_t);
            if(b.timeToLive<0){
                it.remove();
                b.remove();
            }
            else {
                b.update(space);
            }
        }


        System.out.println("MEM: " + getUsedMemorySize()/(1024*1024f));



    }



    private float animX =0,animY =0;




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


        Vector3 up = new Vector3(0,1,0).transform(after);
        Vector3 dir = new Vector3(0,0,1).transform(after);
        Vector3 side = new Vector3(1,0,0).transform(after);

        obj.getAcceleration().set(0,0,0).addMultiply(dir,Renderer.accel*obj.getProperties().getAcceleration());
        obj.move(delta_t);
        obj.update(space);

        animX = MathHelper.lerp(animX, Renderer.analogX, 0.1f);
        animY = MathHelper.lerp(animY,Renderer.analogY,0.1f);


        Vector3 pos = new Vector3(obj.getPosition()).addMultiply(dir,-4).addMultiply(up, 1).addMultiply(up,animY*.5f).addMultiply(side,-animX*.5f);
        Renderer.camera.lookAt(pos ,new Vector3(obj.getPosition()).addMultiply(dir,distance),up);


    }



    @Override
    public void draw(final ShaderProgram shader){
        shader.applyCamera(Renderer.camera, new Matrix().identity());
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
        Renderer.sky.draw(shader, Renderer.camera,mat);
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
    }


}
