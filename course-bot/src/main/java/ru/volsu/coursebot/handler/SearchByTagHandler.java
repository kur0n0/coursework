package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.ArticlePageDto;
import ru.volsu.coursebot.dto.HandleResult;
import ru.volsu.coursebot.dto.PageInfo;
import ru.volsu.coursebot.dto.UserContext;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;
import ru.volsu.coursebot.service.CourseCoreService;
import ru.volsu.coursebot.service.MessageService;

import java.util.List;
import java.util.Optional;

@Component
public class SearchByTagHandler implements MessageHandler {

    @Autowired
    @Qualifier(value = "mainMenuKeyboard")
    private ReplyKeyboardMarkup mainMenuKeyboard;

    @Autowired
    private CourseCoreService courseCoreService;

    @Autowired
    @Qualifier(value = "continueKeyboard")
    private ReplyKeyboardMarkup continueKeyboard;

    @Autowired
    private MessageService messageService;

    @Override
    public HandleResult handle(Update update, UserContext userContext) throws BotException, CoreException {
        Message message = update.getMessage();
        String text = message.getText();
        String chatId = message.getChatId().toString();
        UserCommandEnum tagState = Optional.ofNullable(userContext.getLastCommand()).orElse(UserCommandEnum.ASK_TAG);

        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .chatId(chatId);
        switch (tagState) {
            case ASK_TAG -> {
                userContext.setLastCommand(UserCommandEnum.ENTER_TAG);
                sendMessageBuilder.text("Введите название предмета");
            }
            case ENTER_TAG -> {
                userContext.setTagToSearch(text);
                userContext.setLastCommand(UserCommandEnum.SHOW_PAGE);

                sendMessageBuilder.text("Подтвердите поиск статей");
                sendMessageBuilder.replyMarkup(continueKeyboard);
                sendMessageBuilder.parseMode("Markdown");
            }
            case SHOW_PAGE -> {
                PageInfo cachedPageInfo = userContext.getPageInfo();
                Integer pageNumber = cachedPageInfo == null ? 0 : cachedPageInfo.getCurrentPage();

                ArticlePageDto articlePage = courseCoreService.getPageByTag(pageNumber, userContext.getTagToSearch());
                PageInfo responsePageInfo = new PageInfo(articlePage.getTotalPages(), articlePage.getCurrentPage());
                userContext.setPageInfo(responsePageInfo);

                List<ArticleDto> content = articlePage.getContent();
                if (!content.isEmpty()) {
                    messageService.sendArticleMessage(chatId, content);
                } else {
                    messageService.sendDefaultMessage(chatId, "По данному запросу ничего не найдено");
                }

                userContext.setLastCommand(UserCommandEnum.CHOOSE_PAGE);

                sendMessageBuilder.text("Выберите действие");
                sendMessageBuilder.replyMarkup(getPageKeyboard(responsePageInfo.getCurrentPage(), responsePageInfo.getTotalPages()));
                sendMessageBuilder.parseMode("Markdown");
            }
            case CHOOSE_PAGE -> {
                PageInfo cachedPageInfo = userContext.getPageInfo();
                if (text.equals("Предыдущая страница")) {
                    cachedPageInfo.setCurrentPage(cachedPageInfo.getCurrentPage() - 1);
                    userContext.setLastCommand(UserCommandEnum.SHOW_PAGE);
                    userContext.setPageInfo(cachedPageInfo);

                    sendMessageBuilder
                            .replyMarkup(continueKeyboard)
                            .parseMode("Markdown")
                            .text("Подтвердите действие");
                } else if (text.equals("Следующая страница")) {
                    cachedPageInfo.setCurrentPage(cachedPageInfo.getCurrentPage() + 1);
                    userContext.setLastCommand(UserCommandEnum.SHOW_PAGE);
                    userContext.setPageInfo(cachedPageInfo);

                    sendMessageBuilder
                            .replyMarkup(continueKeyboard)
                            .parseMode("Markdown")
                            .text("Подтвердите действие");
                } else {
                    userContext.setLastCommand(null);
                    userContext.setTagToSearch(null);
                    userContext.setPageInfo(null);
                    userContext.setCurrentBotSection(BotSectionEnum.MAIN_MENU);

                    sendMessageBuilder
                            .replyMarkup(mainMenuKeyboard)
                            .parseMode("Markdown")
                            .text("Переход в главное меню");
                }
            }
        }

        return new HandleResult(sendMessageBuilder.build(), userContext);
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
        if (currentPage > 0 && currentPage < totalPages - 1) {
            keyboard = List.of(previousRow, mainRow, nextRow);
        } else if ((currentPage == totalPages - 1) && totalPages > 1) {
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
