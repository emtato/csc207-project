package view;


import interface_adapter.edit_profile.EditProfileState;
import interface_adapter.edit_profile.EditProfileViewModel;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// TODO: use constants for the hardcoded parts
public class EditProfileView extends JPanel implements PropertyChangeListener {
    private final String viewName = "edit profile";
    private final EditProfileViewModel editProfileViewModel;
    // TODO: declare controllers

    final JLabel title;
    private final ImageIcon profilePicture;
    private final JLabel profilePictureLabel;
    private final JLabel displayName;
    private final JLabel username;
    private final JTextArea bio;
    private final JTextField nameInputField;
    private final JTextArea bioInputField;
    private final JButton uploadProfilePicture;
    private final JTextArea preferenceTagsField;
    private final JButton saveChangesButton;

    private final JButton backButton;


    public EditProfileView(EditProfileViewModel editProfileViewModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.editProfileViewModel.addPropertyChangeListener(this);

        title = new JLabel(EditProfileViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMinimumSize(new Dimension(1000, 50));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setMaximumSize(new Dimension(1500, 1030));
        mainPanel.setMinimumSize(new Dimension(1500, 1030));

        final JPanel profilePreviewPanel = new JPanel();
        profilePreviewPanel.setLayout(new BoxLayout(profilePreviewPanel, BoxLayout.Y_AXIS));
        profilePreviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.setMaximumSize(new Dimension(200, 1030));
        profilePreviewPanel.setMinimumSize(new Dimension(200, 1030));

        Image profilePictureImage =  new ImageIcon("src/main/java/view/temporary_sample_image.png").getImage();
        int newWidth = 200;
        int newHeight = 200;
        profilePictureImage = profilePictureImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        profilePicture = new ImageIcon(profilePictureImage);

        profilePictureLabel = new JLabel(profilePicture);
        profilePictureLabel.setMaximumSize(new Dimension(200, 200));
        profilePictureLabel.setMinimumSize(new Dimension(200, 200));
        profilePictureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.add(profilePictureLabel);

        profilePreviewPanel.add(Box.createRigidArea(new Dimension(200, 50)));

        displayName = new JLabel("Display Name");
        displayName.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.add(displayName);

        username = new JLabel("@Username");
        username.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.add(username);

        bio = new JTextArea(EditProfileViewModel.BIO_ROW_NUM,EditProfileViewModel.BIO_COL_NUM);
        bio.setText("Bio.\n1234567890123456789012345678901234567890");
        bio.setEditable(false);
        bio.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.add(bio);

        profilePreviewPanel.add(Box.createVerticalGlue());

        backButton = new JButton(EditProfileViewModel.BACK_BUTTON_LABEL);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePreviewPanel.add(backButton);

        mainPanel.add(profilePreviewPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        final JPanel editProfilePanel = new JPanel();
        editProfilePanel.setMaximumSize(new Dimension(1200, 1030));
        editProfilePanel.setMinimumSize(new Dimension(1200, 1030));
        editProfilePanel.setLayout(new BoxLayout(editProfilePanel, BoxLayout.Y_AXIS));
        editProfilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //editProfilePanel.setMaximumSize(new Dimension(500, 200));
        //editProfilePanel.setMinimumSize(new Dimension(500, 200));

        final JPanel editNamePanel = new JPanel();
        editNamePanel.setLayout(new BoxLayout(editNamePanel, BoxLayout.X_AXIS));
        final JLabel editNameLabel = new JLabel(EditProfileViewModel.EDIT_NAME_LABEL);
        editNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editNamePanel.add(editNameLabel);
        editNamePanel.add(Box.createRigidArea(new Dimension(50, 50)));
        nameInputField = new JTextField();
        nameInputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        editNamePanel.add(nameInputField);
        editProfilePanel.add(editNamePanel);

        final JPanel editBioPanel = new JPanel();
        editBioPanel.setLayout(new BoxLayout(editBioPanel, BoxLayout.X_AXIS));
        final JLabel editBioLabel = new JLabel(EditProfileViewModel.EDIT_BIO_LABEL);
        editBioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editBioPanel.add(editBioLabel);
        editBioPanel.add(Box.createRigidArea(new Dimension(50, 50)));
        bioInputField = new JTextArea(EditProfileViewModel.BIO_ROW_NUM,EditProfileViewModel.BIO_COL_NUM);
        bioInputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        editBioPanel.add(bioInputField);
        editProfilePanel.add(editBioPanel);

        JPanel uploadProfilePicturePanel = new JPanel();
        uploadProfilePicturePanel.setLayout(new BoxLayout(uploadProfilePicturePanel, BoxLayout.X_AXIS));
        uploadProfilePicture = new JButton(EditProfileViewModel.UPLOAD_PFP_LABEL);
        uploadProfilePicturePanel.add(uploadProfilePicture);
        uploadProfilePicturePanel.add(Box.createHorizontalGlue());
        editProfilePanel.add(uploadProfilePicturePanel);

        JPanel preferenceTagsXPanel = new JPanel();
        preferenceTagsXPanel.setLayout(new BoxLayout(preferenceTagsXPanel, BoxLayout.X_AXIS));
        final JPanel preferenceTagsPanel = new JPanel();
        preferenceTagsPanel.setLayout(new BoxLayout(preferenceTagsPanel, BoxLayout.Y_AXIS));
        preferenceTagsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final JLabel preferenceTagsLabel = new JLabel(EditProfileViewModel.PREFERENCE_TAGS_LABEL);
        preferenceTagsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        preferenceTagsPanel.add(preferenceTagsLabel);
        //preferenceTagsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
        preferenceTagsField = new JTextArea(2,2);
        preferenceTagsField.setAlignmentX(Component.LEFT_ALIGNMENT);
        preferenceTagsPanel.add(preferenceTagsField);
        preferenceTagsXPanel.add(preferenceTagsPanel);
        preferenceTagsXPanel.add(Box.createHorizontalGlue());
        editProfilePanel.add(preferenceTagsXPanel);

        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.setLayout(new BoxLayout(saveButtonPanel, BoxLayout.X_AXIS));
        saveButtonPanel.add(Box.createHorizontalGlue());
        saveChangesButton = new JButton(EditProfileViewModel.SAVE_CHANGES_BUTTON_LABEL);
        saveChangesButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        saveButtonPanel.add(saveChangesButton);

        editProfilePanel.add(saveButtonPanel);

        mainPanel.add(editProfilePanel);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // TODO: write code for the action listeners below
        nameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        bioInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });


        uploadProfilePicture.addActionListener(
                evt -> {
                    if (evt.getSource().equals(uploadProfilePicture)) {
                    }
                }
        );

        preferenceTagsField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        saveChangesButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(saveChangesButton)) {
                    }
                }
        );

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton)) {
                    }
                }
        );

        this.add(title);
        this.add(mainPanel);
    }

    // TODO: implement the propertyChange function and set controllers
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {

        }
    }

    public String getViewName() {
        return viewName;
    }

    // set controllers

}