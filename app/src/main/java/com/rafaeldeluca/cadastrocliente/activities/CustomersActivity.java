package com.rafaeldeluca.cadastrocliente.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
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
    private int selectedPosition = -1; // no customer has been selected
    private View selectedView;
    private Drawable backgroundDrawable;
    private ActionMode actionMode; //lib androidx
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.customers_selected_item, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int menuItemId = menuItem.getItemId();
            if (menuItemId == R.id.menuItemDelete) {
                removeCustomer();
                actionMode.finish(); // close menu
                return true;
            } else {
                if (menuItemId == R.id.menuItemUpdate) {
                    updateCustomer();
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(selectedView!= null) {
                selectedView.setBackground(backgroundDrawable);
            }
            // release objects on memory
            actionMode=null;
            selectedView=null;
            backgroundDrawable=null;
            recyclerViewCustomers.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        setTitle(getString(R.string.controle_de_clientes));

        recyclerViewCustomers = findViewById(R.id.recyclerViewCustomers);
        // mandatory define a layoutManager to a RecycleView
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCustomers.setLayoutManager(layoutManager);
        recyclerViewCustomers.setHasFixedSize(true); // optimize de rendering with the rows have fixed size
        recyclerViewCustomers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        insertCustomersListData();
    }

    private void insertCustomersListData() {

        customersList = new ArrayList<Customer>();
        customerRecyclerViewAdapter = new CustomerRecyclerViewAdapter(this, customersList);

        // simple click
        customerRecyclerViewAdapter.setOnItemClickListener(new CustomerRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                updateCustomer();
            }
        });

        // long click
        customerRecyclerViewAdapter.setOnItemLongClickListener(new CustomerRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                if(actionMode!=null) {
                    return false;
                }
                selectedPosition = position;
                selectedView = view;
                backgroundDrawable = view.getBackground();
                view.setBackgroundColor(Color.GRAY);
                recyclerViewCustomers.setEnabled(false);
                actionMode = startSupportActionMode(actionModeCallback);
                return true;
            }
        });
        recyclerViewCustomers.setAdapter(customerRecyclerViewAdapter);
    }

    public void actionMenuAbout () {
        // object that moves between activities
        Intent intentOpen = new Intent(this, AboutActivity.class);
        startActivity(intentOpen);
    }

    ActivityResultLauncher<Intent> launcherNewCustomer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                        if(activityResult.getResultCode() == CustomersActivity.RESULT_OK) {
                            Intent intent = activityResult.getData();

                            Bundle bundle = intent.getExtras();

                            if(bundle != null) {
                                String reason = bundle.getString(CustomerActivity.KEY_REASON);
                                String name = bundle.getString(CustomerActivity.KEY_NAME);
                                String email = bundle.getString(CustomerActivity.KEY_EMAIL);
                                boolean haveRestriction = bundle.getBoolean(CustomerActivity.KEY_RESTRICTION);
                                String clientTypeString = bundle.getString(CustomerActivity.KEY_TYPE);
                                int division = bundle.getInt(CustomerActivity.KEY_DIVISION);

                                Customer customer = new Customer(name,reason,email,haveRestriction,Type.valueOf(clientTypeString),division);
                                customersList.add(customer);
                                // alert adapter that the list have been change, a new list is render;
                                customerRecyclerViewAdapter.notifyDataSetChanged();
                            }

                        }
                }
            });

    public void actionMenuAddNewCustomer () {

        Intent intentOpen = new Intent(this, CustomerActivity.class);
        intentOpen.putExtra(CustomerActivity.KEY_MODE, CustomerActivity.MODE_INSERT);
        launcherNewCustomer.launch(intentOpen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.customers_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        if(menuItemId == R.id.menuItemAdd) {
            actionMenuAddNewCustomer();
            return true;

        } else {
            if(menuItemId == R.id.menuItemAbout) {
                actionMenuAbout();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void removeCustomer () {
        customersList.remove(selectedPosition);
        // render the list without the remove item
        customerRecyclerViewAdapter.notifyDataSetChanged();
    }

            ActivityResultLauncher<Intent> launcherUpdateCustomer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult activityResult) {
                        if(activityResult.getResultCode() == CustomersActivity.RESULT_OK) {
                            Intent intent = activityResult.getData();
                            Bundle bundle = intent.getExtras();

                            if(bundle != null) {
                                String reason = bundle.getString(CustomerActivity.KEY_REASON);
                                String name = bundle.getString(CustomerActivity.KEY_NAME);
                                String email = bundle.getString(CustomerActivity.KEY_EMAIL);
                                boolean haveRestriction = bundle.getBoolean(CustomerActivity.KEY_RESTRICTION);
                                String clientTypeString = bundle.getString(CustomerActivity.KEY_TYPE);
                                int division = bundle.getInt(CustomerActivity.KEY_DIVISION);

                                Customer updateCustomer = customersList.get(selectedPosition);
                                updateCustomer.setCorporateReason(reason);
                                updateCustomer.setBuyerName(name);
                                updateCustomer.setEmail(email);
                                updateCustomer.setDivision(division);
                                updateCustomer.setRestriction(haveRestriction);
                                Type clientType = Type.valueOf(clientTypeString);
                                updateCustomer.setType(clientType);

                                customerRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                        selectedPosition = -1; // no object of the list is selected
                    }
                });

    private void updateCustomer() {
        Customer updateCustomer = customersList.get(selectedPosition);
        Intent intentOpen = new Intent(this, CustomerActivity.class);

        intentOpen.putExtra(CustomerActivity.KEY_MODE, CustomerActivity.MODE_UPDATE);
        intentOpen.putExtra(CustomerActivity.KEY_NAME, updateCustomer.getBuyerName());
        intentOpen.putExtra(CustomerActivity.KEY_REASON, updateCustomer.getCorporateReason());
        intentOpen.putExtra(CustomerActivity.KEY_EMAIL, updateCustomer.getEmail());
        intentOpen.putExtra(CustomerActivity.KEY_RESTRICTION, updateCustomer.isRestriction());
        intentOpen.putExtra(CustomerActivity.KEY_DIVISION, updateCustomer.getDivision());
        intentOpen.putExtra(CustomerActivity.KEY_TYPE, updateCustomer.getType().toString());

        // load screen with customer data
        launcherUpdateCustomer.launch(intentOpen);
    }

}