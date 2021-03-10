package com.example.codeforces_chaser;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class request_queue {
    private static request_queue my_instance;
    private RequestQueue requestQueue;
    private static Context my_cntx;

    private request_queue(Context context) {
        my_cntx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(my_cntx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized request_queue getInstance(Context context) {
        if(my_instance == null) {
            my_instance = new request_queue(context);
        }
        return my_instance;
    }

    public <T> void add_to_request_queue(Request<T> request) {
        requestQueue.add(request);
    }
}
