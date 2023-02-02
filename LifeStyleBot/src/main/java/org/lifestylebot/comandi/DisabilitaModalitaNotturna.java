package org.lifestylebot.comandi;

import com.vdurmont.emoji.EmojiParser;
import org.lifestylebot.interfacce.BotController;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Timer;

public final class DisabilitaModalitaNotturna extends BotCommand {

    public DisabilitaModalitaNotturna(){
        super("nightmode_off", "Disattiva la modalità notturna");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage messaggio = new SendMessage();
        messaggio.setChatId(chat.getId());
        if(chat.isGroupChat() || chat.isSuperGroupChat()) {
            if (BotController.LIFE_STYLE_BOT.mappaChatNotturneContains(chat.getId())) {
                BotController.LIFE_STYLE_BOT.getTimersChatNotturne(chat.getId()).forEach(Timer::cancel);
                BotController.LIFE_STYLE_BOT.mappaChatNotturneRemove(chat.getId());
                messaggio.setText(EmojiParser.parseToUnicode("Modalità notturna disattivata :white_check_mark:"));
            } else
                messaggio.setText("La modalità notturna non è attiva in questa chat!");
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
