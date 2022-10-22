package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String itemName;
    private String author;
    private String isbn;
    private String description;
    private int price;
    private int quantity;

    private int totalSales;
    private int rating;

    public Item(String itemName, int price, int quantity) {
        this.itemName = itemName;
        this.author = "짬뽕먹고싶다";
        this.price = price;
        this.quantity = quantity;
    }

    public void reduceQty(int count){
        this.quantity -= count;
    }

    public void addQty(int count){
        this.quantity += count;
    }
}
