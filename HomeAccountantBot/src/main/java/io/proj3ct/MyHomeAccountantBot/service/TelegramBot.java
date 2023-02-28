package io.proj3ct.MyHomeAccountantBot.service;

import io.proj3ct.MyHomeAccountantBot.config.BotConfig;
import io.proj3ct.MyHomeAccountantBot.model.Accountant;
import io.proj3ct.MyHomeAccountantBot.model.AccountantRepository;
import io.proj3ct.MyHomeAccountantBot.model.User;
import io.proj3ct.MyHomeAccountantBot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountantRepository accountantRepository;

    final BotConfig config;
    final String HELP_TEXT = """
            Этот бот бухгалтер, создан для подсчета бюджета.
            
            Вы можете выполнять команды из главного меню слева или введя команду:
            
            /start - Команда для запуска бота!
            
            /help - Команда повторяющая данное сообщение.Выводит сообщение
            об обьяснение команд.
            
            /mydata - Вывести информацию которую бот хранит об пользователе.
            
            /deletemydata - Удалить информацию относящуюся к пользователю.
            
            """;
    static final String ERROR_TEXT = "Error occurred: ";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Команда для запуска бота!"));
        listOfCommands.add(new BotCommand("/help", "Дает розьяснение команд."));
        listOfCommands.add(new BotCommand("/mydata", "Информация о пользователе."));
        listOfCommands.add(new BotCommand("/deletemydata", "Удаления информации о пользователе."));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    prepareAndSendMessage(chatId, HELP_TEXT);
                    break;
                case "/mydata":
                    break;
                case "/deletemydata":
                    break;
                case "Доход":
                    //savedIncome();
                    break;
                case "Еда дома":
                    //savedFoodAtHome();
                    break;
                case "Предметы общего пользования":
                    //savedCommonItems();
                    break;
                case "Кредит":
                    savedCredit(update.getMessage(), update.getMessage().getText());
                    break;
                case "Еда на работе":
                    //savedFoodAtWork();
                    break;
                case "Интернет-развлечения":
                    //savedInternetEntertainment();
                    break;
                case "Аренда":
                    //savedRent();;
                    break;
                case "Расходы":
                    savedCost(update.getMessage());
                    break;
                default:
                    prepareAndSendMessage(chatId, "Простите, такой команды не существует!");
            }
        }

    }

    private void registerUser(Message msg) {

        if(userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: " + user);
        }
    }

    private void startCommandReceived(long chatId, String name) {

        String answer = "Привет, " + name + ", рад тебя преветствовать!";
        log.info("Replied to user " + name);

        sendMessage(chatId, answer);

    }

    private void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("Доход");
        row.add("Еда дома");
        row.add("Предметы общего пользования");
        row.add("Кредит");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Расходы");
        row.add("Еда на работе");
        row.add("Интернет-развлечения");
        row.add("Аренда");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try{
            execute(message);
        }
        catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void savedCost(Message msg) {
        Update update = new Update();
        String messageText = update.getMessage().getText();
        int a = Integer.parseInt(messageText);

        var chatId = msg.getChatId();

        Accountant accountant = new Accountant();
        accountant.setChatId(chatId);
        accountant.setCost(2);

        accountantRepository.save(accountant);
        log.info("accountant saved: " + accountant);
    }

    private void savedCredit(Message msg, String str) {
        int b = Integer.parseInt(str);
        Update update = new Update();
        String messageText = update.getMessage().getText();
        int a = Integer.parseInt(messageText);
        var chatId = msg.getChatId();
        Accountant accountant = new Accountant();
        int credit = accountant.getCredit();
        int sumcredit = credit + b;


        accountant.setChatId(chatId);
        accountant.setCredit(sumcredit);

        accountantRepository.save(accountant);
        log.info("accountant saved: " + accountant);
    }
}
