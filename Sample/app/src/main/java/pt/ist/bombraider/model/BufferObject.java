package pt.ist.bombraider.model;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import pt.ist.bombraider.util.Camera;
import pt.ist.bombraider.util.Material;
import pt.ist.bombraider.util.ShaderProgram;

public class BufferObject {
    private ArrayList<Float> vertexData = new ArrayList<Float>();
    private ArrayList<Float> normalData = new ArrayList<Float>();
    private ArrayList<Float> textureData = new ArrayList<Float>();
    private ArrayList<Short> indexData = new ArrayList<Short>();
    private Material material;
    static final int POSITION_DATA_SIZE = 3;
    static final int NORMAL_DATA_SIZE = 3;
    static final int TEXTURE_COORDINATE_DATA_SIZE = 2;
    static final int BYTES_PER_FLOAT = 4;
    public FloatBuffer vertexBuffer;
    public ShortBuffer indexBuffer;
    public IntBuffer vbo;
    public int vbo_size;
    public int index_count;

    public BufferObject() {

    }

    public void setMaterial(Material f) {
        material = f;
    }

    public void addVertex(float f) {
        vertexData.add(f);
    }

    public void addVertex(float x, float y, float z) {
        vertexData.add(x);
        vertexData.add(y);
        vertexData.add(z);
    }

    public void addNormal(float f) {
        normalData.add(f);
    }

    public void addNormal(float x, float y, float z) {
        normalData.add(x);
        normalData.add(y);
        normalData.add(z);
    }

    public void addTexture(float f) {
        textureData.add(f);
    }

    public void addTexture(float x, float y) {
        textureData.add(x);
        textureData.add(y);
    }

    public void addIndex(short f) {
        indexData.add(f);
    }

    public void buildBuffer() {
        ArrayList<Float> packedVector = new ArrayList<Float>();
        vbo_size = vertexData.size();
        while (vertexData.size() > 0 && normalData.size() > 0 && textureData.size() > 0) {
            packedVector.add(vertexData.remove(0));
            packedVector.add(vertexData.remove(0));
            packedVector.add(vertexData.remove(0));
            packedVector.add(normalData.remove(0));
            packedVector.add(normalData.remove(0));
            packedVector.add(normalData.remove(0));
            packedVector.add(textureData.remove(0));
            packedVector.add(textureData.remove(0));
        }
        index_count = indexData.size();
        vertexBuffer = ByteBuffer.allocateDirect(packedVector.size() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(toFloatArray(packedVector)).position(0);

        indexBuffer = ByteBuffer.allocateDirect(indexData.size() * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(toShortArray(indexData)).position(0);

        // CLEAR
        vertexData.clear();
        normalData.clear();
        textureData.clear();
        indexData.clear();
        vertexData = null;
        normalData = null;
        textureData = null;
        indexData = null;

        vbo = IntBuffer.allocate(2);
        GLES20.glGenBuffers(2, vbo);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo.get(0));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                vertexBuffer.capacity() * 4,
                vertexBuffer,
                GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vbo.get(1));
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                indexBuffer.capacity() * 2,
                indexBuffer,
                GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }


    private static float[] toFloatArray(ArrayList<Float> list) {
        float[] ret = new float[list.size()];
        for (int i = 0; i < ret.length; ++i)
            ret[i] = list.get(i);
        return ret;
    }

    private static short[] toShortArray(ArrayList<Short> list) {
        short[] ret = new short[list.size()];
        for (int i = 0; i < ret.length; ++i)
            ret[i] = list.get(i);
        return ret;
    }


    public void draw(ShaderProgram shader, Camera camera) {


        final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;

        //Bind the texture according to the set texture filter
        if (material != null) {
            shader.applyCamera(camera);

            shader.bindTexture(material.texture);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo.get(0));

            GLES20.glVertexAttribPointer(shader.mPositionHandle, 3, GLES20.GL_FLOAT, false, stride, 0);
            GLES20.glEnableVertexAttribArray(shader.mPositionHandle);


            GLES20.glVertexAttribPointer(shader.mNormalHandle, 3, GLES20.GL_FLOAT, false, stride, 3 * 4);
            GLES20.glEnableVertexAttribArray(shader.mNormalHandle);

            GLES20.glVertexAttribPointer(shader.mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, stride, 6 * 4);
            GLES20.glEnableVertexAttribArray(shader.mTextureCoordinateHandle);

            // Draw (GL_LINES for wireframe)
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vbo.get(1));
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, index_count, GLES20.GL_UNSIGNED_SHORT, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        }
    }
}
