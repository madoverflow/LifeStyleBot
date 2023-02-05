package org.lifestylebot.model;

import org.glassfish.grizzly.nio.SelectorHandler;
import org.lifestylebot.interfacce.BotController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.TimerTask;

public class TaskEvent extends TimerTask {
    private final Long chatId;
    private final String testoEvento;
    public TaskEvent(Long chatId, String testoEvento){
        this.chatId = chatId;
        this.testoEvento = testoEvento;
    }

    @Override
    public void run() {
        SendMessage messaggio = new SendMessage();
        messaggio.setChatId(this.chatId);
        messaggio.setText(this.testoEvento);
        try {
            BotController.LIFE_STYLE_BOT.execute(messaggio);
        }
        catch (TelegramApiException ex) {
            System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
        }
    }
}
