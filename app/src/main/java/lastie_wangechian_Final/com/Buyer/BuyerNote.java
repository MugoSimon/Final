package lastie_wangechian_Final.com.Buyer;

public class BuyerNote {

    private String vendor_name;
    private String container_name;
    private String container_image;

    public BuyerNote() {

        //this is an empty constructor
    }

    public BuyerNote(String vendor_name, String container_name, String container_image) {

        this.vendor_name = vendor_name;
        this.container_name = container_name;
        this.container_image = container_image;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public String getContainer_name() {
        return container_name;
    }

    public String getContainer_image() {
        return container_image;
    }
}
