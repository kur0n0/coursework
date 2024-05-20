package ru.volsu.coursebot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.volsu.coursebot.dto.AnswerMappingEnum;
import ru.volsu.coursebot.dto.ArticleDto;
import ru.volsu.coursebot.dto.TaskDTO;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;
import ru.volsu.coursebot.service.CourseCoreService;
import ru.volsu.coursebot.service.MessageService;
import ru.volsu.coursebot.service.UserCacheService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SolveTaskHandler implements MessageHandler {

    @Autowired
    private CourseCoreService courseCoreService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    @Qualifier(value = "mainMenuKeyboard")
    private ReplyKeyboardMarkup mainMenuKeyboard;

    private static String ENTER_ANSWER = "Ввести ответ";
    private static String GET_HINT = "Получить подсказку";

    private Map<Long, UserCommandEnum> userState = new HashMap<>();
    private Map<Long, ArticleDto> hintCache = new HashMap<>();
    private Map<Long, TaskDTO> taskCache = new HashMap<>();

    @Override
    public BotApiMethod<?> handle(Update update) throws BotException, CoreException {
        Message message = update.getMessage();
        User from = message.getFrom();
        Long userId = from.getId();
        String chatId = message.getChatId().toString();

        UserCommandEnum userCommand = userState.getOrDefault(userId, UserCommandEnum.SHOW_RANDOM_TASK);
        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .chatId(chatId);
        switch (userCommand) {
            case SHOW_RANDOM_TASK -> {
                Optional<TaskDTO> taskOpt = courseCoreService.getRandomNotSolvedTask(from.getUserName());
                userState.put(userId, UserCommandEnum.WAIT_ACTION);
                if (taskOpt.isPresent()) {
                    TaskDTO task = taskOpt.get();
                    taskCache.put(userId, task);
                    hintCache.put(userId, task.getHint());
                    sendMessageBuilder
                            .text(task.getQuestion())
                            .parseMode("Markdown")
                            .replyMarkup(getKeyBoard());
                } else {
                    sendMessageBuilder
                            .text("Вы решили все задания!")
                            .parseMode("Markdown")
                            .replyMarkup(mainMenuKeyboard);
                }
            }
            case WAIT_ACTION -> {
                String text = message.getText();
                if (text.equalsIgnoreCase(ENTER_ANSWER)) {
                    userState.put(userId, UserCommandEnum.HANDLE_ANSWER);
                    sendMessageBuilder.text("Введите ответ");
                } else if (text.equalsIgnoreCase(GET_HINT)) {
                    ArticleDto articleDto = hintCache.get(userId);
                    messageService.sendArticleMessage(chatId, List.of(articleDto));
                    userState.put(userId, UserCommandEnum.WAIT_ACTION);
                    sendMessageBuilder.text("");
                } else {
                    userState.remove(userId);
                    hintCache.remove(userId);
                    taskCache.remove(userId);
                    userCacheService.setBotSectionForUser(userId, BotSectionEnum.MAIN_MENU);
                    sendMessageBuilder
                            .replyMarkup(mainMenuKeyboard)
                            .parseMode("Markdown")
                            .text("Переход в главное меню");
                }
            }
            case HANDLE_ANSWER -> {
                String userAnswer = message.getText();
                TaskDTO taskDTO = taskCache.get(userId);
                String correctAnswer = taskDTO.getAnswer();

                Object answer = userAnswer;
                if (taskDTO.getAnswerMapping().equals(AnswerMappingEnum.LONG)) {
                    answer = Long.parseLong(userAnswer);
                }

                courseCoreService.createTaskHistory(taskDTO.getTaskId(), from.getUserName(), answer.toString());
                if ((taskDTO.getAnswerMapping().equals(AnswerMappingEnum.LONG) && answer.equals(Long.parseLong(correctAnswer))) ||
                        (taskDTO.getAnswerMapping().equals(AnswerMappingEnum.STRING) && answer.equals(correctAnswer))) {
                    courseCoreService.createSolvedTask(from.getUserName(), taskDTO.getTaskId());
                }

                userState.remove(userId);
                hintCache.remove(userId);
                taskCache.remove(userId);
                userCacheService.setBotSectionForUser(userId, BotSectionEnum.MAIN_MENU);
                sendMessageBuilder
                        .replyMarkup(mainMenuKeyboard)
                        .parseMode("Markdown")
                        .text("Ответ верный!");
            }
        }

        return sendMessageBuilder.build();
    }

    @Override
    public BotSectionEnum getBotState() {
        return BotSectionEnum.SOLVE_TASK;
    }

    public ReplyKeyboardMarkup getKeyBoard() {
        KeyboardRow mainMenu = new KeyboardRow();
        mainMenu.add(new KeyboardButton("Главное меню"));
        KeyboardRow enterAnswer = new KeyboardRow();
        enterAnswer.add(new KeyboardButton(ENTER_ANSWER));
        KeyboardRow hint = new KeyboardRow();
        hint.add(new KeyboardButton(GET_HINT));
        List<KeyboardRow> keyboard = List.of(mainMenu, enterAnswer, hint);

        ReplyKeyboardMarkup pageKeyboard = new ReplyKeyboardMarkup();
        pageKeyboard.setKeyboard(keyboard);
        pageKeyboard.setSelective(true);
        pageKeyboard.setResizeKeyboard(true);
        pageKeyboard.setOneTimeKeyboard(false);

        return pageKeyboard;
    }
}
