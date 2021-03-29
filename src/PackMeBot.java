import org.telegram.telegrambots.ApiContextInitializer;
import ru.progwards.java1.telegrambot.ProgwardsTelegramBot;

import java.util.*;

public class PackMeBot extends ProgwardsTelegramBot {

    public static void main(String[] args) {
        System.out.println("Hello bot!");
        ApiContextInitializer.init();

        PackMeBot bot = new PackMeBot();
//            bot.username = "Pizza24javabot";
//            bot.token = "904923800:AAE5CjsVcpxBJfT_DG7u4T9rua42sa_nIkY";
        bot.username = "Pack_Me_bot";
        bot.token = "1611064894:AAHe1K2-x6p_40WzBpeCCpRsKZqLz1WOFnc";

/*        //Приветствие. Старт
        bot.addTags("поездка", "поездка, поздка, поездк, поезка, Поездка, Поздка, Поездк, Поезка, П");
        bot.addTags("привет", "привет, Привет, здасьте, здравствуйте, добр, день, вечер, утро, hi, hello");
        bot.addTags("конец", "конец, стоп, Конец, Стоп");
        bot.addTags();  //TODO: Нужен модуль для добавления всех тэгов
        bot.addTags("Командировка", "Командировка");
        bot.addTags("Отпуск", "Отпуск");
        bot.addTags("Природа", "Природа");
        //
        bot.addTags("Другой город", "Другой город");
        bot.addTags("Другая страна", "Другая страна, Др");
        bot.addTags("Кемпинг", "Кемпинг");
        bot.addTags("Пикник", "Пикник");
        bot.addTags("Россия", "Россия, россия, Р");

//        bot.addTags("Паспорт", "Паспорт");
        bot.addTags("Загранпаспорт", "Загранпаспорт");
        bot.addTags("Соль", "Соль");*/

            bot.start();
//        bot.test();
    }


    //переменные класса
    Map<Integer, PreparationToTrip> users = new HashMap<>();
//    PreparationToTrip packMeToTrip = new PreparationToTrip();
    PreparationToTrip packMeToTrip;

    @Override
    public String processMessage(Integer userid, String text) {
//        FoundTags tags = checkTags(text);

//        switch (packMeToTrip.stage){
//            case CHOOSE_THINGS, CONTROL -> {return packMeToTrip.choose(text.trim());}
//            default -> {return packMeToTrip.choose(getLastFound(tags));}
//        }

        if(!users.containsKey(userid)){
            users.put(userid, new PreparationToTrip());
        }
        return users.get(userid).choose(text.trim());

//        return packMeToTrip.choose(text.trim());
    }



    void test() {
    	Scanner in = new Scanner(System.in);
    	String input;
    	String output;
    	TUser user = new TUser();
    	TestClass testClass = new TestClass();
        System.out.println("User: " + testClass.printHello());
        System.out.println(processMessage(user.userID, testClass.printHello()));

//        if(packMeToTrip.stage != PreparationToTrip.Stage.ERROR) {
        if(users.get(user.userID).stage != PreparationToTrip.Stage.ERROR) {
            input = testClass.printDefDirection();
            System.out.println("User: " + input);
            System.out.println(processMessage(user.userID, input));

            input = testClass.printCorrection(input);
            System.out.println("User: " + input);
            System.out.println(processMessage(user.userID, input));

            input = testClass.printOk();
            System.out.println("User: " + input);
            System.out.println(processMessage(user.userID, input));
//        System.out.println(processMessage(user.userID, "Стоп"));

            while (!users.get(user.userID).readSelectedThingsList().isEmpty()) {
                input = testClass.printThing(users.get(user.userID).readSelectedThingsList());
                System.out.println("\nUser: " + input);
                System.out.println(processMessage(user.userID, input));
            }

            do {
                input = in.nextLine();
                System.out.println(processMessage(user.userID, input));
            } while (!input.equals("стоп"));
        }
    }




}
    //Условный пользователь
    class TUser{
        Integer userID;
        public TUser() {
        this.userID = new Random().nextInt();
        }
    }

