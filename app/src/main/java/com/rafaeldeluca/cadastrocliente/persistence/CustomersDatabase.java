package com.rafaeldeluca.cadastrocliente.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rafaeldeluca.cadastrocliente.activities.CustomerActivity;
import com.rafaeldeluca.cadastrocliente.entities.Customer;

@Database(entities = {Customer.class},
        version = 1,
        exportSchema = false)
public abstract class CustomersDatabase extends RoomDatabase {

    public abstract CustomerDAO getCustomerDao();

    //singleton pattern
    private static CustomersDatabase INSTANCE;

    public static CustomersDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (CustomersDatabase.class) {
                // double thread block verification
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                                    CustomersDatabase.class,
                                    "customersDatabase.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
