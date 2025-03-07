package com.rafaeldeluca.cadastrocliente.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rafaeldeluca.cadastrocliente.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.setTitle(R.string.about);
    }

    public void openSiteCourse (View view) {
        openUrl("https://pos-graduacao-ead.cp.utfpr.edu.br/java/");
    }

    public void openSiteAuthor (View view) {
    openUrl("https://github.com/rafaelgauderio");
    }

    private void openUrl(String stringUrl) {
        Intent intentOpen = new Intent(Intent.ACTION_VIEW);
        intentOpen.setData(Uri.parse(stringUrl));

        if(intentOpen.resolveActivity(getPackageManager())!=null) {
            startActivity(intentOpen);
        } else {
            Toast.makeText(this, getString(R.string.nenhum_aplicativo_instalado_para_abrir_paginas_web),
                    Toast.LENGTH_LONG).show();
        }
    }


}