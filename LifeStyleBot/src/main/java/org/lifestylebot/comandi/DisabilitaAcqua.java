package org.lifestylebot.comandi;

import com.vdurmont.emoji.EmojiParser;
import org.lifestylebot.interfacce.BotController;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public final class DisabilitaAcqua extends BotCommand {
    public DisabilitaAcqua(){
        super("acqua_off","Disabilita promemoria acqua");
    }
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage messaggio = new SendMessage();
        messaggio.setChatId(chat.getId());
        if (BotController.LIFE_STYLE_BOT.promemoriaAcquaContains(chat)) {
            BotController.LIFE_STYLE_BOT.promemoriaAcquaRemove(chat);
            messaggio.setText("Promemoria acqua cancellato con successo");
            messaggio.setText(
                    EmojiParser
                            .parseToUnicode("Promemoria acqua cancellato con successo :white_check_mark:"));

        }
        else
            messaggio.setText(
                    EmojiParser
                            .parseToUnicode("Non è stato possibile trovare un promemoria acqua :exclamation:"));
        try {
            absSender.execute(messaggio);
        } catch (TelegramApiException e) {
            System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
        }
    }
}
