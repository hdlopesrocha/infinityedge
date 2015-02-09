package pt.ist.bombraider.dto.request;

import java.io.Serializable;

/**
 * Created by Henrique on 30/12/2014.
 */
public class LeaveDto implements Serializable {
    long id;

    public LeaveDto(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
