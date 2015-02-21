package pt.hidrogine.infinityedge.scene;

import android.content.Context;
import android.opengl.GLES20;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Space;
import hidrogine.math.Vector3;
import hidrogine.math.VisibleObjectHandler;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.model.Model3D;
import pt.hidrogine.infinityedge.object.Asteroid;
import pt.hidrogine.infinityedge.object.BillBoard;
import pt.hidrogine.infinityedge.object.Bullet;
import pt.hidrogine.infinityedge.object.Object3D;
import pt.hidrogine.infinityedge.object.Properties;
import pt.hidrogine.infinityedge.object.SpaceShip;
import pt.hidrogine.infinityedge.model.Model;
import pt.hidrogine.infinityedge.util.FileString;
import pt.hidrogine.infinityedge.util.ShaderProgram;

/**
 * Created by Henrique on 12/02/2015.
 */
public abstract class Scene {
    protected Space space = new Space();
    private TreeMap<String,Properties> properties;
    private Random random = new Random();
    private int size;

    public abstract void update(float delta_t);
    public void draw(final ShaderProgram shader){
        final LinkedList<Object3D> alphaObjects = new LinkedList<Object3D>();
        final LinkedList<Object3D> simpleObjects = new LinkedList<Object3D>();



        /* DRAW OBJECTS */
        space.handleVisibleObjects(Renderer.camera.getBoundingFrustum(), new VisibleObjectHandler() {
            @Override
            public void onObjectVisible(Object o) {
                Object3D obj = (Object3D) o;

                if(obj instanceof BillBoard){
                    alphaObjects.push(obj);
                }
                else {
                    simpleObjects.push(obj);

                }
            }
        });

        Comparator<Object3D> com = new Comparator<Object3D>() {
            @Override
            public int compare(Object3D lhs, Object3D rhs) {
                float ld = (float)lhs.getPosition().distanceSquared(Renderer.camera.getPosition());
                float rd = (float)rhs.getPosition().distanceSquared(Renderer.camera.getPosition());

                return ld > rd ? -1 : ld < rd ? 1 : 0;
            }
        };

        Collections.sort(alphaObjects,com);
        shader.disableLight();
        GLES20.glDisable(GL10.GL_DEPTH_TEST);
        GLES20.glDisable(GL10.GL_CULL_FACE);
        for (Object3D obj : alphaObjects) {
            Model mod = (Model) obj.getModel();
            if(obj instanceof BillBoard){
                obj.getRotation().set(Renderer.camera.getRotation()).conjugate();
            }

            mod.draw(shader, Renderer.camera,obj.getModelMatrix());
        }
        shader.enableLight();
        GLES20.glEnable(GL10.GL_CULL_FACE);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);







        for (Object3D obj : simpleObjects) {
            if(obj instanceof Asteroid) {
                obj.getRotation().multiply(((Asteroid)obj).rotation).normalize();
            }

            if(obj instanceof Bullet) {
                shader.disableLight();
            }

            Model mod = (Model) obj.getModel();
            mod.draw(shader, Renderer.camera, obj.getModelMatrix());
            if(obj instanceof Bullet) {
                shader.enableLight();
            }
        }
    }

    float getRandom(){
        return random.nextFloat()-0.5f;
    }


    public Vector3 convertPosition(String string){
        switch (string) {
            case "random" :
                return new Vector3(getRandom()*size,getRandom()*size,getRandom()*size);
            case "base" :
                break;
            default:
                String [] tokens = string.split(",");
                return new Vector3(Float.valueOf(tokens[0]),Float.valueOf(tokens[1]),Float.valueOf(tokens[2]));
        }
        return null;
    }


    public void load(Context context, String path) {


        try {
            properties = new TreeMap<String,Properties>();
            JSONObject jProperties = new JSONObject(FileString.readAsset(context, "properties.json"));
            Iterator<String> keys = jProperties.keys();
            while (keys.hasNext()){
                String key = keys.next();
                JSONObject jObject = jProperties.getJSONObject(key);
                properties.put(key,new Properties(jObject));
            }



            JSONObject map = new JSONObject(FileString.readAsset(context, path));
            size = map.getInt("size");

            JSONArray objects = map.getJSONArray("objects");
            for (int i=0; i < objects.length() ; ++i){
                JSONObject obj = objects.getJSONObject(i);
                String type = obj.getString("type");
                Integer repeat = obj.has("repeat")? obj.getInt("repeat"):1;
                Properties props = obj.has("properties") ? properties.get(obj.getString("properties")) : null;

                while (repeat-->0) {
                    Vector3 position = convertPosition(obj.getString("position"));
                    switch (type) {
                        case "asteroid1":
                            new Asteroid(position, Renderer.asteroid1).insert(space);
                            break;
                        case "asteroid2":
                            new Asteroid(position, Renderer.asteroid2).insert(space);
                            break;
                        case "asteroid3":
                            new Asteroid(position, Renderer.asteroid3).insert(space);
                            break;
                        case "asteroid4":
                            new Asteroid(position, Renderer.asteroid4).insert(space);
                            break;
                        case "flare":
                            new BillBoard(position, Renderer.flare).insert(space);
                            break;
                        case "cloud":
                            new BillBoard(position, Renderer.smoke1).insert(space);
                            break;
                        case "fighter":
                            new SpaceShip(position,props).insert(space);
                            break;
                        default:
                            System.err.println("MAP: object type not found!");
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    public abstract void end();
}
