import java.util.List;
import java.util.Random;

public class TestClass {
    public static void main(String[] args) {


    }

    PackMeToTrip pmt = new PackMeToTrip();

    public String printHello(){
        return "привет";
    }

    public String printDefDirection(){
        List<String> list = pmt.getDefDirectionList();
        return list.get(new Random().nextInt(list.size()));
    }
    public String printCorrection(String text){
        List<String> list = pmt.getCorrectionLevelOneList(text);
        return list.get(new Random().nextInt(list.size()));
    }
}
