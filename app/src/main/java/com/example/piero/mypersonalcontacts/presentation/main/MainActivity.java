package com.example.piero.mypersonalcontacts.presentation.main;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;





import com.example.piero.mypersonalcontacts.R;

import com.example.piero.mypersonalcontacts.presentation.addContact.AddContactActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends AppCompatActivity {


    private MainActivityViewModel viewModel;
    private RecyclerView list;
    private MainActivityAdapter adapter;
    private FloatingActionButton fab;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        list = findViewById(R.id.list);
        fab = findViewById(R.id.fab);

        // set list adapter
        adapter = new MainActivityAdapter();
        list.setAdapter(adapter);

        // set list layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(),
                layoutManager.getOrientation());

        list.addItemDecoration(dividerItemDecoration);

        viewModel.observeContacts()
                .observe(this, contacts -> {
                    adapter.updateDataSet(contacts);
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setOnClickListener(view -> addContact());
    }

    @Override
    protected void onPause() {
        super.onPause();
        fab.setOnClickListener(null);
    }

    private void addContact() {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);

        /*LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_contact, null, false);

        TextView firstName = view.findViewById(R.id.firstName);
        TextView lastName = view.findViewById(R.id.lastName);
        TextView number = view.findViewById(R.id.number);
        TextView email = view.findViewById(R.id.email);
        TextView company = view.findViewById(R.id.company);
        TextView address = view.findViewById(R.id.address);
        ImageView makePicture  = view.findViewById(R.id.addPhoto);


        new AlertDialog.Builder(this)
               .setTitle("Add contact")
               .setView(view)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    Contact contact = new Contact(
                            firstName.getText().toString(),
                            lastName.getText().toString(),
                            number.getText().toString(),
                            email.getText().toString(),
                            company.getText().toString(),
                            address.getText().toString(),pio );
                    viewModel.addContact(contact);
                })
                .setNegativeButton("Cancel", null)
               .show();

*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));


        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                viewModel.updateFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                viewModel.updateFilter(query);
                return false;
            }
        });
        return true;
    }
}
