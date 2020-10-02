package lastie_wangechian_Final.com.Buyer.Orders;

public class OrderList {

    private String item_name;
    private String item_type;
    private String item_price;
    private String item_quantity;
    private String item_image;
    private String time_of_order;

    public OrderList() {
        //empty constructor required
    }

    public OrderList(String item_name, String item_type, String item_price, String item_quantity, String item_image, String time_of_order) {
        this.item_name = item_name;
        this.item_type = item_type;
        this.item_price = item_price;
        this.item_quantity = item_quantity;
        this.item_image = item_image;
        this.time_of_order = time_of_order;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getTime_of_order() {
        return time_of_order;
    }

    public void setTime_of_order(String time_of_order) {
        this.time_of_order = time_of_order;
    }
}
