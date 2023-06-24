package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.volsu.coursebot.dto.ArticlePage;
import ru.volsu.coursebot.dto.PageInfo;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;
import ru.volsu.coursebot.service.CourseCoreService;
import ru.volsu.coursebot.service.SendMessageService;
import ru.volsu.coursebot.service.UserCacheService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchByTag implements MessageHandler {

    @Autowired
    @Qualifier(value = "mainMenuKeyboard")
    private ReplyKeyboardMarkup mainMenuKeyboard;

    @Autowired
    private CourseCoreService courseCoreService;

    @Autowired
    @Qualifier(value = "continueKeyboard")
    private ReplyKeyboardMarkup continueKeyboard;

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private UserCacheService userCacheService;

    private Map<Long, UserCommandEnum> userState = new HashMap<>();
    private Map<Long, String> enteredTag = new HashMap<>();
    private Map<Long, PageInfo> userPage = new HashMap<>();

    @Override
    public BotApiMethod<?> handle(Update update) throws Exception {
        Message message = update.getMessage();
        String text = message.getText();
        Long userId = update.getMessage().getFrom().getId();
        String chatId = message.getChatId().toString();
        UserCommandEnum tagState = userState.getOrDefault(userId, UserCommandEnum.ASK_TAG);

        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .chatId(chatId);
        switch (tagState) {
            case ASK_TAG -> {
                sendMessageBuilder.text("Введите название тэга");
                userState.put(userId, UserCommandEnum.ENTER_TAG);
            }
            case ENTER_TAG -> {
                enteredTag.put(userId, text);
                sendMessageBuilder.text("Подтвердите поиск статей");
                sendMessageBuilder.replyMarkup(continueKeyboard);
                sendMessageBuilder.parseMode("Markdown");
                userState.put(userId, UserCommandEnum.SHOW_PAGE);
                return sendMessageBuilder
                        .build();
            }
            case SHOW_PAGE -> {
                PageInfo cachedPageInfo = userPage.getOrDefault(userId, null);
                Integer pageNumber = cachedPageInfo == null ? 0 : cachedPageInfo.getCurrentPage();

                ArticlePage articlePage = courseCoreService.getPageByTag(pageNumber, enteredTag.getOrDefault(userId, null));
                PageInfo responsePageInfo = new PageInfo(articlePage.getTotalPages(), articlePage.getCurrentPage());
                userPage.put(userId, responsePageInfo);

                sendMessageService.sendArticleMessage(chatId, articlePage.getContent());
                sendMessageBuilder.text("Выберите действие");
                sendMessageBuilder.replyMarkup(getPageKeyboard(responsePageInfo.getCurrentPage(), responsePageInfo.getTotalPages()));
                sendMessageBuilder.parseMode("Markdown");
                userState.put(userId, UserCommandEnum.CHOOSE_PAGE);
                return sendMessageBuilder
                        .build();
            }
            case CHOOSE_PAGE -> {
                PageInfo cachedPageInfo = userPage.get(userId);
                if (text.equals("Предыдущая страница")) {
                    cachedPageInfo.setCurrentPage(cachedPageInfo.getCurrentPage() - 1);
                    userState.put(userId, UserCommandEnum.SHOW_PAGE);
                    userPage.put(userId, cachedPageInfo);
                    return sendMessageBuilder
                            .replyMarkup(continueKeyboard)
                            .parseMode("Markdown")
                            .text("Подтвердите действие")
                            .build();
                } else if (text.equals("Следующая страница")) {
                    cachedPageInfo.setCurrentPage(cachedPageInfo.getCurrentPage() + 1);
                    userState.put(userId, UserCommandEnum.SHOW_PAGE);
                    userPage.put(userId, cachedPageInfo);
                    return sendMessageBuilder
                            .replyMarkup(continueKeyboard)
                            .parseMode("Markdown")
                            .text("Подтвердите действие")
                            .build();
                } else {
                    userState.remove(userId);
                    enteredTag.remove(userId);
                    userPage.remove(userId);
                    userCacheService.setBotSectionForUser(userId, BotSectionEnum.MAIN_MENU);
                    return sendMessageBuilder
                            .replyMarkup(mainMenuKeyboard)
                            .parseMode("Markdown")
                            .text("Переход в главное меню")
                            .build();
                }
            }
        }

        return sendMessageBuilder
                .build();
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.SEARCH_BY_TAG;
    }

    public ReplyKeyboardMarkup getPageKeyboard(Integer currentPage, Integer totalPages) {
        KeyboardRow previousRow = new KeyboardRow();
        previousRow.add(new KeyboardButton("Предыдущая страница"));
        KeyboardRow nextRow = new KeyboardRow();
        nextRow.add(new KeyboardButton("Следующая страница"));
        KeyboardRow mainRow = new KeyboardRow();
        mainRow.add(new KeyboardButton("В главное меню"));

        List<KeyboardRow> keyboard;
        if ((currentPage == totalPages - 1) && totalPages > 1) {
            keyboard = List.of(previousRow, mainRow);
        } else if (currentPage <= 0 && totalPages > 1) {
            keyboard = List.of(nextRow, mainRow);
        } else {
            keyboard = List.of(mainRow);
        }

        ReplyKeyboardMarkup pageKeyboard = new ReplyKeyboardMarkup();
        pageKeyboard.setKeyboard(keyboard);
        pageKeyboard.setSelective(true);
        pageKeyboard.setResizeKeyboard(true);
        pageKeyboard.setOneTimeKeyboard(false);

        return pageKeyboard;
    }
}
