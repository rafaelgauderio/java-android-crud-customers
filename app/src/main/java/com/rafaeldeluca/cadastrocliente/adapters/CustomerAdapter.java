package com.rafaeldeluca.cadastrocliente.apapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rafaeldeluca.cadastrocliente.R;
import com.rafaeldeluca.cadastrocliente.entities.Customer;

import java.util.List;

public class CustomerAdapter extends BaseAdapter {

    private Context contex;
    private List<Customer> customersList;
    private String [] divisionsSpinner;

    //constructAdapter
    public CustomerAdapter (Context context, List<Customer> customersList) {
        this.contex = context;
        this.customersList = customersList;
        divisionsSpinner = context.getResources().getStringArray(R.array.divisions);
    }

    // pattern Holder - good programming practice
    private static class CustomerHolder {

        public TextView textViewValueName;
        public TextView textViewValueReason;
        public TextView textViewValueEmail;
        public TextView textViewValueRestriction;
        public TextView textViewValueType;
        public TextView textViewValueDivision;

    }

    @Override
    public int getCount() {
        return customersList.size();
    }

    @Override
    public Object getItem(int position) {
        return customersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomerHolder customerHolder;
        if(convertView==null) {
            // return a object able to inflate a layout
            LayoutInflater layoutInflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_customers_list, parent, false); // false because its a child element, not a root

            customerHolder = new CustomerHolder();
            // save the address of the attributes of the row_customers_list view
            customerHolder.textViewValueName = convertView.findViewById(R.id.textViewValueName);
            customerHolder.textViewValueReason = convertView.findViewById(R.id.textViewValueReason);
            customerHolder.textViewValueEmail = convertView.findViewById(R.id.textViewValueEmail);
            customerHolder.textViewValueRestriction = convertView.findViewById(R.id.textViewValueRestriction);
            customerHolder.textViewValueType = convertView.findViewById(R.id.textViewValueType);
            customerHolder.textViewValueDivision = convertView.findViewById(R.id.textViewValueDivision);

            convertView.setTag(customerHolder);
        } else {
            // in the case the we already have a convertView
            customerHolder = (CustomerHolder) convertView.getTag();
        }
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


        return convertView;
    }
}
