package use_case.specific_club;

import data_access.ClubsDataAccessObject;
import data_access.UserDataAccessObject;
import entity.Account;
import entity.Club;

import java.util.ArrayList;

public class SpecificClubInteractor implements SpecificClubInputBoundary {
    private final ClubsDataAccessObject clubsDataAccessObject;
    private final UserDataAccessObject userDataAccessObject;
    private final SpecificClubOutputBoundary specificClubPresenter;

    public SpecificClubInteractor(
            ClubsDataAccessObject clubsDataAccessObject,
            UserDataAccessObject userDataAccessObject,
            SpecificClubOutputBoundary specificClubPresenter) {
        this.clubsDataAccessObject = clubsDataAccessObject;
        this.userDataAccessObject = userDataAccessObject;
        this.specificClubPresenter = specificClubPresenter;
    }

    @Override
    public void execute(SpecificClubInputData inputData) {
        try {
            Club club = clubsDataAccessObject.getClub(Long.parseLong(String.valueOf(inputData.getClubId())));
            if (club == null) {
                specificClubPresenter.prepareFailView("Club not found");
                return;
            }

            specificClubPresenter.prepareSuccessView(
                new SpecificClubOutputData(club, true, null)
            );
        } catch (NumberFormatException e) {
            specificClubPresenter.prepareFailView("Invalid club ID format");
        } catch (Exception e) {
            specificClubPresenter.prepareFailView("Error loading club: " + e.getMessage());
        }
    }

    @Override
    public void leaveClub(String username, long clubId) {
        try {
            Club club = clubsDataAccessObject.getClub(clubId);
            if (club == null) {
                specificClubPresenter.prepareFailView("Club not found");
                return;
            }

            Account user = (Account) userDataAccessObject.get(username);
            if (user == null) {
                specificClubPresenter.prepareFailView("User not found");
                return;
            }

            // Remove user from club members
            ArrayList<Account> members = club.getMembers();
            members.removeIf(member -> member.getUsername().equals(username));

            // Update club in database
            clubsDataAccessObject.writeClub(
                club.getId(),
                members,
                club.getName(),
                club.getDescription(),
                club.getPosts(),
                club.getTags()
            );

            // Update user's club list
            ArrayList<String> userClubs = user.getClubs();
            userClubs.remove(String.valueOf(clubId));
            user.setClubs(userClubs);
            userDataAccessObject.save(user);

            specificClubPresenter.prepareSuccessView(
                new SpecificClubOutputData(club, true, null)
            );
        } catch (Exception e) {
            specificClubPresenter.prepareFailView("Error leaving club: " + e.getMessage());
        }
    }

    @Override
    public void loadClub(long clubId) {
        try {
            Club club = clubsDataAccessObject.getClub(clubId);
            if (club == null) {
                specificClubPresenter.prepareFailView("Club not found");
                return;
            }

            specificClubPresenter.prepareSuccessView(
                new SpecificClubOutputData(club, true, null)
            );
        } catch (Exception e) {
            specificClubPresenter.prepareFailView("Error loading club: " + e.getMessage());
        }
    }

    @Override
    public void fetchAnnouncements(long clubId) {
        try {
            Club club = clubsDataAccessObject.getClub(clubId);
            if (club == null) {
                specificClubPresenter.prepareFailView("Club not found");
                return;
            }

            specificClubPresenter.prepareSuccessView(
                new SpecificClubOutputData(club, true, null)
            );
        } catch (Exception e) {
            specificClubPresenter.prepareFailView("Error fetching announcements: " + e.getMessage());
        }
    }

    @Override
    public void fetchPosts(long clubId) {
        try {
            Club club = clubsDataAccessObject.getClub(clubId);
            if (club == null) {
                specificClubPresenter.prepareFailView("Club not found");
                return;
            }

            specificClubPresenter.prepareSuccessView(
                new SpecificClubOutputData(club, true, null)
            );
        } catch (Exception e) {
            specificClubPresenter.prepareFailView("Error fetching posts: " + e.getMessage());
        }
    }

    @Override
    public boolean isMember(String username, long clubId) {
        try {
            Club club = clubsDataAccessObject.getClub(clubId);
            if (club == null) {
                return false;
            }

            return club.getMembers().stream()
                .anyMatch(member -> member.getUsername().equals(username));
        } catch (Exception e) {
            return false;
        }
    }
}
