package chatbot.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserSession {
    private String sessionId;
    private String userId;
    private String currentNodeId;
    private List<UserChoice> choiceHistory;
    private LocalDateTime startTime;
    private LocalDateTime lastInteraction;
    private boolean isActive;

    public UserSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.startTime = LocalDateTime.now();
        this.lastInteraction = LocalDateTime.now();
        this.choiceHistory = new ArrayList<>();
        this.isActive = true;
    }

    public UserSession(String userId) {
        this();
        this.userId = userId;
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCurrentNodeId() { return currentNodeId; }
    public void setCurrentNodeId(String currentNodeId) { this.currentNodeId = currentNodeId; }

    public List<UserChoice> getChoiceHistory() { return choiceHistory; }
    public void setChoiceHistory(List<UserChoice> choiceHistory) { this.choiceHistory = choiceHistory; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getLastInteraction() { return lastInteraction; }
    public void setLastInteraction(LocalDateTime lastInteraction) { this.lastInteraction = lastInteraction; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public void addChoice(UserChoice choice) {
        this.choiceHistory.add(choice);
        this.lastInteraction = LocalDateTime.now();
    }

    public void reset() {
        this.choiceHistory.clear();
        this.currentNodeId = null;
        this.startTime = LocalDateTime.now();
        this.lastInteraction = LocalDateTime.now();
    }
}