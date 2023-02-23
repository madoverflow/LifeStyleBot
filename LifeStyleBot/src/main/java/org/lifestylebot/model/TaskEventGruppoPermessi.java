package org.lifestylebot.model;

import org.lifestylebot.interfacce.BotController;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPermissions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.TimerTask;

public class TaskEventGruppoPermessi extends TimerTask {
    private final Long chatId;
    private final String testoEvento;
    private final ChatPermissions chatPermessi;
    public TaskEventGruppoPermessi(Long chatId, String testoEvento, ChatPermissions chatPermessi){
        this.chatId = chatId;
        this.testoEvento = testoEvento;
        this.chatPermessi = chatPermessi;
    }

    @Override
    public void run() {
        SendMessage messaggio = new SendMessage();
        SetChatPermissions setChatPermissions = new SetChatPermissions();
        setChatPermissions.setChatId(this.chatId);
        setChatPermissions.setPermissions(this.chatPermessi);
        messaggio.setChatId(this.chatId);
        messaggio.setText(this.testoEvento);
        try {
            BotController.LIFE_STYLE_BOT.execute(setChatPermissions);
            BotController.LIFE_STYLE_BOT.execute(messaggio);
        }
        catch (TelegramApiException ex) {
            System.out.println("Mancata comunicazione con API telegram. Errore nei metodi");
        }
    }
}
