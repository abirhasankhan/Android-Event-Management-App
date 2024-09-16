package edu.ewubd.cse489_fall22_abir013;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class home extends AppCompatActivity {
    ImageView imageView01, imageView02;
    TextView textView01, fname_textView02, lname_textView03, textView04;
    Button home_btn01, home_btn02, home_btn03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}