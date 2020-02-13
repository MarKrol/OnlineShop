package pl.camp.it.model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int availability;
    private boolean state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.id).append("<:@:>").append(this.name).append("<:@:>").append(this.price).append("<:@:>").
                append(this.availability).append("<:@:>").append(this.state);

        return sb.toString();
    }
}
