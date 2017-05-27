package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.User;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.hadar.trempyteam.R.mipmap.join_icon;

public class MainAactivity extends Activity {
    FragmentManager fragmentManager;
    final int main = 1;
    public static final int  REQUEST_CODE_ASK_PERMISSIONS = 1;
    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();
    String TAG = MapsActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        /*Intent intent = new Intent(MainAactivity.this, Main2Activity.class);
        startActivityForResult(intent, main);*/

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        fragmentManager = getFragmentManager();

        searchTrempFragment frg = new searchTrempFragment();
        FragmentTransaction  transaction=fragmentManager.beginTransaction();//create an instance of Fragment-transaction

        transaction.add(R.id.My_Container_1_ID, frg, "Frag_Top_tag");

        transaction.commit();


            mNavItems.add(new NavItem("טרמפים שיצרתי", "Meetup destination", R.mipmap.my));
            mNavItems.add(new NavItem("טרמפים שהצטרפתי אליהם", "Change your preferences", join_icon));
            mNavItems.add(new NavItem("צור טרמפ", "Change your preferences", R.mipmap.add));
            mNavItems.add(new NavItem("התנתק", "Get to know about us", R.mipmap.logout));

            // DrawerLayout
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

            // Populate the Navigtion Drawer with options
            mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
            mDrawerList = (ListView) findViewById(R.id.navList);
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

            startActivity(intent);
        }
        else if (position == 1)
        {
            Intent intent = new Intent(getBaseContext(), ListTrempActivity.class);
            intent.putExtra("cameFrom","personalArea");
            intent.putExtra("isCreated", "false");

            startActivity(intent);

        }
        // Create New Tremp
        else if (position == 2)
        {
            Intent intent = new Intent(MainAactivity.this, CreateNewTrempActivity.class);
            startActivity(intent);
        }
        // Logout
        else
        {
            LoginManager.getInstance().logOut();
            finish();
            Intent intent = new Intent(MainAactivity.this, LoginActivity.class);
            startActivity(intent);

        }

       /* FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();*/

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

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
            //TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
          //  subtitleView.setText( mNavItems.get(position).mSubtitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

                View vieww = (View) LayoutInflater.from(getBaseContext() ).inflate(R.layout.check, null);
                com.example.hadar.trempyteam.ProfilePictureView d =  (com.example.hadar.trempyteam.ProfilePictureView) findViewById(R.id.profile);
                d.setProfileId(user_connected_id);

            return view;
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
