package com.example.codeforces_chaser;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class cbc_list_adapter extends ArrayAdapter<String> {

    Context context;
    List<String> names;
    List<String> ids;

    public cbc_list_adapter(@NonNull Context context, List<String> ids, List<String> names) {
        super(context, R.layout.cbc_list_item, R.id.cbc_contest_list, names);

        this.context = context;
        this.names = names;
        this.ids = ids;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View single_item = convertView;
        cbc_list_view_holder holder = null;

        if (single_item == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            single_item = layoutInflater.inflate(R.layout.cbc_list_item, parent, false);
            holder = new cbc_list_view_holder(single_item);
            single_item.setTag(holder);
        } else {
            holder = (cbc_list_view_holder) single_item.getTag();
        }

        holder.cntst_id.setText(ids.get(position));
        holder.cntst_name.setText(names.get(position));

        single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("chaser_log", "You clicked: " + names.get(position));

                Intent it = new Intent(context, friends_solves.class);
                it.putExtra("id",""+ids.get(position));
                it.putExtra("name", names.get(position));
                context.startActivity(it);
            }
        });

        return single_item;
    }
}