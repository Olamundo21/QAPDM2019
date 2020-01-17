package pt.uac.qa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import pt.uac.qa.model.User;
import pt.uac.qa.ui.AddQuestionActivity;

public class MainActivity extends AppCompatActivity {
    public static final String INTENT_FILTER = "pt.uac.qa.MAIN_INTENT_FILTER";

    private Map<String, Object> fragmentState = new HashMap<>();
    private AppBarConfiguration appBarConfiguration;
    private FloatingActionButton fab;

    public void saveState(final String key, final Object value) {
        fragmentState.put(key, value);
    }

    public Object getState(final String key, Object defaultValue) {
        Object value = fragmentState.get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupToolbar();
        setupActionButton();
        setupNavigation();

        displayUserInformation();
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
                R.id.nav_questions, R.id.nav_my_questions, R.id.nav_my_answers)
                .setDrawerLayout(drawer)
                .build();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_logout) {
                    QAApp app = (QAApp) getApplication();
                    app.logout();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                } if (menuItem.getItemId() != R.id.nav_questions) {
                    Log.d("WTF", "not questions fragment");
                    fab.hide();
                } else {
                    Log.d("WTF", "questions fragment");
                    fab.show();
                }

                return false;
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                final int id = destination.getId();

                if (id == R.id.nav_logout) {
                    QAApp app = (QAApp) getApplication();
                    app.logout();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } if (id != R.id.nav_questions) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            sendBroadcast(new Intent(INTENT_FILTER));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupActionButton() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddQuestionActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            sendBroadcast(new Intent(INTENT_FILTER));
            return true;
        }

        return super.onOptionsItemSelected(item);
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
