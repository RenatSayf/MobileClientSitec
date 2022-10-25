package com.renatsayf.mobileclientsitec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import dagger.hilt.android.AndroidEntryPoint;

import android.os.Bundle;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null)
        {
            NavController navController = navHostFragment.getNavController();
            navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
                int id = navDestination.getId();
                if (id == R.id.usersFragment) {
                    setTitle(getString(R.string.text_start_screen));
                }
                else if (id == R.id.resultFragment) {
                    setTitle(getString(R.string.text_result_screen));
                }
            });
        }
    }
}