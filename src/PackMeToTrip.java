import java.util.*;

public class PackMeToTrip {

    public static void main(String[] args) {
        String str = "-Паспорт(Документы)";
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.deleteCharAt(0);
        StringTokenizer tokenizer = new StringTokenizer(stringBuilder.toString(), " ()");
//
//        List<String> splitText = new ArrayList<>();
//        while (tokenizer.hasMoreTokens()){
//            splitText.add(tokenizer.nextToken());
//        }
//        Thing newThing = new Thing(splitText.get(0), splitText.get(1));
        Thing newThing = new Thing(tokenizer.nextToken(), tokenizer.nextToken());
        System.out.println(newThing.toString());
//        System.out.println("знак: " + splitText.get(0));
    }

    //Переменные класса
    private static List<DefTrip> defTripList = new ArrayList<>();   //Перечень всех дефолтных поездок
    private static List<String> directionList = new ArrayList<>();  //Список основных направлений, начальных вариантов поездок
//    private static List<Thing> thingsList = new ArrayList<>();  //Список вещей, полный
    private static Set<Thing> thingsSet = new HashSet<>();  //Список вещей, полный
    private static List<Thing> selectedThingsList = new ArrayList<>();  //Список выбарнных вещей
    //Пока компаратор отдельный, на случай если понадобится делать разные
    private static Comparator<Thing> categoryComparator = new Comparator<>() {
        @Override
        public int compare(Thing o1, Thing o2) {
            return o1.getCategoryThing().compareTo(o2.getCategoryThing());
        }
    };



    Stage stage;                                        //Этапы работы с ботом
    StringBuilder requestString = new StringBuilder();  //Строка запроса направления
    List<String> nextList;                              //Список для последующего выбора


    //Конструктор класса
    public PackMeToTrip() {
//        String[] helloWords = {"привет","поездка"};                     //Цикл жизни этого массива ????
//        this.nextList = new ArrayList<>(Arrays.asList(helloWords));     //Определение первичного списка
        this.nextList = new ArrayList<>();
        nextList.add("привет");
        nextList.add("поездка");
        this.stage = Stage.HELLO;                                       //Первая стадия сбора (Приветствие)
    }

    //Этапы (стадии) сборов
    public enum Stage{
        HELLO,                      //Стадия приветствия
        CHOOSE_DEFAULT_DIRECTION,   //Стадия выбора первоначального направления
        CHOOSE_CORRECTION,          //Стадия выбора уточнения по направлению
        CHOOSE_THINGS,              //Стадия выбора вещей для направления
        CONTROL                     //Стадия контроля сбора вещей
    }

    //Считыватель из файла деф. поездок
    private void getDefTrip(){
//        String result = "Отпуск;\nКомандировка,\nНа природу";
        defTripList.add(new DefTrip("Командировка", "Другой город"));
        defTripList.add(new DefTrip("Командировка", "Другая страна"));
        defTripList.add(new DefTrip("Отпуск", "Другая страна"));
        defTripList.add(new DefTrip("Отпуск", "Россия"));
        defTripList.add(new DefTrip("Природа", "Кемпинг"));
        defTripList.add(new DefTrip("Природа", "Пикник"));

    }

    //Получение полного множества вещей
    private void getThings(){
        thingsSet.clear();      //TODO: Возможно стоит переопределить equals
        Thing passport = new Thing("Паспорт", "Документы");
        Set<String> thingTags = new HashSet<>();
        thingTags.add("Командировка/Другой город");
        thingTags.add("Командировка/Другая страна");
        thingTags.add("Отпуск/Другая страна");
        thingTags.add("Отпуск/Россия");
        thingTags.add("Природа/Кемпинг");
        thingTags.add("Природа/Пикник");
        passport.tagsList = new HashSet<>(thingTags);
        thingsSet.add(passport);
        Thing InternationalPass = new Thing("Загранпаспорт", "Документы");
        thingTags.clear();
        thingTags.add("Командировка/Другая страна");
        thingTags.add("Отпуск/Другая страна");
        InternationalPass.tagsList = new HashSet<>(thingTags);
        thingsSet.add(InternationalPass);
        Thing salt = new Thing("Соль", "Продукты");
        thingTags.clear();
        thingTags.add("Природа/Кемпинг");
        thingTags.add("Природа/Пикник");
        salt.tagsList = new HashSet<>(thingTags);
        thingsSet.add(salt);

    }

    //Метод для организации взаимодействия с классом бота
    public String choose(String text){
        if (nextList.contains(text)) {
            switch (stage) {
                case HELLO -> {
                    nextList = getDefDirectionList();
                    stage = Stage.CHOOSE_DEFAULT_DIRECTION;
//                    return "Привет! Куда собираешься? Предлагаю варианты:\n" + getDefDirectionString();
                    return "Привет! Куда собираешься? Предлагаю варианты:\n" +
                            getStringFromList(directionList);
                }
//Стадия выбора направления поездки
                case CHOOSE_DEFAULT_DIRECTION -> {
                    nextList = getCorrectionLevelOneList(text);
                    stage = Stage.CHOOSE_CORRECTION;
                    requestString.append(text).append("/");
//                    return "Давай уточним. Предлагаю варианты:\n" + getCorrectionLevelOneString(text);
                    return "Давай уточним. Предлагаю варианты:\n" +
                            getStringFromList(getCorrectionLevelOneList(text));
                }
//Стадия выбора уточнения поездки
                case CHOOSE_CORRECTION -> {
                    stage = Stage.CHOOSE_THINGS;
                    requestString.append(text);
                    getSelectedThingsList(requestString.toString()); //Формирование множества вещей для выбранного направления
                    nextList.clear();//TODO:Применить метод обнуления nextList.
//                    getSelectionMenuList();
//                    return "Отлично! Давай перейдем к вещам. Вот мой совет:\n" + getThingsString() +
//                            "\nЧтобы добавить пиши \"+ Название (Категория)\", удалить пиши \"- Название\".\n" +
//                            "Если готово, пиши \"Готово\".";
                    return "Отлично! Давай перейдем к вещам. Вот мой совет:\n" +
                            getStringFromList(selectedThingsList) +
                            "\nЧтобы добавить пиши \"+ Название (Категория)\", удалить пиши \"- Название\".\n" +
                            "Если готово, пиши \"Готово\".";
                }
            }
        }else {
            switch (stage){
//Стадия выбора вещей для направления
                case CHOOSE_THINGS: return correctSelectedThingsList(text);
//                    return "Список готов. Присмотрим за сбором";
//Стадия контроля сбора вещей
                case CONTROL:return "Control";

            }
            //Обнуление сборов
            if (text.equals("конец")) {
                stage = Stage.HELLO;
                requestString = new StringBuilder();    //TODO: или удалять символы???
                                                        //TODO:Обнулить множества и списки
                nextList.clear();                       //TODO:Ввести метод обнуления nextList.
                nextList.add("привет");
                nextList.add("поездка");
                return "Подготовка к этой поездке завершена. Чтобы начать другую, напиши \"Поездка\"";
            }
        }
        return "Не понял тебя. Попробуй еще раз";       //TODO:Подумать над фразой
    }

    //Метод для корректировки списка выбарнных вещей
    public String correctSelectedThingsList(String text){
        String menuSymbol = String.valueOf(text.charAt(0));
        if(menuSymbol.equals("+")){
            if(addThing(text)){
                return "Добавлено.\nМожно продолжить добавлять или убирать.\n"+
                        "Чтобы посмотреть весь список, введи \"0\"";
            }
        }else {
            if (menuSymbol.equals("-")){
                if(deleteThing(text)) {
                    return "Убрал.\nМожно продолжить добавлять или убирать.\n" +
                            "Чтобы посмотреть весь список, введи \"0\"";
                }
            }else{
                if(text.trim().toLowerCase().equals("готово")){
                    stage = Stage.CONTROL;
                    return "Готово! Давай ничего не забудем.\nПиши что сложено, а я буду вычеркивать.\n" +
                            "Чтобы посмотреть весь список, введи \"0\"";
                } else {
                    if(menuSymbol.equals("0")){
                        return getThingsString();
                    }
                }
            }
        }
        return "Не понял тебя. Попробуй еще раз";
    }

    //Метод для добавления вещи в список
    public boolean addThing(String text){
        StringBuilder str = new StringBuilder(text.trim());   //Избавляет от необходимости делать пробел между знаком и названием
        str.deleteCharAt(0);
        StringTokenizer tokenizer = new StringTokenizer(str.toString().trim(), " ()");
        List<String> textParts = new ArrayList<>();
        while (tokenizer.hasMoreTokens()){
            textParts.add(tokenizer.nextToken());
        }
        if(textParts.size() == 2){
            selectedThingsList.add(new Thing(textParts.get(0), textParts.get(1)));
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
            if(thing.getNameThing().equalsIgnoreCase(deleteThingName)){
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
        getThings();                            //Получить полное множество вещей
        for (Thing thing : thingsSet){          //Формирование множества выбранных вещей.
            if(thing.tagsList.contains(requestTrip)){   //Проверка соответствия тегам
                selectedThingsList.add(thing);
            }
        }
        selectedThingsList.sort(categoryComparator);
    }

    //Обнуление списков и множеств и новой поездке  TODO:
//    public void clear


    //Метод для организации меню выбора
/*    public void getSelectionMenuList(){
        nextList.clear();
        nextList.add("+");
        nextList.add("-");
        nextList.add("ok");
    }*/

    //Перечень начальных вариантов поездок
    public List<String> getDefDirectionList(){
        getDefTrip();                                           //Считывание из файла
//        for(DefTrip dt: defTripList){                           //Выбор начальных вариантов. Формирование списка
//            if(!directionList.contains(dt.direction)) {
//                directionList.add(dt.direction);
//            }
//        }
        //Пример использования лямбда-выражения
        defTripList.forEach(dt -> {if(!directionList.contains(dt.direction)) {
                                    directionList.add(dt.direction);}});
        return directionList;
    }

/*        getDefTrip();                                               //Считывание из файла
        StringBuilder directionLine = new StringBuilder("");
        int i = 1;
        for(DefTrip dt: defTripList){                               //Выбор начальных вариантов. Формирование списка
            if(!directionList.contains(dt.direction)) {
                directionList.add(dt.direction);
                directionLine.append(i).append(". ").append(dt.direction).append("\n");
                i++;
            }
        }
        directionLine.deleteCharAt(directionLine.length()-1);
        //Формирование строки для вывода начальных вариантов поездок
//        for(int i = 0; i < directionList.size(); i++){
//            if(i == directionList.size() - 1){
//                directionLine.append(i+1).append(". ").append(directionList.get(i));
//            }else {
//                directionLine.append(i+1).append(". ").append(directionList.get(i)).append("\n");
//            }
//
//        }
        return new ChooseDirection(directionLine.toString(), directionList);
    }*/

    //Формирование списка уточнений. Зависит от выбранной поездки
    public List<String> getCorrectionLevelOneList(String direction) {
        List<String> correctionLevelOneList = new ArrayList<>();
        for (DefTrip dt : defTripList) {
            if (!correctionLevelOneList.contains(dt.correctionLevelOne) && dt.direction.equals(direction)) {
                correctionLevelOneList.add(dt.correctionLevelOne);
            }
//            correctionLevelOneSet.add(dt.correctionLevelOne);
        }
        return correctionLevelOneList;
    }

    //Формирование строки для вывода списка выбранных вещей
    public String getThingsString(){
        if(selectedThingsList.size() > 0) {
            int i = 1;
            StringBuilder thingsLine = new StringBuilder("");
            for (Thing thing : selectedThingsList) {
                thingsLine.append(i).append(". ").append(thing.toString()).append("\n");
                i++;
            }
            return thingsLine.deleteCharAt(thingsLine.length() - 1).toString();
        }else return "Список пуст";
    }

    //Формирование строки для вывода начальных вариантов поездок
/*    public String getDefDirectionString() {
//        getDefDirectionList();                              //Перед определением строки нужно сформировать список
//        StringBuilder directionLine = new StringBuilder("");
//        for (int i = 0; i < directionList.size(); i++) {
//            if (i == directionList.size() - 1) {
//                directionLine.append(i + 1).append(". ").append(directionList.get(i));
//            } else {
//                directionLine.append(i + 1).append(". ").append(directionList.get(i)).append("\n");
//            }
//
//        }
//        return directionLine.toString();
        return getStringFromList(directionList);
    }*/

    //Формирование строки для уточнения. Зависит от выбранной поездки
/*    public String getCorrectionLevelOneString(String direction){
//    public ChooseDirection getCorrectionLevelOneString(String direction){
//        StringBuilder correctionLine = new StringBuilder("");
//        List<String> list = getCorrectionLevelOneList(direction);
        return getStringFromList(getCorrectionLevelOneList(direction));
//        for(int i = 0; i < list.size(); i++){
//            if(i == list.size() - 1){
//                correctionLine.append(i + 1).append(". ").append(list.get(i));
//            }else {
//                correctionLine.append(i + 1).append(". ").append(list.get(i)).append("\n");
//            }
//        }

//        return correctionLine.toString();
    }*/

    //Метод для формирования строки вывода из списка
    public String getStringFromList(List<?> list){
        if(list.size() > 0) {
//            int i = 1;
            StringBuilder outputString = new StringBuilder("");
            for(int i = 0; i < list.size(); i++){
                if(i == list.size() - 1){
                    outputString.append(i + 1).append(". ").append(list.get(i));
                }else {
                    outputString.append(i + 1).append(". ").append(list.get(i)).append("\n");
                }
            }
//            for (T element : list) {
//                outputString.append(i).append(". ").append(element.toString()).append("\n");
//                i++;
//            }
//            return outputString.deleteCharAt(outputString.length() - 1).toString();
            return outputString.toString();
        }else return "Список пуст";
    }

}

//Описание поездки по умолчанию
class DefTrip{
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
}


//class ChooseDirection{
/*    private String directionString;
    private List<String> directionList;

    public ChooseDirection(String directionString, List<String> directionList) {
        this.directionString = directionString;
        this.directionList = directionList;
    }

    public String getDirectionString() {
        return directionString;
    }

    public List<String> getDirectionList() {
        return directionList;
    }
}*/
