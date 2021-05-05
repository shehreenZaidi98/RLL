

package com.wmsweb.commonData;

import javax.persistence.*;

@Entity
@Table(name = "common_data")
public class CommonData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long order_id;
    private String type;
    private String priority;
    private String date;
    private int status;
    private String truck_bay_no;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public String getTruck_bay_no() {
		return truck_bay_no;
	}

	public void setTruck_bay_no(String truck_bay_no) {
		this.truck_bay_no = truck_bay_no;
	}
    

}
