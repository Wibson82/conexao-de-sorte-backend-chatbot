package chatbot.domain.model;

public class MenuOption {
    private String id;
    private String text;
    private String action;
    private String nextNodeId;
    private String description;
    private boolean isFinal;
    private String keyword;

    public MenuOption() {}

    public MenuOption(String id, String text, String action, String nextNodeId) {
        this.id = id;
        this.text = text;
        this.action = action;
        this.nextNodeId = nextNodeId;
        this.description = "";
        this.isFinal = false;
        this.keyword = null;
    }

    public MenuOption(String id, String text, String action, String nextNodeId, String description) {
        this(id, text, action, nextNodeId);
        this.description = description;
    }

    public MenuOption(String id, String text, String action, String nextNodeId, String description, String keyword) {
        this(id, text, action, nextNodeId, description);
        this.keyword = keyword;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getNextNodeId() { return nextNodeId; }
    public void setNextNodeId(String nextNodeId) { this.nextNodeId = nextNodeId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isFinal() { return isFinal; }
    public void setFinal(boolean isFinal) { this.isFinal = isFinal; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
}