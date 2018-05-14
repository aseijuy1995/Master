package nom.cp101.master.master.Account.MyAccount;

/**
 * Created by chunyili on 2018/5/14.
 */

public class Profession {

    int profession_id;
    String profession_name;

    public Profession(int profession_id, String profession_name) {
        super();
        this.profession_id = profession_id;
        this.profession_name = profession_name;
    }

    public int getProfession_id() {
        return profession_id;
    }

    public void setProfession_id(int profession_id) {
        this.profession_id = profession_id;
    }

    public String getProfession_name() {
        return profession_name;
    }

    public void setProfession_name(String profession_name) {
        this.profession_name = profession_name;
    }

}