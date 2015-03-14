package pt.hidrogine.infinityedge.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import hidrogine.math.MathHelper;
import pt.hidrogine.infinityedge.R;
import pt.hidrogine.infinityedge.scene.Background;
import pt.hidrogine.infinityedge.util.VerticalSeekBar;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link pt.hidrogine.infinityedge.activity.Control.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link pt.hidrogine.infinityedge.activity.Control#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Control extends BaseFragment {

    private ImageView stick;
    private Button analog;
    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static Control newInstance(String param1, String param2) {
        Control fragment = new Control();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Control() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    void setStick(){
        float ax = Renderer.analogX;
        float ay = Renderer.analogY;
        float le = (float) Math.sqrt(ax*ax+ay*ay);
        if(le>1){
            ax/=le;
            ay/=le;
        }

        stick.setX(analog.getX()+(ax+1)*analog.getWidth()*.5f-stick.getWidth()*.5f);
        stick.setY(analog.getY()+(ay+1)*analog.getHeight()*.5f-stick.getHeight()*.5f);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_controls, container, false);
        {
            VerticalSeekBar accel = (VerticalSeekBar) rootView.findViewById(R.id.accel);
            accel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Renderer.accel = progress / 100f;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

        stick = (ImageView) rootView.findViewById(R.id.stick);
        analog = (Button) rootView.findViewById(R.id.analog);
        setStick();


        analog.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public synchronized boolean onTouch(View v, MotionEvent event) {
                float x = MathHelper.clamp((event.getX() / v.getWidth()) * 2f - 1f, -1, 1);
                float y = MathHelper.clamp((event.getY() / v.getHeight()) * 2f - 1f,-1,1);

                switch (event.getAction()) {

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_DOWN:
                        Renderer.analogX = x;
                        Renderer.analogY = y;

                        break;
                    case MotionEvent.ACTION_UP:
                        Renderer.analogX = 0;
                        Renderer.analogY = 0;

                        break;
                    default:
                        break;
                }
                setStick();

                return false;
            }
        });


        ImageButton fire = (ImageButton) rootView.findViewById(R.id.fire);
        fire.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_DOWN:
                        Renderer.fire = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        Renderer.fire=false;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onEnd() {
        Renderer.currentScene = new Background();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
