package pt.uminho.braguia.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.R;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    public enum Status {
        INITIAL,
        LOGGING_IN,
        LOGGED_IN,
        LOGIN_ERROR,
        INVALID_USERNAME,
        INVALID_PASSWORD;
    }

    public class StatusData {
        public final Status status;
        public final Integer messageId;
        public final String message;

        public StatusData(Status status) {
            this.status = status;
            this.messageId = null;
            this.message = null;
        }

        public StatusData(Status status, Integer messageId) {
            this.status = status;
            this.messageId = messageId;
            this.message = null;
        }

        public StatusData(Status status, String message) {
            this.status = status;
            this.message = message;
            this.messageId = null;
        }
    }

    private AuthenticationService authenticationService;

    @Inject
    public LoginViewModel(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private final MutableLiveData<StatusData> status = new MutableLiveData<>(new StatusData(Status.INITIAL));

    public LiveData<StatusData> getStatus() {
        return status;
    }

    public void login(String username, String password) {

        if (username == null || username.isEmpty()) {
            status.setValue(new StatusData(Status.INVALID_USERNAME, R.string.invalid_username));
            return;
        }

        if (password == null || password.isEmpty()) {
            status.setValue(new StatusData(Status.INVALID_PASSWORD, R.string.invalid_password));
            return;
        }

        status.setValue(new StatusData(Status.LOGGING_IN));
        authenticationService.login(username, password, (result) -> {
            if (result.isError()) {
                status.setValue(new StatusData(Status.LOGIN_ERROR, result.getError()));
            } else {
                status.setValue(new StatusData(Status.LOGGED_IN));
            }
        });
    }
}