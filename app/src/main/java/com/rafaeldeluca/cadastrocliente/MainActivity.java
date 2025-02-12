package com.rafaeldeluca.cadastrocliente;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextAverage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById((R.id.editTextNome));
        editTextAverage = findViewById(R.id.editTextMedia);
    }

    public void cleanfields(View view) {
        editTextName.setText(null);
        editTextAverage.setText(null);
        editTextName.requestFocus();
        //message
        Toast.makeText(this, R.string.os_valores_de_nome_e_m_dia_foram_deletados, Toast.LENGTH_LONG).show();
    }

}