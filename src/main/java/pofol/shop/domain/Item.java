package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Item extends BaseEntity{
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

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;
    public Item(String itemName, int price, int quantity) {
        this.itemName = itemName;
        this.author = "짬뽕먹고싶다";
        this.price = price;
        this.quantity = quantity;
    }

    @PrePersist
    void beforePersist(){
        if(description == null) description = "";
    }

    public void reduceQty(int count){
        this.quantity -= count;
    }

    public void addQty(int count){
        this.quantity += count;
    }
}
