package com.example.codeforces_chaser;

import android.view.View;
import android.widget.TextView;

public class fs_list_view_holder {

    TextView frinds_name;
    TextView solves;

    fs_list_view_holder(View v) {

        frinds_name = v.findViewById(R.id.friends_name);
        solves = v.findViewById(R.id.solves);
    }
}