package interface_adapter.create_club;

import entity.Account;
import use_case.create_club.CreateClubInputBoundary;
import use_case.create_club.CreateClubInputData;
import java.util.List;

public class CreateClubController {
    final CreateClubInputBoundary createClubUseCaseInteractor;

    public CreateClubController(CreateClubInputBoundary createClubUseCaseInteractor) {
        this.createClubUseCaseInteractor = createClubUseCaseInteractor;
    }

    public void createClub(String title, String description, List<String> memberUsernames, List<String> tags) {
        // imageUrl currently null / placeholder
        CreateClubInputData inputData = new CreateClubInputData(title, description, null, tags, memberUsernames);
        createClubUseCaseInteractor.execute(inputData);
    }

    public void showMemberSelection(List<Account> currentMembers, String creatorUsername) {
        createClubUseCaseInteractor.showMemberSelection(currentMembers, creatorUsername);
    }
}
