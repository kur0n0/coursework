package ru.volsu.coursebot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;

import java.util.HashMap;
import java.util.Map;

@Component
public class SearchByTag implements MessageHandler {

    private Map<Long, UserCommandEnum> searchByTagState = new HashMap<>();

    @Override
    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        Long userId = update.getMessage().getFrom().getId();
        UserCommandEnum tagState = searchByTagState.getOrDefault(userId, UserCommandEnum.ASK_TAG);

        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .chatId(message.getChatId().toString());
        switch (tagState) {
            case ASK_TAG -> {
                sendMessageBuilder.text("Введите название тэга");
                searchByTagState.put(userId, UserCommandEnum.ENTER_TAG);
            }
            case ENTER_TAG -> {
                text = "test";
                // получение страницы
                searchByTagState.remove(userId);
            }
            default -> searchByTagState.remove(userId);
        }

        return sendMessageBuilder.build();
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.SEARCH_BY_TAG;
    }
}
