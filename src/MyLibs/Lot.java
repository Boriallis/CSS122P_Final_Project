package MyLibs;

public class Lot {
    private int id;
    private String location;
    private double price;
    private double size;
    private String status;
    private Integer ownerId;

    public Lot(int id, String location, double price, double size, String status, Integer ownerId) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.size = size;
        this.status = status;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String status() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }
    
    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
    
    public String toString() {
        return "Lot " + id + " | Location: " + location + " | Price: $" + price 
                + " | Size: " + size + " sqm | Status: " + status 
                + " | Owner: " + (ownerId == null ? "None" : ownerId);
    }
    
}
