package data_access;

import entity.Account;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.create_post.CreatePostDataAccessInterface;
import use_case.delete_account.DeleteAccountUserDataAccessInterface;
import use_case.edit_profile.EditProfileUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.manage_followers.ManageFollowersUserDataAccessInterface;
import use_case.manage_following.ManageFollowingUserDataAccessInterface;
import use_case.view_profile.ProfileUserDataAccessInterface;
import use_case.toggle_settings.SettingsUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.create_club.CreateClubUserDataAccessInterface;

import java.util.ArrayList;

public interface UserDataAccessObject extends
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        SettingsUserDataAccessInterface,
        ProfileUserDataAccessInterface,
        EditProfileUserDataAccessInterface,
        ManageFollowingUserDataAccessInterface,
        ManageFollowersUserDataAccessInterface,
        CreatePostDataAccessInterface,
        DeleteAccountUserDataAccessInterface,
        CreateClubUserDataAccessInterface { // Added create club gateway
    void removeClubFromUser(String username, String clubId);

    /**
     * Gets all users in the system
     *
     * @return ArrayList of all user accounts
     */
    ArrayList<Account> getAllUsers();
}
