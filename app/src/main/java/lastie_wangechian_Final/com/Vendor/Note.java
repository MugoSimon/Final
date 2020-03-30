package lastie_wangechian_Final.com.Vendor;

public class Note {

    private String vendor_name;
    private String container_name;
    private String container_price;
    private String vendor_userID;
    private String container_image;

    public Note() {

        //empty constructor

    }

    public Note(String vendor_name, String vendor_userID, String container_name, String container_price, String container_image) {


        this.vendor_name = vendor_name;
        this.vendor_userID = vendor_userID;
        this.container_name = container_name;
        this.container_price = container_price;
        this.container_image = container_image;

    }


    public String getVendor_name() {
        return vendor_name;
    }

    public String getContainer_name() {
        return container_name;
    }

    public String getContainer_price() {
        return container_price;
    }

    public String getVendor_userID() {
        return vendor_userID;
    }

    public String getContainer_image() {
        return container_image;
    }
}
