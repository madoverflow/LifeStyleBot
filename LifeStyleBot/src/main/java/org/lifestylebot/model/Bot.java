package org.lifestylebot.model;

import com.vdurmont.emoji.EmojiParser;
import org.lifestylebot.comandi.AbilitaAcqua;
import org.lifestylebot.comandi.DisabilitaAcqua;
import org.lifestylebot.comandi.Info;
import org.lifestylebot.comandi.Start;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingCommandBot {
    private final Map<User, Timer> promemoriaAcqua = new HashMap<>();
    private final List<IBotCommand> listaComandi = new ArrayList<>();
    public Bot(){
        this.listaComandi.add(new AbilitaAcqua());
        this.listaComandi.add(new DisabilitaAcqua());
        this.listaComandi.add(new Start());
        this.listaComandi.add(new Info());
        this.registerAll(this.listaComandi.toArray(IBotCommand[]::new));
    }
    @Override
    public String getBotToken() {
        return "token";
    }
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            SendMessage messaggio = new SendMessage();
            if(
                    update.getCallbackQuery().getData().startsWith("bottoneMinuti")
                    ||
                    update.getCallbackQuery().getData().startsWith("bottoneOre")
            ){
                Calendar calendar = new GregorianCalendar();
                Timer timer = new Timer();
                TaskEvent eventoAcqua = new TaskEvent(
                        update.getCallbackQuery().getMessage().getChatId(),
                        EmojiParser.parseToUnicode("Bevi acqua :potable_water:"));
                DeleteMessage cancellazioneMessaggio = new DeleteMessage();
                if(this.promemoriaAcqua.containsKey(update.getCallbackQuery().getFrom())) {
                    this.promemoriaAcqua.get(update.getCallbackQuery().getFrom()).cancel();
                    this.promemoriaAcqua.remove(update.getCallbackQuery().getFrom());
                }
                if(update.getCallbackQuery().getData().startsWith("bottoneMinuti")){
                    try{
                        int minuti = Integer.parseInt(
                                update
                                .getCallbackQuery()
                                .getData()
                                        .substring("bottoneMinuti".length()));
                        timer.schedule(eventoAcqua,calendar.getTime(), (long) minuti *60*1000);
                        this.promemoriaAcqua.put(update.getCallbackQuery().getFrom(), timer);
                        messaggio.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        messaggio.setText("Frequenza promemoria impostata con successo");
                    }
                    catch(NumberFormatException ex){
                        System.out.println("Non è stato possibile determinare i minuti");
                    }
                }
                if(update.getCallbackQuery().getData().startsWith("bottoneOre")){
                    try{
                        int ore = Integer.parseInt(
                                update
                                        .getCallbackQuery()
                                        .getData()
                                        .substring("bottoneOre".length()));
                        timer.schedule(eventoAcqua,calendar.getTime(), (long) ore *3600*1000);
                        this.promemoriaAcqua.put(update.getCallbackQuery().getFrom(), timer);
                        messaggio.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        messaggio.setText("Frequenza promemoria impostata con successo");
                    }
                    catch(NumberFormatException ex){
                        System.out.println("Non è stato possibile determinare i minuti");
                    }
                }
                cancellazioneMessaggio.setChatId(update.getCallbackQuery().getMessage().getChatId());
                cancellazioneMessaggio.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                try{
                    this.execute(cancellazioneMessaggio);
                }
                catch (TelegramApiException e) {
                    System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
                }
            }
            try{
                this.execute(messaggio);
            }
            catch (TelegramApiException e) {
                System.out.println("Mancata comunicazione con API telegram. Messaggio non mandato!");
            }
        }
    }
    @Override
    public void processInvalidCommandUpdate(Update update) {
        super.processInvalidCommandUpdate(update);
    }
    @Override
    public boolean filter(Message message) {
        return super.filter(message);
    }
    @Override
    public void onRegister() {
        super.onRegister();
    }
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
    @Override
    public String getBotUsername() {
        return "username bot";
    }
    public boolean promemoriaAcquaContains(User user){
        return this.promemoriaAcqua.containsKey(user);
    }
    public void promemoriaAcquaRemove(User user){
        this.promemoriaAcqua.get(user).cancel();
        this.promemoriaAcqua.remove(user);
    }
}
