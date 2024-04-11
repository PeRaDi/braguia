package pt.uminho.braguia.services;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class ServiceContainer {
    private static ServiceContainer instance;
    private final HashMap<Class<? extends IBraGuiaService>, IBraGuiaService> serviceContainer;
    private final Context context;

    private ServiceContainer(Context context) {
        instance = this;
        this.context = context;
        serviceContainer = new HashMap<>();
    }

    public static synchronized ServiceContainer getInstance(Context context) {
        if (instance == null) {
            instance = new ServiceContainer(context);
        }
        return instance;
    }

    public synchronized <T extends IBraGuiaService> void add(Class<T> serviceClass) {
        try {
            if (!serviceContainer.containsKey(serviceClass)) {
                T serviceInstance = serviceClass.getConstructor(Context.class).newInstance(context);
                serviceContainer.put(serviceClass, serviceInstance);

                serviceInstance.onServiceConstructed();
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T extends IBraGuiaService> T getService(Class<T> serviceClass) {
        IBraGuiaService service = serviceContainer.get(serviceClass);
        return service == null ? null : (T) service;
    }
}
