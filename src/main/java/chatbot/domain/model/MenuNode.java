package chatbot.domain.model;

import java.util.List;
import java.util.Map;

public class MenuNode {
    private String id;
    private String title;
    private String description;
    private List<MenuOption> options;
    private MenuType type;
    private String responseMessage;
    private String nextNodeId;
    private Map<String, String> metadata;

    public MenuNode() {}

    public MenuNode(String id, String title, String description, MenuType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<MenuOption> getOptions() { return options; }
    public void setOptions(List<MenuOption> options) { this.options = options; }

    public MenuType getType() { return type; }
    public void setType(MenuType type) { this.type = type; }

    public String getResponseMessage() { return responseMessage; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }

    public String getNextNodeId() { return nextNodeId; }
    public void setNextNodeId(String nextNodeId) { this.nextNodeId = nextNodeId; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
}