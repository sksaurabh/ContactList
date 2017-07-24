package com.contactlist.contactlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactlist.contactlist.bean.ContactDetails;

import java.util.List;

/**
 * Created by saurabh on 5/20/2017.
 */

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<ContactDetails> contactDetailsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mPno;
        RelativeLayout mEdit, mDelete;

        public MyViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.vT_adc_name);
            mPno = (TextView) view.findViewById(R.id.vT_adc_number);
            mEdit = (RelativeLayout) view.findViewById(R.id.vR_adc_edit_circle);
            mDelete = (RelativeLayout) view.findViewById(R.id.vR_adc_delete_circle);
        }
    }


    public ContactAdapter(List<ContactDetails> contactDetailsList, Context mContext) {
        this.contactDetailsList = contactDetailsList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contact, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ContactDetails contactDetails = contactDetailsList.get(position);
        holder.mName.setText(contactDetails.getmFName() + " " + contactDetails.getmLName());
        holder.mPno.setText(contactDetails.getpNo());

        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateContactActivity.class);
                intent.putExtra("PHONE", contactDetailsList.get(position).getpNo() + "");
                intent.putExtra("ID", contactDetailsList.get(position).getId() + "");
                mContext.startActivity(intent);
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(contactDetailsList.get(position).getpNo());
            }
        });

    }

    private void removeItem(String s) {
        Helper helper = new Helper(mContext);
        helper.openDataBase();
        helper.deleteItem(s);
        helper.close();

    }

    @Override
    public int getItemCount() {
        if (contactDetailsList == null || contactDetailsList.size() == 0)
            return 0;
        return contactDetailsList.size();
    }
}