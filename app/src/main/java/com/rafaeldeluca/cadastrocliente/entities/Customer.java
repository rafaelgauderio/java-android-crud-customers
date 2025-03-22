package com.rafaeldeluca.cadastrocliente.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.rafaeldeluca.cadastrocliente.entities.enums.Type;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@Entity(tableName = "customer")
public class Customer implements Cloneable {

    public static Comparator<Customer> orderByBuyerNameAsc = new Comparator<Customer>() {
        @Override
        public int compare(Customer c1, Customer c2) {
            return c1.getBuyerName().compareToIgnoreCase(c2.getBuyerName());
        }
    };

    public static Comparator<Customer> orderByBuyerNameDesc = new Comparator<Customer>() {
        @Override
        public int compare(Customer c1, Customer c2) {
            return c2.getBuyerName().compareToIgnoreCase(c1.getBuyerName());
        }
    };

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    @ColumnInfo(index = true)
    private String buyerName;
    @NonNull
    private String corporateReason;
    @NonNull
    private String email;
    private boolean restriction;
    private Type type;
    private int division;


    public Customer() {

    }

    public Customer(String buyerName, String corporateReason, String email, boolean restriction, Type type, int division) {
        this.buyerName = buyerName;
        this.corporateReason = corporateReason;
        this.email = email;
        this.restriction = restriction;
        this.type = type;
        this.division = division;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getCorporateReason() {
        return corporateReason;
    }

    public void setCorporateReason(String corporateReason) {
        this.corporateReason = corporateReason;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRestriction() {
        return restriction;
    }

    public void setRestriction(boolean restriction) {
        this.restriction = restriction;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        // this class has only primitives attributes or immutable
        // clone method is sufficient
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return restriction == customer.restriction
                && division == customer.division
                && buyerName.equals(customer.buyerName)
                && corporateReason.equals(customer.corporateReason)
                && email.equals(customer.email)
                && type == customer.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerName, corporateReason, email, restriction, type, division);
    }

    @Override
    public String toString() {
        return
                buyerName + "\n" +
                        corporateReason + "\n" +
                        email + "\n" +
                        restriction + "\n" +
                        type + "\n" +
                        division;
    }
}