package com.ganesh.vendorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.fragments.OutOfStockFragment;
import com.ganesh.vendorapp.fragments.ProductFragment;
import com.ganesh.vendorapp.models.User;
import com.ganesh.vendorapp.storage.ProductDbHelper;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity{

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProductDbHelper productDbHelper = new ProductDbHelper(this);
        SQLiteDatabase database = productDbHelper.getReadableDatabase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        User user = UsersSharedPrefManager.getInstance(this).getUser();

        getSupportActionBar().setTitle("Welcome "+ user.getFullName().split(" ",2)[0]);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FloatingActionButton addProducts = findViewById(R.id.add_product_item);
        addProducts.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,AddProductActivity.class);
            startActivity(intent);
        });

        ProductFragment productFragment = new ProductFragment();
        if (getIntent().getExtras() != null) {
            productFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container_fragment,productFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.container_fragment,productFragment).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment selectedFragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.item_stock:
                        selectedFragment = new OutOfStockFragment();
                        break;

                    case R.id.item_product:
                        selectedFragment = new ProductFragment();
                        break;
                    default:
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,selectedFragment).commit();
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.item_logout:
                logOut();
                break;

            case R.id.item_about:
                break;

            case R.id.item_help:
                break;

            case R.id.item_delete:
                break;

            case R.id.item_search:
                break;

            case R.id.item_profile:
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){

        UsersSharedPrefManager.getInstance(getApplicationContext())
                .clear();
        signOut();

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!UsersSharedPrefManager.getInstance(this).isLoggedIn()){
            logOut();
        }

    }
}