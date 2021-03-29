import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class PreparationToTrip {

    public static void main(String[] args) {

    }

    //Переменные класса
/*//    private  List<DefTrip> defTripList = new ArrayList<>();   //Перечень всех дефолтных поездок
//    private  List<Trip> defTripList;  //Перечень всех дефолтных поездок
//    private  List<String> directionList = new ArrayList<>();  //Список основных направлений, начальных вариантов поездок
//    private static List<Thing> thingsList = new ArrayList<>();  //Список вещей, полный
//    private  Set<Thing> thingsSet = new HashSet<>();  //Список вещей, полный
//    private  List<Thing> thingsList = new ArrayList<>();  //Список вещей, полный*/
    private  List<Thing> selectedThingsList = new ArrayList<>();  //Список выбарнных вещей
    private  List<Thing> tookThingsList = new ArrayList<>();  //Список взятых вещей
    //Пока компаратор отдельный, на случай если понадобится делать разные
    private  Comparator<Thing> categoryComparator = new Comparator<>() {   //TODO: Здесь или отдельный файл?
        @Override
        public int compare(Thing o1, Thing o2) {
            return o1.getCategoryThing().compareTo(o2.getCategoryThing());
        }
    };

    public Stage stage;                                        //Этапы работы с ботом
    private StringBuilder requestString = new StringBuilder();  //Строка запроса направления
    private List<String> nextList;                              //Список для последующего выбора

    TripsData tripsData;
    String tripHistoryPath = "C:\\Java\\Progwards\\PackMe\\src\\TripHistory.txt";
    UserTrip userTrip = new UserTrip();

    //Конструктор класса
    public PreparationToTrip() {
        this.nextList = new ArrayList<>();
        nextList.add("привет");
        nextList.add("поездка");
        nextList.add("/start");
        this.stage = Stage.HELLO;                                       //Первая стадия сбора (Приветствие)
        checkConnectDataFile(tripHistoryPath);
    }

    //Этапы (стадии) сборов
    public enum Stage{
        HELLO,                      //Стадия приветствия
        CHOOSE_DEFAULT_DIRECTION,   //Стадия выбора первоначального направления
        CHOOSE_CORRECTION,          //Стадия выбора уточнения по направлению
        CHOOSE_THINGS,              //Стадия выбора вещей для направления
        CONTROL,                    //Стадия контроля сбора вещей
        ERROR                       //Ошибка
    }

    //Метод для проверки соединения с файлом базы данных
    private void checkConnectDataFile(String tripHistoryPath){
        try{
            tripsData = new TripsData(tripHistoryPath);
        }catch (FileNotFoundException exception){
            stage = Stage.ERROR;
        }
    }

    //Считыватель из файла деф. поездок
/*    private void getDefTrip(){
        defTripList = tripsData.getFrequentDirectionList(3);
//        String result = "Отпуск;\nКомандировка,\nНа природу";
*//*        defTripList.add(new DefTrip("Командировка", "Другой город"));
        defTripList.add(new DefTrip("Командировка", "Другая страна"));
        defTripList.add(new DefTrip("Отпуск", "Другая страна"));
        defTripList.add(new DefTrip("Отпуск", "Россия"));
        defTripList.add(new DefTrip("Природа", "Кемпинг"));
        defTripList.add(new DefTrip("Природа", "Пикник"));*//*

    }*/

    //Получение полного множества вещей
/*    private void getThings(){
        thingsList.clear();
        try{
            TripsData tripsData = new TripsData(tripHistoryPath);
            thingsList = tripsData.getTingsList();
        }catch (FileNotFoundException exception){
            stage = Stage.ERROR;
        }

*//*        thingsSet.clear();      //TODO: Возможно стоит переопределить equals
        Thing passport = new Thing("Паспорт", "Документы");
        Set<String> thingTags = new HashSet<>();
        thingTags.add("Командировка/Другой город".toLowerCase());
        thingTags.add("Командировка/Другая страна".toLowerCase());
        thingTags.add("Отпуск/Другая страна".toLowerCase());
        thingTags.add("Отпуск/Россия".toLowerCase());
        thingTags.add("Природа/Кемпинг".toLowerCase());
        thingTags.add("Природа/Пикник".toLowerCase());
        thingTags.add("Москва/Кремль".toLowerCase());
        passport.tagsList = new HashSet<>(thingTags);
        thingsSet.add(passport);
        Thing InternationalPass = new Thing("Загранпаспорт", "Документы");
        thingTags.clear();
        thingTags.add("Командировка/Другая страна".toLowerCase());
        thingTags.add("Отпуск/Другая страна".toLowerCase());
        InternationalPass.tagsList = new HashSet<>(thingTags);
        thingsSet.add(InternationalPass);
        Thing salt = new Thing("Соль", "Продукты");
        thingTags.clear();
        thingTags.add("Природа/Кемпинг".toLowerCase());
        thingTags.add("Природа/Пикник".toLowerCase());
        salt.tagsList = new HashSet<>(thingTags);
        thingsSet.add(salt);*//*

    }*/

    //Метод для организации взаимодействия с классом бота
    public String choose(String text){
        if (text.trim().toLowerCase().equals("конец")) {
            toStart();
            return "Подготовка к этой поездке завершена. Чтобы начать другую, напиши \"Поездка\"";
        }else {
            switch (stage) {
                //Стадия приветсвия
                case HELLO -> {return doHelloStage(text);}
                //Стадия выбора направления поездки
                case CHOOSE_DEFAULT_DIRECTION -> {return doChooseDirectionStage(text);}
                //Стадия выбора уточнения поездки
                case CHOOSE_CORRECTION -> {return doChooseCorrectionStage(text);}
                //Стадия выбора вещей для направления
                case CHOOSE_THINGS -> {return correctSelectedThingsList(text);}
                //Стадия контроля сбора вещей
                case CONTROL -> {return control(text);}
                case ERROR -> {return "Извините, но работа временно невозможна :(";}


            }
        }
        return "Не понял тебя. Попробуй еще раз";
    }

    //Подготовка к стадии приветствия (Stage.HELLO).
    //Проверка введенного слова, подготовка списка для следующего выбора
    private String doHelloStage(String text){
        if (nextList.contains(text.toLowerCase().trim())) {
            nextList.clear();
//            nextList = getDirectionList();
            nextList = getDirectionList();
//            nextList.add("+");
            stage = Stage.CHOOSE_DEFAULT_DIRECTION;
            return "Привет! Куда собираешься? Предлагаю варианты:\n" + getStringFromList(nextList) +
                    "\nЕсли варианты не подходят, можешь ввести свой, написав \"Куда\" (Например, \"К бабушке\")";
        }else return "Не понял тебя. Попробуй еще раз";
    }

    //Подготовка к стадии приветствия (Stage.CHOOSE_DEFAULT_DIRECTION).
    //Проверка введенного слова, подготовка списка для следующего выбора
    private String doChooseDirectionStage(String text){
        if (nextList.contains(text)) {
            nextList = getCorrectionList(text);
            stage = Stage.CHOOSE_CORRECTION;
            requestString.append(text).append("/");
            userTrip.setDirection(text);
            return "Давай уточним. Предлагаю варианты:\n" + getStringFromList(nextList) +
                    "\nНо можешь ввести свой.";
        }else {
            if(!text.isBlank()) {
                StringBuilder menuSymbol = new StringBuilder(text.trim());   //Избавляет от необходимости делать пробел между знаком и названием
                if (menuSymbol.charAt(0) == "+".charAt(0)) {        //Такой вариант сравнения???
                    menuSymbol.deleteCharAt(0);
                    requestString.append(menuSymbol.toString().trim()).append("/");
                    stage = Stage.CHOOSE_CORRECTION;
                    return "Давай уточним. Например: для \"Командировка\" можно написать \"Россия\"\n" +
                            "Введи уточнение для направления";
                }
            }
        }
        return "Не понял тебя. Попробуй еще раз";
    }

    //Подготовка к стадии ввода уточнения (Stage.CHOOSE_CORRECTION).
    //Проверка введенного слова, подготовка списка для следующего выбора
    private String doChooseCorrectionStage(String text){
        stage = Stage.CHOOSE_THINGS;
        if(!text.isBlank()) {
            requestString.append(text);
            userTrip.setCorrection(text);
        }else{
            requestString.append("-");
            userTrip.setCorrection("-");
        }
        getSelectedThingsList(requestString.toString().toLowerCase()); //Формирование множества вещей для выбранного направления
        nextList.clear();
        return "Отлично! Давай перейдем к вещам. Вот мой совет:\n" + getStringFromList(selectedThingsList) +
                "\nЧтобы добавить пиши \"+ Название (Категория)\", удалить пиши \"- Название\".\n" +
                "Если готово, пиши \"Готово\".";
    }

    //Сброс всех данных по поездке
    private void toStart(){
        stage = Stage.HELLO;
        requestString = new StringBuilder();    //TODO: или удалять символы???
        nextList.clear();
        nextList.add("привет");
        nextList.add("поездка");
        nextList.add("/start");
        selectedThingsList.clear();
        tookThingsList.clear();
    }

    //Метод для контроля за сбором
    private String control(String text) {
        if (!text.isBlank()) {
            if (text.trim().toLowerCase().equals("конец")) {
                toStart();
                return "Подготовка к этой поездке завершена. Чтобы начать другую, напиши \"Поездка\"";
            }
            for (Thing thing : selectedThingsList) {
                if (thing.getNameThing().trim().equalsIgnoreCase(text.trim())) {
                    shiftTing(thing);
                    if (selectedThingsList.isEmpty()) {
                        toStart();
                        try {
                            tripsData.writeTrip(userTrip);
                        } catch (IOException exc) {
                            return "Всё собрано! Хорошей поездки!\n" +
                                    "Произошла ошибка записи\n" +
                                    "Чтобы начать новую, напиши \"Поездка\"";
                        }
                        return "Всё собрано! Хорошей поездки!\n" +
                                "Чтобы начать новую, напиши \"Поездка\"";
                    } else
                        return "Осталось сложить:\n" + getStringFromList(selectedThingsList) +
                                "\nСложено:\n" + getStringFromList(tookThingsList);
                }
            }
            return "Не нашел такой вещи. Попробуй еще раз";
        } else
            return "Ничего не введено. Впиши название сложенной вещи";
    }

    //Метод для перемещения вещи из списка собираемых в список собраных
    public void shiftTing(Thing thing){
        selectedThingsList.remove(thing);
        selectedThingsList.sort(categoryComparator);
        tookThingsList.add(thing);
        tookThingsList.sort(categoryComparator);
    }

    //Метод для корректировки списка выбарнных вещей
    public String correctSelectedThingsList(String text) {
        if (text.trim().toLowerCase().equals("готово")) {
            stage = Stage.CONTROL;
            userTrip.setUserTripThings(selectedThingsList);
            return "Готово! Давай ничего не забудем.\nПиши что сложено, а я буду вычеркивать.";
        } else {
            if (text.trim().toLowerCase().equals("конец")) {
                toStart();
                return "Подготовка к этой поездке завершена. Чтобы начать другую, напиши \"Поездка\"";
            } else {
                return checkMenuSymbol(text);
            }
        }
    }

    //Метод для проверки специальных знаков для изменения списка вещей
    public String checkMenuSymbol(String text){
        String menuSymbol = String.valueOf(text.charAt(0));     //Чтобы использовать equals
        if (menuSymbol.equals("+")) {
            if (addThing(text)) {
                return "Добавлено.\nМожно продолжить добавлять или убирать.\n" +
                        "Чтобы посмотреть весь список, введи \"0\"";
            }
        }
        if (menuSymbol.equals("-")) {
            if (deleteThing(text)) {
                return "Убрал.\nМожно продолжить добавлять или убирать.\n" +
                        "Чтобы посмотреть весь список, введи \"0\"";
            }
        }
        if (menuSymbol.equals("0")) {
            return getStringFromList(selectedThingsList);
        }
        return "Не понял тебя. Попробуй еще раз." +
                "\nЧтобы добавить пиши \"+ Название (Категория)\", удалить пиши \"- Название\".\n" +
                "Если готово, пиши \"Готово\".";
    }

    //Метод для добавления вещи в список
    public boolean addThing(String text){
        StringBuilder str = new StringBuilder(text.trim());   //Избавляет от необходимости делать пробел между знаком и названием
        str.deleteCharAt(0);
        StringTokenizer tokenizer = new StringTokenizer(str.toString().trim(), "()");
        List<String> textParts = new ArrayList<>();
        while (tokenizer.hasMoreTokens()){
            textParts.add(tokenizer.nextToken());
        }
        if(textParts.size() == 2){
            selectedThingsList.add(new Thing(textParts.get(0).trim(), textParts.get(1).trim()));
            selectedThingsList.sort(categoryComparator);
            return true;
        }
        return false;
    }

    //Метод для удаления вещи из списка выбранных
    public boolean deleteThing(String text){
        StringBuilder str = new StringBuilder(text.toLowerCase().trim());
        str.deleteCharAt(0);
        String deleteThingName = str.toString().trim();
        for (Thing thing : selectedThingsList){
            if(thing.getNameThing().trim().equalsIgnoreCase(deleteThingName)){
                selectedThingsList.remove(thing);
                selectedThingsList.sort(categoryComparator);
                return true;
            }
        }
        return false;
    }

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

    //Множество вещей соответствующих запросу
    public void getSelectedThingsList(String requestTrip){
//        getThings();                            //Получить полное множество вещей
//        private  List<Thing> thingsList = new ArrayList<>();  //Список вещей, полный
        List<Thing> thingsList = tripsData.getTingsList();
        selectedThingsList.clear();
        //Выборка с использованием множества
/*        for (Thing thing : thingsSet){          //Формирование множества выбранных вещей.
            if(thing.tagsList.contains(requestTrip.toLowerCase())){   //Проверка соответствия тегам
                selectedThingsList.add(thing);
            }
        }*/
        //Выборка с использованием списка
        for (Thing thing : thingsList){          //Формирование списка выбранных вещей.
            if(thing.tagsMap.containsKey(requestTrip)){   //Проверка соответствия тегам
                selectedThingsList.add(thing);
            }
        }

        selectedThingsList.sort(new Comparator<Thing>() {       //TODO:Проверить на большой выборке
            @Override
            public int compare(Thing o1, Thing o2) {
                if(!o1.getCategoryThing().equals(o2.getCategoryThing())){
                    return o1.getCategoryThing().compareTo(o2.getCategoryThing());
                }else {
                    return o2.tagsMap.get(requestTrip).compareTo(o1.tagsMap.get(requestTrip));
                }
            }
        });

//        selectedThingsList.sort(categoryComparator);
    }

    //Перечень начальных вариантов поездок
    public List<String> getDirectionList(){
        List<String> directionList = new ArrayList<>();
//        getDefTrip();                                           //Считывание из файла
//        defTripList = tripsData.getFrequentDirectionList(3);
        List<Trip> tripList = new ArrayList<>(tripsData.getFrequentTripsList(null, 3));
//        for(DefTrip dt: defTripList){                           //Выбор начальных вариантов. Формирование списка
//            if(!directionList.contains(dt.direction)) {
//                directionList.add(dt.direction);
//            }
//        }
        //Пример использования лямбда-выражения
//        defTripList.forEach(dt -> {if(!directionList.contains(dt.direction)) {
//                                    directionList.add(dt.direction);}});
//        return directionList;
        tripList.forEach(dt -> {if(!directionList.contains(dt.getDirection())) {
            directionList.add(dt.getDirection());}});
        return directionList;
    }

    //Формирование списка уточнений. Зависит от выбранной поездки
    public List<String> getCorrectionList(String direction) {
        List<String> correctionList = new ArrayList<>();
//        for (DefTrip dt : defTripList) {
//            if (!correctionList.contains(dt.correctionLevelOne) && dt.direction.equals(direction)) {
//                correctionList.add(dt.correctionLevelOne);
//            }
//        }
        List<Trip> tripList = new ArrayList<>(tripsData.getFrequentTripsList(direction, 5));
//        for (Trip dt : tripList) {
//            if (!correctionList.contains(dt.getCorrection())) {
//                correctionList.add(dt.getCorrection());
//            }
//        }
        tripList.forEach(dt -> {if(!correctionList.contains(dt.getCorrection())) {
            correctionList.add(dt.getCorrection());}});
        return correctionList;
    }

    //Метод для формирования строки вывода из списка
    public String getStringFromList(List<?> list){
        if(list.size() > 0) {
            StringBuilder outputString = new StringBuilder("");
            for(int i = 0; i < list.size(); i++){
                if(i == list.size() - 1){
                    outputString.append(i + 1).append(". ").append(list.get(i));
                }else {
                    outputString.append(i + 1).append(". ").append(list.get(i)).append("\n");
                }
            }
            return outputString.toString();
        }else return "Список пуст";
    }

    //Метод для автотеста
    public List<Thing> readSelectedThingsList(){
        return selectedThingsList;
    }

}

//Описание поездки по умолчанию
/*class DefTrip{
//    String name;
    String direction;
    String correctionLevelOne;
//    String correctionLevelTwo;

    public DefTrip(String direction, String correctionLevelOne) {
//        this.name = name;
        this.direction = direction;
        this.correctionLevelOne = correctionLevelOne;
//        this.correctionLevelTwo = correctionLevelTwo;
    }
}*/


