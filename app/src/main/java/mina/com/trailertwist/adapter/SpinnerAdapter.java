package mina.com.trailertwist.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import mina.com.trailertwist.R;

/**
 * Created by Mena on 11/21/2017.
 */


// stackoverflow solution for spinner crash

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(Context context, String[] arrayOfOptions) {
        super(context, R.layout.action_sort, R.id.sort_option_1, arrayOfOptions);
    }
}
