package view;/**
 * Created by Emilia on 2025-08-13!
 * Description:
 * ^ • ω • ^
 */

import data_access.InMemoryPostCommentLikesDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entity.Club;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_post.CreatePostPresenter;
import interface_adapter.create_post.CreatePostViewModel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import use_case.create_post.CreatePostInteractor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.io.*;

@Nested
class CreateNewPostViewTest {

    @Test
     void createNewPostTest() throws FileNotFoundException {

        CreateNewPostView createNewPostView = new CreateNewPostView(new ViewManagerModel(), new CreatePostViewModel());
        JButton recipesButton = new JButton("post new recipe :3 ");
        JButton generalPostButton = new JButton("General Post");
        JButton announcementPostButton = new JButton("Announcement");

        ActionEvent e = new ActionEvent(recipesButton, 0, "");
        ActionEvent e1 = new ActionEvent(generalPostButton, 0, "");
        ActionEvent e2 = new ActionEvent(announcementPostButton, 0, "");

        createNewPostView.actionPerformed(e);
        createNewPostView.actionPerformed(e1);
        createNewPostView.actionPerformed(e2);

        Club c = new Club("hi", "desc", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 300l, new ArrayList<>());
        CreateNewPostView createNewPostView2 = new CreateNewPostView(new ViewManagerModel(), new CreatePostViewModel(), c);

        createNewPostView2.actionPerformed(e);
    }

    @Test
    void test() {
        CreateNewPostView view = new CreateNewPostView(new ViewManagerModel(), new CreatePostViewModel());
        String name = view.getViewName();
    }

    @Test
    public void propertyChangeStateTest() {
        CreateNewPostView view = new CreateNewPostView(new ViewManagerModel(), new CreatePostViewModel());
        CreatePostViewModel vm = new CreatePostViewModel();
        java.beans.PropertyChangeEvent evt = new java.beans.PropertyChangeEvent(view, "state", null, vm.getState());
        view.propertyChange(evt);
    }

    @Test
    public void setCreatePostControllerTest() {
        CreateNewPostView view = new CreateNewPostView(new ViewManagerModel(), new CreatePostViewModel());
        view.setCreatePostController(null);
        InMemoryPostCommentLikesDataAccessObject mem= (InMemoryPostCommentLikesDataAccessObject) InMemoryPostCommentLikesDataAccessObject.getInstance();
        InMemoryUserDataAccessObject memu = (InMemoryUserDataAccessObject) InMemoryUserDataAccessObject.getInstance();
        interface_adapter.create_post.CreatePostController stub = new interface_adapter.create_post.CreatePostController(new CreatePostInteractor(mem,memu,new CreatePostPresenter(new CreatePostViewModel()))) {
        };
        view.setCreatePostController(stub);
    }

    @Test
    public void setClubTest() {
        CreateNewPostView view = new CreateNewPostView(new ViewManagerModel(), new CreatePostViewModel());
        Club c = new Club("club", "d", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1L, new ArrayList<>());
        view.setClub(c);
    }
}
