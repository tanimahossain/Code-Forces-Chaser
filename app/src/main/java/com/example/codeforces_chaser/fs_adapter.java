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

public class fs_adapter  extends ArrayAdapter<String> {

    Context context;
    List<String> friends_list;
    List<String> solves;

    public fs_adapter(@NonNull Context context, List<String> friends_list, List<String> solves) {
        super(context, R.layout.fs_item, R.id.solves_list, friends_list);

        this.context = context;
        this.friends_list = friends_list;
        this.solves = solves;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View single_item = convertView;
        fs_list_view_holder holder = null;

        if (single_item == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            single_item = layoutInflater.inflate(R.layout.fs_item, parent, false);
            holder = new fs_list_view_holder(single_item);
            single_item.setTag(holder);
        }
        else {
            holder = (fs_list_view_holder) single_item.getTag();
        }

        holder.frinds_name.setText(friends_list.get(position));
        holder.solves.setText(solves.get(position));

        single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("chaser_log", "You clicked: " + friends_list.get(position));
            }
        });

        return single_item;
    }
}