package pt.uminho.braguia.auth;


import androidx.lifecycle.LiveData;

import pt.uminho.braguia.user.User;
import pt.uminho.braguia.util.ResultConsumer;

public interface AuthenticationService {

    class AuthInfo {
        public final User user;
        public final Boolean authenticated;

        public AuthInfo(User user, Boolean authenticated) {
            this.user = user;
            this.authenticated = authenticated;
        }
    }

    LiveData<AuthInfo> authInfo();

    User currentUser();

    boolean isAuthenticated();

    void login(String username, String password, ResultConsumer<User> result);

    void logout();

}
