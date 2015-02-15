package pt.hidrogine.infinityedge.activity;

import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.Stack;

import pt.hidrogine.infinityedge.R;


public class Game extends FragmentActivity implements  Home.OnFragmentInteractionListener, Style.OnFragmentInteractionListener, Control.OnFragmentInteractionListener{
    private GLSurfaceView mGLSurfaceView;

    public static Game activity;
    public Stack<BaseFragment> fragments = new Stack<BaseFragment>();

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


        replace( new Home());

    }

    public void replace(BaseFragment newFragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.ui, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
        fragments.push(newFragment);
    }

    public void revert(){
        fragments.pop().onEnd();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.ui,fragments.peek());
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        if(fragments.size()>1){
            revert();
        }else {

            finish();
        }
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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}