package pt.hidrogine.infinityedge.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Henrique on 21/02/2015.
 */
public class Properties {

    float attack, defence, velocity, acceleration, life;
    int guns;
    public float friction;


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

    public Properties(float attack, float defence, float velocity, float acceleration, float life, int guns) {
        this.attack = attack;
        this.defence = defence;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.life = life;
        this.guns = guns;
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
            this.friction = acceleration/(velocity*velocity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public float getFriction() {
        return friction;
    }
}
