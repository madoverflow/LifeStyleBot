package org.lifestylebot.model;

import com.vdurmont.emoji.EmojiParser;
import org.lifestylebot.comandi.*;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingCommandBot {
    private final Map<Chat, Timer> promemoriaAcqua = new HashMap<>();
    private final List<IBotCommand> listaComandi = new ArrayList<>();
    private final Map<Long, List<Timer>> mappaChatNotturne = new TreeMap<>();
    public Bot(){
        this.listaComandi.add(new AbilitaAcqua());
        this.listaComandi.add(new DisabilitaAcqua());
        this.listaComandi.add(new Start());
        this.listaComandi.add(new Info());
        this.listaComandi.add(new AbilitaModalitaNotturna());
        this.listaComandi.add(new DisabilitaModalitaNotturna());
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
                if(this.promemoriaAcqua.containsKey(update.getCallbackQuery().getMessage().getChat())) {
                    this.promemoriaAcqua.get(update.getCallbackQuery().getMessage().getChat()).cancel();
                    this.promemoriaAcqua.remove(update.getCallbackQuery().getMessage().getChat());
                }
                if(update.getCallbackQuery().getData().startsWith("bottoneMinuti")){
                    try{
                        int minuti = Integer.parseInt(
                                update
                                .getCallbackQuery()
                                .getData()
                                        .substring("bottoneMinuti".length()));
                        timer.schedule(eventoAcqua,calendar.getTime(), (long) minuti *60*1000);
                        this.promemoriaAcqua.put(update.getCallbackQuery().getMessage().getChat(), timer);
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
                        this.promemoriaAcqua.put(update.getCallbackQuery().getMessage().getChat(), timer);
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
        return "bot username";
    }
    public boolean promemoriaAcquaContains(Chat chat){
        return this.promemoriaAcqua.containsKey(chat);
    }
    public void promemoriaAcquaRemove(Chat chat){
        this.promemoriaAcqua.get(chat).cancel();
        this.promemoriaAcqua.remove(chat);
    }
    public void aggiungiChatNotturna(Long chatId, List<Timer> timers){
        this.mappaChatNotturne.put(chatId, timers);
    }
    public boolean mappaChatNotturneContains(Long chatId){
        return this.mappaChatNotturne.containsKey(chatId);
    }
    public void mappaChatNotturneRemove(Long chatId){
        this.mappaChatNotturne.remove(chatId);
    }
    public List<Timer> getTimersChatNotturne(Long chatId){
        return new ArrayList<>(this.mappaChatNotturne.get(chatId));
    }
}
