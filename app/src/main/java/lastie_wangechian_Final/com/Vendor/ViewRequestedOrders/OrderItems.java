package lastie_wangechian_Final.com.Vendor.ViewRequestedOrders;

public class OrderItems {
    String item_name;
    String item_price;
    String item_type;
    String item_quantity;
    String item_address;
    String item_image;
    String phone;
    String username;
    String time_of_order;

    public OrderItems() {
        //empty constructor
        //its necessary
    }

    public OrderItems(String item_name, String item_price, String item_address, String item_type, String phone, String item_quantity, String item_image, String username, String time_of_order) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_type = item_type;
        this.item_quantity = item_quantity;
        this.item_address = item_address;
        this.item_image = item_image;
        this.username = username;
        this.phone = phone;
        this.time_of_order = time_of_order;
    }


    public String getItem_address() {
        return item_address;
    }

    public void setItem_address(String item_address) {
        this.item_address = item_address;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime_of_order() {
        return time_of_order;
    }

    public void setTime_of_order(String time_of_order) {
        this.time_of_order = time_of_order;
    }


}
