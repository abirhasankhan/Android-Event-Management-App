package edu.ewubd.cse489_fall22_abir013;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText eTName, etEmail, etPhone, etUserId, etPassword, etReEPass;
    CheckBox checkboxRUI,checkboxRPass;
    TextView tvSignup, tvSignUp, tvAHA, tVName, tVEmail, tVPhone, tVUserId, tVPassword, tVReEPass;
    Button btnBack, btnGo;
    SharedPreferences myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        myPref = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);

        eTName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone= findViewById(R.id.etPhone);
        etUserId = findViewById(R.id.etUserId);
        etPassword = findViewById(R.id.etPassword);
        etReEPass = findViewById(R.id.etReEPass);

        tVName = findViewById(R.id.tvName);
        tVEmail = findViewById(R.id.tvEmail);
        tVPhone= findViewById(R.id.tvPhone);
        tVUserId = findViewById(R.id.tvUserId);
        tVPassword = findViewById(R.id.tvPassword);
        tVReEPass = findViewById(R.id.tvReEPass);

        checkboxRUI = findViewById(R.id.checkboxRUI);
        checkboxRPass = findViewById(R.id.checkboxRPass);

        tvSignup = findViewById(R.id.tvSignup);
        tvSignUp = findViewById(R.id.tvLogin);
        tvAHA = findViewById(R.id.tvAHA);

        btnBack = findViewById(R.id.btnExit);
        btnGo = findViewById(R.id.btnGo);

        tvSignup.setText("Login");

        tVName.setVisibility(View.INVISIBLE);
        eTName.setVisibility(View.INVISIBLE);

        tVEmail.setVisibility(View.INVISIBLE);
        etEmail.setVisibility(View.INVISIBLE);

        tVPhone.setVisibility(View.INVISIBLE);
        etPhone.setVisibility(View.INVISIBLE);

        tVReEPass.setVisibility(View.INVISIBLE);
        etReEPass.setVisibility(View.INVISIBLE);

        checkboxRUI.setVisibility(View.INVISIBLE);
        checkboxRPass.setVisibility(View.INVISIBLE);

        tvAHA.setText("Create an account?");
        tvSignUp.setText("SignUp");


        btnGo.setText("Login");

        final String s_userID = etUserId.getText().toString().trim();
        final String s_pass = etPassword.getText().toString().trim();

        if (s_userID.isEmpty() && s_pass.isEmpty()){
            String strUid = myPref.getString("userId","");
            String strPass = myPref.getString("password","");

            etUserId.setText(strUid);
            etPassword.setText(strPass);
        }

        tvSignUp.setOnClickListener(view -> funGoSignUp());

        btnBack.setOnClickListener(view -> funBack());

        btnGo.setOnClickListener(view -> funGo());

    }

    private void funGoSignUp() {

        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
    }

    void funBack(){
        this.finishAffinity();

    }
    void funGo(){

        final String userID = etUserId.getText().toString().trim();
        final String pass = etPassword.getText().toString().trim();

        String str_Uid = myPref.getString("userId","");
        String str_Pass = myPref.getString("password","");


        if (userID.equals(str_Uid) && pass.equals(str_Pass)){
            Intent intent = new Intent(Login.this, UpcomingEventActivity.class);
            startActivity(intent);
        } else {
            showMessage("User Name or Password didn't match");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }
}

