package pt.uac.qa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.fragment.app.Fragment;
import pt.uac.qa.MainActivity;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 16-01-2020.
 */
public abstract class BaseFragment extends Fragment {
    private final BroadcastReceiver mainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    protected abstract void refresh();


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mainReceiver, new IntentFilter(MainActivity.INTENT_FILTER));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mainReceiver);
        super.onPause();
    }
}
