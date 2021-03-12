import java.util.List;
import java.util.Set;

public class Thing {
    private String nameThing;       //Название вещи
    private String categoryThing;   //Категория
    public Set<String> tagsList;
    private String forTravel;       //Необходимость в поездке

    public Thing(String nameThing, String categoryThing) {
        this.nameThing = nameThing;
        this.categoryThing = categoryThing;
    }

    public Thing() {
    }

    public String getNameThing() {
        return nameThing;
    }

    public String getCategoryThing() {
        return categoryThing;
    }

    @Override
    public String toString() {
        return this.getNameThing() + " (" + this.getCategoryThing() + ")";
    }
}
