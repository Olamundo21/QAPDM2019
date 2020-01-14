package pt.uac.qa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import pt.uac.qa.client.AccessTokenClient;
import pt.uac.qa.client.ClientException;
import pt.uac.qa.client.UserClient;
import pt.uac.qa.model.AccessToken;
import pt.uac.qa.model.User;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupToolbar();
        setupActionButton();
        setupNavigation();

        login();

        //displayUserInformation();
    }

    @SuppressLint("StaticFieldLeak")
    private void login() {
        new AsyncTask<Void, Void, Void>() {
            private Throwable error;

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final AccessTokenClient accessTokenClient = new AccessTokenClient(MainActivity.this);
                    final AccessToken accessToken = accessTokenClient.getAccessToken("jane.doe@example.com", "HLoZ6r1LzID8^G");
                    final QAApp app = (QAApp) getApplication();

                    app.setAccessToken(accessToken);

                    final UserClient userClient = new UserClient(MainActivity.this);
                    final User user = userClient.getUser();

                    app.setUser(user);
                } catch (ClientException e) {
                    Log.e(MainActivity.class.getSimpleName(), null, e);
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (error != null) {
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else {
                    displayUserInformation();
                }
            }
        }.execute();
    }

    private void displayUserInformation() {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final TextView usernameView = navigationView.getHeaderView(0).findViewById(R.id.usernameView);
        final TextView emailView = navigationView.getHeaderView(0).findViewById(R.id.emailView);
        final QAApp app = (QAApp) getApplication();
        final User user = app.getUser();

        usernameView.setText(user.getName());
        emailView.setText(user.getEmail());
    }

    private void setupNavigation() {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_questions, R.id.nav_my_questions, R.id.nav_my_answers, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_logout){
                QAApp app = (QAApp) getApplication();
                app.logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }
                return false;
        }
    });

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    private void setupActionButton() {
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, AddQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
