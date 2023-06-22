package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.volsu.coursebot.dto.ArticleDto;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;
import ru.volsu.coursebot.service.CourseCoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchByTag implements MessageHandler {

    @Autowired
    private CourseCoreService courseCoreService;

    @Autowired
    private ReplyKeyboardMarkup continueKeyboard;

    private Map<Long, UserCommandEnum> userState = new HashMap<>();
    private Map<Long, String> enteredTag = new HashMap<>();

    @Override
    public SendMessage handle(Update update) throws Exception {
        Message message = update.getMessage();
        String text = message.getText();
        Long userId = update.getMessage().getFrom().getId();
        UserCommandEnum tagState = userState.getOrDefault(userId, UserCommandEnum.ASK_TAG);

        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .chatId(message.getChatId().toString());
        switch (tagState) {
            case ASK_TAG -> {
                sendMessageBuilder.text("Введите название тэга");
                userState.put(userId, UserCommandEnum.ENTER_TAG);
            }
            case ENTER_TAG -> {
                enteredTag.put(userId, text);
                sendMessageBuilder.text("Подтвердите поиск статей");
                sendMessageBuilder.replyMarkup(continueKeyboard);
                userState.put(userId, UserCommandEnum.SHOW_PAGE);
            }
            case SHOW_PAGE -> {
                List<ArticleDto> page = courseCoreService.getPageByTag(0, enteredTag.getOrDefault(userId, null));
                ArticleDto articleDto = page.get(0);
                text = String.format("Название статьи: %s \n" +
                        "Тэг: %s \n" +
                        "Текст: %s", articleDto.getTitle(), articleDto.getTag(), articleDto.getTitle());
                sendMessageBuilder.text(text);
                // получение страницы
                userState.remove(userId);
            }
            default -> userState.remove(userId);
        }

        return sendMessageBuilder
                .build();
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.SEARCH_BY_TAG;
    }
}
