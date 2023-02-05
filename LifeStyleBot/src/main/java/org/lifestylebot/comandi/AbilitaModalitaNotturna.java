package org.lifestylebot.comandi;

import com.vdurmont.emoji.EmojiParser;
import org.lifestylebot.interfacce.BotController;
import org.lifestylebot.model.TaskEventGruppoPermessi;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public final class AbilitaModalitaNotturna extends BotCommand {
    private final ChatPermissions chatPermessiNotturnaOn = new ChatPermissions();
    private final ChatPermissions chatPermessiNotturnaOff = new ChatPermissions();
    public AbilitaModalitaNotturna(){
        super("nightmode_on", "Non permette agli utenti di un gruppo di " +
                "inviare messaggi dopo la mezzanotte");
        this.chatPermessiNotturnaOn.setCanSendMessages(false);
        this.chatPermessiNotturnaOn.setCanSendMediaMessages(false);
        this.chatPermessiNotturnaOn.setCanSendPolls(false);
        this.chatPermessiNotturnaOn.setCanSendOtherMessages(false);
        this.chatPermessiNotturnaOn.setCanAddWebPagePreviews(false);
        this.chatPermessiNotturnaOn.setCanPinMessages(false);
        this.chatPermessiNotturnaOn.setCanChangeInfo(false);
        this.chatPermessiNotturnaOn.setCanInviteUsers(false);
        this.chatPermessiNotturnaOff.setCanSendMessages(true);
        this.chatPermessiNotturnaOff.setCanSendMediaMessages(true);
        this.chatPermessiNotturnaOff.setCanSendPolls(true);
        this.chatPermessiNotturnaOff.setCanSendOtherMessages(true);
        this.chatPermessiNotturnaOff.setCanAddWebPagePreviews(true);
        this.chatPermessiNotturnaOff.setCanPinMessages(false);
        this.chatPermessiNotturnaOff.setCanChangeInfo(false);
        this.chatPermessiNotturnaOff.setCanInviteUsers(false);
    }
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage messaggio = new SendMessage();
        Timer timerOn,timerOff;
        Calendar calendar = new GregorianCalendar();
        TaskEventGruppoPermessi eventoModalitaNotturnaOn, eventoModalitaNotturnaOff;
        messaggio.setChatId(chat.getId());
        if(chat.isGroupChat() || chat.isSuperGroupChat()) {
            if (BotController.LIFE_STYLE_BOT.mappaChatNotturneContains(chat.getId()))
                messaggio.setText("Modalità notturna già attivata in questa chat!");
            else {
                timerOn = new Timer();
                timerOff = new Timer();
                eventoModalitaNotturnaOn = new TaskEventGruppoPermessi(
                        chat.getId(),
                        EmojiParser.parseToUnicode(
                                """
                                        Modalità notturna attiva :zzz:
                                        D'ora in poi non sarà più possibile inviare alcun messaggio.
                                        Vi auguro una dolce notte :sleeping:"""),
                        this.chatPermessiNotturnaOn);
                eventoModalitaNotturnaOff = new TaskEventGruppoPermessi(
                        chat.getId(),
                        EmojiParser.parseToUnicode(
                                """
                                        Buongiorno a tutti :sun_with_face:
                                        Da questo momento sarà possibile inviare nuovamente messaggi.
                                        Modalità notturna spenta :no_entry_sign:"""),
                        this.chatPermessiNotturnaOff);
                calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH) + 1,
                        0,
                        0,
                        0
                );
                timerOn.schedule(eventoModalitaNotturnaOn, calendar.getTime(), 24 * 3600 * 1000);
                calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH) + 1,
                        7,
                        0,
                        0
                );
                timerOff.schedule(eventoModalitaNotturnaOff, calendar.getTime(), 24 * 3600 * 1000);
                BotController.LIFE_STYLE_BOT.aggiungiChatNotturna(chat.getId(), List.of(timerOn, timerOff));
                messaggio.setText(EmojiParser.parseToUnicode("Modalità notturna attivata :heavy_check_mark:"));
            }
        }
        else
            messaggio.setText("Questo comando funziona solo per le chat di gruppo");
        try {
            absSender.execute(messaggio);
        } catch (TelegramApiException e) {
            System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
        }
    }
}
