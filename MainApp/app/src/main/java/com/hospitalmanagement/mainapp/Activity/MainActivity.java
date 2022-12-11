package com.hospitalmanagement.mainapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.hospitalmanagement.mainapp.Fragment.FragmentFamilyDetail;
import com.hospitalmanagement.mainapp.Fragment.FragmentGenInfo;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.Sessions.SessionManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TextView editLoginUsername;
    View navigationHeader;
    Toolbar toolbar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApp();
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentGenInfo()).commit();
            navigationView.setCheckedItem(R.id.nav_add_family);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_add_family:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentGenInfo()).commit();
                navigationView.setCheckedItem(R.id.nav_add_family);
                break;

            case R.id.nav_family_detail:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentFamilyDetail()).commit();
                navigationView.setCheckedItem(R.id.nav_family_detail);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initApp() {
        toolbar = findViewById(R.id.toolbar);
        sessionManager = new SessionManager(MainActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_home);
        navigationView = findViewById(R.id.navigationView);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationHeader = navigationView.getHeaderView(0);
        editLoginUsername = (TextView) navigationHeader.findViewById(R.id.editLoginUsername);
    }

//    private FragmentManager getLastFragmentManager(FragmentManager fragmentManager) {
//
//        FragmentManager lastFragmentMamager = fragmentManager;
//        List<Fragment> fragments = fragmentManager.getFragments();
//
//        for(Fragment fragment : fragments) {
//            if(fragment.getChildFragmentManager() != null  && fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
//                lastFragmentMamager = fragment.getFragmentManager();
//                FragmentManager childFragmentManager = getLastFragmentManager(fragment.getChildFragmentManager());
//
//                if(childFragmentManager != lastFragmentMamager) {
//                    lastFragmentMamager = childFragmentManager;
//                }
//            }
//        }
//        return lastFragmentMamager;
//    }

    private boolean check = false;
    @Override
    public void onBackPressed() {
//
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }


        if (getFragmentName().equals("FragmentFamilyDetail")) {
            if(check) {
                // close
                check = false;
                super.onBackPressed();
            } else {
                check = true;
                changeFragment(new FragmentFamilyDetail());
            }
        } else {
            super.onBackPressed();
        }
//        if(getFragmentName().equals("FragmentFamilyDetailChildInformation") || getFragmentName().equals("FragmentFamilyDetailCoupleInformation") || getFragmentName().equals("FragmentFamilyDetailGeneralInformation") || getFragmentName().equals("FragmentFamilyDetailMemberInformation") || getFragmentName().equals("FragmentFamilyDetailPregnancyInformation")) {
//            changeFragment(null);
//        }
//        if (getFragmentName().equals("FragmentGenInfo")) {
//            return;
//        } else if (getFragmentName().equals("FragmentMem1")) {
//            changeFragment(new FragmentGenInfo());
//        } else if (getFragmentName().equals("FragmentMem2")) {
//            changeFragment(new FragmentMem1());
//        } else if (getFragmentName().equals("FragmentMem3")) {
//            changeFragment(new FragmentMem2());
//        } else if (getFragmentName().equals("FragmentMem4")) {
//            changeFragment(new FragmentMem3());
//        } else if (getFragmentName().equals("FragmentMem5")) {
//            changeFragment(new FragmentMem4());
//        } else if (getFragmentName().equals("FragmentMem6")) {
//            changeFragment(new FragmentMem5());
//        } else if (getFragmentName().equals("FragmentMem7")) {
//            changeFragment(new FragmentMem6());
//        } else if (getFragmentName().equals("FragmentMem8")) {
//            changeFragment(new FragmentMem7());
//        } else if (getFragmentName().equals("FragmentMem9")) {
//            changeFragment(new FragmentMem8());
//        } else if (getFragmentName().equals("FragmentMem10")) {
//            changeFragment(new FragmentMem9());
//        } else if (getFragmentName().equals("FragmentMem11")) {
//            changeFragment(new FragmentMem10());
//        } else if (getFragmentName().equals("FragmentMem12")) {
//            changeFragment(new FragmentMem11());
//        } else if (getFragmentName().equals("FragmentMem13")) {
//            changeFragment(new FragmentMem12());
//        } else if (getFragmentName().equals("FragmentMem14")) {
//            changeFragment(new FragmentMem13());
//        } else if (getFragmentName().equals("FragmentMem15")) {
//            changeFragment(new FragmentMem14());
//        } else if (getFragmentName().equals("FragmentMem16")) {
//            changeFragment(new FragmentMem15());
//        } else if (getFragmentName().equals("FragmentMem17")) {
//            changeFragment(new FragmentMem16());
//        } else if (getFragmentName().equals("FragmentMem18")) {
//            changeFragment(new FragmentMem17());
//        } else if (getFragmentName().equals("FragmentMem19")) {
//            changeFragment(new FragmentMem18());
//        } else if (getFragmentName().equals("FragmentMem20")) {
//            changeFragment(new FragmentMem19());
//        } else if (getFragmentName().equals("FragmentCouple1")) {
//            goToMemberFragment();
//        } else if (getFragmentName().equals("FragmentCouple2")) {
//            changeFragment(new FragmentCouple1());
//        } else if (getFragmentName().equals("FragmentCouple3")) {
//            changeFragment(new FragmentCouple2());
//        } else if (getFragmentName().equals("FragmentCouple4")) {
//            changeFragment(new FragmentCouple3());
//        } else if (getFragmentName().equals("FragmentCouple5")) {
//            changeFragment(new FragmentCouple4());
//        } else if (getFragmentName().equals("FragmentFamilyDetail")) {
//            changeFragment(new FragmentFamilyDetail());
//        } else {
//            super.onBackPressed();
//        }
//
//        FragmentManager fragmentManager = getLastFragmentManager(getSupportFragmentManager());
//
//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                FragmentManager fragmentManager = getLastFragmentManager(getSupportFragmentManager());
//                if(fragmentManager != null) {
//                    int backStackEntryCount = fragmentManager.getBackStackEntryCount();
//                    if(backStackEntryCount != 0) {
//                        Fragment fragment = fragmentManager.getFragments().get(backStackEntryCount - 1);
//                        fragmentManager.popBackStack();
//                        fragment.onResume();
//                    }
//                }
//            }
//        });

//        if(fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.popBackStack();
//            return;
//        }
//
////        super.onBackPressed();
//
    }
    private String getFragmentName() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        String fragmentName = fragment.getClass().getSimpleName();
        return fragmentName;
    }
    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }
}
