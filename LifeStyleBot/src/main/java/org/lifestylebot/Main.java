package org.lifestylebot;

import org.apache.log4j.BasicConfigurator;
import org.lifestylebot.interfacce.BotController;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String... args){
        BasicConfigurator.configure();
        try{
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(BotController.LIFE_STYLE_BOT);
            System.out.println("Bot registrato correttamente.");
        }
        catch (TelegramApiException ex){
            System.out.println("Problemi ad interrogare le api telegram. Bot non avviato");
        }
    }
}