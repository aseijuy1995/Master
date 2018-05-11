package nom.cp101.master.master.Account.MyCourse.CLASS;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chunyili on 2018/4/22.
 */

public class Course implements Serializable {
    private int course_id;
    private String user_id;
    private int profession_id;
    private int course_category_id;
    private String course_name;
    private Date course_date;
    private String course_content;
    private int course_price;
    private String course_need;
    private String course_qualification;
    private String course_location;
    private Date course_apply_deadline;
    private int course_people_number;
    private int course_applied_number;
    private int course_image_id;
    private String course_note;
    private int course_status_id;


    // Update courseProfile
    public Course(int course_id, String user_id,
                  int course_category_id, Date course_date,
                  Date course_apply_deadline, int course_people_number,
                  int course_applied_number, int course_image_id, int course_status_id) {
        this.course_id = course_id;
        this.user_id = user_id;
        this.course_category_id = course_category_id;
        this.course_date = course_date;
        this.course_apply_deadline = course_apply_deadline;
        this.course_people_number = course_people_number;
        this.course_applied_number = course_applied_number;
        this.course_image_id = course_image_id;
        this.course_status_id = course_status_id;
    }

    //Update courseDetail
    public Course(int course_category_id, int profession_id, String course_name, String course_content, int course_price,
                  String course_need, String course_qualification,
                  String course_location, String course_note) {
        this.course_category_id = course_category_id;
        this.profession_id = profession_id;
        this.course_name = course_name;
        this.course_content = course_content;
        this.course_price = course_price;
        this.course_need = course_need;
        this.course_qualification = course_qualification;
        this.course_location = course_location;
        this.course_note = course_note;

    }

    public Course(int course_id, String user_id,
                  int profession_id, int course_category_id, int course_status_id,
                  String course_name, Date course_date,
                  String course_content, int course_price,
                  String course_need, String course_qualification,
                  String course_location, Date course_apply_deadline,
                  int course_people_number, int course_applied_number,
                  int course_image_id, String course_note) {
        this.course_id = course_id;
        this.user_id = user_id;
        this.profession_id = profession_id;
        this.course_category_id = course_category_id;
        this.course_status_id = course_status_id;
        this.course_name = course_name;
        this.course_date = course_date;
        this.course_content = course_content;
        this.course_price = course_price;
        this.course_need = course_need;
        this.course_qualification = course_qualification;
        this.course_location = course_location;
        this.course_apply_deadline = course_apply_deadline;
        this.course_people_number = course_people_number;
        this.course_applied_number = course_applied_number;
        this.course_image_id = course_image_id;
        this.course_note = course_note;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getProfession_id() {
        return profession_id;
    }

    public void setProfession_id(int profession_id) {
        this.profession_id = profession_id;
    }

    public int getCourse_category_id() {
        return course_category_id;
    }

    public void setCourse_category_id(int course_category_id) {
        this.course_category_id = course_category_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Date getCourse_date() {
        return course_date;
    }

    public void setCourse_date(Date course_date) {
        this.course_date = course_date;
    }

    public String getCourse_content() {
        return course_content;
    }

    public void setCourse_content(String course_content) {
        this.course_content = course_content;
    }

    public int getCourse_price() {
        return course_price;
    }

    public void setCourse_price(int course_price) {
        this.course_price = course_price;
    }

    public String getCourse_need() {
        return course_need;
    }

    public void setCourse_need(String course_need) {
        this.course_need = course_need;
    }

    public String getCourse_qualification() {
        return course_qualification;
    }

    public void setCourse_qualification(String course_qualification) {
        this.course_qualification = course_qualification;
    }

    public String getCourse_location() {
        return course_location;
    }

    public void setCourse_location(String course_location) {
        this.course_location = course_location;
    }

    public Date getCourse_apply_deadline() {
        return course_apply_deadline;
    }

    public void setCourse_apply_deadline(Date course_apply_deadline) {
        this.course_apply_deadline = course_apply_deadline;
    }

    public int getCourse_people_number() {
        return course_people_number;
    }

    public void setCourse_people_number(int course_people_number) {
        this.course_people_number = course_people_number;
    }

    public int getCourse_applied_number() {
        return course_applied_number;
    }

    public void setCourse_applied_number(int course_applied_number) {
        this.course_applied_number = course_applied_number;
    }

    public int getCourse_image_id() {
        return course_image_id;
    }

    public void setCourse_image_id(int course_image_id) {
        this.course_image_id = course_image_id;
    }

    public String getCourse_note() {
        return course_note;
    }

    public void setCourse_note(String course_note) {
        this.course_note = course_note;
    }

    public int getCourse_status_id() {
        return course_status_id;
    }

    public void setCourse_status_id(int course_status_id) {
        this.course_status_id = course_status_id;
    }
}
