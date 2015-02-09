package pt.hidrogine.infinityedge.dto.request;

import java.io.Serializable;

/**
 * Created by Henrique on 30/12/2014.
 */
public class ChangeObjectDto implements Serializable {

    long id;
    float angle;

    public ChangeObjectDto(long id, float angle){
        this.id = id;
        this.angle = angle;
    }

    public long getId() {
        return id;
    }

    public float getAngle() {
        return angle;
    }
}
