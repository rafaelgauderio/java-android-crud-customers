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

    private EditText editTextName;
    private EditText editTextRazao;
    private CheckBox checkBoxRestriction;
    private RadioGroup radioGroupClientType;
    private Spinner spinnerDivision;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        editTextName = findViewById((R.id.editTextNome));
        editTextRazao = findViewById(R.id.editTextRazao);
        checkBoxRestriction = findViewById(R.id.checkBoxRestriction);
        radioGroupClientType = findViewById(R.id.radioGroupClientType);
        spinnerDivision = findViewById(R.id.spinnerDivision);

        //insertDataSpinnerDivision ();
    }
    /*
    private void insertDataSpinnerDivision() {
        ArrayList<String> arrayDivisions = new ArrayList<String>();

        arrayDivisions.add(getString(R.string.lazer));
        arrayDivisions.add(getString(R.string.saude));
        arrayDivisions.add(getString(R.string.comercio));
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

     */

    public void cleanfields(View view) {
        editTextName.setText(null);
        editTextRazao.setText(null);
        checkBoxRestriction.setChecked(false);
        radioGroupClientType.clearCheck();
        spinnerDivision.setSelection(0); // spiner always have a select option, can not be null

        editTextName.requestFocus();
        //message
        Toast.makeText(this, R.string.os_valores_dos_campos_foram_limpos, Toast.LENGTH_LONG).show();
    }

    public void saveFieldsValues(View view) {

        String name = editTextName.getText().toString();
        String nameWithoutSpace = name.trim();
        String razao = editTextRazao.getText().toString();
        String razaoWithoutSpace = razao.trim();

        int radioButtonId = radioGroupClientType.getCheckedRadioButtonId();

        if (name == null || nameWithoutSpace.isEmpty() || nameWithoutSpace.isBlank()) {
            Toast.makeText(this, R.string.campo_nome_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();

            editTextName.requestFocus();
            return;
        }

        if(razao ==null || razaoWithoutSpace.isEmpty() || razaoWithoutSpace.isBlank()) {
            Toast.makeText(this, R.string.campo_razao_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            editTextRazao.requestFocus();
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

        String division = (String) spinnerDivision.getSelectedItem();

        if(division==null) {
            Toast.makeText(this,"O spinner não carregou os dados", Toast.LENGTH_LONG).show();
            return;
        }

        boolean haveRestriction = checkBoxRestriction.isChecked();

        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(getString(R.string.nome_valor));
        finalMessage.append(name);
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append(getString(R.string.razao_social));
        finalMessage.append(razao);
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append( haveRestriction==true
                ? getString(R.string.possui_restricao) +getString(R.string.venda_somente_vista)
                : getString(R.string.nao_possui_restricao_financeira));
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append(clientType);
        finalMessage.append(System.getProperty("line.separator"));
        finalMessage.append(getString(R.string.divisao_valor));
        finalMessage.append(division);

        // if everithing ok, show fields values on a Toast
        Toast.makeText(this,
                finalMessage.toString(),
                Toast.LENGTH_LONG).show();
    }
}