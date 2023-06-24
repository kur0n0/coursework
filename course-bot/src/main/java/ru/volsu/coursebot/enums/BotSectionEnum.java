package ru.volsu.coursebot.enums;

import java.util.Collections;
import java.util.List;

public enum BotSectionEnum {
    MAIN_MENU(Collections.emptyList()),
    SEARCH_BY_TITLE(List.of(UserCommandEnum.ASK_TITLE)),
    SEARCH_BY_TAG(List.of(UserCommandEnum.ASK_TAG));

    private List<UserCommandEnum> userCommandEnumList;

    BotSectionEnum(List<UserCommandEnum> userCommandEnumList) {
        this.userCommandEnumList = userCommandEnumList;
    }

    public List<UserCommandEnum> getUserCommandEnumList() {
        return userCommandEnumList;
    }
}
