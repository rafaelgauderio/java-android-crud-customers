package com.rafaeldeluca.cadastrocliente;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.rafaeldeluca.cadastrocliente.entities.Customer;
import com.rafaeldeluca.cadastrocliente.entities.enums.Type;

import java.util.ArrayList;
import java.util.List;

public class CustomersActivity extends AppCompatActivity {

    private ListView customersListView;
    private List<Customer> customersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customers);

        customersListView = findViewById(R.id.listViewCustomers);

        //insertData
        insertCustomersListData();
    }

    private void insertCustomersListData() {

        String[] customersBuyerName = getResources().getStringArray(R.array.customers_buyers_name);
        String[] customersCorporateReason = getResources().getStringArray(R.array.customers_corporate_reason);
        String[] customersEmail = getResources().getStringArray(R.array.customers_email);
        int[] customersRestriction = getResources().getIntArray(R.array.customers_restriction);
        int[] customersType = getResources().getIntArray(R.array.customers_type);
        int[] customersDivision = getResources().getIntArray(R.array.customers_divison);

        customersList = new ArrayList<Customer>();

        Customer customer;
        boolean restriction;
        Type type;
        Type[] valuesCustomersType = Type.values();

        for (int index = 0; customersBuyerName.length > index; index++) {
            if (customersRestriction[index] == 1) {
                restriction = true;
            } else {
                restriction = false;
            }
            type = valuesCustomersType[customersType[index]];
            customer = new Customer(customersBuyerName[index],
                    customersCorporateReason[index],
                    customersEmail[index],
                    restriction, type, customersDivision[index]);

            customersList.add(customer);
        } // end for

        ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, customersList);
        customersListView.setAdapter(customerAdapter);
    }
}