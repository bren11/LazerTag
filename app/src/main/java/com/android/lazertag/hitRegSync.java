/*package com.android.lazertag;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

class HitRegSync {

    private Context context;
    RequestQueue queue;
    private String response;

    public HitRegSync(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(this.context);
    }

    public void networkPost(final String data, String urlTarget){
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlTarget,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        HitRegSync.this.response = response;
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("data", data);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void networkGet(String urlTarget) {
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlTarget,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        HitRegSync.this.response = response;
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);
    }


}*/