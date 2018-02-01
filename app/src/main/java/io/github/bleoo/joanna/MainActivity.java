package io.github.bleoo.joanna;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Activity_main_ViewBinder viewBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("--->", "===================");
        new Test();
        Log.e("--->", "===================");
        viewBinder = new Activity_main_ViewBinder(this);
        viewBinder.tv_text.setText("gradle changed me");
    }
}
