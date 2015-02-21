package pt.hidrogine.infinityedge.dto;

import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Vector3;

/**
 * Created by Henrique on 12/02/2015.
 */
public class SpaceShip extends Object3D {
    public Properties properties;
    public Vector3 aceleration, velocity;


    public SpaceShip(IVector3 position, IModel3D model, Properties properties) {
        super(position, model);
        this.aceleration = new Vector3();
        this.velocity = new Vector3();
        this.properties = properties;

    }



    public void move(float deltaT)
    {
        float vel = velocity.length();
        final IVector3 accel = new Vector3(aceleration).addMultiply(velocity, -vel * properties.getFriction());
        velocity.addMultiply(accel, deltaT);
        getPosition().addMultiply(velocity,deltaT);
        System.out.println(vel);
    }


    public Properties getProperties() {
        return properties;
    }
}