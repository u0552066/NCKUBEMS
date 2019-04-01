package com.example.jason.nckubems;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Yan on 2017/3/13.
 */

public class JsonStr {
    @SerializedName("action")
    private String action;

    @SerializedName("token")
    private String token;

    @SerializedName("other_input")
    private String other_input;

    @SerializedName("captcha")
    private String captcha;

    @SerializedName("account")
    private String account;

    @SerializedName("password")
    private String password;

    @SerializedName("cluster")
    private String cluster;

    @SerializedName("category")
    private String category;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("office_id")
    private String office_id;

    @SerializedName("co2_upper_bound")
    private String co2_upper_bound;

    @SerializedName("co2_lower_bound")
    private String co2_lower_bound;

    @SerializedName("lux_upper_bound")
    private String lux_upper_bound;

    @SerializedName("lux_lower_bound")
    private String lux_lower_bound;

    @SerializedName("motion_off_waiting")
    private String motion_off_waiting;

    @SerializedName("type")
    private String type;

    @SerializedName("floor")
    private String floor;

    //房型
    @SerializedName("genre")
    private String genre;

    @SerializedName("value")
    private String value;

    @SerializedName("field")
    private String field;

    @SerializedName("address")
    private String address;

    @SerializedName("brand")
    private String brand;


    @SerializedName("place_id")
    private String place_id;

    @SerializedName("checked")
    private String checked;

    @SerializedName("power")
    private String power;

    @SerializedName("power_setting")
    private String power_setting;

    @SerializedName("setting")
    private String setting;

    @SerializedName("recover")
    private String recover;

    @SerializedName("start_hour")
    private String start_hour;

    @SerializedName("start_min")
    private String start_min;

    @SerializedName("end_hour")
    private String end_hour;

    @SerializedName("end_min")
    private String end_min;

    @SerializedName("mode")
    private String mode;

    @SerializedName("remote_charging_setting")
    private String remote_charging_setting;

    @SerializedName("manual_initial_soc")
    private String manual_initial_soc;

    @SerializedName("charging_speed")
    private String charging_speed;

    @SerializedName("manual_final_soc")
    private String manual_final_soc;

    @SerializedName("pwm_setting")
    private String pwm_setting;

    @SerializedName("comfort")
    private String comfort;

    @SerializedName("air_condition_switch")
    private String air_condition_switch;

    @SerializedName("light_switch")
    private String light_switch;

    @SerializedName("air_cleaner_switch")
    private String air_cleaner_switch;

    @SerializedName("year")
    private String year;

    @SerializedName("month")
    private String month;

    @SerializedName("day")
    private String day;

    @SerializedName("light_dimmer_ch1")
    private String light_dimmer_ch1;

    @SerializedName("light_dimmer_ch2")
    private String light_dimmer_ch2;

    @SerializedName("light_dimmer_ch3")
    private String light_dimmer_ch3;

    @SerializedName("light_dimmer_ch4")
    private String light_dimmer_ch4;

    @SerializedName("light_dimmer_ch5")
    private String light_dimmer_ch5;

    @SerializedName("light_dimmer_ch6")
    private String light_dimmer_ch6;

    @SerializedName("switch")
    private String switching;

    @SerializedName("date")
    private String date;

    @SerializedName("price")
    private String price;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOther_input(String other_input) {
        this.other_input = other_input;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public void setCo2_upper_bound(String co2_upper_bound) {
        this.co2_upper_bound = co2_upper_bound;
    }

    public void setCo2_lower_bound(String co2_lower_bound) {
        this.co2_lower_bound = co2_lower_bound;
    }

    public void setLux_upper_bound(String lux_upper_bound) {
        this.lux_upper_bound = lux_upper_bound;
    }

    public void setLux_lower_bound(String lux_lower_bound) {
        this.lux_lower_bound = lux_lower_bound;
    }

    public void setMotion_off_waiting(String motion_off_waiting) {
        this.motion_off_waiting = motion_off_waiting;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setPower_setting(String power_setting) {
        this.power_setting = power_setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public void setStart_min(String start_min) {
        this.start_min = start_min;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public void setEnd_min(String end_min) {
        this.end_min = end_min;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setRemote_charging_setting(String remote_charging_setting) {
        this.remote_charging_setting = remote_charging_setting;
    }

    public void setManual_initial_soc(String manual_initial_soc) {
        this.manual_initial_soc = manual_initial_soc;
    }

    public void setManual_final_soc(String manual_final_soc) {
        this.manual_final_soc = manual_final_soc;
    }

    public void setCharging_speed(String charging_speed) {
        this.charging_speed = charging_speed;
    }

    public void setPwm_setting(String pwm_setting) {
        this.pwm_setting = pwm_setting;
    }

    public void setComfort(String comfort) {
        this.comfort = comfort;
    }

    public void setAir_condition_switch(String air_condition_switch) {
        this.air_condition_switch = air_condition_switch;
    }

    public void setLight_switch(String light_switch) {
        this.light_switch = light_switch;
    }

    public void setAir_cleaner_switch(String air_cleaner_switch) {
        this.air_cleaner_switch = air_cleaner_switch;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setLight_dimmer_ch1(String light_dimmer_ch1) {
        this.light_dimmer_ch1 = light_dimmer_ch1;
    }

    public void setLight_dimmer_ch2(String light_dimmer_ch2) {
        this.light_dimmer_ch2 = light_dimmer_ch2;
    }

    public void setLight_dimmer_ch3(String light_dimmer_ch3) {
        this.light_dimmer_ch3 = light_dimmer_ch3;
    }

    public void setLight_dimmer_ch4(String light_dimmer_ch4) {
        this.light_dimmer_ch4 = light_dimmer_ch4;
    }

    public void setLight_dimmer_ch5(String light_dimmer_ch5) {
        this.light_dimmer_ch5 = light_dimmer_ch5;
    }

    public void setLight_dimmer_ch6(String light_dimmer_ch6) {
        this.light_dimmer_ch6 = light_dimmer_ch6;
    }

    public void setSwitching(String switching) {
        this.switching = switching;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
