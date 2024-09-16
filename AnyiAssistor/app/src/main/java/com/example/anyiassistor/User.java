package com.example.anyiassistor;



import java.io.Serializable;
import java.util.List;


public class User {


    private long uid;
    private String openid;

    private String phone;

    @Override
    public String toString() {
        return "User{" +
                "openid='" + openid + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", company='" + company + '\'' +
                '}';
    }

    private String role;

    private String company;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    private String name;

    private String location;

    private String Useraccount;

    private String passWord;

    public String getUseraccount() {
        return Useraccount;
    }

    public void setUseraccount(String useraccount) {
        Useraccount = useraccount;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public List<OrderBean> getOrderLIst() {
        return OrderLIst;
    }

    public void setOrderLIst(List<OrderBean> orderLIst) {
        OrderLIst = orderLIst;
    }

    private List<OrderBean> OrderLIst;

    private String imageUrl;

    public void setUserTYpe(int userTYpe) {
        this.userTYpe = userTYpe;
    }

    public int getUserTYpe() {
        return userTYpe;
    }

    private int userTYpe = 0;
    private String gender;

    private String avatar;
    private String pinyin;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

}
