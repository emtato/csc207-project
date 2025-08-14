package use_case.create_club;

import java.util.List;

public class CreateClubInputData {
    private final String title;
    private final String description;
    private final String imageUrl;
    private final List<String> tags;
    private final List<String> memberUsernames;

    public CreateClubInputData(String title, String description, String imageUrl,
                             List<String> tags, List<String> memberUsernames) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.memberUsernames = memberUsernames;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public List<String> getTags() { return tags; }
    public List<String> getMemberUsernames() { return memberUsernames; }
}
