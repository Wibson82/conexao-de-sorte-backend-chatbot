package chatbot.domain.model;

import java.time.LocalDateTime;

public class UserChoice {
    private String menuNodeId;
    private String optionId;
    private String optionText;
    private LocalDateTime timestamp;

    public UserChoice() {
        this.timestamp = LocalDateTime.now();
    }

    public UserChoice(String menuNodeId, String optionId, String optionText) {
        this();
        this.menuNodeId = menuNodeId;
        this.optionId = optionId;
        this.optionText = optionText;
    }

    public String getMenuNodeId() { return menuNodeId; }
    public void setMenuNodeId(String menuNodeId) { this.menuNodeId = menuNodeId; }

    public String getOptionId() { return optionId; }
    public void setOptionId(String optionId) { this.optionId = optionId; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}