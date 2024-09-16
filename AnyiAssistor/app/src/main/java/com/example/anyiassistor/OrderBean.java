package com.example.anyiassistor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrderBean  implements Parcelable{
    private String id;
    private String order_id;
    private String contact_man;
    private String contact;
    private String start_place;
    private String dest_place;
    private String area_id;

    @Override
    public String toString() {
        return "OrderBean{" +
                "id='" + id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", contact_man='" + contact_man + '\'' +
                ", contact='" + contact + '\'' +
                ", start_place='" + start_place + '\'' +
                ", dest_place='" + dest_place + '\'' +
                ", area_id='" + area_id + '\'' +
                ", appoint_start_time='" + appoint_start_time + '\'' +
                ", appoint_end_time='" + appoint_end_time + '\'' +
                ", appoint_date='" + appoint_date + '\'' +
                ", appoint_date_txt='" + appoint_date_txt + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", way='" + way + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", total_fee_txt='" + total_fee_txt + '\'' +
                ", remark='" + remark + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getContact_man() {
        return contact_man;
    }

    public void setContact_man(String contact_man) {
        this.contact_man = contact_man;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStart_place() {
        return start_place;
    }

    public void setStart_place(String start_place) {
        this.start_place = start_place;
    }

    public String getDest_place() {
        return dest_place;
    }

    public void setDest_place(String dest_place) {
        this.dest_place = dest_place;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getAppoint_start_time() {
        return appoint_start_time;
    }

    public void setAppoint_start_time(String appoint_start_time) {
        this.appoint_start_time = appoint_start_time;
    }

    public String getAppoint_end_time() {
        return appoint_end_time;
    }

    public void setAppoint_end_time(String appoint_end_time) {
        this.appoint_end_time = appoint_end_time;
    }

    public String getAppoint_date() {
        return appoint_date;
    }

    public void setAppoint_date(String appoint_date) {
        this.appoint_date = appoint_date;
    }

    public String getAppoint_date_txt() {
        return appoint_date_txt;
    }

    public void setAppoint_date_txt(String appoint_date_txt) {
        this.appoint_date_txt = appoint_date_txt;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTotal_fee_txt() {
        return total_fee_txt;
    }

    public void setTotal_fee_txt(String total_fee_txt) {
        this.total_fee_txt = total_fee_txt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String appoint_start_time;
    private String appoint_end_time;
    private String appoint_date;
    private String appoint_date_txt;
    private String start_time;
    private String end_time;
    private String way;
    private String total_fee;
    private String total_fee_txt;
    private String remark;
    private String state;

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    private String finish_time;
    private String company;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    private String account;

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(order_id);
        parcel.writeString(contact_man);
        parcel.writeString(contact);
        parcel.writeString(start_place);
        parcel.writeString(dest_place);
        parcel.writeString(area_id);
        parcel.writeString(appoint_start_time);
        parcel.writeString(appoint_end_time);
        parcel.writeString(appoint_date);
        parcel.writeString(appoint_date_txt);
        parcel.writeString(start_time);
        parcel.writeString(end_time);
        parcel.writeString(way);
        parcel.writeString(total_fee);
        parcel.writeString(total_fee_txt);
        parcel.writeString(remark);
        parcel.writeString(state);
    }
    public OrderBean() {};
    protected OrderBean(Parcel in) {
        id = in.readString();
        order_id = in.readString();
        contact_man = in.readString();
        contact = in.readString();
        start_place = in.readString();
        dest_place = in.readString();
        area_id = in.readString();
        appoint_start_time = in.readString();
        appoint_end_time = in.readString();
        appoint_date = in.readString();
        appoint_date_txt = in.readString();
        start_time = in.readString();
        end_time = in.readString();
        way = in.readString();
        total_fee = in.readString();
        total_fee_txt = in.readString();
        remark = in.readString();
        state = in.readString();
    }
    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel in) {
            return new OrderBean(in);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };

}
