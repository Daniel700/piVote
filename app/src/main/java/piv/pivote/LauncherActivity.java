package piv.pivote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import fragments.FragmentAbout;
import fragments.FragmentMyPolls;
import fragments.FragmentQuestionList;
import fragments.FragmentRecentlyVoted;
import fragments.FragmentTopPolls;


public class LauncherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyInstallation();

        setContentView(R.layout.activity_launcher);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Apply Listener on NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Animate the Hamburger Icon
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,  R.string.close);
        mDrawerLayout.setDrawerListener(drawerToggle);

        //Navigate to the initial fragment
        navigate(navigationView.getMenu().findItem(R.id.nav_all_polls));



    }

    private void navigate(final MenuItem menuItem) {

        Fragment fragment = null;
/*
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
            case R.id.nav_top_polls:
                fragmentClass = FragmentTopPolls.class;
                break;
            case R.id.nav_about:
                fragmentClass = FragmentAbout.class;
                break;
            default:
                fragmentClass = FragmentQuestionList.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.windowError), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment, fragmentClass.getName()).commit();
        */

        //ToDo: Error sometimes: "java.lang.NullPointerException: Attempt to write to field 'int android.support.v4.app.Fragment.mNextAnim' on a null object reference" ------- Tritt auf bei Reihenfolge: Start -> Filter -> myPolls
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(menuItem.getItemId()){
            case R.id.nav_all_polls:
                    if (fragmentManager.getFragments() != null) {
                        for (Fragment fra: fragmentManager.getFragments()) {
                            fragmentManager.beginTransaction().detach(fra).commit();
                        }
                    }
                    if (fragmentManager.findFragmentByTag(FragmentQuestionList.class.getName()) == null){
                        fragment = new FragmentQuestionList();
                        fragmentManager.beginTransaction().add(R.id.content, fragment, FragmentQuestionList.class.getName()).commit();
                    }
                    else {
                        fragment = fragmentManager.findFragmentByTag(FragmentQuestionList.class.getName());
                        fragmentManager.beginTransaction().attach(fragment).commit();
                    }
                break;
            case R.id.nav_my_polls:
                    if (fragmentManager.getFragments() != null) {
                        for (Fragment fra: fragmentManager.getFragments()) {
                            fragmentManager.beginTransaction().detach(fra).commit();
                        }
                    }
                    if (fragmentManager.findFragmentByTag(FragmentMyPolls.class.getName()) == null){
                        fragment = new FragmentMyPolls();
                        fragmentManager.beginTransaction().add(R.id.content, fragment, FragmentMyPolls.class.getName()).commit();
                    }
                    else {
                        fragment = fragmentManager.findFragmentByTag(FragmentMyPolls.class.getName());
                        fragmentManager.beginTransaction().attach(fragment).commit();
                    }
                break;
            case R.id.nav_recently:
                    if (fragmentManager.getFragments() != null) {
                        for (Fragment fra: fragmentManager.getFragments()) {
                            fragmentManager.beginTransaction().detach(fra).commit();
                        }
                    }
                    if (fragmentManager.findFragmentByTag(FragmentRecentlyVoted.class.getName()) == null){
                        fragment = new FragmentRecentlyVoted();
                        fragmentManager.beginTransaction().add(R.id.content, fragment, FragmentRecentlyVoted.class.getName()).commit();
                    }
                    else {
                        fragment = fragmentManager.findFragmentByTag(FragmentRecentlyVoted.class.getName());
                        fragmentManager.beginTransaction().attach(fragment).commit();
                    }
                break;
            case R.id.nav_top_polls:
                    if (fragmentManager.getFragments() != null) {
                        for (Fragment fra: fragmentManager.getFragments()) {
                            fragmentManager.beginTransaction().detach(fra).commit();
                        }
                    }
                    if (fragmentManager.findFragmentByTag(FragmentTopPolls.class.getName()) == null){
                        fragment = new FragmentTopPolls();
                        fragmentManager.beginTransaction().add(R.id.content, fragment, FragmentTopPolls.class.getName()).commit();
                    }
                    else {
                        fragment = fragmentManager.findFragmentByTag(FragmentTopPolls.class.getName());
                        fragmentManager.beginTransaction().attach(fragment).commit();
                    }
                break;
            case R.id.nav_about:
                    if (fragmentManager.getFragments() != null) {
                        for (Fragment fra: fragmentManager.getFragments()) {
                            fragmentManager.beginTransaction().detach(fra).commit();
                        }
                    }
                    if (fragmentManager.findFragmentByTag(FragmentAbout.class.getName()) == null){
                        fragment = new FragmentAbout();
                        fragmentManager.beginTransaction().add(R.id.content, fragment, FragmentAbout.class.getName()).commit();
                    }
                    else {
                        fragment = fragmentManager.findFragmentByTag(FragmentAbout.class.getName());
                        fragmentManager.beginTransaction().attach(fragment).commit();
                    }
                break;
            default:
                if (fragmentManager.getFragments() != null) {
                    for (Fragment fra: fragmentManager.getFragments()) {
                        fragmentManager.beginTransaction().detach(fra).commit();
                    }
                }
                if (fragmentManager.findFragmentByTag(FragmentQuestionList.class.getName()) == null){
                    fragment = new FragmentQuestionList();
                    fragmentManager.beginTransaction().add(R.id.content, fragment, FragmentQuestionList.class.getName()).commit();
                }
                else {
                    fragment = fragmentManager.findFragmentByTag(FragmentQuestionList.class.getName());
                    fragmentManager.beginTransaction().attach(fragment).commit();
                }
        }


        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        View coordinatorLayout = null;

        //Start snackbar if previous action was submitting a vote
        if (data != null && data.getExtras() != null) {
            String snackbarText = data.getStringExtra("snackbarDetailed");

            if (requestCode == 100) {
                coordinatorLayout = findViewById(R.id.coordinatorLayoutQuestionList);
            }
            if (requestCode == 200) {
                coordinatorLayout = findViewById(R.id.coordinatorLayoutMyPolls);
            }
            if (requestCode == 400) {
                coordinatorLayout = findViewById(R.id.coordinatorLayoutTopPolls);
            }
            if (snackbarText != null && coordinatorLayout != null)
                Snackbar.make(coordinatorLayout, getString(R.string.voteSubmit1) + " " + snackbarText + " " + getString(R.string.voteSubmit2), Snackbar.LENGTH_LONG).show();
        }
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



    public void verifyInstallation(){

        boolean uuid_exists;

        SharedPreferences sharedPreferences = getSharedPreferences("InstallSettings", MODE_PRIVATE);
        uuid_exists = sharedPreferences.getBoolean("UUID_Exists", false);

        //Create UUID if it doesn't exist yet
        if (!uuid_exists) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String uuid = java.util.UUID.randomUUID().toString();
            editor.putString("UUID", uuid);
            editor.putBoolean("UUID_Exists", true);
            editor.apply();
        }

    }

}
