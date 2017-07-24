package com.contactlist.contactlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.contactlist.contactlist.bean.ContactDetails;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    TextView noContacts;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ContactAdapter contactAdapter;
    List<ContactDetails> contactDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        initializeViews();

    }

    private void initializeViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.vR_pb_contact_list);
        noContacts = (TextView) findViewById(R.id.vT_pb_nocontacts);
    }

    private void setValues() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        contactAdapter = new ContactAdapter(contactDetailsList, PhoneBookActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getvalues();
    }

    private void getvalues() {
        Helper helper = new Helper(this);
        helper.openDataBase();
        contactDetailsList = helper.getContactTable();
        helper.close();

        if (contactDetailsList == null || contactDetailsList.size() == 0) {
            noContacts.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            noContacts.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            setValues();
        }
    }


}
