package com.example.talk8.conn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Activity xxl";
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.bt_jump_to_wifi);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Jump to Wifi Activity");
                Intent intent = new Intent(getApplicationContext(),WifiActivity.class);
                startActivity(intent);
            }
        });
    }
}
