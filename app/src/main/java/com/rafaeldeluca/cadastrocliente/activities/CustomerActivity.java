package com.rafaeldeluca.cadastrocliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rafaeldeluca.cadastrocliente.R;
import com.rafaeldeluca.cadastrocliente.entities.Customer;
import com.rafaeldeluca.cadastrocliente.entities.enums.Type;

import java.security.Key;

public class CustomerActivity extends AppCompatActivity {

    public static final String KEY_REASON = "KEY_REASON";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_RESTRICTION = "KEY_RESTRICTION";
    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_DIVISION = "KEY_DIVISION";
    private EditText editTextName;
    private EditText editTextReason;
    private CheckBox checkBoxRestriction;
    private RadioGroup radioGroupClientType;
    private Spinner spinnerDivision;
    private EditText editTextEmailComercial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        this.setTitle(getString(R.string.inserir_cliente_novo));

        editTextName = findViewById((R.id.editTextNome));
        editTextReason = findViewById(R.id.editTextReason);
        checkBoxRestriction = findViewById(R.id.checkBoxRestriction);
        radioGroupClientType = findViewById(R.id.radioGroupClientType);
        spinnerDivision = findViewById(R.id.spinnerDivision);
        editTextEmailComercial = findViewById(R.id.editTextEmailComercial);

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
        editTextReason.setText(null);
        editTextEmailComercial.setText(null);
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
        String reason = editTextReason.getText().toString();
        String reasonWithoutSpace = reason.trim();
        String email = editTextEmailComercial.getText().toString();

        int radioButtonId = radioGroupClientType.getCheckedRadioButtonId();

        if (name == null || nameWithoutSpace.isEmpty() || nameWithoutSpace.isBlank()) {
            Toast.makeText(this, R.string.campo_nome_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();

            editTextName.requestFocus();
            return;
        }

        if(reason ==null || reasonWithoutSpace.isEmpty() || reasonWithoutSpace.isBlank()) {
            Toast.makeText(this, R.string.campo_razao_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            editTextReason.requestFocus();
            return;
        }

        if(email ==null || email.trim().isBlank() || email.trim().isBlank()) {
            Toast.makeText(this, R.string.campo_email_de_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            editTextEmailComercial.requestFocus();
            return;
        }

        Type clientType = null;

        if(radioButtonId == -1) {
            Toast.makeText(this, R.string.necessario_escolher_o_tipo_de_cliente, Toast.LENGTH_LONG).show();
            return;

        } else {
            if (radioButtonId==R.id.radioNewClient)
                clientType = Type.NOVO;
            else if(radioButtonId==R.id.radioButtonClientReativated)
                clientType = Type.REATIVADO;
                        else if (radioButtonId==R.id.radioButtonRecurringClient)
                            clientType = Type.RECORRENTE;
        }

        int division =  spinnerDivision.getSelectedItemPosition();

        if(division== AdapterView.INVALID_POSITION) {
            Toast.makeText(this,"O spinner não carregou os dados", Toast.LENGTH_LONG).show();
            return;
        }

        boolean haveRestriction = checkBoxRestriction.isChecked();

        Intent intentResponse = new Intent();
        intentResponse.putExtra(KEY_REASON,reason);
        intentResponse.putExtra(KEY_NAME,name);
        intentResponse.putExtra(KEY_EMAIL,email);
        intentResponse.putExtra(KEY_RESTRICTION,haveRestriction);
        intentResponse.putExtra(KEY_TYPE, clientType.toString());
        intentResponse.putExtra(KEY_DIVISION,division);

        setResult(CustomerActivity.RESULT_OK, intentResponse);
        finish();

    }
}