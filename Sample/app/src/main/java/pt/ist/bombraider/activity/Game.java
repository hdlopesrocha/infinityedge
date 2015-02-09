package pt.ist.bombraider.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import pt.ist.bombraider.R;


public class Game extends Activity {
    private GLSurfaceView mGLSurfaceView;

    public static Game activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.game);

        /* ============
           OPENGL STUFF
           ============ */

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(2);
            Renderer mRenderer = new Renderer();
            mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
            mGLSurfaceView.setPreserveEGLContextOnPause(true);

            mGLSurfaceView.setRenderer(mRenderer);

        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }

    }




    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onResume() {
        // The activity must call the GL surface view's onResume() on activity
        // onResume().
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // The activity must call the GL surface view's onPause() on activity
        // onPause().
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}