package use_case.join_club;

import use_case.club.ClubAnnouncementCollector;
import use_case.club.ClubAnnouncementResult;
import use_case.club.ClubReadOperations;
import use_case.create_club.ClubWriteOperations;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;

import java.util.ArrayList;

public class JoinClubInteractor implements JoinClubInputBoundary {
    private final ClubReadOperations clubRead; // replaces ClubLookup + ClubListing
    private final ClubWriteOperations clubWrite; // replaces ClubCreationWrite
    private final UserDataAccessObject userDataAccessObject;
    private final JoinClubOutputBoundary presenter;

    public JoinClubInteractor(ClubReadOperations clubRead,
                              ClubWriteOperations clubWrite,
                              UserDataAccessObject userDataAccessObject,
                              JoinClubOutputBoundary presenter) {
        this.clubRead = clubRead;
        this.clubWrite = clubWrite;
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(JoinClubInputData inputData) {
        try {
            String username = inputData.getUsername();
            long clubId = inputData.getClubId();
            Club club = clubRead.getClub(clubId);
            if (club == null) {
                presenter.prepareFailView("Club not found");
                return;
            }
            Account user = (Account) userDataAccessObject.get(username);
            if (user == null) {
                presenter.prepareFailView("User not found");
                return;
            }

            // Add user to club if not already a member
            boolean already = club.getMembers().stream().anyMatch(a -> a.getUsername().equals(username));
            if (!already) {
                ArrayList<Account> members = club.getMembers();
                members.add(user);
                clubWrite.writeClub(club.getId(), members, club.getName(), club.getDescription(), club.getImageUrl(), club.getPosts(), club.getTags());
            }

            // Update user club list
            ArrayList<String> userClubs = user.getClubs();
            if (userClubs == null) {
                userClubs = new ArrayList<>();
            }
            String clubIdStr = String.valueOf(clubId);
            if (!userClubs.contains(clubIdStr)) {
                userClubs.add(clubIdStr);
                user.setClubs(userClubs);
                userDataAccessObject.save(user);
            }

            // Build updated lists via collector
            ArrayList<Club> allClubs = clubRead.getAllClubs();
            ClubAnnouncementResult res = ClubAnnouncementCollector.collect(user, allClubs);
            ArrayList<Club> memberClubs = new ArrayList<>(res.memberClubs());
            ArrayList<Club> nonMemberClubs = new ArrayList<>(res.nonMemberClubs());
            ArrayList<Post> announcements = new ArrayList<>(res.announcements());

            presenter.prepareSuccessView(new JoinClubOutputData(memberClubs, nonMemberClubs, announcements, true, null));
        } catch (Exception e) {
            presenter.prepareFailView("Error joining club: " + e.getMessage());
        }
    }
}
