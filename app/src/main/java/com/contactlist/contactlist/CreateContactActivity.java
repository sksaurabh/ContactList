package com.contactlist.contactlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.contactlist.contactlist.bean.ContactDetails;

import java.util.ArrayList;
import java.util.List;

public class CreateContactActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mFirst, mLast, mNumber, mNick;
    RelativeLayout mok, mcancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        initializeViews();
        if (getIntent().getStringExtra("PHONE") == null) {

        } else {
            setValues(getIntent().getStringExtra("PHONE"));
        }

    }

    private void setValues(String phone) {
        Helper helper = new Helper(this);
        helper.openDataBase();
        ContactDetails contactDetails = helper.getContact(phone);
        helper.close();

        mFirst.setText(contactDetails.getmFName());
        mLast.setText(contactDetails.getmLName());
        mNumber.setText(contactDetails.getpNo());
        mNick.setText(contactDetails.getnName());
    }

    private void initializeViews() {
        mFirst = (EditText) findViewById(R.id.vE_acc_firstname);
        mLast = (EditText) findViewById(R.id.vE_acc_lastname);
        mNumber = (EditText) findViewById(R.id.vE_acc_phonenumber);
        mNick = (EditText) findViewById(R.id.vE_acc_nick);
        mok = (RelativeLayout) findViewById(R.id.vR_acc_ok);
        mcancel = (RelativeLayout) findViewById(R.id.vR_acc_cancel);

        mok.setOnClickListener(this);
        mcancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vR_acc_ok:
                if (checkvalues()) {
                    Helper helper = new Helper(this);
                    helper.openDataBase();
                    boolean value = helper.isthereExists(mNumber.getText().toString());
                    helper.close();

                    if (getIntent().getStringExtra("PHONE")==null) {

                        if (value) {
                            Toast.makeText(this, "Number Already exist", Toast.LENGTH_SHORT).show();
                        } else {
                            ContactDetails contactDetails = new ContactDetails(mFirst.getText().toString(), mLast.getText().toString(),
                                    mNumber.getText().toString(), mNick.getText().toString());

                            helper.openDataBase();
                            helper.insertIntoContactTable(contactDetails);
                            helper.close();
                            finish();
                        }
                    }else {
                        ContactDetails contactDetails = new ContactDetails(mFirst.getText().toString(), mLast.getText().toString(),
                                mNumber.getText().toString(), mNick.getText().toString());

                        helper.openDataBase();
                        helper.updateIntoContactTable(contactDetails,getIntent().getStringExtra("ID"));
                        helper.close();
                        finish();
                    }

                }
                break;
            case R.id.vR_acc_cancel:
                mFirst.setText("");
                mLast.setText("");
                mNumber.setText("");
                mNick.setText("");
                break;
            default:
                break;
        }
    }

    private boolean checkvalues() {
        if (mFirst.getText() == null || mFirst.getText().toString().length() == 0) {
            Toast.makeText(CreateContactActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mLast.getText() == null || mLast.getText().toString().length() == 0) {
            Toast.makeText(CreateContactActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mNumber.getText() == null || mNumber.getText().toString().length() == 0) {
            Toast.makeText(CreateContactActivity.this, "Please enter phone number name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mNick.getText() == null || mNick.getText().toString().length() == 0) {
            Toast.makeText(CreateContactActivity.this, "Please enter nickname name", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
