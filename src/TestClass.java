import java.util.List;
import java.util.Random;

public class TestClass {
    public static void main(String[] args) {


    }

    PreparationToTrip pmt = new PreparationToTrip();

    public String printHello(){
        return "привет";
    }

    public String printDefDirection(){
        List<String> list = pmt.getDirectionList();
        return list.get(new Random().nextInt(list.size()));
    }
    public String printCorrection(String text){
        List<String> list = pmt.getCorrectionList(text);
        return list.get(new Random().nextInt(list.size()));
    }

    public String printOk(){
        return "готово";
    }

    public String printThing(List<Thing> thingList){
        return thingList.get(new Random().nextInt(thingList.size())).getNameThing();
    }
}
