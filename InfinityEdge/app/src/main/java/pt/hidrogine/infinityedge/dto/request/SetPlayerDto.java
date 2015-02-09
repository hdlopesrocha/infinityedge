package pt.hidrogine.infinityedge.dto.request;

import java.io.Serializable;

/**
 * Created by Henrique on 30/12/2014.
 */
public class SetPlayerDto implements Serializable {
    long id;
    boolean value;


    public SetPlayerDto(long id, boolean val){
        this.id = id;
        this.value = val;

    }

    public long getId() {
        return id;
    }

    public boolean getValue() {
        return value;
    }
}
