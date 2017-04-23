package com.diplom.ilya.diplom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.diplom.ilya.diplom.utils.AsyncResponse;
import com.diplom.ilya.diplom.utils.RunRequests;

public class ChooseTypeForSubjectActivity extends AppCompatActivity implements AsyncResponse {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type_for_subject);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getIntent().getStringExtra("title"));

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Intent i = new Intent();
                i.setClass(getApplicationContext(), ChooseFileActivity.class);
                i.putExtra("title", getTitle());
                i.putExtra("type", o.toString());
                i.putExtra("path", getIntent().getStringExtra("path") + "/" + o.toString());
                startActivity(i);
            }
        });




        RunRequests.delegate = this;
        RunRequests.runGET(ChooseTypeForSubjectActivity.this, getIntent().getStringExtra("path"));


    }

    @Override
    public void asyncProcessFinished(String result) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, result.split(", "));
        listView.setAdapter(adapter);
        System.out.println("response:"+result);
        Toast.makeText(this, "response:"+result, Toast.LENGTH_SHORT).show();
    }
}
