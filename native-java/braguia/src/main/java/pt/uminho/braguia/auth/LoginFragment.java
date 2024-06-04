package pt.uminho.braguia.auth;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import dagger.hilt.android.AndroidEntryPoint;
import pt.uminho.braguia.R;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final Button btLogin = view.findViewById(R.id.btn_login);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        final EditText usernameEditText = view.findViewById(R.id.txt_username);
        final EditText passwordEditText = view.findViewById(R.id.txt_password);

        mViewModel.getStatus()
                .observe(getViewLifecycleOwner(), statusData -> {
                    switch (statusData.status) {
                        case INITIAL:
                            btLogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            break;
                        case INVALID_USERNAME:
                            usernameEditText.setError(getString(statusData.messageId));
                            break;
                        case INVALID_PASSWORD:
                            passwordEditText.setError(getString(statusData.messageId));
                            break;
                        case LOGGING_IN:
                            btLogin.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case LOGGED_IN:
                            progressBar.setVisibility(View.GONE);
                            btLogin.setVisibility(View.VISIBLE);
                            NavDirections directions = LoginFragmentDirections.actionLoginFragmentToTrailsFragment();
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(directions);
                            break;
                        case LOGIN_ERROR:
                            progressBar.setVisibility(View.GONE);
                            btLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), statusData.message, Toast.LENGTH_LONG).show();
                            break;
                    }
                });

        btLogin.setOnClickListener((v) -> {
            mViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        });
    }

}