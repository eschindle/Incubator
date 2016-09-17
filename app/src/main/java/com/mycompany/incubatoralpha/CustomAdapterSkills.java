package com.mycompany.incubatoralpha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kevin on 3/10/2016.
 */
public class CustomAdapterSkills extends BaseAdapter {

    List<String> allSkills;
    Context context;
    int mUid;

    private static LayoutInflater inflater=null;
    public CustomAdapterSkills(MainActivity mainActivity, List<String> skills,int id) {
        // TODO Auto-generated constructor stub
        allSkills = skills;
        context=mainActivity;
        mUid = id;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return allSkills.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.skills_row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.gridViewText);


        holder.tv.setText(allSkills.get(position));


        rowView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                showDeleteMessage(position);
                return true;
            }
        });

        return rowView;
    }
    private void showDeleteMessage(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String ski = allSkills.get(position);


                        User poop = new User(mUid, ski);
                        allSkills.remove(position);
                        notifyDataSetChanged();
                        deleteWork(poop);

                    }
                });
        dialogBuilder.setPositiveButton("No", null);
        dialogBuilder.show();
    }

    private void deleteWork(User user){
        ServerRequests serverRequests = new ServerRequests(context);
        serverRequests.deleteUserSkillInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

            }

            @Override
            public void donezo(String string) {

            }
        });
    }
}