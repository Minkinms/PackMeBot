/*
package ru.progwards.PackMe;C:\Java\Progwards\PackMe

import java.util.Scanner;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.objects.User;

import ru.progwards.java1.telegrambot.ProgwardsTelegramBot;

public class PizzaBot extends ProgwardsTelegramBot {

    private final String menu = "У нас есть пицца, напитки и десерт";
	
    */
/**
     * Метод, который возвращает ответ бота
     * @return ответ
     *//*

    @Override
    public String processMessage(Integer userid, String text) {
		FoundTags tags = checkTags(text);
		if (foundCount(tags) == 1) {
			if (checkLastFound(tags, "привет"))
				return "Приветствую тебя, мой повелитель!\nЧто желаешь? " + menu;
			if (checkLastFound(tags, "конец"))
				return "Спасибо за заказ.";
			if (checkLastFound(tags, "дурак"))
				return "Не надоругаться. Я не волшебник, я только учусь";
			
			return "Отлично, добавляю в заказ " + getLastFound(tags) + " Желаешь что-то еще?";
		}
		if (foundCount(tags) > 1)
			return "Под твой запрос подходит: \n" + extract(tags) + "Выбери что-то одно, и я добавлю это в заказ.";
    	return "Я не понял, возможно у нас этго нет, попробуй сказать по другому. " + menu;
    }
	
    public static void main(String[] args) {
    	System.out.println("Hello bot!");
    	ApiContextInitializer.init();

        PizzaBot bot = new PizzaBot();
        bot.username = "Pizza24javabot";
        bot.token = "904923800:AAE5CjsVcpxBJfT_DG7u4T9rua42sa_nIkY";
//        bot.username = "Pack_Me_bot";
//        bot.token = "1611064894:AAHe1K2-x6p_40WzBpeCCpRsKZqLz1WOFnc";
        
        bot.addTags("привет", "привет, здасьте, здравствуйте, добр, день, вечер, утро, hi, hello");
        bot.addTags("конец", "конец, все, стоп, нет");
        bot.addTags("дурак", "дурак, идиот, тупой");
        bot.addTags("заказ", "заказ");
        
        bot.addTags("Пицца гавайская", "гавайск, пицц, ананасы, курица");
        bot.addTags("Пицца маргарита", "маргарит, пицц, моцарелла, сыр, кетчуп, помидор");
        bot.addTags("Пицца пеперони", "пеперони, пицц, салями, колюас, сыр, кетчуп, помидор");
        
        bot.addTags("Торт тирамису", "десерт, кофе, маскарпоне, бисквит");
        bot.addTags("Торт медовик", "десерт, мед, бисквит");
        bot.addTags("Эклеры", "десерт, заварной, крем, тесто");
        
        bot.addTags("Кола", "напит, пить, кола");
        bot.addTags("Холодный чай", "напит, пить, чай, липтон, лимон");
        bot.addTags("Сок", "напит, пить, сок, апельсиноый, яблочный, вишневый");
        
        bot.start();
        //bot.test();
    }
    
    */
/*
    void test() {
    	Scanner in = new Scanner(System.in);
    	String input;
    	TUser user = new TUser();
    	do {
    		input = in.nextLine();
    		
    		System.out.println(processMessage(user, input));
    	} while (!input.equals("стоп"));
    }*//*

}
*/
