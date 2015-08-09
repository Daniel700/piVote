package piv.pivote;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import fragments.FragmentMyPolls;
import fragments.FragmentQuestionList;
import fragments.FragmentRecentlyVoted;


public class LauncherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //createSplashScreen();

        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set the menu icon instead of the launcher icon. (Not needed when you use ActionBarDrawerToggle)
        // final ActionBar ab = getSupportActionBar();
        // ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        // ab.setDisplayHomeAsUpEnabled(true);

        //Apply Listener on NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Animate the Hamburger Icon
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,  R.string.close);
        mDrawerLayout.setDrawerListener(drawerToggle);

        //Handle navigation to according fragments
        String previousActivityName = getIntent().getStringExtra("from");
        if (getResources().getString(R.string.activity_detailed_poll).equals(previousActivityName))
            navigate(navigationView.getMenu().findItem(R.id.nav_all_polls));
        else if (getResources().getString(R.string.activity_create_poll).equals(previousActivityName))
            navigate(navigationView.getMenu().findItem(R.id.nav_my_polls));
        else
            navigate(navigationView.getMenu().findItem(R.id.nav_all_polls));

        //ToDo: Navigation back from detailed poll always redirects to "All Polls"



        /*
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All Polls"));
        tabLayout.addTab(tabLayout.newTab().setText("My Polls"));
        tabLayout.addTab(tabLayout.newTab().setText("Recently"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        */
    }

    private void navigate(final MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_all_polls:
                fragmentClass = FragmentQuestionList.class;
                break;
            case R.id.nav_my_polls:
                fragmentClass = FragmentMyPolls.class;
                break;
            case R.id.nav_recently:
                fragmentClass = FragmentRecentlyVoted.class;
                break;
            default:
                fragmentClass = FragmentQuestionList.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        menuItem.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        navigate(menuItem);

        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();

        //Start snackbar if previous action was submitting a vote
        String snackbarText = getIntent().getStringExtra("snackbar_detailed");
        View coord = findViewById(R.id.snackbarCoordinatorLayout);
        if (snackbarText != null)
            Snackbar.make(coord, "Your Vote has been submitted", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void createSplashScreen(){
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished)
            {
            }
            public void onFinish() {
                //activate main window
                setContentView(R.layout.activity_launcher);
            }
        }.start();
    }

}
