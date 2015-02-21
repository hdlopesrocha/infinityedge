package pt.hidrogine.infinityedge.util;

/**
 * Created by Henrique on 20/02/2015.
 */
public class MathHelper {

    public static float clamp(float val, float min, float max){
        return val<min?min:val>max?max:val;
    }


    public static int clamp(int val, int min, int max){
        return val<min?min:val>max?max:val;
    }
}