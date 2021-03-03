//package ru.progwards.java1.lessons.Pack_Me_bot; //

import java.util.Scanner;

import org.telegram.telegrambots.ApiContextInitializer;

import ru.progwards.java1.telegrambot.ProgwardsTelegramBot;
import ru.progwards.java1.telegrambot.ProgwardsTelegramBot.FoundTags;

public class ExampleBot extends ProgwardsTelegramBot {

    private final String menu = "У нас есть пицца, напитки и десерт";

    private static final String orderKey = "order";
    private static final String addressKey = "address";

    private boolean stop = false;

    // Пердложить группы блюд
    // спросить адрес доставки
    String finishCheck(Integer userid) {
        // проверить что все 4 группы блюд в заказе
        // если какой-то группы нет && бот не предлагал
        // то предложить и учтановить ключ, что бы не предлагать 2 раза

        // спросить адрес доставки
        // проверить что заказ не пуст
        if (getUserData(userid, addressKey) == null ) {
            setUserData(userid, addressKey, "*");

            return "Укажите, пожалуйста адрес доставки";
        }
        stop = true;
        // если заказ пуст выдать другое сообщение
        return "Спасибо за заказ.";
    }

    // сохранить в заказ
    void saveOrderItem(Integer userid, FoundTags tags) {
        // считать количество
        Integer count = 0;
        String str = getUserData(userid, orderKey);
        if (str != null)
            count = Integer.parseInt(str);
        // инкрементировать количество
        count++;
        // сохранить количество
        setUserData(userid, orderKey, count.toString());
        // сохранить позицию в заказе как orderKey + count
        // ключи - order1, order2
        // данные - getLastFound(tags)

        // дополнительно 1
        // проверить связанные покупки
        // если он заказывает картошку, то предложить соус, если не предлагали
    }

    String getOrder(Integer userid) {
        // считать количество

        // в цикле по каждому элементу вывести содержимое
        // ключ orderKey + номер
        return "Выш заказ";
    }

    /**
     * Метод, который возвращает ответ бота
     * @return ответ
     */
    @Override
    public String processMessage(Integer userid, String text) {
        // проверяем, спрашивали ли адрес доставки
        if (getUserData(userid, addressKey) != null && getUserData(userid, addressKey).equals("*")) {
            setUserData(userid, addressKey, text);
            return finishCheck(userid);
        }
        // сброс всех данных для пользователя - нужно для тестирования
        if (text.equals("/reset"))
            cleartUserData(userid);

        // ищем подходящие тэги рлд запрос из заданных через addTags
        FoundTags tags = checkTags(text);
        // если найдено всего один вариант
        if (foundCount(tags) == 1) {
            if (checkLastFound(tags, "привет"))
                return "Приветствую тебя, мой повелитель!\nЧто желаешь? " + menu;
            if (checkLastFound(tags, "конец"))
                return finishCheck(userid);
            if (checkLastFound(tags, "дурак"))
                return "Не надо ругаться. Я не волшебник, я только учусь";
            if (checkLastFound(tags, "нет"))
                return "Ну нет, так нет";
            if (checkLastFound(tags, "заказ"))
                return getOrder(userid);
            // Добавить связанные предложения, например если он заказывает картошку, то предложить соус,
            // если отказывается - сохранить флажок, что бы бесконечно не предлагать
            // дополнительно 2
            // реализовать отмену позиции заказа

            saveOrderItem(userid, tags);
            return "Отлично, добавляю в заказ " + getLastFound(tags) + " Желаешь что-то еще?";
        }
        if (foundCount(tags) > 1)
            return "Под твой запрос подходит: \n" + extract(tags) + "Выбери что-то одно, и я добавлю это в заказ.";
        return "Я не понял, возможно у нас этго нет, попробуй сказать по другому. " + menu;
    }

    public static void main(String[] args) {
        System.out.println("Hello bot!");
        ApiContextInitializer.init();

        // инициализируем бота
        ExampleBot bot = new ExampleBot();
        bot.username = "Pizza24javabot";
        bot.token = "904923800:AAE5CjsVcpxBJfT_DG7u4T9rua42sa_nIkY";
//        bot.username = "Pack_Me_bot";
//        bot.token = "1611064894:AAHe1K2-x6p_40WzBpeCCpRsKZqLz1WOFnc";


        // наполняем тэгами
        bot.addTags("привет", "привет, здасьте, здравствуйте, добр, день, вечер, утро, hi, hello");
        bot.addTags("конец", "конец, все, стоп, нет");
        bot.addTags("дурак", "дурак, идиот, тупой");

        // добавлено
        bot.addTags("заказ", "заказ");
        bot.addTags("нет", "нет");

        bot.addTags("Пицца гавайская", "гавайск, пицц, ананасы, курица");
        bot.addTags("Пицца маргарита", "маргарит, пицц, моцарелла, сыр, кетчуп, помидор");
        bot.addTags("Пицца пеперони", "пеперони, пицц, салями, колюас, сыр, кетчуп, помидор");

        bot.addTags("Торт тирамису", "десерт, кофе, маскарпоне, бисквит");
        bot.addTags("Торт медовик", "десерт, мед, бисквит");
        bot.addTags("Эклеры", "десерт, заварной, крем, тесто");

        bot.addTags("Кола", "напит, пить, кола");
        bot.addTags("Холодный чай", "напит, пить, чай, липтон, лимон");
        bot.addTags("Сок", "напит, пить, сок, апельсиноый, яблочный, вишневый");

//        bot.start();
        bot.test();
    }

    void test() {
        Scanner in = new Scanner(System.in);
        String input;
        do {
            input = in.nextLine();

            System.out.println(processMessage(123, input));
        } while (!stop);
        in.close();
    }
}
