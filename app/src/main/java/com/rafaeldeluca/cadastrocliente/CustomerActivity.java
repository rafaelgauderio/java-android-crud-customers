package com.rafaeldeluca.cadastrocliente;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerActivity extends AppCompatActivity {

    private EditText editTextName, editTextAverage;
    private CheckBox checkBoxRestriction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        editTextName = findViewById((R.id.editTextNome));
        editTextAverage = findViewById(R.id.editTextMedia);
        checkBoxRestriction = findViewById(R.id.checkBoxRestriction);
    }

    public void cleanfields(View view) {
        editTextName.setText(null);
        editTextAverage.setText(null);
        editTextName.requestFocus();
        checkBoxRestriction.setChecked(false);
        //message
        Toast.makeText(this, R.string.os_valores_de_nome_e_m_dia_foram_deletados, Toast.LENGTH_LONG).show();
    }

    public void saveFiedlsValues(View view) {

        String name = editTextName.getText().toString();
        String nameWithoutSpace = name.trim();


        if (name == null || nameWithoutSpace.isEmpty() || nameWithoutSpace.isBlank()) {
            Toast.makeText(this, R.string.campo_nome_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();

            editTextName.requestFocus();
            return;
        }

        String averageString = editTextAverage.getText().toString();
        if (averageString == null || averageString.trim().isEmpty() || averageString.trim().isBlank()) {
            Toast.makeText(this, R.string.campo_media_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            editTextAverage.requestFocus();
            return;
        }
        int averageNumber=0;

        try {
            averageNumber = Integer.parseInt(averageString);
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, R.string.media_deve_ser_um_valor_inteiro, Toast.LENGTH_LONG).show();
            editTextAverage.requestFocus();
            editTextAverage.setSelection(0, averageString.length());
            return;
        }
        if (averageNumber <0 || averageNumber >100) {
            Toast.makeText(this, R.string.media_deve_ser_um_valor_maior_que_zero_e_menor_que_100, Toast.LENGTH_LONG).show();
            editTextAverage.requestFocus();
            editTextAverage.setSelection(0, averageString.length());
            return;
        }

        boolean haveRestriction = checkBoxRestriction.isChecked();

        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(getString(R.string.nome_valor));
        finalMessage.append(name);
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append(getString(R.string.media_valor));
        finalMessage.append(averageNumber);
        finalMessage.append( haveRestriction==true ? getString(R.string.possui_restricao) +getString(R.string.venda_somente_vista)
                : getString(R.string.nao_possui_restricao_financeira));
        // if everithing ok, show values on a Toast
        /*Toast.makeText(this,
                getString(R.string.nome_valor) + name + "\n" +
                getString(R.string.media_valor) + averageNumber,
                Toast.LENGTH_LONG).show();
         */
        Toast.makeText(this,
                finalMessage.toString(),
                Toast.LENGTH_LONG).show();
    }
}