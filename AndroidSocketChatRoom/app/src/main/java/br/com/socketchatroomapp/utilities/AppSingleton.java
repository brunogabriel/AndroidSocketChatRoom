package br.com.socketchatroomapp.utilities;

/**
 * Created by brunogabriel on 8/8/16.
 */
public class AppSingleton {

    private static AppSingleton instance;

    private AppSingleton() {
        // Empty constructor
    }

    public static AppSingleton getInstance() {
        if (instance == null) {
            instance = new AppSingleton();
        }

        return instance;
    }
}

