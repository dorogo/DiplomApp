package com.diplom.ilya.diplom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.diplom.ilya.diplom.CustomAdapter.CustomAdapter;
import com.diplom.ilya.diplom.CustomAdapter.Items;
import com.diplom.ilya.diplom.utils.AsyncResponse;
import com.diplom.ilya.diplom.utils.RunRequests;

import java.util.ArrayList;

public class ChooseFileActivity extends AppCompatActivity implements AsyncResponse {

    private ListView listView;
    private String[] arrFilesForPath;
    private ArrayList<Items> arrItems;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        toolbar.setSubtitle(getIntent().getStringExtra("type"));
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);

                if (!o.toString().equals(getString(R.string.noFileInDir))) {
                    Intent i = new Intent();
                    if (getIntent().getStringExtra("type").equals(getString(R.string.type1)) || getIntent().getStringExtra("type").equals(getString(R.string.type2))) {
                        i.setClass(getApplicationContext(), PDFActivity.class);
                    } else if (getIntent().getStringExtra("type").equals(getString(R.string.type3))) {
                        i.setClass(getApplicationContext(), TestActivity.class);
                    }

                    i.putExtra("title", o.toString());
                    i.putExtra("id", position);
                    i.putExtra("path", getIntent().getStringExtra("path") + "/" + arrFilesForPath[position]);
                    startActivityForResult(i, 1);
                }
            }
        });


        RunRequests.delegate = this;
        RunRequests.runGET(ChooseFileActivity.this, getIntent().getStringExtra("path"));
    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        if (resultCode == RESULT_OK) {
            arrItems.get(data.getIntExtra("id",0)).setResult(data.getIntExtra("correct", 0) + "/" + data.getIntExtra("total", 0));
            customAdapter.notifyDataSetChanged();
            saveArray(arrItems, getIntent().getStringExtra("path"),getApplicationContext());
        }
    }



    @Override
    public void asyncProcessFinished(String result) {
        arrFilesForPath = result.split(", ");

        arrItems = new ArrayList<>();

        String[] a = loadArray(getIntent().getStringExtra("path"),getApplicationContext());

        String res;
        for (int i = 0; i < arrFilesForPath.length; i++) {
            res = "";
            if (i < a.length ) res = a[i];
            arrItems.add(new Items(arrFilesForPath[i].replaceAll("\\.[A-Za-z]+",""), res));
        }
        customAdapter = new CustomAdapter(this, R.layout.row_layout, arrItems);
        if (getIntent().getStringExtra("type").equals(getString(R.string.type3))) {
            listView.setAdapter(customAdapter);
        } else {
            listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result.replaceAll("\\.[A-Za-z]+", "").split(", ")));
        }

    }

    private void saveArray(ArrayList<Items> array, String arrayName, Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i).getResult());
        editor.apply();
    }

    private String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

}
