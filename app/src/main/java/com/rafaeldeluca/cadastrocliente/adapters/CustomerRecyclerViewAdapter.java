package com.rafaeldeluca.cadastrocliente.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafaeldeluca.cadastrocliente.R;
import com.rafaeldeluca.cadastrocliente.entities.Customer;

import java.util.List;

public class CustomerRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRecyclerViewAdapter.CustomerHolder> {

    private Context context;
    private List<Customer> customersList;
    private String[] divisionsSpinner;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    // mandatory use design pattern holder
    public class CustomerHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView textViewValueName;
        public TextView textViewValueReason;
        public TextView textViewValueEmail;
        public TextView textViewValueRestriction;
        public TextView textViewValueType;
        public TextView textViewValueDivision;

        public CustomerHolder(@NonNull View itemView) {
            super(itemView); // object of the view already create

            textViewValueName = itemView.findViewById(R.id.textViewValueName);
            textViewValueReason = itemView.findViewById(R.id.textViewValueReason);
            textViewValueEmail = itemView.findViewById(R.id.textViewValueEmail);
            textViewValueRestriction = itemView.findViewById(R.id.textViewValueRestriction);
            textViewValueType = itemView.findViewById(R.id.textViewValueType);
            textViewValueDivision = itemView.findViewById(R.id.textViewValueDivision);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(onItemClickListener !=null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(view, position);
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(onItemClickListener !=null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemLongClick(view, position);
                    return true;
                }
            }
            return false;
        }
    }

    // constructor
    public CustomerRecyclerViewAdapter(Context context, List<Customer> customersList, OnItemClickListener listener) {
        this.context = context;
        this.customersList = customersList;
        this.onItemClickListener = listener;

        divisionsSpinner = context.getResources().getStringArray(R.array.divisions);
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.row_customers_list, parent, false);

        return new CustomerHolder(itemView);
    }

    // call every time we have to change/update the value of a row (itemCustomer)
    @Override
    public void onBindViewHolder(@NonNull CustomerHolder customerHolder, int position) {

        // get the attributes and save on the respective fields
        Customer customerListItem = customersList.get(position);

        customerHolder.textViewValueName.setText(customerListItem.getBuyerName());
        customerHolder.textViewValueReason.setText(customerListItem.getCorporateReason());
        customerHolder.textViewValueEmail.setText(customerListItem.getEmail());

        if (customerListItem.isRestriction() == true) {
            customerHolder.textViewValueRestriction.setText(R.string.possui_restricao);
        } else {
            customerHolder.textViewValueRestriction.setText(R.string.nao_possui_restricao_financeira);
        }

        switch (customerListItem.getType()) {
            case NOVO:
                customerHolder.textViewValueType.setText(R.string.cliente_novo);
                break;
            case REATIVADO:
                customerHolder.textViewValueType.setText(R.string.cliente_reativado);
                break;
            case RECORRENTE:
                customerHolder.textViewValueType.setText(R.string.cliente_recorrente);
        }
        customerHolder.textViewValueDivision.setText(divisionsSpinner[customerListItem.getDivision()]);

    }

    @Override
    public int getItemCount() {
        return customersList.size();
    }

}
