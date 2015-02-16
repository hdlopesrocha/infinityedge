package pt.hidrogine.infinityedge.dto;

import java.util.Random;

import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Quaternion;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.activity.Renderer;

/**
 * Created by Henrique on 12/02/2015.
 */
public class SpaceShip extends Object3D {
    public float maxAcceleration = 70; // (m/s)
    public float friction = 0.05f;
    public Vector3 aceleration, lastPosition, velocity;


    public SpaceShip(IVector3 position, IModel3D model) {
        super(position, model);
        lastPosition = new Vector3(position);
        aceleration = new Vector3();
        velocity = new Vector3();

    }



    public void move(float deltaT)
    {
        lastPosition.set(getPosition());
        float vel = velocity.length();
        final IVector3 accel = new Vector3(aceleration).addMultiply(velocity, -vel * friction);
        velocity.addMultiply(accel, deltaT);
        getPosition().addMultiply(velocity,deltaT);
    }


}