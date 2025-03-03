package com.rafaeldeluca.cadastrocliente.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafaeldeluca.cadastrocliente.R;
import com.rafaeldeluca.cadastrocliente.adapters.CustomerRecyclerViewAdapter;
import com.rafaeldeluca.cadastrocliente.entities.Customer;
import com.rafaeldeluca.cadastrocliente.entities.enums.Type;

import java.util.ArrayList;
import java.util.List;

public class CustomersActivity extends AppCompatActivity {

    private List<Customer> customersList;
    private RecyclerView recyclerViewCustomers;
    private RecyclerView.LayoutManager layoutManager;
    private CustomerRecyclerViewAdapter customerRecyclerViewAdapter;
    private CustomerRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customers);

        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);
        // mandatory define a layoutManager to a RecycleView
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCustomers.setLayoutManager(layoutManager);
        recyclerViewCustomers.setHasFixedSize(true); // optimize de rendering with the rows have fixed size
        recyclerViewCustomers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //  event handling when user clicks on an item in the list
        onItemClickListener = new CustomerRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Customer customer = customersList.get(position);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.empresa_de_razao_social) + customer.getCorporateReason().toUpperCase() + getString(R.string.foi_selecionada),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Customer customer = customersList.get(position);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.empresa_de_razao_social) + customer.getCorporateReason().toUpperCase() + getString(R.string.recebeu_um_click_longo),
                        Toast.LENGTH_LONG).show();
            }

        };
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
        int index =0;
        for (String s : customersBuyerName) {
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
            index++;
        } // end for

        /*
        ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, customersList);

        */
        // change the arrayAdapter for a customerRecycleViewAdapter
        customerRecyclerViewAdapter = new CustomerRecyclerViewAdapter(this, customersList, onItemClickListener);
        recyclerViewCustomers.setAdapter(customerRecyclerViewAdapter);
    }
}