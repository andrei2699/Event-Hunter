package com.example.eventhunter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventhunter.authentication.AuthenticationActivity;
import com.example.eventhunter.authentication.AuthenticationService;
import com.example.eventhunter.authentication.FirebaseAuthenticationService;
import com.example.eventhunter.collaborator.service.CollaboratorService;
import com.example.eventhunter.collaborator.service.MockCollaboratorService;
import com.example.eventhunter.di.ServiceLocator;
import com.example.eventhunter.events.service.EventService;
import com.example.eventhunter.events.service.FirebaseEventService;
import com.example.eventhunter.profile.service.CollaboratorProfileService;
import com.example.eventhunter.profile.service.FirebaseProfileService;
import com.example.eventhunter.profile.service.OrganizerProfileService;
import com.example.eventhunter.profile.service.RegularUserProfileService;
import com.example.eventhunter.repository.PhotoManager;
import com.example.eventhunter.repository.PhotoRepository;
import com.example.eventhunter.repository.impl.FirebaseRepositoryImpl;
import com.example.eventhunter.repository.impl.FirestorageRepositoryImpl;
import com.example.eventhunter.utils.photoUpload.FileUtil;
import com.example.eventhunter.utils.photoUpload.PhotoUploadService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PhotoUploadService {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private AuthenticationService authenticationService;

    private static final int START_AUTH_ACTIVITY_REQUEST_CODE = 1033;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final int PICK_IMAGE_FROM_GALLERY_REQUEST_CODE = 1035;

    public static final String AUTH_ACTIVITY_REQUEST_EXTRA = "MESS";

    private Consumer<Bitmap> onImageTakenConsumer;
    private Consumer<Bitmap> onImageSelectedConsumer;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerDependencyInjection();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
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

        if (!authenticationService.isLoggedIn()) {
            startAuthActivity();
        } else {
            View headerView = navigationView.getHeaderView(0);

            authenticationService.getLoggedUserData(loggedUserData -> {
                TextView drawerUserEmailTextView = headerView.findViewById(R.id.drawerUserEmailTextView);
                drawerUserEmailTextView.setText(loggedUserData.email);

                TextView drawerUserNameTextView = headerView.findViewById(R.id.drawerUserNameTextView);
                drawerUserNameTextView.setText(loggedUserData.name);
            });

            authenticationService.getProfilePhoto(bitmap -> {
                ImageView drawerProfilePhotoImageView = headerView.findViewById(R.id.drawerProfilePhotoImageView);

                if (bitmap != null) {
                    drawerProfilePhotoImageView.setImageBitmap(bitmap);
                }
            });
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

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
                if (onImageTakenConsumer != null) {
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            Bitmap takenImage = (Bitmap) data.getExtras().get("data");
                            onImageTakenConsumer.accept(takenImage);
                        } else {
                            onImageTakenConsumer.accept(null);
                        }
                    } else {
                        onImageTakenConsumer.accept(null);
                        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case PICK_IMAGE_FROM_GALLERY_REQUEST_CODE: {
                if (onImageSelectedConsumer != null) {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data == null) {
                            onImageSelectedConsumer.accept(null);
                        } else {
                            try {
                                Uri selectedImageUri = data.getData();
                                onImageSelectedConsumer.accept(FileUtil.bitmapFrom(this, selectedImageUri));
                            } catch (Exception e) {
                                onImageSelectedConsumer.accept(null);
                                e.printStackTrace();
                            }
                        }
                    } else {
                        onImageSelectedConsumer.accept(null);
                        Toast.makeText(this, "Picture not selected!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home_events) {
            if (navController.getCurrentDestination().getId() != R.id.nav_home_events) {
                navController.navigate(R.id.nav_home_events);
            }
            drawer.close();
        }

        if (item.getItemId() == R.id.nav_home_collaborators) {
            if (navController.getCurrentDestination().getId() != R.id.nav_home_collaborators) {
                navController.navigate(R.id.nav_home_collaborators);
            }
            drawer.close();
        }

        if (item.getItemId() == R.id.nav_home_organizers) {
            if (navController.getCurrentDestination().getId() != R.id.nav_home_organizers) {
                navController.navigate(R.id.nav_home_organizers);
            }
            drawer.close();
        }

        if (item.getItemId() == R.id.nav_profile) {
            authenticationService.getLoggedUserData(loggedUserData -> {
                Bundle bundle = new Bundle();
                String userId = loggedUserData.id;
                String userType = loggedUserData.userType;

                switch (userType) {
                    case "Organizer": {
                        bundle.putString("organizerId", userId);
                        navController.navigate(R.id.nav_organizerProfile, bundle);
                    }
                    break;

                    case "Collaborator": {
                        bundle.putString("collaboratorId", userId);
                        navController.navigate(R.id.nav_collaborator_profile_fragment, bundle);
                    }
                    break;

                    default: {
                        bundle.putString("regularUserId", userId);
                        navController.navigate(R.id.nav_regular_user_profile_page, bundle);
                    }
                    break;
                }
            });

            drawer.close();
        }

        if (item.getItemId() == R.id.nav_logout) {
            authenticationService.logout();
            drawer.close();
            startAuthActivity();
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceLocator.getInstance().dispose();
        PhotoManager.getInstance().dispose();
    }

    @Override
    public void launchCamera(Consumer<Bitmap> onImageTaken) {
        this.onImageTakenConsumer = onImageTaken;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openGallery(Consumer<Bitmap> onImageSelected) {
        this.onImageSelectedConsumer = onImageSelected;

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY_REQUEST_CODE);
    }

    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivityForResult(intent, START_AUTH_ACTIVITY_REQUEST_CODE);
    }

    private void registerDependencyInjection() {
        PhotoRepository photoRepository = new FirestorageRepositoryImpl();
        FirebaseProfileService firebaseProfileService = new FirebaseProfileService(photoRepository, new FirebaseRepositoryImpl<>(), new FirebaseRepositoryImpl<>(), new FirebaseRepositoryImpl<>(), new FirebaseRepositoryImpl<>(), new FirebaseRepositoryImpl<>());
        authenticationService = new FirebaseAuthenticationService(new FirebaseRepositoryImpl<>(), firebaseProfileService);

        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        serviceLocator.register(AuthenticationService.class, authenticationService);
        serviceLocator.register(CollaboratorService.class, new MockCollaboratorService());
        serviceLocator.register(EventService.class, new FirebaseEventService(new FirebaseRepositoryImpl<>(), photoRepository));
        serviceLocator.register(PhotoUploadService.class, this);

        serviceLocator.register(CollaboratorProfileService.class, firebaseProfileService);
        serviceLocator.register(OrganizerProfileService.class, firebaseProfileService);
        serviceLocator.register(RegularUserProfileService.class, firebaseProfileService);
    }
}