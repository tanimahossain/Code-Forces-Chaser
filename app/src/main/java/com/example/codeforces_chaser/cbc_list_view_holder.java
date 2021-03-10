package com.example.codeforces_chaser;

import android.view.View;
import android.widget.TextView;

public class cbc_list_view_holder {

    TextView cntst_id;
    TextView cntst_name;

    cbc_list_view_holder(View v) {

        cntst_id = v.findViewById(R.id.cbc_contest_id);
        cntst_name = v.findViewById(R.id.cbc_contest_name);
    }
}