package tr.yildiz.wardrobe;

public class Dress {
    private int id;
    private String type;
    private String color;
    private String texture;
    private String buyDate;
    private String cost;
    private String photo;

    public Dress(String type, String color, String texture, String buyDate, String cost, String photo) {
        this.type = type;
        this.color = color;
        this.texture = texture;
        this.buyDate = buyDate;
        this.cost = cost;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
