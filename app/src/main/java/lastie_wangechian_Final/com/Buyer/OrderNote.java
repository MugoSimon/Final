package lastie_wangechian_Final.com.Buyer;

import java.sql.Time;

public class OrderNote {

    String container_name;
    String total_amount;
    String no_of_containers;
    String container_image;
    String vendor_name;
    Time time_of_order;

    public OrderNote() {

        //empty constructor;

    }

    public OrderNote(String container_name, String total_amount, String no_of_containers, String container_image, String vendor_name, Time time_of_order) {
        this.container_name = container_name;
        this.total_amount = total_amount;
        this.no_of_containers = no_of_containers;
        this.container_image = container_image;
        this.vendor_name = vendor_name;
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

    public String getVendor_name() {
        return vendor_name;
    }

    public Time getTime_of_order() {
        return time_of_order;
    }
}
