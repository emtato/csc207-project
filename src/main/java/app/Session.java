package app;/**
 * Created by Emilia on 2025-08-02!
 * Description:
 * ^ • ω • ^
 */

import data_access.DBUserDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;

/**
 * Class to store the current logged in user (username)
 */

public class Session {
    private static String currentUsername = "example_user";
    private static Account currentAccount;
    private static UserDataAccessObject userDataAccessObject;

    public static void setCurrentUsername(String username) {
        System.out.println("Current username is: " + username);
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setUserDataAccessObject(UserDataAccessObject userDataAccessObject) {
        Session.userDataAccessObject = userDataAccessObject;
    }

    public static void setCurrentAccount() {
        currentAccount = (Account) userDataAccessObject.get(currentUsername);
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }
}
