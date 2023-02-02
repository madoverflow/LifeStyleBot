package org.lifestylebot.comandi;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public final class Info extends BotCommand {
    private final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
    private final List<List<InlineKeyboardButton>> righeBottoni = new ArrayList<>();
    private final List<InlineKeyboardButton> rigaBottoni = new ArrayList<>();
    private final InlineKeyboardButton contattami = new InlineKeyboardButton();
    public Info(){
        super("info", "Informazioni del bot");
        this.contattami.setText("Contattami");
        this.contattami.setUrl("https://t.me/emanuelecastronovo");
        this.rigaBottoni.add(contattami);
        this.righeBottoni.add(this.rigaBottoni);
        this.keyboardMarkup.setKeyboard(this.righeBottoni);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage messaggio = new SendMessage();
        messaggio.setChatId(chat.getId());
        messaggio.setText(EmojiParser.parseToUnicode(
                "Dettagli tecnici bot :pager: :warning:\n\n" +
                        "Codice sorgente: https://github.com/madoverflow/LifeStyleBot\n" +
                        "Versione 1.2\n"+
                        "Developed by @emanuelecastronovo\n"));
        messaggio.setReplyMarkup(this.keyboardMarkup);
        try{
            absSender.execute(messaggio);
        }
        catch (TelegramApiException e) {
            System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
        }
    }
}
