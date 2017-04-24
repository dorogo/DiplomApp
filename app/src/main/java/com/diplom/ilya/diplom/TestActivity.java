package com.diplom.ilya.diplom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.diplom.ilya.diplom.utils.AsyncResponse;
import com.diplom.ilya.diplom.utils.DownloadTask;
import com.diplom.ilya.diplom.utils.Utils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.util.*;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse{


    private List<List<String>> list = new ArrayList<>();
    private ListIterator<List<String>> iter = null;
    private TextView t;
    private TextView t2;
    private boolean isClicked = false;

    private int currQuestion = 1;
    private int countCorrectAnswer = 0;

    private Button[] arrB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        t = (TextView) findViewById(R.id.textView2);
        t2 = (TextView) findViewById(R.id.textView3);
        t.setOnClickListener(this);
        Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.button5);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.button8);
        btn3.setOnClickListener(this);
        Button btn4 = (Button) findViewById(R.id.button7);
        btn4.setOnClickListener(this);


        setTitle(getIntent().getStringExtra("title"));
        arrB = new Button[] {btn, btn2, btn3, btn4};

        DownloadTask downloadTask = new DownloadTask(this);
        downloadTask.setDelegate(this);
        downloadTask.execute(getString(R.string.data_URL) + Utils.encodeURL(getIntent().getStringExtra("path")));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        final ViewGroup testLayout = (ViewGroup) findViewById(R.id.testLayout);

        if (v.getId() == R.id.textView2) {
            if (isClicked) {
                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1000);
                anim.setRepeatCount(0);
                anim.setRepeatMode(Animation.ABSOLUTE);
                AlphaAnimation anim2 = new AlphaAnimation(1.0f, 0.0f);
                anim2.setDuration(1000);
                anim2.setRepeatCount(0);
                anim2.setRepeatMode(Animation.ABSOLUTE);
                t.setClickable(false);

                if (!iter.hasNext()) {
                    Toast.makeText(getApplicationContext(), "End!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                    builder.setTitle("Тест пройден!")
                            .setMessage("Ваш результат:" + countCorrectAnswer + "/" + list.size())
                            .setCancelable(false)
                            .setNegativeButton("Принято",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Intent intent = new Intent();
                                            intent.putExtra("correct", countCorrectAnswer);
                                            intent.putExtra("total", list.size());
                                            intent.putExtra("id", getIntent().getIntExtra("id", 0));
                                            setResult(RESULT_OK, intent);
                                            TestActivity.this.finish();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    testLayout.animate().setDuration(500).alpha(0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            resetBtns();
                            testLayout.animate().setDuration(500).alpha(1).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    isClicked = false;
                                    t.setClickable(true);
                                }
                            });
                        }
                    });
                }
            }
        } else if (!isClicked) {
            Button btn1 = (Button) findViewById(v.getId());
            if (btn1.getTag() == "true") {
                btn1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                countCorrectAnswer++;
            } else {
                btn1.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                arrB[0].getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            }
            isClicked = true;
        }
    }

    private void resetBtns() {
        shuffleArray(arrB);
        List<String> tmp = null;
        tmp = iter.next();

        t.setText(tmp.get(0));
        for (int i = 0; i < arrB.length; i++) {
            arrB[i].setTag("");
            arrB[i].getBackground().clearColorFilter();
            arrB[i].setText(String.valueOf(tmp.get(i + 1)));
        }
        arrB[0].setTag("true");
        t2.setText(++currQuestion + "/" + list.size());
    }

    private void shuffleArray(Button[] ar) {
        //TODO: мб подкорректировать перемешивани
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(ar.length - 1);
//            int index = rnd.nextInt(i + 1);
            // Simple swap
            Button a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    @Override
    public void asyncProcessFinished(String file) {
        List<String> tmpList = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            FileInputStream fis = new FileInputStream(file);
            xpp.setInput(new InputStreamReader(fis));

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("question")) {
                            list.add(tmpList);
                            tmpList = new ArrayList<>();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (!xpp.isWhitespace()) {
                            tmpList.add(xpp.getText());
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        t2.setText(currQuestion + "/" + list.size());

        shuffleArray(arrB);
        iter = list.listIterator();
        t.setText(iter.next().get(0));

        arrB[0].setTag("true");
        for (int i = 0; i < arrB.length; i++) {
            arrB[i].setText(String.valueOf(list.get(0).get(i+1)));
        }

    }
}
