package interface_adapter.create_club;

public class CreateClubState {
    private String title = "";
    private String description = "";
    private java.util.ArrayList<String> memberUsernames = new java.util.ArrayList<>();
    private java.util.ArrayList<String> tags = new java.util.ArrayList<>();
    private String error = null;

    public CreateClubState(CreateClubState copy) {
        this.title = copy.title;
        this.description = copy.description;
        this.memberUsernames = new java.util.ArrayList<>(copy.memberUsernames);
        this.tags = new java.util.ArrayList<>(copy.tags);
        this.error = copy.error;
    }

    // Default constructor
    public CreateClubState() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public java.util.ArrayList<String> getMemberUsernames() { return memberUsernames; }
    public void setMemberUsernames(java.util.ArrayList<String> memberUsernames) {
        this.memberUsernames = memberUsernames;
    }

    public java.util.ArrayList<String> getTags() { return tags; }
    public void setTags(java.util.ArrayList<String> tags) { this.tags = tags; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
