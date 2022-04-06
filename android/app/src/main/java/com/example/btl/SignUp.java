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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SignUp extends AppCompatActivity {
    public static String name="LOL";
    private long id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EditText name_edit=findViewById(R.id.signup_name);
        EditText email_edit=findViewById(R.id.signup_email);
        EditText phone_edit=findViewById(R.id.signup_phone);
        EditText pass_edit=findViewById(R.id.signup_pass);
        EditText pass_edit2=findViewById(R.id.signup_repass);
        Button sign_up=findViewById(R.id.button1);
        TextView login=findViewById(R.id.textView2);
        String text="Log In";
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
                Intent login=new Intent(SignUp.this, Login.class);
                startActivity(login);
            }
        };
        ss.setSpan(clickableSpan,0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=name_edit.getText().toString();
                String email=email_edit.getText().toString();
                String phone=phone_edit.getText().toString();
                String pass1=pass_edit.getText().toString();
                String pass2=pass_edit2.getText().toString();
                if (name.isEmpty()||email.isEmpty()||phone.isEmpty()||pass1.isEmpty()||pass2.isEmpty()){
                    Toast.makeText(SignUp.this,"All field is required",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass1.equals(pass2)){
                    Toast.makeText(SignUp.this,"The confirm password doesn't match",Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase.getInstance().getReference("User").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.getChildrenCount()!=0){
                            Toast.makeText(SignUp.this,"Account existed. Try another email",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
                            format.setTimeZone(TimeZone.getTimeZone("GMT+8:12"));
                            Login.time_log=format.format(Calendar.getInstance().getTime());
                            Login.time_create=Login.time_log;
                            Login.email=email;
                            Login.name=name;
                            Login.phone=phone;
                            FirebaseDatabase.getInstance().getReference("User").push().setValue(new User(name,email,phone,pass1,Login.time_log,Login.time_create));
                            Toast.makeText(SignUp.this,"Account created successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignUp.this, Main.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }


}