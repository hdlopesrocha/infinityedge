package pt.ist.bombraider.util;


import hidrogine.math.BoundingFrustum;
import hidrogine.math.Matrix;
import hidrogine.math.Vector3;


public class Camera {

    private Matrix viewMatrix,projectionMatrix;

    private float eye_x, eye_y, eye_z;
    private float look_x, look_y, look_z;
    private float up_x, up_y, up_z;
    private BoundingFrustum frustum = new BoundingFrustum();

    public Camera(){
        viewMatrix = new Matrix();
        projectionMatrix = new Matrix();
    }
    public float distanceSquared(float x, float y, float z){
        float dx=eye_x-x;
        float dy=eye_y-y;
        float dz=eye_z-z;
        return dx*dx + dy*dy + dz*dz;
    }


    public  Matrix getViewMatrix(){
        return viewMatrix;
    }
    public  Matrix getProjectionMatrix(){
        return projectionMatrix;
    }

    public void update(Matrix projectionMatrix){
        this.viewMatrix = new hidrogine.math.Matrix();
        this.viewMatrix.createLookAt(new Vector3(eye_x,eye_y,eye_z),new Vector3(look_x-eye_x,look_y-eye_y,look_z-eye_z).normalize(),new Vector3(up_x,up_y,up_z).normalize());
        this.projectionMatrix = projectionMatrix;
        frustum.setMatrix(viewMatrix);
    }


    public void lookAt(float height, float back, float x, float y, float z) {
        eye_x = x ;
        eye_y = y + height;
        eye_z = z + back;
        look_x = x;
        look_y = y;
        look_z = z;
        up_x = 0;
        up_y = 1;
        up_z = 0;

    }
}
