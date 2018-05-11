package nom.cp101.master.master.Account.MyCourse.CLASS;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chunyili on 2018/4/29.
 */

public class Apply implements Serializable {

    private int apply_id;

    private int course_id;

    private String user_id;

    private int apply_status_id;

    private Date apply_time;

    private String course_name;

    private String user_name;

    private String apply_status_name;

    private Date course_date;

    private String course_status_name;


    //find by user name
    public Apply(String course_name, Date course_date, String course_status_name) {
        super();
        this.course_name = course_name;
        this.course_date = course_date;
        this.course_status_name = course_status_name;
    }


    //find by course
    public Apply(int course_id, int apply_id, String user_name, String apply_status_name, Date apply_time) {
        super();
        this.course_id = course_id;
        this.apply_id = apply_id;
        this.user_name = user_name;
        this.apply_status_name = apply_status_name;
        this.apply_time = apply_time;
    }


    public Apply(int apply_id, int course_id, String user_id, int apply_status_id, Date apply_time) {
        super();
        this.apply_id = apply_id;
        this.course_id = course_id;
        this.user_id = user_id;
        this.apply_status_id = apply_status_id;
        this.apply_time = apply_time;
    }


    public int getApply_id() {
        return apply_id;
    }


    public void setApply_id(int apply_id) {
        this.apply_id = apply_id;
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


    public int getApply_status_id() {
        return apply_status_id;
    }


    public void setApply_status_id(int apply_status_id) {
        this.apply_status_id = apply_status_id;
    }


    public Date getApply_time() {
        return apply_time;
    }


    public void setApply_time(Date apply_time) {
        this.apply_time = apply_time;
    }


    public String getCourse_name() {
        return course_name;
    }


    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }


    public String getUser_name() {
        return user_name;
    }


    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getApply_status_name() {
        return apply_status_name;
    }


    public void setApply_status_name(String apply_status_name) {
        this.apply_status_name = apply_status_name;
    }


    public Date getCourse_date() {
        return course_date;
    }


    public void setCourse_date(Date course_date) {
        this.course_date = course_date;
    }


    public String getCourse_status_name() {
        return course_status_name;
    }


    public void setCourse_status_name(String course_status_name) {
        this.course_status_name = course_status_name;
    }

}