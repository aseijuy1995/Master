package nom.cp101.master.master.CourseArticle;

import java.io.Serializable;
import java.util.List;

//包裝各類別對應的項目
public class ProfessionData implements Serializable {

    String profession_category;
    List<String> profession_item;


    public ProfessionData(String profession_category, List<String> profession_item) {
        this.profession_category = profession_category;
        this.profession_item = profession_item;
    }

    public String getProfession_category() {
        return profession_category;
    }

    public void setProfession_category(String profession_category) {
        this.profession_category = profession_category;
    }

    public List<String> getProfession_item() {
        return profession_item;
    }

    public void setProfession_item(List<String> profession_item) {
        this.profession_item = profession_item;
    }
}
