package com.rafaeldeluca.cadastrocliente;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {

    private EditText editTextName, editTextAverage;
    private CheckBox checkBoxRestriction;
    private RadioGroup radioGroupClientType;
    private Spinner spinnerDivision;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        editTextName = findViewById((R.id.editTextNome));
        editTextAverage = findViewById(R.id.editTextMedia);
        checkBoxRestriction = findViewById(R.id.checkBoxRestriction);
        radioGroupClientType = findViewById(R.id.radioGroupClientType);
        spinnerDivision = findViewById(R.id.spinnerDivision);

        insertDataSpinnerDivision ();
    }

    private void insertDataSpinnerDivision() {
        ArrayList<String> arrayDivisions = new ArrayList<String>();

        arrayDivisions.add(getString(R.string.lazer_restaurantes_bares));
        arrayDivisions.add(getString(R.string.saude_clinicas_hospitais));
        arrayDivisions.add(getString(R.string.comercio_lojas_supermercados));
        arrayDivisions.add(getString(R.string.industria));
        arrayDivisions.add(getString(R.string.condominio));
        arrayDivisions.add(getString(R.string.escritorio));
        arrayDivisions.add(getString(R.string.outros));

        // renderizing each line of the spinner
        ArrayAdapter<String> adapterDivisons = new ArrayAdapter<>(
                this, android.R.layout.preference_category, arrayDivisions
        );
        spinnerDivision.setAdapter(adapterDivisons);

    }

    public void cleanfields(View view) {
        editTextName.setText(null);
        editTextAverage.setText(null);
        checkBoxRestriction.setChecked(false);
        radioGroupClientType.clearCheck();

        editTextName.requestFocus();
        //message
        Toast.makeText(this, R.string.os_valores_dos_campos_foram_limpos, Toast.LENGTH_LONG).show();
    }

    public void saveFiedlsValues(View view) {

        String name = editTextName.getText().toString();
        String nameWithoutSpace = name.trim();
        String averageString = editTextAverage.getText().toString();
        int radioButtonId = radioGroupClientType.getCheckedRadioButtonId();

        if (name == null || nameWithoutSpace.isEmpty() || nameWithoutSpace.isBlank()) {
            Toast.makeText(this, R.string.campo_nome_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();

            editTextName.requestFocus();
            return;
        }


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

        String clientType="";

        if(radioButtonId == -1) {
            Toast.makeText(this, R.string.necessario_escolher_o_tipo_de_cliente, Toast.LENGTH_LONG).show();
            return;

        } else {
            if (radioButtonId==R.id.radioNewClient)
                clientType = getString(R.string.cliente_novo);
            else if(radioButtonId==R.id.radioButtonClientReativated)
                clientType = getString(R.string.cliente_reativado);
                        else if (radioButtonId==R.id.radioButtonRecurringClient)
                            clientType = getString(R.string.cliente_recorrente);
        }

        boolean haveRestriction = checkBoxRestriction.isChecked();

        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(getString(R.string.nome_valor));
        finalMessage.append(name);
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append(getString(R.string.media_valor));
        finalMessage.append(averageNumber);
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append( haveRestriction==true
                ? getString(R.string.possui_restricao) +getString(R.string.venda_somente_vista)
                : getString(R.string.nao_possui_restricao_financeira));
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append(clientType);

        // if everithing ok, show fields values on a Toast
        Toast.makeText(this,
                finalMessage.toString(),
                Toast.LENGTH_LONG).show();
    }
}