package ru.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private final String BOT_TOKEN = "5138285415:AAFkdLTWx9mJnDBUhdv3SrUdqZKXnqWdH8I";
    private final String BOT_NAME =  "AntiTopor";
    private final String RESPONSE =  "\uD83D\uDD95ТОПОР ПОШЕЛ НАХУЙ\uD83D\uDD95";

    private Long chatId;
    private Integer messageId;

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (fromTopor(update) || containsTopor(update) || captionTopor(update)) {
            Message messageFrom = update.getMessage();
            messageId = messageFrom.getMessageId();
            chatId = messageFrom.getChatId();

            SendMessage messageTo = new SendMessage();
            messageTo.setChatId(chatId.toString());
            messageTo.setReplyToMessageId(messageId);
            messageTo.setText(RESPONSE);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId.toString());
            deleteMessage.setMessageId(messageId);

            try {
                execute(messageTo);
                execute(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean fromTopor(Update update) {
        if (update.getMessage().getForwardFromChat() != null) {
            return update.getMessage().getForwardFromChat().getTitle().toLowerCase().contains("топор");
        }
        return false;
    }

    public Boolean containsTopor(Update update) {
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            return text.contains("\uD83D\uDC49 Топор Live") || text.contains("\uD83D\uDC49 Топор +18");
        }
        return false;
    }

    public Boolean captionTopor(Update update) {
        if (update.getMessage().getCaption() != null) {
            String text = update.getMessage().getCaption();
            return text.contains("\uD83D\uDD79КиберТопор");
        }
        return false;
    }
}
