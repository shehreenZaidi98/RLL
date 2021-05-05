// 
// Decompiled by Procyon v0.5.36
// 

package com.wmsweb.transport;

import javax.persistence.*;

@Entity
@Table(name = "transport")
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;
    private String driver_name;
    private String contact_no;
    private String vehicle_no;
    private String party_name;
    private String address;
    private String state;
    private int status;
    private String date;
    private String permit_no;
    private String truck_bay_no;
    private String total_qty;
    private String vehicle_type;
    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public long getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getDriver_name() {
        return this.driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getContact_no() {
        return this.contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getVehicle_no() {
        return this.vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getParty_name() {
        return this.party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPermit_no() {
        return this.permit_no;
    }

    public void setPermit_no(String permit_no) {
        this.permit_no = permit_no;
    }

    public String getTruck_bay_no() {
        return this.truck_bay_no;
    }

    public void setTruck_bay_no(String truck_bay_no) {
        this.truck_bay_no = truck_bay_no;
    }
}
