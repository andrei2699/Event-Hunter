package com.example.eventhunter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.eventhunter.authentication.AuthenticationActivity;
import com.example.eventhunter.authentication.AuthenticationService;
import com.example.eventhunter.authentication.FirebaseAuthenticationService;
import com.example.eventhunter.collaborator.service.CollaboratorService;
import com.example.eventhunter.collaborator.service.MockCollaboratorService;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.events.service.FirebaseEventService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private AuthenticationService authenticationService;

    private static final int START_AUTH_ACTIVITY_REQUEST_CODE = 2;

    public static final String AUTH_ACTIVITY_REQUEST_EXTRA = "MESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerDependencyInjection();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_events, R.id.nav_home_organizers, R.id.nav_home_collaborators, R.id.create_event_form_navigation)
                .setOpenableLayout(drawer)
                .build();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_home_events || destination.getId() == R.id.nav_home_organizers || destination.getId() == R.id.nav_home_collaborators) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            navController.navigate(R.id.create_event_form_navigation);
        });

        if (!authenticationService.isLoggedIn()) {
            startAuthActivity();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case START_AUTH_ACTIVITY_REQUEST_CODE: {
                if (data != null && data.hasExtra(AUTH_ACTIVITY_REQUEST_EXTRA)) {
                    String mess = data.getStringExtra(AUTH_ACTIVITY_REQUEST_EXTRA);

                } else {
                    if (!authenticationService.isLoggedIn()) {
                        startAuthActivity();
                    }
                }
            }
            break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            navController.navigate(R.id.nav_organizerProfile);
        }

        if (item.getItemId() == R.id.nav_logout) {
            authenticationService.logout();
            startAuthActivity();
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceLocator.getInstance().dispose();
    }

    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivityForResult(intent, START_AUTH_ACTIVITY_REQUEST_CODE);
    }

    private void registerDependencyInjection() {
        authenticationService = new FirebaseAuthenticationService(this);
        ServiceLocator.getInstance().register(AuthenticationService.class, authenticationService);
        ServiceLocator.getInstance().register(CollaboratorService.class, new MockCollaboratorService());
        ServiceLocator.getInstance().register(EventService.class, new FirebaseEventService(this));
    }
}