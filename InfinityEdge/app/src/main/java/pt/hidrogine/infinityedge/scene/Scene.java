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
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import javax.microedition.khronos.opengles.GL10;

import hidrogine.math.Vector3;
import hidrogine.math.Space;
import hidrogine.math.Vector3;
import hidrogine.math.VisibleObjectHandler;
import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.object.Asteroid;
import pt.hidrogine.infinityedge.object.BillBoard;
import pt.hidrogine.infinityedge.object.Bullet;
import pt.hidrogine.infinityedge.object.Cloud;
import pt.hidrogine.infinityedge.object.Object3D;
import pt.hidrogine.infinityedge.object.Properties;
import pt.hidrogine.infinityedge.object.SpaceShip;
import pt.hidrogine.infinityedge.model.Model;
import pt.hidrogine.infinityedge.object.Team;
import pt.hidrogine.infinityedge.util.FileString;
import pt.hidrogine.infinityedge.util.ShaderProgram;

/**
 * Created by Henrique on 12/02/2015.
 */
public abstract class Scene {
    protected Space space = new Space();
    private TreeMap<String,Properties> properties = new TreeMap<String, Properties>();
    private Random random = new Random();
    private int size;
    private float time=0;
    private TreeMap<String,Team> teams = new TreeMap<String,Team>();

    public void update(float delta_t){
        time+=delta_t;
    }
    public void draw(final ShaderProgram shader){
        final LinkedList<Object3D> alphaObjects = new LinkedList<Object3D>();
        final LinkedList<Object3D> simpleObjects = new LinkedList<Object3D>();



        /* DRAW OBJECTS */
        space.handleVisibleObjects(Renderer.camera, new VisibleObjectHandler() {
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

        Collections.sort(alphaObjects, com);
        shader.disableLight();
        GLES20.glDisable(GL10.GL_DEPTH_TEST);
        for (Object3D obj : alphaObjects) {
            Model mod = (Model) obj.getModel();
            if(obj instanceof BillBoard){
                obj.getRotation().set(Renderer.camera.getRotation()).conjugate();
            }
            if(obj instanceof Cloud) {
                shader.setDiffuseColor(1, 1, 1, 0.25f);
            }

            mod.draw(shader, Renderer.camera,obj.getModelMatrix());

            if(obj instanceof Cloud) {
                shader.setDiffuseColor(1,1,1,1);
            }
        }
        shader.enableLight();
        GLES20.glEnable(GL10.GL_DEPTH_TEST);

        Collections.sort(simpleObjects,com);

        for (Object3D obj : simpleObjects) {

            if(obj instanceof Asteroid) {
                obj.getRotation().multiply(((Asteroid)obj).rotation).normalize();
            }
            else if(obj instanceof Bullet) {
                shader.disableLight();
                shader.setDiffuseColor(1, 1, 0, 1);
            }

            Model mod = (Model) obj.getModel();
            mod.draw(shader, Renderer.camera, obj.getModelMatrix());


            List<Vector3> lights = mod.getLights();
            if(lights != null && lights.size()>0) {
                shader.setDiffuseColor(1, 0, 0, (float) Math.sin(time*4)*.5f+.5f);

                shader.disableLight();
                for (Vector3 light : mod.getLights()) {
                    Vector3 rot = new Vector3(light).transform(obj.getRotation()).add(obj.getPosition());
                    BillBoard billBoard = new BillBoard(rot, Renderer.flare2);
                    billBoard.getRotation().set(Renderer.camera.getRotation()).conjugate();
                    Renderer.flare2.draw(shader, Renderer.camera, billBoard.getModelMatrix());
                }
                shader.enableLight();
                shader.setDiffuseColor(1, 1, 1, 1);

            }



            if(obj instanceof Bullet) {
                BillBoard billBoard = new BillBoard(obj.getPosition(),Renderer.flare);
                billBoard.getRotation().set(Renderer.camera.getRotation()).conjugate();
                Renderer.flare.draw(shader, Renderer.camera, billBoard.getModelMatrix());
                shader.enableLight();
                shader.setDiffuseColor(1, 1, 1, 1);
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


    public float getTime() {
        return time;
    }
    public void load(Context context, String path) {


        try {


            JSONObject map = new JSONObject(FileString.readAsset(context, path));
            size = map.getInt("size");

            if (map.has("properties")) {
                JSONObject jProperties = map.getJSONObject("properties");
                Iterator<String> keys = jProperties.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject jObject = jProperties.getJSONObject(key);
                    properties.put(key, new Properties(jObject));
                }
            }

            if(map.has("teams")){
                JSONObject jTeams = map.getJSONObject("teams");
                Iterator<String> it = jTeams.keys();
                while (it.hasNext()){
                    String key = it.next();
                    JSONObject jTeam = jTeams.getJSONObject(key);
                    teams.put(key,new Team(jTeam.getString("name")));
                }
            }


            JSONArray objects = map.getJSONArray("objects");
            for (int i = 0; i < objects.length(); ++i) {
                JSONObject jObj = objects.getJSONObject(i);
                String type = jObj.getString("type");
                Integer repeat = jObj.has("repeat") ? jObj.getInt("repeat") : 1;
                Properties props = jObj.has("properties") ? properties.get(jObj.getString("properties")) : null;
                Team team = jObj.has("team") ? teams.get(jObj.getString("team")) : null;


                while (repeat-- > 0) {
                    Vector3 position = convertPosition(jObj.getString("position"));
                    Object3D obj=null;
                    switch (type) {
                        case "asteroid1":
                            obj = new Asteroid(position, Renderer.asteroid1);
                            break;
                        case "asteroid2":
                            obj = new Asteroid(position, Renderer.asteroid2);
                            break;
                        case "asteroid3":
                            obj = new Asteroid(position, Renderer.asteroid3);
                            break;
                        case "asteroid4":
                            obj = new Asteroid(position, Renderer.asteroid4);
                            break;
                        case "flare":
                            obj = new BillBoard(position, Renderer.flare);
                            break;
                        case "cloud":
                            obj = new Cloud(position, Renderer.clouds[random.nextInt(Renderer.clouds.length)]);
                            break;
                        case "fighter":
                            obj = new SpaceShip(position, props);
                            break;
                        default:
                            System.err.println("MAP: object type not found!");
                            break;
                    }
                    if(obj!=null){
                        obj.insert(space);
                        if(team!=null){
                            team.getObjects().add(obj);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static long getUsedMemorySize() {

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedSize;

    }

    public abstract void end();
}
