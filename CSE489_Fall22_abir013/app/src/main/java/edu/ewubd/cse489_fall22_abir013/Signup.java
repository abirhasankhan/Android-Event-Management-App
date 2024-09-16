package edu.ewubd.cse489_fall22_abir013;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {
    EditText eTName, etEmail, etPhone, etUserId, etPassword, etReEPass;
    CheckBox checkboxRUI,checkboxPass;
    TextView tvSignup, tvLogin, tvAHA;
    Button btnExit, btnGo;

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


        checkboxRUI = findViewById(R.id.checkboxRUI);
        checkboxPass = findViewById(R.id.checkboxRPass);

        tvSignup = findViewById(R.id.tvSignup);
        tvLogin = findViewById(R.id.tvLogin);
        tvAHA = findViewById(R.id.tvAHA);

        btnExit = findViewById(R.id.btnExit);
        btnGo = findViewById(R.id.btnGo);

        tvLogin.setOnClickListener(view -> funLogin());

        btnExit.setOnClickListener(view -> funExit());
        btnGo.setOnClickListener(view -> funGo());

        String userId = etUserId.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();


        checkboxRUI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){
                    myPref.edit().putString("userId",userId).apply();
                    Toast.makeText(Signup.this,"Checked",Toast.LENGTH_SHORT).show();

                } else if (!buttonView.isChecked()){
                    myPref.edit().putString("userId",userId).apply();
                    Toast.makeText(Signup.this,"Unchecked",Toast.LENGTH_SHORT).show();

                }
            }
        });
        checkboxPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){

                    myPref.edit().putString("password",pass).apply();
                    Toast.makeText(Signup.this,"Checked",Toast.LENGTH_SHORT).show();

                } else if (!buttonView.isChecked()){
                    myPref.edit().putString("password",pass).apply();
                    Toast.makeText(Signup.this,"Unchecked",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    void funLogin(){

        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);

    }
    void funExit(){
        this.finishAffinity();
    }
    void funGo(){

        final String name = eTName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String userId = etUserId.getText().toString().trim();
        final String pass = etPassword.getText().toString().trim();
        final String rePass = etReEPass.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()  || phone.isEmpty()  || userId.isEmpty()  || pass.isEmpty()  || rePass.isEmpty()){

            //for invalid field
            //error message
            showMessage("Please Verify all fields");

        } else {
            if (pass.equals(rePass)){
                //for ok field
                myPref.edit().putString("userId",userId).apply();
                myPref.edit().putString("password",pass).apply();
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);

            } else {
                showMessage("Password didn't match");
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}