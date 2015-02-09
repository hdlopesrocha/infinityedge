package pt.hidrogine.infinityedge.util;

import android.content.Context;


public class Material {
    public Float[] Ka;
    public Float[] Kd;
    public Float[] Ks;
    public Float Tf;
    public Float illum;
    public Float d;
    public Float Ns;
    public Float sharpness;
    public Float Ni;
    public int texture = 0;

    public Material() {

    }

    public void setTexture(Context context, String mapKd) {
        mapKd = mapKd.split("\\.")[0];

        try {
            texture = TextureLoader.load(context, context.getResources().getIdentifier("drawable/" + mapKd, "drawable", "pt.hidrogine.infinityedge"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
