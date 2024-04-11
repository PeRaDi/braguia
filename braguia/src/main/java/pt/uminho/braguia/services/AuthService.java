package pt.uminho.braguia.services;

import android.content.Context;

public class AuthService implements IBraGuiaService {

    private Context context;
    private EncryptedSharedPreferencesService sharedPreferencesService;

    public AuthService(Context context) {
        this.context = context;
    }

    @Override
    public void onServiceConstructed() {
        sharedPreferencesService = ServiceContainer.getInstance(context).getService(EncryptedSharedPreferencesService.class);
    }

    public boolean isAuthenticated() {
        String token = sharedPreferencesService.getSharedPreferences().getString("token", null);

        if (token == null)
            return false;

        return true;
    }
}
