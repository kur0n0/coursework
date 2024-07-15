package ru.volsu.coursebot.dto;

import ru.volsu.commons.dto.TaskDTO;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;

import java.io.Serial;
import java.io.Serializable;

public class UserContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 2000544602018295992L;

    private long userId;

    private BotSectionEnum currentBotSection;

    private UserCommandEnum lastCommand;

    private String titleToSearch;

    private String tagToSearch;

    private PageInfo pageInfo;

    private String fullTextQuery;

    private TaskDTO taskDTO;

    public UserContext(Long userId, BotSectionEnum botSectionEnum) {
        this.userId = userId;
        this.currentBotSection = botSectionEnum;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BotSectionEnum getCurrentBotSection() {
        return currentBotSection;
    }

    public void setCurrentBotSection(BotSectionEnum currentBotSection) {
        this.currentBotSection = currentBotSection;
    }

    public UserCommandEnum getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand(UserCommandEnum lastCommand) {
        this.lastCommand = lastCommand;
    }

    public String getTitleToSearch() {
        return titleToSearch;
    }

    public void setTitleToSearch(String titleToSearch) {
        this.titleToSearch = titleToSearch;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public void setTagToSearch(String tagToSearch) {
        this.tagToSearch = tagToSearch;
    }

    public String getTagToSearch() {
        return tagToSearch;
    }

    public String getFullTextQuery() {
        return fullTextQuery;
    }

    public void setFullTextQuery(String fullTextQuery) {
        this.fullTextQuery = fullTextQuery;
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }
}
