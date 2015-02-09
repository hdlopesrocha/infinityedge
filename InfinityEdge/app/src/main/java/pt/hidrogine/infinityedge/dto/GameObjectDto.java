package pt.hidrogine.infinityedge.dto;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class GameObjectDto {


    private float X, Y, Z;

    private long id;


    private String type;

    public GameObjectDto(String t) {
        type = t;
        X = Y = Z = 0;
    }

    public GameObjectDto(String t, long i, float x, float y, float z) {
        X = x;
        Y = y;
        Z = z;
        id = i;
        type = t;
    }


    public boolean isEnabled() {
        return true;
    }

    public String getType() {
        return type;
    }

    public GameObjectDto(String t, JSONObject o) throws JSONException {

        type = t;
        String[] split = o.getString("p").split(",");
        X = Float.valueOf(split[0]);
        //    Y = (float) o.getDouble("y");
        Z = Float.valueOf(split[1]);
        id = o.getLong("i");
    }

    public final long getId() {
        return id;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public float getZ() {
        return Z;
    }


    public JSONObject toJSON() throws JSONException {


        JSONObject json = new JSONObject();

        json.put("p", String.format("%.2f", this.X).replace(',', '.') + "," + String.format("%.2f", this.Z).replace(',', '.'));
        json.put("t", this.type);
        json.put("i", this.id);

        return json;
    }


    public static String round(float f) {
        return String.format("%.2f", f).replace(',', '.');
    }
}
