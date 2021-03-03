import org.telegram.telegrambots.ApiContextInitializer;
import ru.progwards.java1.telegrambot.ProgwardsTelegramBot;

import java.util.Random;
import java.util.Scanner;

public class PackMeBot extends ProgwardsTelegramBot {

    public static void main(String[] args) {
        System.out.println("Hello bot!");
        ApiContextInitializer.init();

        PackMeBot bot = new PackMeBot();
//            bot.username = "Pizza24javabot";
//            bot.token = "904923800:AAE5CjsVcpxBJfT_DG7u4T9rua42sa_nIkY";
        bot.username = "Pack_Me_bot";
        bot.token = "1611064894:AAHe1K2-x6p_40WzBpeCCpRsKZqLz1WOFnc";

        //Приветствие. Старт
        bot.addTags("поездка", "поездка, поздка, поездк, поезка, Поездка, Поздка, Поездк, Поезка, П");
        bot.addTags("привет", "привет, Привет, здасьте, здравствуйте, добр, день, вечер, утро, hi, hello");
        bot.addTags("конец", "конец, стоп, Конец, Стоп");
        bot.addTags("дальше", "дальше, Д");
/*//            bot.addTags("дурак", "дурак, идиот, тупой");
//            bot.addTags("заказ", "заказ");
//
//            bot.addTags("Пицца гавайская", "гавайск, пицц, ананасы, курица");
//            bot.addTags("Пицца маргарита", "маргарит, пицц, моцарелла, сыр, кетчуп, помидор");
//            bot.addTags("Пицца пеперони", "пеперони, пицц, салями, колюас, сыр, кетчуп, помидор");
//
//            bot.addTags("Торт тирамису", "десерт, кофе, маскарпоне, бисквит");
//            bot.addTags("Торт медовик", "десерт, мед, бисквит");
//            bot.addTags("Эклеры", "десерт, заварной, крем, тесто");
//
//            bot.addTags("Кола", "напит, пить, кола");
//            bot.addTags("Холодный чай", "напит, пить, чай, липтон, лимон");
//            bot.addTags("Сок", "напит, пить, сок, апельсиноый, яблочный, вишневый");*/

//            bot.start();
        bot.test();
    }


    //переменные класса
//  private final String menu = "У нас есть пицца, напитки и десерт";
    boolean choosingTrip = false;
    PackMeToTrip packMeToTrip = new PackMeToTrip();
    private final String trips = packMeToTrip.getDefTrip();

    @Override
    public String processMessage(Integer userid, String text) {
        FoundTags tags = checkTags(text);
        if (!choosingTrip && (checkLastFound(tags, "поездка") || checkLastFound(tags, "привет")) ) {
            choosingTrip = true;
            return "Привет! Куда собираешься? Предлагаю варианты:\n" + trips;
        }
        //Обнуление сборов
        if (checkLastFound(tags, "конец")) {
            choosingTrip = false;
            return "Подготовка к этой поездке завершена. Чтобы начать другую, напиши \"Поездка\"";
        }
        //Этап выбора поездки
        if (choosingTrip){
            if (checkLastFound(tags, "дальше")) {
                return "try";
            }

        }

        return "Не понял тебя. Попробуй еще раз";
    }



    void test() {
    	Scanner in = new Scanner(System.in);
    	String input;
    	TUser user = new TUser();
    	TestClass testClass = new TestClass();
        System.out.println("User: " + testClass.print());
        System.out.println(processMessage(user.userID, testClass.print()));
    	do {
    		input = in.nextLine();

    		System.out.println(processMessage(user.userID, input));
    	} while (!input.equals("стоп"));
    }




}
    //Условный пользователь
    class TUser{
        Integer userID;
        public TUser() {
        this.userID = new Random().nextInt();
        }
    }

