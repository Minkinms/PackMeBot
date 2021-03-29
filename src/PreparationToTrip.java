import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class PreparationToTrip {

    //Переменные класса
    private List<Thing> selectedThingsList = new ArrayList<>();     //Список выбарнных вещей
    private List<Thing> tookThingsList = new ArrayList<>();         //Список взятых вещей
    //Пока компаратор отдельный, на случай если понадобится делать разные
    private Comparator<Thing> categoryComparator = new Comparator<>() {   //TODO: Здесь или отдельный файл?
        @Override
        public int compare(Thing o1, Thing o2) {
            return o1.getCategoryThing().compareTo(o2.getCategoryThing());
        }
    };

    public Stage stage;                                             //Этапы работы с ботом
    private StringBuilder requestString = new StringBuilder();      //Строка запроса направления
    private List<String> nextList;                                  //Список для последующего выбора
    private TripsData tripsData;
    private UserTrip userTrip = new UserTrip();

    //Конструктор класса
    public PreparationToTrip() {
        this.nextList = new ArrayList<>();
        nextList.add("привет");
        nextList.add("поездка");
        nextList.add("/start");
        this.stage = Stage.HELLO;                                       //Первая стадия сбора (Приветствие)
        String tripHistoryPath = "C:\\Java\\Progwards\\PackMe\\src\\TripHistory.txt";
        checkConnectDataFile(tripHistoryPath);
    }

    //Этапы (стадии) сборов
    public enum Stage {
        HELLO,                      //Стадия приветствия
        CHOOSE_DEFAULT_DIRECTION,   //Стадия выбора первоначального направления
        CHOOSE_CORRECTION,          //Стадия выбора уточнения по направлению
        CHOOSE_THINGS,              //Стадия выбора вещей для направления
        CONTROL,                    //Стадия контроля сбора вещей
        ERROR                       //Ошибка
    }

    //Метод для проверки соединения с файлом базы данных
    private void checkConnectDataFile(String tripHistoryPath) {
        try {
            tripsData = new TripsData(tripHistoryPath);
        } catch (FileNotFoundException exception) {
            stage = Stage.ERROR;
        }
    }

    //Метод для организации взаимодействия с классом бота
    public String choose(String text) {
        if (text.trim().toLowerCase().equals("конец")) {
            toStart();
            return "Подготовка к этой поездке завершена. Чтобы начать другую, напиши \"Поездка\"";
        } else {
            switch (stage) {
                case HELLO -> {
                    return doHelloStage(text);
                }
                case CHOOSE_DEFAULT_DIRECTION -> {
                    return doChooseDirectionStage(text);
                }
                case CHOOSE_CORRECTION -> {
                    return doChooseCorrectionStage(text);
                }
                case CHOOSE_THINGS -> {
                    return correctSelectedThingsList(text);
                }
                case CONTROL -> {
                    return control(text);
                }
                case ERROR -> {
                    return "Извините, но работа временно невозможна :(";
                }
            }
        }
        return "Не понял тебя. Попробуй еще раз";
    }

    //Подготовка к стадии приветствия (Stage.HELLO).
    //Проверка введенного слова, подготовка списка для следующего выбора
    private String doHelloStage(String text) {
        if (nextList.contains(text.toLowerCase().trim())) {
            nextList.clear();
            nextList = getDirectionList();
            stage = Stage.CHOOSE_DEFAULT_DIRECTION;
            return "Привет! Куда собираешься? Предлагаю варианты:\n" + getStringFromList(nextList) +
                    "\nЕсли варианты не подходят, можешь ввести свой, написав \"+Куда\" (Например, \"+ К бабушке\")";
        } else return "Не понял тебя. Попробуй еще раз";
    }

    private String doChooseDirectionStage(String text) {
        if (nextList.contains(text)) {
            nextList = getCorrectionList(text);
            stage = Stage.CHOOSE_CORRECTION;
            requestString.append(text).append("/");
            userTrip.setDirection(text);
            return "Давай уточним. Предлагаю варианты:\n" + getStringFromList(nextList) +
                    "\nНо можешь ввести свой.";
        } else {
            if (!text.isBlank()) {
                StringBuilder addDirection = new StringBuilder(text);
                if (addDirection.charAt(0) == "+".charAt(0)) {        //TODO: Такой вариант сравнения???
                    addDirection.deleteCharAt(0);
                    String direction = addDirection.toString().trim();
                    if (!nextList.contains(direction)) {
                        requestString.append(direction).append("/");
                        stage = Stage.CHOOSE_CORRECTION;
                        userTrip.setDirection(direction);
                        return "Давай уточним. Например: для \"Командировка\" можно написать \"Россия\"\n" +
                                "Введи уточнение для направления";
                    } else return "Такое уже есть в списке";
                }
            }
        }
        return "Не понял тебя. Попробуй еще раз";
    }

    private String doChooseCorrectionStage(String text) {
        stage = Stage.CHOOSE_THINGS;
        if (!text.isBlank()) {
            requestString.append(text);
            userTrip.setCorrection(text);
        } else {
            requestString.append("-");
            userTrip.setCorrection("-");
        }
        getSelectedThingsList(requestString.toString().toLowerCase());
        nextList.clear();
        return "Отлично! Давай перейдем к вещам. Вот мой совет:\n" + getStringFromList(selectedThingsList) +
                "\nЧтобы добавить пиши \"+ Название (Категория)\", удалить пиши \"- Название\".\n" +
                "Если готово, пиши \"Готово\".";
    }

    //Сброс всех данных по поездке
    private void toStart() {
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
                    moveTing(thing);
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
    private void moveTing(Thing thing) {
        selectedThingsList.remove(thing);
        selectedThingsList.sort(categoryComparator);
        tookThingsList.add(thing);
        tookThingsList.sort(categoryComparator);
    }

    //Метод для корректировки списка выбарнных вещей
    private String correctSelectedThingsList(String text) {
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
    private String checkMenuSymbol(String text) {
        String menuSymbol = String.valueOf(text.charAt(0));     //TODO: использовать equals или ==
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
    private boolean addThing(String text) {
        StringBuilder str = new StringBuilder(text);
        str.deleteCharAt(0);
        StringTokenizer tokenizer = new StringTokenizer(str.toString().trim(), "()");
        List<String> textParts = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            textParts.add(tokenizer.nextToken());
        }
        if (textParts.size() == 2) {
            selectedThingsList.add(new Thing(textParts.get(0).trim(), textParts.get(1).trim()));
            selectedThingsList.sort(categoryComparator);
            return true;
        }
        return false;
    }

    //Метод для удаления вещи из списка выбранных
    private boolean deleteThing(String text) {
        StringBuilder str = new StringBuilder(text.toLowerCase());
        str.deleteCharAt(0);
        String deleteThingName = str.toString().trim();
        for (Thing thing : selectedThingsList) {
            if (thing.getNameThing().trim().equalsIgnoreCase(deleteThingName)) {
                selectedThingsList.remove(thing);
                selectedThingsList.sort(categoryComparator);
                return true;
            }
        }
        return false;
    }

    //Множество вещей соответствующих запросу
    private void getSelectedThingsList(String requestTrip) {
        List<Thing> thingsList = tripsData.getTingsList();
        selectedThingsList.clear();
        for (Thing thing : thingsList) {
            if (thing.tagsMap.containsKey(requestTrip)) {
                selectedThingsList.add(thing);
            }
        }
        selectedThingsList.sort(new Comparator<Thing>() {   //Сортировка по кол-ву использований в поездках
            @Override
            public int compare(Thing o1, Thing o2) {
                if (!o1.getCategoryThing().equals(o2.getCategoryThing())) {
                    return o1.getCategoryThing().compareTo(o2.getCategoryThing());
                } else {
                    return o2.tagsMap.get(requestTrip).compareTo(o1.tagsMap.get(requestTrip));
                }
            }
        });
    }

    //Перечень начальных вариантов поездок
    //public для использования в класе Test
    public List<String> getDirectionList() {
        List<String> directionList = new ArrayList<>();
        List<Trip> tripList = new ArrayList<>(tripsData.getFrequentTripsList(null, 3));
        tripList.forEach(dt -> {
            if (!directionList.contains(dt.getDirection())) {
                directionList.add(dt.getDirection());
            }
        });
        return directionList;
    }

    //Формирование списка уточнений. Зависит от выбранной поездки
    //public для использования в класе Test
    public List<String> getCorrectionList(String direction) {
        List<String> correctionList = new ArrayList<>();
        List<Trip> tripList = new ArrayList<>(tripsData.getFrequentTripsList(direction, 5));
        tripList.forEach(dt -> {
            if (!correctionList.contains(dt.getCorrection())) {
                correctionList.add(dt.getCorrection());
            }
        });
        return correctionList;
    }

    //Метод для формирования строки вывода из списка
    private String getStringFromList(List<?> list) {
        if (list.size() > 0) {
            StringBuilder outputString = new StringBuilder("");
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    outputString.append(i + 1).append(". ").append(list.get(i));
                } else {
                    outputString.append(i + 1).append(". ").append(list.get(i)).append("\n");
                }
            }
            return outputString.toString();
        } else return "Список пуст";
    }

    //Метод для автотеста
    public List<Thing> readSelectedThingsList() {
        return selectedThingsList;
    }

}



