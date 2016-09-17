package com.mycompany.incubatoralpha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class CustomAdapterWork extends ArrayAdapter<String> {
    TextView company;
    TextView title;
    TextView description;
    String sCompany;
    String sTitle;
    String sDescription;
    List<String> titles = new ArrayList<>();
    List<String> descriptions = new ArrayList<>();
    List<String> comps = new ArrayList<>();
    final Context mContext;
    int mUid;

    public CustomAdapterWork(Context context, List<String> companyList, List<String> titleList, List<String> descriptionList
    ,int uid) {
        super(context, R.layout.custom_row_work,R.id.companyTextView,companyList);
        this.mContext = context;
        this.titles = titleList;
        this.descriptions = descriptionList;
        this.comps = companyList;
        this.mUid = uid;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater workInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = workInflater.inflate(R.layout.custom_row_work,parent,false);


        sCompany = comps.get(position);
        sTitle = titles.get(position);
        sDescription = descriptions.get(position);

        company = (TextView) customView.findViewById(R.id.companyTextView);
        title = (TextView) customView.findViewById(R.id.titleTextView);
        description = (TextView) customView.findViewById(R.id.workDescriptionTextView);


        customView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteMessage(position);
                //Message that ask if they want to delete
                //if yes then delete row
                return true;
            }
        });

        company.setText(sCompany);
        title.setText(sTitle);
        description.setText(sDescription);


        return customView;
    }



    private void showDeleteMessage(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext)
        .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String sComp = comps.get(position);
                        String sTit = titles.get(position);
                        String sDes = descriptions.get(position);

                        comps.remove(position);
                        titles.remove(position);
                        descriptions.remove(position);
                        notifyDataSetChanged();

                        User poop = new User(mUid,sComp,sTit, sDes);
                        deleteWork(poop);
                    }
                });
        dialogBuilder.setNegativeButton("No", null);
        dialogBuilder.show();
    }

    private void deleteWork(User user){
        ServerRequests serverRequests = new ServerRequests(mContext);
        serverRequests.deleteUserWorkInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

            }

            @Override
            public void donezo(String string) {

            }
        });
    }

}


