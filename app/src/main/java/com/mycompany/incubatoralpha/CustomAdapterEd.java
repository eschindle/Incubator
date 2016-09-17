package com.mycompany.incubatoralpha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kevin on 2/24/2016.
 */
class CustomAdapterEd extends ArrayAdapter<String>{

    TextView school;
    TextView major;
    TextView year;
    String sSchool;
    String sMajor;
    String sYear;
    int mUid;
    Context mContext;
    List<String> majors;
    List<String> years;
    List<String> schools;

    public CustomAdapterEd(Context context, List<String> educationList,List<String> majorList, List<String> yearList, int id) {
        super(context, R.layout.custom_row_ed, educationList);
        majors = majorList;
        years = yearList;
        schools = educationList;
        mUid = id;
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater edInflater = LayoutInflater.from(getContext());
        View customView = edInflater.inflate(R.layout.custom_row_ed, parent, false);

        sSchool = getItem(position);
        school = (TextView) customView.findViewById(R.id.schoolTextView);
        sMajor = majors.get(position);
        major = (TextView) customView.findViewById(R.id.majorTextView);
        sYear = years.get(position);
        year = (TextView) customView.findViewById(R.id.yearTextView);



        customView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteMessage(position);
                //Message that ask if they want to delete
                //if yes then delete row
                return true;
            }
        });

        school.setText(sSchool);
        major.setText(sMajor);
        year.setText(sYear);
        return customView;
    }
    private void showDeleteMessage(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext)
                .setMessage("Are you sure you want to delete?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String schoo = schools.get(position);
                        String majs = majors.get(position);
                        String yer = years.get(position);

                        schools.remove(position);
                        majors.remove(position);
                        years.remove(position);
                        notifyDataSetChanged();

                        User poop = new User(mUid, schoo, majs, yer);
                        deleteWork(poop);
                    }
                });
        dialogBuilder.setPositiveButton("No", null);
        dialogBuilder.show();
    }

    private void deleteWork(User user){
        ServerRequests serverRequests = new ServerRequests(mContext);
        serverRequests.deleteUserEducationInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

            }

            @Override
            public void donezo(String string) {

            }
        });
    }
}

