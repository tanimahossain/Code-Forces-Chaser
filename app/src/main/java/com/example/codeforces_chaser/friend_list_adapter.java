package com.example.codeforces_chaser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class friend_list_adapter extends ArrayAdapter<String> {

    Context context;
    String[] frns;
    int[] curat;
    String[] rank_color;

    public friend_list_adapter(@NonNull Context context, String[] frns, int[] curat, String[] rank_color) {
        super(context, R.layout.list_item, R.id.list_of_friends, frns);

        this.context = context;
        this.frns = frns;
        this.curat = curat;
        this.rank_color = rank_color;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View single_item = convertView;
        friend_list_view_holder holder = null;

        if(single_item == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            single_item = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new friend_list_view_holder(single_item);
            single_item.setTag(holder);
        }
        else {
            holder = (friend_list_view_holder) single_item.getTag();
        }

        holder.hndl_v.setText(frns[position]);
        holder.rat_v.setText((curat[position]+""));
        holder.llr.setBackgroundColor(Color.parseColor(rank_color[position]));

        single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "You clicked: "+frns[position], Toast.LENGTH_SHORT).show();
                Log.d("chaser_log","You clicked: "+frns[position]);


                /*
                String search = names[position].replace(' ', '+');
                Intent open_link = new Intent(Intent.ACTION_VIEW, Uri.parse(links[position]+search));
                context.startActivity(open_link);*/
            }
        });

        return  single_item;
    }
}
