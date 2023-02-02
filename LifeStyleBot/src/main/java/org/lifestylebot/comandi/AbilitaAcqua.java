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

public final class AbilitaAcqua extends BotCommand {
    private final InlineKeyboardMarkup keyboardMarkupFrequenze = new InlineKeyboardMarkup();
    private final List<List<InlineKeyboardButton>> righeBottoni = new ArrayList<>();
    private final List<InlineKeyboardButton> frequenzeBasse = new ArrayList<>();
    private final List<InlineKeyboardButton> frequenzeAlte = new ArrayList<>();
    private final InlineKeyboardButton bottoneMinuti10 = new InlineKeyboardButton();
    private final InlineKeyboardButton bottoneMinuti20 = new InlineKeyboardButton();
    private final InlineKeyboardButton bottoneMinuti30 = new InlineKeyboardButton();
    private final InlineKeyboardButton bottoneMinuti45 = new InlineKeyboardButton();
    private final InlineKeyboardButton bottoneOre1 = new InlineKeyboardButton();
    private final InlineKeyboardButton bottoneOre2 = new InlineKeyboardButton();

    public AbilitaAcqua(){
        super("acqua_on","Abilita promemoria acqua");
        this.bottoneMinuti10.setCallbackData("bottoneMinuti10");
        this.bottoneMinuti10.setText("10m");
        this.bottoneMinuti20.setCallbackData("bottoneMinuti20");
        this.bottoneMinuti20.setText("20m");
        this.bottoneMinuti30.setCallbackData("bottoneMinuti30");
        this.bottoneMinuti30.setText("30m");
        this.bottoneMinuti45.setCallbackData("bottoneMinuti45");
        this.bottoneMinuti45.setText("45m");
        this.bottoneOre1.setCallbackData("bottoneOre1");
        this.bottoneOre1.setText("1h");
        this.bottoneOre2.setCallbackData("bottoneOre2");
        this.bottoneOre2.setText("2h");
        this.frequenzeBasse.add(this.bottoneMinuti10);
        this.frequenzeBasse.add(this.bottoneMinuti20);
        this.frequenzeBasse.add(this.bottoneMinuti30);
        this.frequenzeAlte.add(this.bottoneMinuti45);
        this.frequenzeAlte.add(this.bottoneOre1);
        this.frequenzeAlte.add(this.bottoneOre2);
        this.righeBottoni.add(this.frequenzeBasse);
        this.righeBottoni.add(this.frequenzeAlte);
        this.keyboardMarkupFrequenze.setKeyboard(this.righeBottoni);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage messaggio = new SendMessage();
        SendMessage messaggioBottoni = new SendMessage();
        messaggio.setChatId(chat.getId());
        messaggio.setText(EmojiParser.parseToUnicode(
                "Ricordarsi di bere non è un'azione scontata.\n"+
                        "Per fortuna, ci siamo conosciuti :sweat_drops:"));
        messaggioBottoni.setChatId(chat.getId());
        messaggioBottoni.setText("Scegli la frequenza del promemoria");
        messaggioBottoni.setReplyMarkup(this.keyboardMarkupFrequenze);
        try {
            absSender.execute(messaggio);
            absSender.execute(messaggioBottoni);
        }
        catch (TelegramApiException e) {
            System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
        }
    }
}
