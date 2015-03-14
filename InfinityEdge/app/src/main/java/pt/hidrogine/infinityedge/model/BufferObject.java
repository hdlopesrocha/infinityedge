package pt.hidrogine.infinityedge.model;

import android.opengl.GLES20;

import java.nio.IntBuffer;

import hidrogine.math.IBufferObject;
import hidrogine.math.Material;
import pt.hidrogine.infinityedge.util.ShaderProgram;

public class BufferObject extends IBufferObject{
    private Material material;
    static final int POSITION_DATA_SIZE = 3;
    static final int NORMAL_DATA_SIZE = 3;
    static final int TEXTURE_COORDINATE_DATA_SIZE = 2;
    static final int BYTES_PER_FLOAT = 4;

    public IntBuffer vbo;

    public BufferObject() {
        super(false);
    }

    public void setMaterial(Material f) {
        material = f;
    }


    public void buildBuffer() {

        super.buildBuffer();


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




    public void draw(ShaderProgram shader) {


        final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;

        //Bind the texture according to the set texture filter
        if (material != null) {

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
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, 0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        }
    }
}
