package pt.hidrogine.infinityedge.dto.request;

import java.io.Serializable;

/**
 * Created by Henrique on 30/12/2014.
 */
public class CreatePlayerDto implements Serializable{
    String name;
    public CreatePlayerDto(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
