package data_access;

import use_case.UserDataAccessInterface;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.create_post.CreatePostDataAccessInterface;
import use_case.delete_account.DeleteAccountUserDataAccessInterface;
import use_case.edit_profile.EditProfileUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.manage_followers.ManageFollowersUserDataAccessInterface;
import use_case.manage_following.ManageFollowingUserDataAccessInterface;
import use_case.note.NoteDataAccessInterface;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.settings.SettingsUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public interface UserDataAccessObject extends
        UserDataAccessInterface,
        SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        NoteDataAccessInterface,
        SettingsUserDataAccessInterface,
        ProfileUserDataAccessInterface,
        EditProfileUserDataAccessInterface,
        ManageFollowingUserDataAccessInterface,
        ManageFollowersUserDataAccessInterface,
        CreatePostDataAccessInterface,
        DeleteAccountUserDataAccessInterface {

}
