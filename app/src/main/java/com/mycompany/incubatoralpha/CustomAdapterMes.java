package com.mycompany.incubatoralpha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterMes extends ArrayAdapter<String> {

    TextView namey;
    ImageView like,nope;

    String sName;

    int mUid;
    List<String> mOid;
    Context mContext;

    List<String> pendNames;

    public CustomAdapterMes(Context context, List<String> nameList, int id,List<String> oid) {
        super(context, R.layout.custom_row_ed, nameList);

        pendNames = nameList;
        mUid = id;
        mOid = oid;
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater edInflater = LayoutInflater.from(getContext());
        View customView = edInflater.inflate(R.layout.custom_row_pending, parent, false);

        sName = getItem(position);
        namey = (TextView) customView.findViewById(R.id.pendNameTextView);
        like = (ImageView) customView.findViewById(R.id.yButton);
        nope = (ImageView) customView.findViewById(R.id.xButton);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLikeMessage(position);
                //Message that ask if they want to delete
                //if yes then delete row
            }
        });

        nope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteMessage(position);
                //Message that ask if they want to delete
                //if yes then delete row
            }
        });

        namey.setText(sName);
        return customView;
    }
    private void showDeleteMessage(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext)
                .setMessage("Are you sure you want to reject them?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String nam = pendNames.get(position);
                        String otherUID = mOid.get(position);
                        int newOuid = Integer.parseInt(otherUID);


                        User poop = new User(mUid, newOuid, 0);
                        pendNames.remove(position);
                        deleteWork(poop);
                    }
                });
        dialogBuilder.setPositiveButton("No", null);
        dialogBuilder.show();
    }

    private void deleteWork(User user){
        ServerRequests serverRequests = new ServerRequests(mContext);
        serverRequests.updateUserConnectInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                notifyDataSetChanged();
            }

            @Override
            public void donezo(String string) {
                notifyDataSetChanged();
            }
        });
    }

    private void showLikeMessage(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext)
                .setMessage("Are you sure you want to accept them?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String nam = pendNames.get(position);
                        String otherUID = mOid.get(position);
                        int newOuid = Integer.parseInt(otherUID);


                        User poop = new User(mUid, newOuid, 2);
                        pendNames.remove(position);
                        likeThem(poop);
                    }
                });
        dialogBuilder.setPositiveButton("No", null);
        dialogBuilder.show();
    }

    private void likeThem(User user){
        ServerRequests serverRequests = new ServerRequests(mContext);
        serverRequests.updateUserConnectInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                notifyDataSetChanged();
            }

            @Override
            public void donezo(String string) {
                notifyDataSetChanged();


            }
        });
    }
}
