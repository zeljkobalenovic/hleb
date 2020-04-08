package baki.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private Double price;
    private String description;
    private String picture;
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "products_group_id")
    private ProductGroup productGroup;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> ordersitem;

}