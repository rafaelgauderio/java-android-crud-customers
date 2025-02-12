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

    public void saveFiedlsValues(View view) {

        String name = editTextName.getText().toString();
        String nameWithoutSpace = name.trim();
        String average = editTextAverage.getText().toString();

        if (name == null || nameWithoutSpace.isEmpty() || nameWithoutSpace.isBlank()) {
            Toast.makeText(this, R.string.campo_nome_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();

            editTextName.requestFocus();
            return;
        }
        if (average == null || average.isEmpty() || average.isBlank()) {
            Toast.makeText(this, R.string.campo_media_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            editTextAverage.requestFocus();
            return;
        }
        int averageNumber = 0;

        try {
            averageNumber = Integer.parseInt(average);
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, R.string.media_dever_ser_um_valor_inteiro_entre_zero_e_100, Toast.LENGTH_LONG).show();
            editTextAverage.requestFocus();
            editTextAverage.setSelection(0, average.length());
            return;
        }
        StringBuilder finalToast = new StringBuilder();
        finalToast.append(getString(R.string.nome_valor));
        finalToast.append(name + "\n");
        finalToast.append(R.string.media_valor);
        finalToast.append(averageNumber);
        // if everithing ok, show values on a Toast
        Toast.makeText(this, finalToast, Toast.LENGTH_LONG).show();
    }
}