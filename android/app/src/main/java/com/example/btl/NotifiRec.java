package com.example.btl;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotifiRec extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lol);
        RadioGroup group=findViewById(R.id.l);
        RadioButton btn1=findViewById(R.id.radioButton);
        RadioButton btn2=findViewById(R.id.radioButton2);
        Button btn=findViewById(R.id.bt);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.radioButton)
                    Toast.makeText(NotifiRec.this,"1",Toast.LENGTH_SHORT).show();
                else if (i==R.id.radioButton2)
                    Toast.makeText(NotifiRec.this,"2",Toast.LENGTH_SHORT).show();
            }

        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setChecked(true);
            }
        });
    }
}
