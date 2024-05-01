package pt.uminho.braguia.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.auth.AuthenticationService;

@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private AuthenticationService authenticationService;


    public enum Status {
        LOGGED_IN,
        LOGGED_OUT;
    }

    private MutableLiveData<Status> status;

    @Inject
    public SettingsViewModel(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        if (authenticationService.isAuthenticated()) {
            status = new MutableLiveData<>(Status.LOGGED_IN);
        } else {
            status = new MutableLiveData<>(Status.LOGGED_OUT);
        }
    }

    public LiveData<Status> getStatus() {
        return status;
    }

    public boolean isAuthenticated() {
        return authenticationService.isAuthenticated();
    }

    public void logout() {
        this.authenticationService.logout();
        this.status.setValue(Status.LOGGED_OUT);
    }

}
