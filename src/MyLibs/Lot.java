package MyLibs;

public class Lot {
    private int id;
    private String location;
    private double price;
    private double size;
    private String status;

    public Lot(int id, String location, double price, double size, String status) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.size = size;
        this.status = status;
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
    
    public String toString(){
        return "Lot " + id + " | Location: " + location + " | Price: $" + price + " | Size: " + size + " sqm | Status: " + status;
    }
    
}
