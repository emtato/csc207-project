package app;/**
 * Created by Emilia on 2025-08-02!
 * Description:
 * ^ • ω • ^
 */

/**
 * Class to store the current logged in user (username)
 */

public class Session {
    private static String currentUsername = "example_user";

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }
}
