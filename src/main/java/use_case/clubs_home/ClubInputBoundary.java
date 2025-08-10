package use_case.clubs_home;

import use_case.create_club.CreateClubInputData;

public interface ClubInputBoundary {
    ClubOutputData getClubsData(String username);
    ClubOutputData joinClub(ClubInputData clubInputData);
    ClubOutputData createClub(CreateClubInputData createClubInputData);
}
