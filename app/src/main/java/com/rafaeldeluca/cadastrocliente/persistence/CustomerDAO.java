package com.rafaeldeluca.cadastrocliente.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rafaeldeluca.cadastrocliente.entities.Customer;

import java.util.List;

@Dao
public interface CustomerDAO {

    @Insert
    long insertCustomer(Customer customer);

    @Update
    int updateCustomer(Customer customer);

    @Delete
    int deleteCustomer(Customer customer);

    @Query("SELECT * " +
            "FROM customer " +
            "ORDER BY buyerName ASC")
    List<Customer> gelAllCustomerAscending();

    @Query("SELECT * " +
            "FROM customer " +
            "ORDER BY buyerName DESC")
    List<Customer> gelAllCustomerDescending();

    @Query("SELECT * " +
            "FROM customer " +
            "WHERE id=:id")
    Customer getCustomerById(long id);

}
