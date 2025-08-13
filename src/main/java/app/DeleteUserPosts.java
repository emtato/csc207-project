package app;

import data_access.*;

public class DeleteUserPosts {
    public static void main(String[] args) {
        String targetUsername = "kat3sjy";
        PostCommentsLikesDataAccessObject postDAO = DBPostCommentLikesDataAccessObject.getInstance();
        UserDataAccessObject userDAO = DBUserDataAccessObject.getInstance();
        ClubsDataAccessObject clubsDAO = DBClubsDataAccessObject.getInstance(postDAO);
        System.out.println("Deleting all posts for user: " + targetUsername);
        postDAO.deleteAllPostsByUser(targetUsername, clubsDAO, userDAO);
        System.out.println("Finished deleting posts for user: " + targetUsername);
    }
}

