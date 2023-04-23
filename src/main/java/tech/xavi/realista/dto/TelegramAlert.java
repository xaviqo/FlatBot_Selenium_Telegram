package tech.xavi.realista.dto;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class TelegramAlert extends SendMessage {

    public TelegramAlert(String chatId, String text) {
        super();
        enableMarkdown(true);
        setChatId(chatId);
        setText(text);
    }

}
