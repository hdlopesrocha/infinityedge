package pt.hidrogine.infinityedge.dto.request;

import java.io.Serializable;

/**
 * Created by Henrique on 30/12/2014.
 */
public class DropBombDto implements Serializable {

    long id;

    public DropBombDto(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
