package mina.com.trailertwist.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

import mina.com.trailertwist.R;

import static android.R.attr.resource;

/**
 * Created by Mena on 11/21/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(Context context, String[] arrayOfOptions) {
        super(context, R.layout.action_sort, R.id.sort_option_1, arrayOfOptions);
    }
}
