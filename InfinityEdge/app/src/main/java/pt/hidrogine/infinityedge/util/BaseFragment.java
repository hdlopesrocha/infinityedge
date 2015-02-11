package pt.hidrogine.infinityedge.util;

import android.app.Fragment;
import android.app.FragmentTransaction;

import pt.hidrogine.infinityedge.R;

/**
 * Created by Henrique on 11/02/2015.
 */
public class BaseFragment extends Fragment {

    public void replace(Fragment newFragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.ui, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }
}
