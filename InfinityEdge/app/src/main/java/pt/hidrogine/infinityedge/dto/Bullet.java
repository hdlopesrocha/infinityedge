package pt.hidrogine.infinityedge.dto;

import java.util.Random;

import hidrogine.math.IModel3D;
import hidrogine.math.IVector3;
import hidrogine.math.Quaternion;

/**
 * Created by Henrique on 12/02/2015.
 */
public class Bullet extends Object3D {

    public Bullet(IVector3 position, IModel3D model) {
        super(position, model);
    }
}