package lastie_wangechian_Final.com.Buyer.AddtoCart;

public class CartList {

    private String export_name;
    private String export_image;
    private String export_price;
    private String export_type;
    private String list_id;
    private String vendor_id;

    public CartList() {
        //empty constructor
    }

    public CartList(String export_name, String export_image, String export_price, String export_type, String list_id, String vendor_id) {
        this.export_name = export_name;
        this.export_image = export_image;
        this.export_price = export_price;
        this.export_type = export_type;
        this.list_id = list_id;
        this.vendor_id = vendor_id;
    }

    public String getExport_name() {
        return export_name;
    }

    public void setExport_name(String export_name) {
        this.export_name = export_name;
    }

    public String getExport_image() {
        return export_image;
    }

    public void setExport_image(String export_image) {
        this.export_image = export_image;
    }

    public String getExport_price() {
        return export_price;
    }

    public void setExport_price(String export_price) {
        this.export_price = export_price;
    }

    public String getExport_type() {
        return export_type;
    }

    public void setExport_type(String export_type) {
        this.export_type = export_type;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }
}
