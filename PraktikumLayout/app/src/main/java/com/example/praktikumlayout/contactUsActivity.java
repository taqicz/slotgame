package com.example.praktikumlayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class contactUsActivity extends AppCompatActivity {

    private Button buttonWhatsApp, buttonEmail, buttonWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        buttonWhatsApp = findViewById(R.id.bt_wa);
        buttonEmail = findViewById(R.id.bt_email);
        buttonWeb = findViewById(R.id.bt_website);

        // Implicit Intent untuk WhatsApp
        buttonWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "082139949136";
                String message = "Uy, bingung nih.";
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message)));
                startActivity(sendIntent);
            }
        });

        // Implicit Intent untuk Email
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:praktikum@example.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Uy, Bingung nih.");
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                }
            }
        });

        // Implicit Intent untuk membuka website
        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse("https://www.google.com"));
                startActivity(webIntent);
            }
        });
    }
}