package pt.hidrogine.infinityedge.object;

import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Vector3;

/**
 * Created by Henrique on 12/02/2015.
 */
public class Bullet extends Object3D {


    public IVector3 velocity;
    public float timeToLive = 2;

    public Bullet(SpaceShip owner, IModel3D model) {
        super( new Vector3(owner.getPosition()), model);

        float vel = owner.getVelocity().length();
        velocity = new Vector3(0,0,1).transform(owner.getRotation()).multiply(vel+70);
        getRotation().set(owner.getRotation());

    }

    public void move(float deltaT)
    {
        timeToLive-=deltaT;
        getPosition().addMultiply(velocity,deltaT);
    }

}