package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelRest;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.User;
import com.example.hadar.trempyteam.Model.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.hadar.trempyteam.R.mipmap.join_icon;

public class ListTrempActivity extends Activity {

    String detailsSet = "";
    List<Tremp> trempsList ;
    Boolean check = false;
    final TrempsAdapter adapter = new TrempsAdapter();
     int index_choosen_tremp = 0;

    MenuItem myMenu;
    public static final int  REQUEST_CODE_ASK_PERMISSIONS = 1;
    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();
    String TAG = MapsActivity.class.getSimpleName();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
     //ProgressDialog pd = new ProgressDialog(getApplicationContext());
   public ProgressDialog pd ;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_list_tremps);

        pd = new ProgressDialog(ListTrempActivity.this);
        final ActionBar actionBar = this.getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#212121"));
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        actionBar.setBackgroundDrawable(colorDrawable);
        getActionBar().setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));



        mNavItems.add(new NavItem("טרמפים שיצרתי", "טרמפים שיצרתי", R.mipmap.my));
        mNavItems.add(new NavItem("טרמפים שהצטרפתי אליהם", "טרמפים שהצטרפתי אליהם", join_icon));
        mNavItems.add(new NavItem("צור טרמפ", "צור טרמפ", R.mipmap.add));
        mNavItems.add(new NavItem("התנתק", "התנתק", R.mipmap.logout));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout1);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane1);
        mDrawerList = (ListView) findViewById(R.id.navList1);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


       // ProgressDialog progressdialog;

        String cameFrom = (String) getIntent().getExtras().get("cameFrom");
        if (cameFrom != null && cameFrom.equals("personalArea")) {
            String isCreated = (String) getIntent().getExtras().get("isCreated");
            ModelSql modelSql = ModelSql.getInstance();
            ModelRest modelRest = ModelRest.getInstance();


            Intent intent = getIntent();

            if (intent.getExtras().getString("isCreated").equals("true")) {

                trempsList = modelRest.getTremps(User.GetAppUser().Id);

                if (trempsList.size() == 0)
                {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ListTrempActivity.this);
                    dlgAlert.setMessage("לא יצרת טרמפים");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Intent intent = new Intent(ListTrempActivity.this, MainAactivity.class);
                            startActivity(intent);
                        }
                    });
                    dlgAlert.show();
                }

            }
            else
            {
                trempsList = modelRest.getTrempsJoined(User.GetAppUser().Id);

                if (trempsList.size() == 0)
                {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ListTrempActivity.this);
                    dlgAlert.setMessage("לא הצטרפת לאף טרמפ");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Intent intent = new Intent(ListTrempActivity.this, MainAactivity.class);
                            startActivity(intent);
                        }
                    });
                    dlgAlert.show();
                }
            }


            CreateList();

            detailsSet = "personalArea";
        }
        else
        {
            final String dest = (String) getIntent().getExtras().get("dest");
            final String from = (String) getIntent().getExtras().get("from");
            final String date = (String) getIntent().getExtras().get("date");
            final String time = (String) getIntent().getExtras().get("time");

            ModelFirebase fbModel = new ModelFirebase();

            ModelRest modelRest = ModelRest.getInstance();

            trempsList = modelRest.getTremps(user_connected_id, Utils.getLocationFromAddress(ListTrempActivity.this, from), Utils.getLocationFromAddress(ListTrempActivity.this, dest), date + "T" + time);


            if (trempsList.size() == 0)
            {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ListTrempActivity.this);
                dlgAlert.setMessage("אין טרמפים העונים לחיפוש שביקשת");
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Intent intent = new Intent(ListTrempActivity.this, MainAactivity.class);
                        startActivity(intent);
                    }
                });
                dlgAlert.show();
            }
                    CreateList();

            detailsSet = "Search";
       }

        try {

            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/" + user_connected_id,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                TextView user_name = (TextView) findViewById(R.id.userName1);
                                user_name.setText(response.getJSONObject().getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).executeAsync();

        }
        catch (Exception e){
            Log.d("exception", "can't get user name " + e.getMessage());
        }


        //progressdialog.dismiss();
    }

    public void CreateList()
    {

        ListView list = (ListView) findViewById(R.id.Tremps_listView1);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                index_choosen_tremp =  i;
                Tremp tremp =  trempsList.get(i);
                Utils.currentChosenTremp = tremp;
                String source = "", dest="";

                if(tremp.getSource() != null)
                    source = Utils.getAddressFromLocation(ListTrempActivity.this, tremp.getSource());

                if(tremp.getDest() != null)
                    dest = Utils.getAddressFromLocation(ListTrempActivity.this, tremp.getDest());



                Intent intent = new Intent(ListTrempActivity.this, TrempDetailsActivity.class);
                intent.putExtra("id",  tremp.getId());
                intent.putExtra("index",  String.valueOf(index_choosen_tremp));
                intent.putExtra("phone",  tremp.getPhoneNumber());
                intent.putExtra("source",  source);
                intent.putExtra("dest",  dest);
                intent.putExtra("seets",  tremp.getSeets());
                intent.putExtra("car",  tremp.getCarModel());
                intent.putExtra("image",  tremp.getImageName());
                intent.putExtra("driverId",  tremp.getDriverId());
                intent.putExtra("date",  tremp.getTrempDateTime());
                intent.putExtra("cameFrom", detailsSet);

                startActivityForResult(intent, 1);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TAG", "handle action bar");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buttons, menu);

        View view = (View) LayoutInflater.from(getBaseContext() ).inflate(R.layout.check, null);
        com.example.hadar.trempyteam.ProfilePictureView editText =  (com.example.hadar.trempyteam.ProfilePictureView) view.findViewById(R.id.friendProfilePicture);
        editText.setProfileId(user_connected_id);

        MenuItem personalArea =  menu.findItem(R.id.personalArea);
        personalArea.setVisible(true);
        personalArea.setActionView(view);

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            ModelSql modelSql = ModelSql.getInstance();
            trempsList = modelSql.getAllTremps(true);

            CreateList();
        }

        if(resultCode == Activity.RESULT_FIRST_USER){
            Log.d("gggggggg", String.valueOf(index_choosen_tremp));

            Intent intent = new Intent(ListTrempActivity.this, MainAactivity.class);
            startActivity(intent);

        }

        if(resultCode == Activity.RESULT_CANCELED){


            Intent intent = new Intent(ListTrempActivity.this, MainAactivity.class);
            startActivity(intent);

        }

    }
    class TrempsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return trempsList.size();
        }

        @Override
        public Object getItem(int i) {

            return trempsList.get(i);
        }

        @Override
        public long getItemId(int i) {

            return i;
        }

        @Override
        public View  getView(final int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.tremp_list_raw, null);
            }
            final TextView name = (TextView) view.findViewById(R.id.DriverName);
            //final ImageView image = (ImageView) view.findViewById(R.id.android);
            final com.example.hadar.trempyteam.ProfilePictureView profilePictureView = (com.example.hadar.trempyteam.ProfilePictureView) view.findViewById(R.id.friendProfilePicture);
            final TextView time = (TextView) view.findViewById(R.id.TrempExitTime);
            final TextView seats = (TextView) view.findViewById(R.id.ava_seats);
            final Tremp st = trempsList.get(i);
            final String driver_id = st.getDriverId();
            final String avilableSeats = String.valueOf(st.getSeets());
              seats.setText(avilableSeats + " מקומות פנויים ");

            String dateTime = st.getTrempDateTime();
            String newTime = "";
            String newDate = "";
                String[] splitDate = dateTime.split(" ");
                newDate = splitDate[0];
                newTime = splitDate[1];

                String[] splitTime = newTime.split(":");


                if (splitTime[1].length() < 2)
                {
                    newTime = splitTime[0] + ":0" + splitTime[1];
                }
            else
                {
                    newTime = splitTime[0] + ":" + splitTime[1];
                }


               time.setText(newDate + "  " + newTime);
               profilePictureView.setProfileId(driver_id);

            try {

                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + driver_id,
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                try {
                                    name.setText(response.getJSONObject().getString("name"));
                                    } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).executeAsync();

            }
            catch (Exception e){
                Log.d("exception", "can't get user name " + e.getMessage());
            }


            return view;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    /*
* Called when a particular item from the navigation drawer
* is selected.
* */
    private void selectItemFromDrawer(int position) {

        if (position == 0)
        {
            Intent intent = new Intent(getBaseContext(), ListTrempActivity.class);
            intent.putExtra("cameFrom","personalArea");
            intent.putExtra("isCreated", "true");

            pd.setMessage("מחפש טרמפים שיצרת ...");
            pd.show();

            startActivity(intent);
        }
        else if (position == 1)
        {
            Intent intent = new Intent(getBaseContext(), ListTrempActivity.class);
            intent.putExtra("cameFrom","personalArea");
            intent.putExtra("isCreated", "false");

            pd.setMessage("מחפש טרמפים שהצטרפת אליהם ...");
            pd.show();
            startActivity(intent);

        }
        // Create New Tremp
        else if (position == 2)
        {
            Intent intent = new Intent(ListTrempActivity.this, CreateNewTrempActivity.class);
            startActivity(intent);
        }
        // Logout
        else
        {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ListTrempActivity.this, LoginActivity.class);
            startActivity(intent);

        }

       /* FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();*/

        mDrawerList.setItemChecked(position, true);
        // setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            }
            return;
        }
    }


    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }


    class DrawerListAdapter extends BaseAdapter  {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }
            TextView titleView = (TextView) view.findViewById(R.id.title);
            // TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);


            titleView.setText( mNavItems.get(position).mTitle );

            //  subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            View vieww = (View) LayoutInflater.from(getBaseContext() ).inflate(R.layout.check, null);
            com.example.hadar.trempyteam.ProfilePictureView d =  (com.example.hadar.trempyteam.ProfilePictureView) findViewById(R.id.profile1);
            d.setProfileId(user_connected_id);



            return view;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/" + user_connected_id,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                TextView user_name = (TextView) findViewById(R.id.userName1);
                                user_name.setText(response.getJSONObject().getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).executeAsync();
        }
        catch (Exception e){
            Log.d("exception", "can't get user name " + e.getMessage());
        }
    }
    @Override
    public void onBackPressed() {

pd.dismiss();
        Intent intent = new Intent(ListTrempActivity.this, MainAactivity.class);
        startActivity(intent);


    }





}
