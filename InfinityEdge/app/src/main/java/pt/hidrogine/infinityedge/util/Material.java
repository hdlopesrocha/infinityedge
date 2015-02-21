package pt.hidrogine.infinityedge.util;

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


    public void setTexture(TextureLoader loader, String mapKd) {
        mapKd = mapKd.split("\\.")[0];

        try {

            texture = loader.load(mapKd );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
