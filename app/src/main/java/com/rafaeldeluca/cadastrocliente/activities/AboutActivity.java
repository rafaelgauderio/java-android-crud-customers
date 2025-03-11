package com.rafaeldeluca.cadastrocliente.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

    public void sendEmailToAuthor (View view) {
        sendEmail(new String[]{"rafaelluca@alunos.utfpr.edu.br"}, getString(R.string.email_enviado_pelo_aplicativo));
    }

    private void sendEmail(String [] addresses, String subject) {
        Intent intentOpen = new Intent(Intent.ACTION_SENDTO);
        intentOpen.setData(Uri.parse("mailto:"));
        intentOpen.putExtra(Intent.EXTRA_EMAIL, addresses);
        intentOpen.putExtra(Intent.EXTRA_SUBJECT, subject);

        if(intentOpen.resolveActivity(getPackageManager())==null) {
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_instalado_para_enviar_email,
                    Toast.LENGTH_LONG).show();
        } else {
            startActivity(intentOpen);
        }

    }

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        if(menuItemId == android.R.id.home) {
            this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }
     */
}