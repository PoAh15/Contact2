package com.example.piero.mypersonalcontacts.presentation.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.piero.mypersonalcontacts.R;
import com.example.piero.mypersonalcontacts.model.entity.Contact;
import com.example.piero.mypersonalcontacts.presentation.detail.DetailContactActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piero on 07/05/18.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<ContactViewHolder>  {

    private List<Contact> data = new ArrayList<>();
    //private List<Contact> dataFiltered;

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        ContactViewHolder holder = new ContactViewHolder(view);
        setupClickListener(holder);
        return holder;
    }

    private void setupClickListener(ContactViewHolder holder) {
        View view = holder.itemView;
        Context context = view.getContext();

        view.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Contact contact = data.get(position);
            DetailContactActivity.newInstance(context, contact.getId());
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateDataSet(List<Contact> contacts) {
        this.data = contacts;
        notifyDataSetChanged();// obbligatorio, aggiorna visivamente la lista
    }

   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataFiltered = data;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : data) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirstName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    dataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values =dataFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

}