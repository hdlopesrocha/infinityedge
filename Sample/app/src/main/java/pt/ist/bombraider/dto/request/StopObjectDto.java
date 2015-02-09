package pt.ist.bombraider.dto.request;

import java.io.Serializable;

/**
 * Created by Henrique on 30/12/2014.
 */
public class StopObjectDto implements Serializable {

    long id;

    public StopObjectDto(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
