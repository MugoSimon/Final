package lastie_wangechian_Final.com.Vendor.MainActivity;

public class InsertData {

    String item_name;
    String item_price;
    String item_type;
    String item_image;
    String id;

    public InsertData(String item_name, String item_price, String item_type, String item_image, String id) {
        //empty constructor
    }

    public InsertData(String item_name, String item_price, String item_type, String item_image) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_type = item_type;
        this.id = id;
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public String getItem_type() {
        return item_type;
    }

    public String getItem_image() {
        return item_image;
    }

    public String getId() {
        return id;
    }
}
