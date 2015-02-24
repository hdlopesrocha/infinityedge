package pt.hidrogine.infinityedge.object;

import java.util.Random;

import hidrogine.math.IModel3D;
import hidrogine.math.Vector3;
import hidrogine.math.Quaternion;

/**
 * Created by Henrique on 12/02/2015.
 */
public class Asteroid extends Object3D {
   public Quaternion rotation;

private  static Random random = new Random();
    public Asteroid(Vector3 position, IModel3D model) {
        super(position, model);

        rotation= new Quaternion(getRandom(),getRandom(),getRandom(),1f).normalize();


    }


    private static float getRandom(){
      return  (random.nextFloat()-0.5f)*0.05f;
    }

}