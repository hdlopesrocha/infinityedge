package pt.hidrogine.infinityedge.util;

import android.content.Context;
import android.opengl.GLES20;

import java.util.Stack;

import hidrogine.math.IVector3;
import hidrogine.math.Matrix;
import hidrogine.math.Vector3;
import pt.hidrogine.infinityedge.R;


public class ShaderProgram {

    private int mProgramHandle;
    private Matrix mModelMatrix =  new Matrix();
    private Matrix mProjectionMatrix = new Matrix();
    private Matrix mLightModelMatrix = new Matrix();
    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mLightPosHandle;
    private int mTextureUniformHandle;
    public int mLightEnabledHandle;
    public int mPositionHandle;
    public int mNormalHandle;
    public int mTextureCoordinateHandle;
    private int mAmbientColor;
    private int mDiffuseColor;

    Stack<Matrix> matrixStack = new Stack<Matrix>();


    public ShaderProgram(Context contexts) {
        final String vertexShader = RawResourceReader.readTextFileFromRawResource(contexts, R.raw.vertex);
        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(contexts, R.raw.fragment);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"a_Position", "a_Normal", "a_TexCoordinate"});

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_LightPos");
        mLightEnabledHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_LightEnabled");

        mAmbientColor = GLES20.glGetUniformLocation(mProgramHandle, "u_AmbientColor");
        mDiffuseColor = GLES20.glGetUniformLocation(mProgramHandle, "u_DiffuseColor");

        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
        GLES20.glUseProgram(mProgramHandle);

        setDefaultColors();
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glLineWidth(2);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);



        // Enable transparency
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        enableLight();
    }

    public int getProgram() {
        return mProgramHandle;
    }

    public void setDefaultColors() {
        setAmbientColor(0.0f, 0.0f, 0.0f, 0.0f);
        setDiffuseColor(1.0f, 1.0f, 1.0f, 1.0f);
    }


    public void applyCamera(Camera cam) {
        Matrix mViewMatrix = cam.getViewMatrix();
        GLES20.glUseProgram(mProgramHandle);
        final IVector3 mLightPosInModelSpace = new Vector3(10.0f, 10.0f, 10.0f);
        mLightModelMatrix.createTranslation(0, 0, -1f);
        final IVector3 mLightPosInWorldSpace = new Matrix().createTranslation(mLightPosInModelSpace).multiply(mLightModelMatrix).getTranslation();
        final IVector3 mLightPosInEyeSpace = new Matrix().createTranslation(mLightPosInWorldSpace).multiply(mViewMatrix).getTranslation();
        Matrix matrix = new Matrix(mModelMatrix);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, matrix.multiply(mViewMatrix).toArray(), 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrix.multiply(mProjectionMatrix).toArray(), 0);
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace.getX(), mLightPosInEyeSpace.getY(), mLightPosInEyeSpace.getZ());
    }

    public void updateViewPort(int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
       /* final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 1000.0f;*/
        mProjectionMatrix.createPerspectiveFieldOfView((float)Math.PI/4,ratio, 1.0f, 100f);

        //  Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }



    public void bindTexture(int tex) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex);
        GLES20.glUniform1i(mTextureUniformHandle, 0);
    }


    public void pushMatrix() {
        matrixStack.push(new Matrix(mModelMatrix));
    }

    public void popMatrix() {
        if (matrixStack.size() > 0) {
            mModelMatrix = matrixStack.pop();

        }
    }

    public void setIdentity() {
        mModelMatrix.identity();
    }

    public void scale(float s) {
        mModelMatrix.scale(s);
    }



    public void translate(float x, float y, float z) {
        mModelMatrix.translate(new Vector3(x, y, z));

    }

    public void rotate(float x, float y, float z) {
        mModelMatrix.multiply(new Matrix().createFromYawPitchRoll(x, y, z));
    }


    public void setDiffuseColor(float r, float g, float b, float a) {
        GLES20.glUniform4f(mDiffuseColor, r, g, b, a);

    }

    public void enableLight() {
        GLES20.glUniform1i(mLightEnabledHandle,1);

    }

    public void disableLight() {
        GLES20.glUniform1i(mLightEnabledHandle,0);

    }

    public void setAmbientColor(float r, float g, float b, float a) {
        GLES20.glUniform4f(mAmbientColor, r, g, b, a);

    }

    public void updateCamera(Camera camera) {
        camera.update(mProjectionMatrix);
    }
}
