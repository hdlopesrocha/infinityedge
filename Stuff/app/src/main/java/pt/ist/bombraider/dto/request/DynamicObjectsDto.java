package pt.ist.bombraider.dto.request;

import java.io.Serializable;

/**
 * Created by Henrique on 30/12/2014.
 */
public class DynamicObjectsDto implements Serializable {

long timeStamp;
    public DynamicObjectsDto(long ts){
        timeStamp = ts;
    }



    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long ts){
        timeStamp = ts;
    }
}
