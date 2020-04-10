package lastie_wangechian_Final.com.Vendor;

import java.sql.Time;

public class OrderVendorNote {

    String container_name;
    String total_amount;
    String no_of_containers;
    String container_image;
    String buyer_name;
    Time time_of_order;

    public OrderVendorNote() {

        //empty constructor
    }

    public OrderVendorNote(String container_name, String total_amount, String no_of_containers, String container_image, String buyer_name, Time time_of_order) {
        this.container_name = container_name;
        this.total_amount = total_amount;
        this.no_of_containers = no_of_containers;
        this.container_image = container_image;
        this.buyer_name = buyer_name;
        this.time_of_order = time_of_order;
    }

    public String getContainer_name() {
        return container_name;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getNo_of_containers() {
        return no_of_containers;
    }

    public String getContainer_image() {
        return container_image;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public Time getTime_of_order() {
        return time_of_order;
    }
}
