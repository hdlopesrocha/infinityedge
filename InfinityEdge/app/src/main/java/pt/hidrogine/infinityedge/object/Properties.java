package pt.hidrogine.infinityedge.object;

import org.json.JSONException;
import org.json.JSONObject;

import pt.hidrogine.infinityedge.activity.Renderer;
import pt.hidrogine.infinityedge.model.AndroidModel3D;

/**
 * Created by Henrique on 21/02/2015.
 */
public class Properties {

    private float attack, defence, velocity, acceleration, life;
    private int guns;
    private float friction;
    private String model;

    public float getAttack() {
        return attack;
    }

    public float getDefence() {
        return defence;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getLife() {
        return life;
    }

    public int getGuns() {
        return guns;
    }

    public Properties(float attack, float defence, float velocity, float acceleration, float life, int guns, String model) {
        this.attack = attack;
        this.defence = defence;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.life = life;
        this.guns = guns;
        this.model=model;
        this.friction = acceleration/(velocity*velocity);
    }

    public Properties(JSONObject json){
        try {
            guns = json.getInt("guns");
            attack = (float)json.getDouble("attack");
            defence = (float)json.getDouble("defence");
            velocity = (float)json.getDouble("velocity");
            acceleration = (float)json.getDouble("acceleration");
            life = (float)json.getDouble("guns");
            model = json.getString("model");
            this.friction = acceleration/(velocity*velocity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AndroidModel3D getModel3D(){
        switch (getModel()){
            case "fighter1": return Renderer.fighter1;
            case "fighter2": return Renderer.fighter2;
            default:return null;
        }
    }


    public float getFriction() {
        return friction;
    }

    public String getModel() {
        return model;
    }
}
