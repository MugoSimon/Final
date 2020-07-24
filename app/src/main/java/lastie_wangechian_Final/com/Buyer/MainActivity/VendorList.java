package lastie_wangechian_Final.com.Buyer.MainActivity;

class VendorList {

    String username;
    String location;
    String vendor_image;

    public VendorList() {

        //empty constructor

    }

    public VendorList(String username, String location, String vendor_image) {

        this.username = username;
        this.location = location;
        this.vendor_image = vendor_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVendor_image() {
        return vendor_image;
    }

    public void setVendor_image(String vendor_image) {
        this.vendor_image = vendor_image;
    }

}
