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


        //Приветствие. Старт
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

        bot.addTags("Паспорт", "Паспорт");
        bot.addTags("Загранпаспорт", "Загранпаспорт");
        bot.addTags("Соль", "Соль");

//        bot.addTags("+", "+, +");   //Ru, eng
//        bot.addTags("-", "-, -");
//        bot.addTags("ok", "ok, OK, ок, ОК");
//        bot.addTags("", "");



//        defTripList.add(new DefTrip("Командировка", "Другой город"));
//        defTripList.add(new DefTrip("Командировка", "Другая страна"));
//        defTripList.add(new DefTrip("Отпуск", "Другая страна"));
//        defTripList.add(new DefTrip("Отпуск", "Россия"));
//        defTripList.add(new DefTrip("Природа", "Кемпинг"));
//        defTripList.add(new DefTrip("Природа", "Пикник"));
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
//    private int choosingTrip = 0;
    PackMeToTrip packMeToTrip = new PackMeToTrip();
//    List<String> nextList;
//    StringBuilder requestString = new StringBuilder();

    public void addTags(){
        String str1 = "дальше";
        String str2 = "Д";
        this.addTags(str1, str2);
    }


    @Override
    public String processMessage(Integer userid, String text) {

        FoundTags tags = checkTags(text);
//        System.out.println(getLastFound(tags));
//        return getLastFound(tags);
//        if (choosingTrip == 0 && (checkLastFound(tags, "поездка") || checkLastFound(tags, "привет")) ) {
//        if (checkLastFound(tags, text)) {
//            System.out.println(getLastFound(tags));
        if(packMeToTrip.stage == PackMeToTrip.Stage.CHOOSE_THINGS){
//            System.out.println("Передал: " + text);
            return packMeToTrip.choose(text.trim());
        }else
            return packMeToTrip.choose(getLastFound(tags));

/*            if (choosingTrip == 0) {
                choosingTrip = 1;
                nextList = packMeToTrip.getDefDirectionList();
                return "Привет! Куда собираешься? Предлагаю варианты:\n"
                        + packMeToTrip.getDefDirectionString();
            }
            //Обнуление сборов
            if (checkLastFound(tags, "конец")) {
                choosingTrip = 0;
                requestString = new StringBuilder();

                //TODO:Обнулить множества и списки
                return "Подготовка к этой поездке завершена. Чтобы начать другую, напиши \"Поездка\"";
            }
            //Этап выбора уточнения поездки
            if (choosingTrip == 1 && nextList.contains(text)) {
                nextList = packMeToTrip.getCorrectionLevelOneList(text);
                choosingTrip = 2;
                requestString.append(text).append("/");
                return "Давай уточним. Предлагаю варианты:\n"
                        + packMeToTrip.getCorrectionLevelOneString(text);
            }
            if (choosingTrip == 2 && nextList.contains(text)) {
                requestString.append(text);
                packMeToTrip.getSelectedThingsSet(requestString.toString());
                return "Отлично! Давай перейдем к вещам. Вот мой совет:\n" + packMeToTrip.getThingsString();
                //+ packMeToTrip.getCorrectionLevelOneString(text);
            }*/
//        }else return "Не понял тебя. Попробуй еще раз";
    }



    void test() {
    	Scanner in = new Scanner(System.in);
    	String input;
    	String output;
    	TUser user = new TUser();
    	TestClass testClass = new TestClass();
        System.out.println("User: " + testClass.printHello());
        System.out.println(processMessage(user.userID, testClass.printHello()));

        input = testClass.printDefDirection();
        System.out.println("User: " + input);
        System.out.println(processMessage(user.userID, input));

        input = testClass.printCorrection(input);
        System.out.println("User: " + input);
        System.out.println(processMessage(user.userID, input));

//        System.out.println(processMessage(user.userID, "Стоп"));


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

