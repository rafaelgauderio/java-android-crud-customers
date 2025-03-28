package com.rafaeldeluca.cadastrocliente.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.rafaeldeluca.cadastrocliente.R;
import com.rafaeldeluca.cadastrocliente.entities.Customer;
import com.rafaeldeluca.cadastrocliente.entities.enums.Type;
import com.rafaeldeluca.cadastrocliente.persistence.CustomersDatabase;
import com.rafaeldeluca.cadastrocliente.useful.UsefulAlert;

public class CustomerActivity extends AppCompatActivity {

    public static final String KEY_ID = "ID";
    public static final String KEY_MODE = "MODE";
    public static final String KEY_SUGGEST_DIVISION = "SUGGEST_DIVISION";
    public static final String KEY_LAST_DIVISION = "LAST_DIVISION";
    public static final int MODE_INSERT = 0;
    public static final int MODE_UPDATE = 1;
    private EditText editTextName;
    private EditText editTextReason;
    private CheckBox checkBoxRestriction;
    private RadioGroup radioGroupClientType;
    private RadioButton radioButtonNew, radioButtonReactivated, radioButtonRecurrence;
    private Spinner spinnerDivision;
    private EditText editTextEmailCommercial;
    private int mode;
    private Customer originalCustomer;
    private boolean suggestDivision = false;
    private int lastDivision = 0;

    public MenuItem menuItemSuggestDivision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        editTextName = findViewById((R.id.editTextNome));
        editTextReason = findViewById(R.id.editTextReason);
        editTextEmailCommercial = findViewById(R.id.editTextEmailComercial);
        checkBoxRestriction = findViewById(R.id.checkBoxRestriction);
        radioGroupClientType = findViewById(R.id.radioGroupClientType);
        spinnerDivision = findViewById(R.id.spinnerDivision);
        radioButtonNew = findViewById(R.id.radioButtonNewClient);
        radioButtonReactivated = findViewById(R.id.radioButtonClientReativated);
        radioButtonRecurrence = findViewById(R.id.radioButtonRecurrenceClient);

        readPreferences();
        Intent intentOpen = getIntent();
        Bundle bundle = intentOpen.getExtras();

        if (bundle != null) {
            mode = bundle.getInt(KEY_MODE);
            if (mode == MODE_INSERT) {
                this.setTitle(getString(R.string.inserir_cliente_novo));
                if (suggestDivision == true) {
                    spinnerDivision.setSelection(lastDivision);
                }
            } else {
                this.setTitle(getString(R.string.update_customer));

                long id = bundle.getLong(KEY_ID);
                CustomersDatabase customersDatabase = CustomersDatabase.getInstance(this);
                originalCustomer = customersDatabase.getCustomerDao().getCustomerById(id);

                editTextName.setText(originalCustomer.getBuyerName());
                editTextReason.setText(originalCustomer.getCorporateReason());
                editTextEmailCommercial.setText(originalCustomer.getEmail());
                checkBoxRestriction.setChecked(originalCustomer.isRestriction());
                spinnerDivision.setSelection(originalCustomer.getDivision());
                Type clientType = originalCustomer.getType();

                if (clientType == Type.NOVO) {
                    radioButtonNew.setChecked(true);
                } else {
                    if (clientType == Type.REATIVADO) {
                        radioButtonReactivated.setChecked(true);
                    } else {
                        if (clientType == Type.RECORRENTE) {
                            radioButtonRecurrence.setChecked(true);
                        }
                    }
                }
                editTextName.requestFocus();
                editTextName.setSelection(editTextName.getText().length());
            }
        }
    }

    public void cleanFields() {

        final String buyerName = editTextName.getText().toString();
        final String reason = editTextReason.getText().toString();
        final String emailCommercial = editTextEmailCommercial.getText().toString();
        final boolean hasRestriction = checkBoxRestriction.isChecked();
        final int radioButtonTypeId = radioGroupClientType.getCheckedRadioButtonId();
        final int division = spinnerDivision.getSelectedItemPosition();
        final ScrollView scrollView = findViewById(R.id.main);
        final View viewWithFocus = scrollView.findFocus();

        editTextName.setText(null);
        editTextReason.setText(null);
        editTextEmailCommercial.setText(null);
        checkBoxRestriction.setChecked(false);
        radioGroupClientType.clearCheck();
        spinnerDivision.setSelection(0); // spinner always have a select option, can not be null
        editTextName.requestFocus();

        Snackbar snackbar = Snackbar.make(scrollView, R.string.os_valores_dos_campos_foram_limpos,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setText(buyerName);
                editTextReason.setText(reason);
                editTextEmailCommercial.setText(emailCommercial);
                checkBoxRestriction.setChecked(hasRestriction);
                spinnerDivision.setSelection(division);
                if (radioButtonTypeId == R.id.radioButtonNewClient) {
                    radioButtonNew.setChecked(true);
                } else {
                    if (radioButtonTypeId == R.id.radioButtonClientReativated) {
                        radioButtonReactivated.setChecked(true);
                    } else {
                        if (radioButtonTypeId == R.id.radioButtonRecurrenceClient) {
                            radioButtonReactivated.setChecked(true);
                        }
                    }
                }
                if (viewWithFocus != null) {
                    viewWithFocus.requestFocus();
                }
            }
        });

        //UsefulAlert.showAlertDialog(this, R.string.os_valores_dos_campos_foram_limpos);
        //Toast.makeText(this, R.string.os_valores_dos_campos_foram_limpos, Toast.LENGTH_LONG).show();
        snackbar.show();

    }

    public void saveFieldsValues() {

        String name = editTextName.getText().toString();
        String nameWithoutSpace = name.trim();
        String reason = editTextReason.getText().toString();
        String reasonWithoutSpace = reason.trim();
        String email = editTextEmailCommercial.getText().toString();

        int radioButtonId = radioGroupClientType.getCheckedRadioButtonId();

        if (name == null || nameWithoutSpace.isEmpty() || nameWithoutSpace.isBlank() || nameWithoutSpace.length() <=3) {
            UsefulAlert.showAlertDialog(this, R.string.campo_nome_preenchimento_obrigatorio);
            //Toast.makeText(this, R.string.campo_nome_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            editTextName.requestFocus();
            return;
        }

        if (reason == null || reasonWithoutSpace.isEmpty() || reasonWithoutSpace.isBlank() || reasonWithoutSpace.length() <=3) {
            //Toast.makeText(this, R.string.campo_razao_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            UsefulAlert.showAlertDialog(this, R.string.campo_razao_preenchimento_obrigatorio);
            editTextReason.requestFocus();
            return;
        }

        if (email == null || email.trim().isBlank() || email.trim().isBlank() || email.trim().length() <=5) {
            //Toast.makeText(this, R.string.campo_email_de_preenchimento_obrigatorio, Toast.LENGTH_LONG).show();
            UsefulAlert.showAlertDialog(this, R.string.campo_email_de_preenchimento_obrigatorio);
            editTextEmailCommercial.requestFocus();
            return;
        }

        Type clientType = null;

        if (radioButtonId == -1) {
            //Toast.makeText(this, R.string.necessario_escolher_o_tipo_de_cliente, Toast.LENGTH_LONG).show();
            UsefulAlert.showAlertDialog(this, R.string.necessario_escolher_o_tipo_de_cliente);
            return;

        } else {
            if (radioButtonId == R.id.radioButtonNewClient)
                clientType = Type.NOVO;
            else if (radioButtonId == R.id.radioButtonClientReativated)
                clientType = Type.REATIVADO;
            else if (radioButtonId == R.id.radioButtonRecurrenceClient)
                clientType = Type.RECORRENTE;
        }

        int division = spinnerDivision.getSelectedItemPosition();
        if (division == AdapterView.INVALID_POSITION) {
            //Toast.makeText(this, R.string.o_spinner_nao_carregou_os_dados, Toast.LENGTH_LONG).show();
            UsefulAlert.showAlertDialog(this, R.string.o_spinner_nao_carregou_os_dados);
            return;
        }
        boolean haveRestriction = checkBoxRestriction.isChecked();

        Customer customer = new Customer(name, reason, email, haveRestriction, clientType, division);


        // test with it was any change on the edition mode, if not don´t update the object
        if (customer.equals(originalCustomer)) {
            // the values are the same, do not save
            this.setResult(CustomerActivity.RESULT_CANCELED);
            this.finish();
            return;
        }

        Intent intentResponse = new Intent();
        CustomersDatabase customersDatabase = CustomersDatabase.getInstance(this);
        if (mode == MODE_INSERT) {
            long newId = customersDatabase.getCustomerDao().insertCustomer(customer);
            if (newId <= 0) {
                UsefulAlert.showAlertDialog(this, R.string.error_while_trying_to_insert_new_customer);
                return;
            }
            customer.setId(newId);

        } else if (mode == MODE_UPDATE) {
            customer.setId(originalCustomer.getId());
            int quantityCustomersChanged = customersDatabase.getCustomerDao().updateCustomer(customer);
            if(quantityCustomersChanged !=1) {
                UsefulAlert.showAlertDialog(this, R.string.error_while_trying_to_update_a_customer);
                return;
            }
        }

        saveLastDivision(division);
        intentResponse.putExtra(KEY_ID, customer.getId());
        setResult(CustomerActivity.RESULT_OK, intentResponse);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custosmer_options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuItemSuggestDivision);
        item.setChecked(suggestDivision);
        return true; // mandatory to show de Menu of division options
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        if (menuItemId == R.id.menuItemClean) {
            cleanFields();
            return true;
        } else if (menuItemId == R.id.menuItemSave) {
            saveFieldsValues();
            return true;
        } else if (menuItemId == R.id.menuItemSuggestDivision) {
            boolean menuItemValue = !menuItem.isChecked();
            saveSuggestDivision(menuItemValue);
            menuItem.setChecked(menuItemValue);
            // if user habilitate suggest division, show last division used
            if (suggestDivision) {
                spinnerDivision.setSelection(lastDivision);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    private void readPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(CustomersActivity.FILE_PREFERENCES, Context.MODE_PRIVATE);
        suggestDivision = sharedPreferences.getBoolean(KEY_SUGGEST_DIVISION, suggestDivision);
        lastDivision = sharedPreferences.getInt(KEY_LAST_DIVISION, lastDivision);
    }

    private void saveSuggestDivision(boolean newSuggestValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(CustomersActivity.FILE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SUGGEST_DIVISION, newSuggestValue);
        editor.commit(); // synchronous - thread safe
        suggestDivision = newSuggestValue;
    }

    private void saveLastDivision(int newDivisionValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(CustomersActivity.FILE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LAST_DIVISION, newDivisionValue);
        editor.commit();
        lastDivision = newDivisionValue;
    }

}