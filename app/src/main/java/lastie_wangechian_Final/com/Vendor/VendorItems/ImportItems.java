package lastie_wangechian_Final.com.Vendor.VendorItems;

public class ImportItems {

    String item_name;
    String item_price;
    String item_type;
    String item_image;
    String id;

    public ImportItems() {
        //empty constuctor
    }

    public ImportItems(String item_name, String id, String item_price, String item_type, String item_image) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_type = item_type;
        this.id = id;
        this.item_image = item_image;
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

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
