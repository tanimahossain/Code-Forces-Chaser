package com.example.codeforces_chaser;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class friend_list_view_holder {

    TextView hndl_v;
    TextView rat_v;
    LinearLayout llr;

    friend_list_view_holder(View v) {

        hndl_v = v.findViewById(R.id.frnds_handle);
        rat_v = v.findViewById(R.id.frnds_rating);
        llr = v.findViewById(R.id.friends_list_item);
    }
}
