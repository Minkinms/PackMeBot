import java.time.LocalDate;

public class Trip {
    private String nameTrip;      //Название поездки
    private String directionTrip;     //Направление поездки, "куда"
    private LocalDate whenTrip;   //Когда поездка
    private int durationTrip;     //Продолжительность поездки
    private int numberPerson;       //Количество человек
    private boolean kids;           //Есть ли дети?

/*    public Trip(String name, String whereTravel, LocalDate whenTravel, int durationTravel, int numberPerson, boolean kids) {
        this.name = name;
        this.directionTrip = whereTravel;
        this.whenTravel = whenTravel;
        this.durationTravel = durationTravel;
        this.numberPerson = numberPerson;
        this.kids = kids;
    }*/

    public Trip(String nameTrip, String whereTrip) {
        this.nameTrip = nameTrip;
        this.directionTrip = whereTrip;
    }
}
