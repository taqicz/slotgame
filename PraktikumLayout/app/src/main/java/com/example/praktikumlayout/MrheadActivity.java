package com.example.praktikumlayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.example.praktikumlayout.R;

public class MrheadActivity extends AppCompatActivity {
    TextView tvEmail, tvPassword;
    Button btContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mrhead); // Pastikan layout ini adalah layout Mr. Head

        // Inisialisasi komponen
        tvEmail = findViewById(R.id.tv_email);

        // Ambil data dari Intent
        Intent intent = getIntent();
        tvEmail.setText("Welcome,  " + intent.getStringExtra("EMAIL") + ";)");


        // Inisialisasi komponen Mr. Head
        ImageView imageViewHair = findViewById(R.id.imageView2);
        ImageView imageViewEyebrow = findViewById(R.id.imageView3);
        ImageView imageViewEyes = findViewById(R.id.imageView4);
        ImageView imageViewBeard = findViewById(R.id.imageView5);
        ImageView imageViewMoustache = findViewById(R.id.imageView6);
        Button btContact = findViewById(R.id.bt_contact);

        CheckBox checkBoxHair = findViewById(R.id.checkBox);
        CheckBox checkBoxEyebrow = findViewById(R.id.checkBox2);
        CheckBox checkBoxEyes = findViewById(R.id.checkBox3);
        CheckBox checkBoxMoustache = findViewById(R.id.checkBox4);
        CheckBox checkBoxBeard = findViewById(R.id.checkBox5);

        // Event handler untuk CheckBox
        checkBoxHair.setOnCheckedChangeListener((buttonView, isChecked) -> {
            imageViewHair.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        checkBoxEyebrow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            imageViewEyebrow.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        checkBoxEyes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            imageViewEyes.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        checkBoxMoustache.setOnCheckedChangeListener((buttonView, isChecked) -> {
            imageViewMoustache.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        checkBoxBeard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            imageViewBeard.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        btContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MrheadActivity.this, contactUsActivity.class);
                startActivity(intent);
                }
        });
    }

}
