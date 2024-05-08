package pt.uminho.braguia.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;
import pt.uminho.braguia.auth.AuthenticationService;

@AndroidEntryPoint
public class UserActivity extends AppCompatActivity {
    @Inject
    AuthenticationService authenticationService;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        getUser();
    }

    private void getUser() {
        this.user = authenticationService.currentUser();
        refreshData();
    }

    private void refreshData() {

        TextView username = (TextView) findViewById(R.id.username);
        username.append(user.getUsername());

        TextView name = (TextView) findViewById(R.id.fullName);
        String fullName = user.getFirstName() + " " + user.getLastName();
        name.setText(fullName);

        TextView email = (TextView) findViewById(R.id.email);
        email.append(user.getEmail());

        TextView premium = (TextView) findViewById(R.id.isPremium);
        premium.append(user.isPremium() ? "Yes" : "No");

        TextView superuser = (TextView) findViewById(R.id.isSuperuser);
        superuser.append(user.isSuperUser() ? "Yes" : "No");

        TextView staff = (TextView) findViewById(R.id.isStaff);
        staff.append(user.isStaff() ? "Yes" : "No");

        TextView active = (TextView) findViewById(R.id.isActive);
        active.append(user.isActive() ? "Yes" : "No");

        TextView joinDate = (TextView) findViewById(R.id.joinDate);
        SimpleDateFormat dateJoin = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateJoin.format(user.getDateJoined());
        joinDate.append(formattedDate);

        TextView lastLogin = (TextView) findViewById(R.id.lastLogin);
        SimpleDateFormat dateLast = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateLast.format(user.getDateJoined());
        lastLogin.append(date);

    }
}
