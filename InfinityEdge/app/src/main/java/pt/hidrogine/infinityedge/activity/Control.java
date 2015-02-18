package pt.hidrogine.infinityedge.activity;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView stick;
    private Button analog;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Control newInstance(String param1, String param2) {
        Control fragment = new Control();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Control() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public float clamp(float x){
        if(x<-1)
            return -1;
        if(x>1)
            return 1;
        return x;

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
                float x = clamp((event.getX() / v.getWidth()) * 2f - 1f);
                float y = clamp((event.getY() / v.getHeight()) * 2f - 1f);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
