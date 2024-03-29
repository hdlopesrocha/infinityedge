
package pt.hidrogine.infinityedge.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.util.TreeMap;


public class TextureLoader {

    private  Context context;

    private TreeMap<String,Integer> loadedTextures  = new TreeMap<String,Integer>();

    public TextureLoader(Context ctx){
        this.context = ctx;
    }


    public int load(final String path) {
        Integer tex = loadedTextures.get(path);
        if(tex!=null){
            return tex;
        }
        else {

            int id = context.getResources().getIdentifier("drawable/" + path, "drawable", "pt.hidrogine.infinityedge");


            final int[] textureHandle = new int[1];
            GLES20.glGenTextures(1, textureHandle, 0);

            if (textureHandle[0] != 0) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;    // No pre-scaling
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                // Read in the resource
                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);

                // Bind to the texture in OpenGL
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

                // Recycle the bitmap, since its data has been loaded into OpenGL.
                bitmap.recycle();
            } else {
                throw new RuntimeException("Error loading texture.");
            }
            loadedTextures.put(path,textureHandle[0]);
            return textureHandle[0];
        }
    }
}
