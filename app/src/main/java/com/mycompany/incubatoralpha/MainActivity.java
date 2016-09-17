package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nameText, educationText;
    UserLocalStore userLocalStore;
    String fullName;
    ListView edListView, experienceListView;
    GridView skillsGridview;


    String JSON_STRING;

    //For the profile image.
    Uri fileURI;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private  static final int IMAGE_CAPTURE = 102;

    private ImageView theBut;
    Button educationButton, workButton, skillButton;


    LinearLayout layoutview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Name
        nameText = (TextView) findViewById(R.id.nameTextView);
        educationText = (TextView) findViewById(R.id.educationTextView);
        educationButton = (Button) findViewById(R.id.addEducation);
        workButton = (Button) findViewById(R.id.addWork);
        skillButton = (Button) findViewById(R.id.addSkill);
        layoutview = (LinearLayout) findViewById(R.id.layout_1);

        workButton.setOnClickListener(this);
        educationButton.setOnClickListener(this);
        skillButton.setOnClickListener(this);


        userLocalStore = new UserLocalStore(this);

        layoutview.setScrollContainer(false);

        //Education
        edListView = (ListView) findViewById(R.id.edListView);

        //Experience
        experienceListView = (ListView) findViewById(R.id.experienceListView);
        //GridView
        skillsGridview = (GridView) findViewById(R.id.skillsGridView);

        theBut = (ImageView) findViewById(R.id.theImageView);


        //Enables camera usage
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            theBut.setEnabled(true);
        }
        else{
            theBut.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate()){
            User user = userLocalStore.getLoggedInUser();
            getJSON(user);
        }else{
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        }
    }


    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    private void getJSON(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserAttributesInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
            }

            @Override
            public void donezo(String s) {
                userLocalStore.storeUserAttributes(s);
                displayUserDetails();
            }
        });
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();

        fullName = user.fName + " " + user.lName;

        nameText.setText(fullName);

        JSONObject jsonObject;
        ArrayList<HashMap<String,String>> testList = new ArrayList<>();
        List<String> compName = new ArrayList<>();
        List<String> jobT = new ArrayList<>();
        List<String> jobD = new ArrayList<>();

        List<String> schN = new ArrayList<>();
        List<String> maJ = new ArrayList<>();
        List<String> gradY = new ArrayList<>();

        List<String> skill = new ArrayList<>();

        try {
            jsonObject = new JSONObject(userLocalStore.getAttributes());
            JSONArray result = jsonObject.getJSONArray(UserLocalStore.TAG_JSON_ARRAY);
            String companyName,jobTitle,jobDescription,schoolName,major,gradYear,skillName;
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                HashMap<String,String> attributes = new HashMap<>();
                if (jo.has("companyName")) {
                    companyName = jo.getString(UserLocalStore.TAG_companyName);
                    jobTitle = jo.getString(UserLocalStore.TAG_jobTitle);
                    jobDescription = jo.getString(UserLocalStore.TAG_jobDescription);
                    compName.add(companyName);
                    jobT.add(jobTitle);
                    jobD.add(jobDescription);

                }
                if (jo.has("schoolName")) {
                    schoolName = jo.getString(UserLocalStore.TAG_schoolName);
                    major = jo.getString(UserLocalStore.TAG_major);
                    gradYear = jo.getString(UserLocalStore.TAG_gradYear);
                    schN.add(schoolName);
                    maJ.add(major);
                    gradY.add(gradYear);

                }
                if (jo.has("skillName")) {
                    skillName = jo.getString(UserLocalStore.TAG_skillName);
                    skill.add(skillName);
                }
                testList.add(attributes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Custom Adapter for Education
        ArrayAdapter<String> edAdapter = new CustomAdapterEd(this,schN,maJ,gradY,user.uid);

        //Custom Adapter for Work
        ArrayAdapter<String> experienceAdapter = new CustomAdapterWork(this,compName,jobT,jobD,user.uid);

        //Custom Adapter for Skills
        BaseAdapter skillAdapter = new CustomAdapterSkills(this,skill,user.uid);

        edListView.setAdapter(edAdapter);
        experienceListView.setAdapter(experienceAdapter);
        skillsGridview.setAdapter(skillAdapter);
        setListViewHeightBasedOnChildren(edListView);
        setListViewHeightBasedOnChildren(experienceListView);


    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.addEducation:
                startActivity(new Intent(MainActivity.this, EducationActivity.class));
                break;
            case R.id.addWork:
                startActivity(new Intent(MainActivity.this, WorkExperienceActivity.class));
                break;
            case R.id.addSkill:
                startActivity(new Intent(MainActivity.this, SkillActivity.class));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_discover:
                startActivity(new Intent(MainActivity.this, DiscoverActivity.class));
                return true;

            case R.id.action_profile:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;

            case R.id.action_idea:
                startActivity(new Intent(MainActivity.this, ideaActivity.class));
                return true;

            case R.id.action_messenger:
                startActivity(new Intent(MainActivity.this, messangerActivity.class));
                return true;

            case R.id.menuLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent logoutIntent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(logoutIntent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //enables camera usage to change your profile image
    public void changeImage(View v){
        fileURI = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Intent theInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        theInt.putExtra(MediaStore.EXTRA_OUTPUT,fileURI);
        startActivityForResult(theInt, IMAGE_CAPTURE);
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"MyCameraApp");
        if(!myDir.exists()) {
            if (!myDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
            }
        }

        String myTimeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File myMediaFile;
        if(type == MEDIA_TYPE_IMAGE){
            myMediaFile = new File(myDir.getPath() + File.separator + "IMG_" + myTimeStamp + ".jpg");
        }
        else return null;
        return myMediaFile;
    }

    protected void onActivityResult(int requestCode,int resCode, Intent data){

        if (resCode==RESULT_OK) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileURI.getPath(),options);
            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(fileURI.getPath(),options);
            theBut.setImageBitmap(bm);

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }else if (resCode==RESULT_CANCELED){
            Toast.makeText(this,"Canceled",Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this,"There was a problem",Toast.LENGTH_SHORT).show();
    }


        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ArrayAdapter listAdapter = (ArrayAdapter)listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
                }
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }


}
