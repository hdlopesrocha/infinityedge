package pt.ist.bombraider.dto;


public class BoxDto extends GameObjectDto {


    public BoxDto(long id, float x, float z) {
        super("O", id, x, 0, z);
    }
}
