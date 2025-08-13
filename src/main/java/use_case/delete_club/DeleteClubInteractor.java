package use_case.delete_club;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;
import entity.Post;
import java.util.ArrayList;

public class DeleteClubInteractor implements DeleteClubInputBoundary {
    private final ClubsDataAccessObject clubsDAO;
    private final UserDataAccessObject userDAO;
    private final DeleteClubOutputBoundary presenter;

    public DeleteClubInteractor(ClubsDataAccessObject clubsDAO,
                                UserDataAccessObject userDAO,
                                DeleteClubOutputBoundary presenter) {
        this.clubsDAO = clubsDAO;
        this.userDAO = userDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteClubInputData inputData) {
        try {
            long clubId = inputData.getClubId();
            String requester = inputData.getRequesterUsername();

            Club club = clubsDAO.getClub(clubId);
            if (club == null) {
                presenter.prepareFailView("Club not found");
                return;
            }
            Account user = (Account) userDAO.get(requester);
            if (user == null) {
                presenter.prepareFailView("User not found");
                return;
            }
            boolean isMember = false;
            for (Account m : club.getMembers()) {
                if (m.getUsername().equals(requester)) { isMember = true; break; }
            }
            if (!isMember) {
                presenter.prepareFailView("Not authorized to delete this club");
                return;
            }

            // Remove club from each member's club list explicitly before DAO deletion (defensive)
            for (Account member : club.getMembers()) {
                if (member != null) {
                    Account full = (Account) userDAO.get(member.getUsername());
                    if (full != null) {
                        ArrayList<String> cl = full.getClubs();
                        if (cl != null && cl.remove(String.valueOf(clubId))) {
                            full.setClubs(cl);
                            userDAO.save(full);
                        }
                    }
                }
            }

            // Perform deletion
            clubsDAO.deleteClub(clubId);

            // Rebuild lists
            ArrayList<Club> allClubs = clubsDAO.getAllClubs();
            ArrayList<Club> memberClubs = new ArrayList<>();
            ArrayList<Club> nonMemberClubs = new ArrayList<>();
            ArrayList<Post> announcements = new ArrayList<>();

            Account refreshedUser = (Account) userDAO.get(requester);
            ArrayList<String> userClubs = refreshedUser != null ? refreshedUser.getClubs() : new ArrayList<>();
            if (userClubs == null) userClubs = new ArrayList<>();

            for (Club c : allClubs) {
                String cid = String.valueOf(c.getId());
                if (userClubs.contains(cid)) {
                    memberClubs.add(c);
                    if (c.getPosts() != null) {
                        for (Post p : c.getPosts()) {
                            if (p != null && p.getType() != null && p.getType().trim().equalsIgnoreCase("announcement")) {
                                announcements.add(p);
                            }
                        }
                    }
                } else {
                    nonMemberClubs.add(c);
                }
            }

            presenter.prepareSuccessView(new DeleteClubOutputData(memberClubs, nonMemberClubs, announcements, true, "Club deleted"));
        } catch (Exception e) {
            presenter.prepareFailView("Error deleting club: " + e.getMessage());
        }
    }
}
