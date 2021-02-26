package com.ganesh.vendorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.fragments.OutOfStockFragment;
import com.ganesh.vendorapp.fragments.ProductFragment;
import com.ganesh.vendorapp.models.User;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.ganesh.vendorapp.viewmodel.SavedProductViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private SavedProductViewModel savedProductViewModel;
    private BottomNavigationView bottomNavigationView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.nav_view);
        user = UsersSharedPrefManager.getInstance(this).getUser();
        savedProductViewModel = ViewModelProviders.of(this).get(SavedProductViewModel.class);

        getSupportActionBar().setTitle("Welcome " + user.getFullName().split(" ", 2)[0]);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FloatingTextButton addProducts = findViewById(R.id.add_product_item);
        addProducts.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        ProductFragment productFragment = new ProductFragment();
        if (getIntent().getExtras() != null) {
            productFragment.setArguments(getIntent().getExtras());
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container_fragment, productFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment selectedFragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.item_stock:
                        selectedFragment = new OutOfStockFragment();
                        break;

                    case R.id.item_product:
                        selectedFragment = new ProductFragment();
                        break;
                    default:
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, selectedFragment).commit();
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_logout:
                logOut();
                break;

            case R.id.item_help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
            case R.id.item_delete:

            case R.id.item_search:
                break;

            case R.id.item_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        savedProductViewModel.deleteAllLocalDb();
        UsersSharedPrefManager.getInstance(getApplicationContext()).clear();
        signOut();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!UsersSharedPrefManager.getInstance(this).isLoggedIn()) {
            logOut();
        }
    }
}