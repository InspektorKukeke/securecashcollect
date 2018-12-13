package com.example.ssalu.cashcollect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ssalu.artgallery.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {

    //declare buttons and edit text
    Button btnSignIn;
    EditText edEmail, edPw;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //assign values to objects
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        edEmail = (MaterialEditText) findViewById(R.id.edEmail);
        edPw = (MaterialEditText) findViewById(R.id.edPw);
        db = new DatabaseHelper(this);

        //sign in logic - will give feedback via Toast and progressdialog
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dlg = new ProgressDialog(SignIn.this);
                dlg.setMessage("Validating");
                dlg.show();
                if(db.validate(edEmail.getText().toString(),edPw.getText().toString())){
                    Toast.makeText(SignIn.this, "Success!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),Settings.class);
                    startActivity(i);
                }else{
                    Toast.makeText(SignIn.this, "Incorrect details.", Toast.LENGTH_SHORT).show();
                }
                dlg.dismiss();
            }
        });
    }
}