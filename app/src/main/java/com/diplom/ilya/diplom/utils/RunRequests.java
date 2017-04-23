package com.diplom.ilya.diplom.utils;

import android.app.ProgressDialog;
import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 25.03.17.
 */
public class RunRequests {

    public static AsyncResponse delegate = null;

    public static void runGET(final Context context) {
        runGET(context,"");
    }
    public static void runGET(final Context context, final String pathFrom) {

        final ProgressDialog pd;
        pd = new ProgressDialog(context);
        pd.setTitle("Loading...");
        pd.setMessage("Getting data...");
        pd.setIndeterminate(true);
        pd.show();


        RequestQueue queue = Volley.newRequestQueue(context);
        String url = null;
        try {
            url = "https://testingd.azurewebsites.net/files?path=data/" + URLEncoder.encode(pathFrom, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        delegate.asyncProcessFinished(response);
                        pd.hide();
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
                pd.setMessage("Error.");
                pd.setIndeterminate(false);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        }
        ;
        queue.add(stringRequest);
    }

}
