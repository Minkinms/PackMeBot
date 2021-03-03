import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class PackMeToTrip {

    public String getDefTrip(){
        String result = "Отпуск;\nКомандировка,\nНа природу";
        List<DefTrip> defTripList = new ArrayList<>();
        defTripList.add(new DefTrip("Командировка", "Другой город"));
        defTripList.add(new DefTrip("Командировка", "Другая страна"));
        defTripList.add(new DefTrip("Отпуск", "Другая страна"));
        defTripList.add(new DefTrip("Отпуск", "Россия"));
        defTripList.add(new DefTrip("На природу", "Кемпинг"));
        defTripList.add(new DefTrip("На природу", "Пикник"));

        return result;
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
