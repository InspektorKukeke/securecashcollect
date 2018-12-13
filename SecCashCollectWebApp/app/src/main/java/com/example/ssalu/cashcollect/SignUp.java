package com.example.ssalu.cashcollect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    //declare buttons and edit text
    Button btnSignUp;
    EditText edEmail, edPw, edName;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //assign values to objects
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        edEmail = (MaterialEditText) findViewById(R.id.edEmail);
        edName = (MaterialEditText) findViewById(R.id.edName);
        edPw = (MaterialEditText) findViewById(R.id.edPw);
        //Db instance
        db = new DatabaseHelper(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign up validations
                if(!isValidEmail(edEmail.getText().toString())){
                   toastMmessage("Invalid email!");
                }else if(!edName.getText().toString().matches("^[A-Za-z]+$") ||
                        edName.getText().toString().length() < 6){
                    toastMmessage("Invalid name!");
                }else if(edPw.getText().toString().length() < 4){
                    toastMmessage("Password too short!");
                }else if(db.isPresent("user", "email", edEmail.getText().toString())){
                    toastMmessage("Email already exists!");
                }else{
                    //if all good, add to db and off to Settings activity
                    db.addData(edEmail.getText().toString(),edName.getText().toString(), edPw.getText().toString() );
                    toastMmessage("Sign up successful. Welcome to Cash collect website!");
                    Intent i = new Intent(getApplicationContext(), Settings.class);
                    startActivity(i);
                }
            }
        });


    }

    //Stack overflow
    //https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void toastMmessage(String msg){
        Toast.makeText(SignUp.this, msg, Toast.LENGTH_LONG).show();
    }
}
