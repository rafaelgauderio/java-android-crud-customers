package com.rafaeldeluca.cadastrocliente.activities;

import static com.rafaeldeluca.cadastrocliente.entities.Customer.orderByBuyerNameAsc;
import static com.rafaeldeluca.cadastrocliente.entities.Customer.orderByBuyerNameDesc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.rafaeldeluca.cadastrocliente.R;
import com.rafaeldeluca.cadastrocliente.adapters.CustomerRecyclerViewAdapter;
import com.rafaeldeluca.cadastrocliente.entities.Customer;
import com.rafaeldeluca.cadastrocliente.entities.enums.Type;
import com.rafaeldeluca.cadastrocliente.persistence.CustomersDatabase;
import com.rafaeldeluca.cadastrocliente.useful.UsefulAlert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomersActivity extends AppCompatActivity {

    public static final String FILE_PREFERENCES = "com.rafaeldeluca.cadastrocliente.PREFERENCES";
    public static final String KEY_ASCENDING_ORDER = "ASCENDING_ORDER";
    public static final boolean INITIAL_ASCENDING_SORT_PATTER = true;
    private List<Customer> customersList;
    private RecyclerView recyclerViewCustomers;
    private RecyclerView.LayoutManager layoutManager;
    private CustomerRecyclerViewAdapter customerRecyclerViewAdapter;
    private int selectedPosition = -1; // no customer has been selected
    private View selectedView;
    private Drawable backgroundDrawable;
    private ActionMode actionMode; //lib androidx
    private boolean ascendingOrder = INITIAL_ASCENDING_SORT_PATTER;
    private MenuItem menuItemSort;

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
                //actionMode.finish(); // close menu
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
            if (selectedView != null) {
                selectedView.setBackground(backgroundDrawable);
            }
            // release objects on memory
            actionMode = null;
            selectedView = null;
            backgroundDrawable = null;
            recyclerViewCustomers.setEnabled(true);
        }
    };

    @SuppressLint("ResourceAsColor")
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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(R.color.green_dark));
        recyclerViewCustomers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewCustomers.addItemDecoration(dividerItemDecoration);

        readPreferences();
        insertCustomersListData();
    }

    private void insertCustomersListData() {

        CustomersDatabase customersDatabase = CustomersDatabase.getInstance(this);
        if (ascendingOrder) {
            customersList = customersDatabase.getCustomerDao().gelAllCustomerAscending();
        } else {
            customersList = customersDatabase.getCustomerDao().gelAllCustomerDescending();
        }

        //customersList = new ArrayList<Customer>();
        customerRecyclerViewAdapter = new CustomerRecyclerViewAdapter(this, customersList);
        // simple click
        customerRecyclerViewAdapter.setOnItemClickListener(new CustomerRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedPosition = position;
                updateCustomer();
            }
        });

        // long click
        customerRecyclerViewAdapter.setOnItemLongClickListener(new CustomerRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                if (actionMode != null) {
                    return false;
                }
                selectedPosition = position;
                selectedView = view;
                backgroundDrawable = view.getBackground();
                //view.setBackgroundColor(Color.rgb(3,234,116));
                view.setBackgroundColor(getColor(R.color.selectColor));
                recyclerViewCustomers.setEnabled(false);
                actionMode = startSupportActionMode(actionModeCallback);
                return true;
            }
        });
        recyclerViewCustomers.setAdapter(customerRecyclerViewAdapter);
    }

    public void actionMenuAbout() {
        // object that moves between activities
        Intent intentOpen = new Intent(this, AboutActivity.class);
        startActivity(intentOpen);
    }

    ActivityResultLauncher<Intent> launcherNewCustomer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    if (activityResult.getResultCode() == CustomersActivity.RESULT_OK) {
                        Intent intent = activityResult.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            // getting customer from database
                            long id = bundle.getLong(CustomerActivity.KEY_ID);
                            CustomersDatabase customersDatabase = CustomersDatabase.getInstance(CustomersActivity.this);
                            Customer customer = customersDatabase.getCustomerDao().getCustomerById(id);
                            customersList.add(customer);
                            sortCustomerList();
                        }
                    }
                }
            });

    public void actionMenuAddNewCustomer() {

        Intent intentOpen = new Intent(this, CustomerActivity.class);
        intentOpen.putExtra(CustomerActivity.KEY_MODE, CustomerActivity.MODE_INSERT);
        launcherNewCustomer.launch(intentOpen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.customers_options, menu);
        menuItemSort = menu.findItem(R.id.menuItemSort);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.updateOrderIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.menuItemAdd) {
            actionMenuAddNewCustomer();
            return true;
        } else {
            if (menuItemId == R.id.menuItemAbout) {
                actionMenuAbout();
                return true;
            } else {
                if (menuItemId == R.id.menuItemSort) {
                    savePreferencesAscendingOrder(!ascendingOrder);
                    updateOrderIcon();
                    sortCustomerList();
                    return true;
                } else {
                    if (menuItemId == R.id.menuItemRestoreFactoryDefaults) {
                        this.confirmationRestoreFactoryDefaults();
                        return true;
                    } else {
                        return super.onOptionsItemSelected(item);
                    }
                }
            }
        }
    }

    private void removeCustomer() {
        // make variable final to continue existing after close method remove Customer
        final Customer customer = customersList.get(selectedPosition);
        //String message = getString(R.string.are_you_sure_you_want_to_delete_company)
        // + customer.getCorporateReason() + getString(R.string.quotes);
        String message = getString(R.string.are_you_sure_you_want_to_delete_company, customer.getCorporateReason());

        DialogInterface.OnClickListener listenerYes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete form database
                CustomersDatabase customersDatabase = CustomersDatabase.getInstance(CustomersActivity.this);
                int quantityChangedCustomers = customersDatabase.getCustomerDao().deleteCustomer(customer);
                if (quantityChangedCustomers != 1) {
                    UsefulAlert.showAlertDialog(CustomersActivity.this, R.string.error_while_trying_to_delete_customer);
                    return;
                }
                // delete from memory
                customersList.remove(selectedPosition);
                // render the list without the remove item
                customerRecyclerViewAdapter.notifyItemRemoved(selectedPosition);
                actionMode.finish();
            }
        };
        // do nothing if user chose "NO"
        UsefulAlert.confirmationActionAlertDialog(this, message, listenerYes, null);
    }

    ActivityResultLauncher<Intent> launcherUpdateCustomer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    if (activityResult.getResultCode() == CustomersActivity.RESULT_OK) {
                        Intent intent = activityResult.getData();
                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {

                            // undo the update on the database
                            final Customer originalCustomer = customersList.get(selectedPosition);
                            long id = bundle.getLong(CustomerActivity.KEY_ID);
                            final CustomersDatabase customersDatabase = CustomersDatabase.getInstance(CustomersActivity.this);
                            final Customer updatedCustomer = customersDatabase.getCustomerDao().getCustomerById(id);
                            customersList.set(selectedPosition, updatedCustomer);
                            sortCustomerList();

                            // snack bar undo edit Customer
                            final ConstraintLayout constraintLayout = findViewById(R.id.main);
                            Snackbar snackbar = Snackbar.make(constraintLayout,
                                    R.string.data_update_customer, Snackbar.LENGTH_LONG);
                            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // undo on database
                                    int quantityChangedCustomers = customersDatabase.getCustomerDao().updateCustomer(originalCustomer);
                                    if (quantityChangedCustomers != 1) {
                                        UsefulAlert.showAlertDialog(CustomersActivity.this, R.string.error_while_trying_to_update_a_customer);
                                        return;
                                    }
                                    // undo on memory
                                    customersList.remove(updatedCustomer);
                                    // add object customer before the edition
                                    customersList.add(originalCustomer);
                                    sortCustomerList();
                                }
                            });
                            snackbar.show();
                        }
                    }
                    selectedPosition = -1; // no object of the list is selected

                    // unselected object from list
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                }
            });

    private void updateCustomer() {
        Customer updateCustomer = customersList.get(selectedPosition);
        Intent intentOpen = new Intent(this, CustomerActivity.class);

        intentOpen.putExtra(CustomerActivity.KEY_MODE, CustomerActivity.MODE_UPDATE);
        intentOpen.putExtra(CustomerActivity.KEY_ID, updateCustomer.getId());
        // load screen with customer data
        launcherUpdateCustomer.launch(intentOpen);
    }

    private void readPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_PREFERENCES", Context.MODE_PRIVATE);
        ascendingOrder = sharedPreferences.getBoolean(KEY_ASCENDING_ORDER, ascendingOrder);
    }

    private void savePreferencesAscendingOrder(boolean newOrderValue) {
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ASCENDING_ORDER, newOrderValue);
        editor.commit();
        ascendingOrder = newOrderValue;
    }

    private void sortCustomerList() {
        if (ascendingOrder == true) {
            Collections.sort(customersList, orderByBuyerNameAsc);
        } else {
            Collections.sort(customersList, orderByBuyerNameDesc);
        }
        // render list after sort
        customerRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void updateOrderIcon() {
        if (ascendingOrder == true) {
            menuItemSort.setIcon(R.drawable.ic_action_name_ascending_order);
        } else {
            menuItemSort.setIcon(R.drawable.ic_action_name_descending_order);
        }
    }

    public void restoreFactoryDefaults() {
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // clean by each key
        editor.remove(KEY_ASCENDING_ORDER);
        editor.remove(CustomerActivity.KEY_SUGGEST_DIVISION);
        editor.remove(CustomerActivity.KEY_LAST_DIVISION);
        // clean all menus
        editor.clear();
        editor.commit();
        ascendingOrder = INITIAL_ASCENDING_SORT_PATTER;
    }

    private void confirmationRestoreFactoryDefaults() {
        DialogInterface.OnClickListener listenerYes = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restoreFactoryDefaults();
                updateOrderIcon();
                sortCustomerList();
                Toast.makeText(CustomersActivity.this,
                        R.string.configuracoes_restauradas_para_o_padrao_de_fabrica,
                        Toast.LENGTH_SHORT).show();
            }
        };

        UsefulAlert.confirmationActionAlertDialog(this,
                getString(R.string.confirm_restore_to_factory_defaults),
                listenerYes, null
        );
    }
}