import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TripsData {
    public static void main(String[] args) throws IOException {
        String tripHistoryPath = "C:\\Java\\Progwards\\PackMe\\src\\TripHistory.txt";
        TripsData tripsData = new TripsData(tripHistoryPath);
//        tripsData.getTingsList();
//        System.out.println(tripsData.getFrequentDirectionSet(3));
//        tripsData.getAllTrips();
//        for(UserTrip userTrip:tripsData.allTrips){System.out.println(userTrip);}
//
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS");
//        LocalDateTime dateTrip = LocalDateTime.now();
//        System.out.println(LocalDateTime.now().toString());
//        System.out.println(dateTimeFormatter.format(dateTrip));
//        String str = dateTimeFormatter.format(LocalDateTime.now());
//        System.out.println(str);

        List<Thing> testThingList = new ArrayList<>();
        testThingList.add(new Thing("Блокнот", "Документы"));
        testThingList.add(new Thing("Визитка", "свое"));
        tripsData.writeTrip(new UserTrip("Москва", "Кремль", testThingList));

        System.out.println("------------------------------------------------------------");
        tripsData.getAllTrips();
        for(UserTrip userTrip:tripsData.allTrips){System.out.println(userTrip);}

    }

    String tripHistoryPath;     //Путь к файлу истории поездок
    List<UserTrip> allTrips = new ArrayList<>();    //Полный список поездок
    DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS");

    //Конструктор
    public TripsData(String tripHistoryPath) throws FileNotFoundException {
        this.tripHistoryPath = tripHistoryPath;
        getAllTrips();
    }

    //Метод для получения множества всех поездок истории
    public void getAllTrips() throws FileNotFoundException {
        this.allTrips.clear();
        File tripHistoryFile = new File(tripHistoryPath);
        Scanner scanner = new Scanner(tripHistoryFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isBlank() && line.startsWith("tr")) {
                String[] arrayTripLine = line.split(",");
                String[] arrayDirection = arrayTripLine[1].split("/");
                allTrips.add(new UserTrip(arrayDirection[0].trim(),             //Direction
                                         arrayDirection[1].trim(),              //Correction
                                         getTripThingsList(arrayTripLine)));    //UserTripThingsList
            }
        }
        scanner.close();

/*        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isBlank() && line.startsWith("tr")) {
                String[] arrayDirectionLine = line.split(",");
                arrayDirection = arrayDirectionLine[1].split("/");
                readThings = true;
            }else {
                if (!line.isBlank() && !line.startsWith("tr") && readThings) {
                    allTrips.add(new UserTrip(arrayDirection[0].trim(),
                            arrayDirection[1].trim(),
                            getThingsList(line)));
                    readThings = false;
                }
            }
        }*/
    }

    //Метод для получения списка вещей.
    // Источник - массив строк, полученный из строки поездки, считанной из файла всех поездок
    public List<Thing> getTripThingsList(String[] arrayTripLine) {
        List<Thing> thingsList = new ArrayList<>();
//        String[] arrayThings = allThingsString.split(",");
        for (int i = 3; i < arrayTripLine.length; i++) {
            StringTokenizer tokenizer = new StringTokenizer(arrayTripLine[i].trim(), "()");
            List<String> textParts = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                textParts.add(tokenizer.nextToken());
            }
            if (textParts.size() == 2) {
                thingsList.add(new Thing(textParts.get(0).trim(), textParts.get(1).trim()));
                thingsList.sort(new CategoryComparator());
            }
        }
        return thingsList;
    }


    //Метод для записи строки поездки в файл
    public void writeTrip(UserTrip userTrip) throws IOException {
        File tripHistoryFile = new File(tripHistoryPath);
        FileWriter tripWriter = new FileWriter(tripHistoryFile, true);
        StringBuilder stringToWrite = new StringBuilder();
        stringToWrite.append("\ntr").append(",");
        stringToWrite.append(userTrip.getDirection()).append("/");
        stringToWrite.append(userTrip.getCorrection()).append(",");
        //Дата записи не используется в работе. Сохранение необходимо для анализа истории при необходимости
        stringToWrite.append(dateTimeFormatter.format(LocalDateTime.now())).append(",");
        for (Thing thing : userTrip.getUserTripThings()) {
            stringToWrite.append(thing.toString()).append(",");
        }
        stringToWrite.deleteCharAt(stringToWrite.length() - 1);
        tripWriter.write(stringToWrite.toString());
        tripWriter.close();
    }

    //Метод для добавления поездки в список
    public void addTrip(List<Trip> tripList, Trip addTrip){
        if (tripList.contains(addTrip)) {
            int count = tripList.get(tripList.indexOf(addTrip)).getUseCount() + 1;
            tripList.get(tripList.indexOf(addTrip)).setUseCount(count);
        } else {
            addTrip.setUseCount(1);
            tripList.add(addTrip);
        }
    }

    //Метод для получения частых направлений поездок
    public List<Trip> getFrequentTripsList(String direction, int numberOfFrequentTrips){
        List<Trip> frequentTrips = new ArrayList<>();
        for (UserTrip userTrip : allTrips) {
            Trip trip = new Trip(userTrip.getDirection(), userTrip.getCorrection());
            if(direction == null) {
                addTrip(frequentTrips, trip);
            }else {
                if(userTrip.getDirection().equals(direction)) {
                    addTrip(frequentTrips, trip);
                }
            }
        }
        frequentTrips.sort(new Comparator<Trip>() {
            @Override
            public int compare(Trip o1, Trip o2) {
                return Integer.compare(o2.getUseCount(), o1.getUseCount());
            }
        });
        if (numberOfFrequentTrips > 0 && numberOfFrequentTrips < frequentTrips.size()) {
                return frequentTrips.subList(0, numberOfFrequentTrips);
        } else return frequentTrips;
    }

    //Метод для получения полного множества вещей
    public List<Thing> getTingsList() {
        List<Thing> thingsList = new ArrayList<>();
        for (UserTrip userTrip : allTrips) {
            for (Thing thing : userTrip.getUserTripThings()) {
                String key = (userTrip.getDirection() + "/" + userTrip.getCorrection()).toLowerCase();
                if (thingsList.contains(thing)) {
////                    thingsList.get(thingsList.indexOf(thing)).tagsList.add(userTrip.getDirection() + "/" + userTrip.getCorrection());
////                    Integer entry = thingsList.get(thingsList.indexOf(thing)).tagsMap.get(key);
//                    Thing thing1 = thingsList.get(thingsList.indexOf(thing));
//                    Integer entry = thing1.tagsMap.get(key)
//                    int entry1  = entry + 1;
//                    thingsList.get(thingsList.indexOf(thing)).tagsMap.put(key,entry);
                    Thing extractedThing = thingsList.get(thingsList.indexOf(thing));
                    if (extractedThing.tagsMap.containsKey(key)) {
                        extractedThing.tagsMap.put(key, extractedThing.tagsMap.get(key) + 1);
                    } else {
                        extractedThing.tagsMap.put(key, 1);
                    }
                } else {
//                    thing.tagsList.add(userTrip.getDirection() + "/" + userTrip.getCorrection());
                    thing.tagsMap.put(key, 1);
                    thingsList.add(thing);

                }
            }
        }
        return thingsList;
    }
}
