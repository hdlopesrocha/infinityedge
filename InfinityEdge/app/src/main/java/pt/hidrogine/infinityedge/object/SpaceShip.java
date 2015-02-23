package pt.hidrogine.infinityedge.object;

import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.model.Model3D;

/**
 * Created by Henrique on 12/02/2015.
 */
public class SpaceShip extends Object3D {
    private Properties properties;
    private Vector3 acceleration, velocity;


    public SpaceShip(IVector3 position, Properties properties) {
        super(position, properties.getModel3D());
        this.acceleration = new Vector3();
        this.velocity = new Vector3();
        this.properties = properties;




    }



    public void move(float deltaT)
    {
        float vel = velocity.length();
        final IVector3 accel = new Vector3(acceleration).addMultiply(velocity, -vel * properties.getFriction());
        velocity.addMultiply(accel, deltaT);
        getPosition().addMultiply(velocity,deltaT);
 //       System.out.println(vel);
    }


    public Properties getProperties() {
        return properties;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public Vector3 getAcceleration() {
        return acceleration;
    }
}