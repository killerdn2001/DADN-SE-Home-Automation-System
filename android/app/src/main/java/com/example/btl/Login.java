package com.example.btl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
public class Login extends AppCompatActivity {
    public static String email="",name="",phone="",time_log="",time_create="",auto="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EditText email_input=findViewById(R.id.editTextTextEmailAddress);
        EditText pass_input=findViewById(R.id.editTextNumberPassword);
        TextView sign_up=findViewById(R.id.textView2);

//        Direct to sign up view when click on "Sign Up"
        String text="Sign Up";
        SpannableString ss=new SpannableString(text);
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#00cc66"));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View view) {
                Intent login=new Intent(Login.this, SignUp.class);
                startActivity(login);
            }
        };
        ss.setSpan(clickableSpan,0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sign_up.setText(ss);
        sign_up.setMovementMethod(LinkMovementMethod.getInstance());

        AppCompatButton login=findViewById(R.id.button);
        login.setOnClickListener(view -> {
            String email1=email_input.getText().toString();
            String pass=pass_input.getText().toString();
            if(email1.isEmpty()||pass.isEmpty()){
                Toast.makeText(Login.this,"All fields are required",Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseDatabase.getInstance().getReference("User").orderByChild("email").equalTo(email1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount()==0){
                        System.out.println(snapshot.child("password").getValue());
                        Toast.makeText(Login.this,"Email isn't existed",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if (!(snap.child("password").getValue()).equals(pass))
                                Toast.makeText(Login.this, "Wrong password. Try again", Toast.LENGTH_SHORT).show();
                            else {
                                name=(String)snap.child("name").getValue();
                                phone=(String)snap.child("phone").getValue();
                                time_create=(String)snap.child("time_create").getValue();
                                auto=(String)snap.child("auto").getValue();
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
                                time_log=format.format(Calendar.getInstance().getTime());
                                snap.getRef().child("last_login").setValue(time_log);
                                Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                email = email1;
                                Intent intent = new Intent(Login.this, Main.class);
                                startActivity(intent);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

    }

}