package org.lifestylebot.comandi;

import com.vdurmont.emoji.EmojiParser;
import org.lifestylebot.interfacce.BotController;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public final class Start extends BotCommand {

    public Start(){
        super("start", "Avvio bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage messaggio = new SendMessage();
        messaggio.setChatId(chat.getId());
        messaggio.setText(EmojiParser.parseToUnicode(
                "Ciao " + user.getFirstName() + ",\n" +
                        "piacere di conoscerti :heart_eyes:\n" +
                        "Proverò a rendere il tuo stile di vita migliore a 360 gradi.\n" +
                        "Se vuoi sapere cosa posso fare, clicca il menu alla tua sinistra :heart:\n" +
                        "Se invece vuoi avere informazioni su di me come il mio codice, il mio creatore, " +
                        "la mia versione, etc..., usa il comando /info :green_heart:"));
        try{
            absSender.execute(messaggio);
        }
        catch (TelegramApiException e) {
            System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
        }
    }
}
