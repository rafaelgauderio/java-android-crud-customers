package com.rafaeldeluca.cadastrocliente.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    private OnItemLongClickListener onItemLongClickListener;
    private OnCreateContextMenu onCreateContextMenu;
    private OnContextMenuClickListener  onContextMenuClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface OnCreateContextMenu {
        void onCreateContextMenu (ContextMenu contextMenu,View view,
                                  ContextMenu.ContextMenuInfo contextMenuInfo, int position,
                                  MenuItem.OnMenuItemClickListener onMenuItemClickListener);
    }

    public interface OnContextMenuClickListener {
        boolean onContextMenuItemListener(MenuItem menuItem, int position);
    }

    // mandatory use design pattern holder
    public class CustomerHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

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
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {

                int position = getAdapterPosition();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
            }
        }

        @Override
        public boolean onLongClick(View view) {
                int position = getAdapterPosition();
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(view, position);
                    return true;
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if(onCreateContextMenu !=null) {
                onCreateContextMenu.onCreateContextMenu(contextMenu, view, contextMenuInfo,
                                                        getAdapterPosition(), onMenuItemClickListener);
            }
        }

        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                if(onContextMenuClickListener !=null) {
                    onContextMenuClickListener.onContextMenuItemListener(menuItem, getAdapterPosition());
                    return true;
                }

                return false;
            }
        };
    } // end of holder

    // constructor
    public CustomerRecyclerViewAdapter(Context context, List<Customer> customersList) {
        this.context = context;
        this.customersList = customersList;
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

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnCreateContextMenu getOnCreateContextMenu() {
        return onCreateContextMenu;
    }

    public void setOnCreateContextMenu(OnCreateContextMenu onCreateContextMenu) {
        this.onCreateContextMenu = onCreateContextMenu;
    }

    public OnContextMenuClickListener getOnContextMenuClickListener() {
        return onContextMenuClickListener;
    }

    public void setOnContextMenuClickListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.onContextMenuClickListener = onContextMenuClickListener;
    }
}
