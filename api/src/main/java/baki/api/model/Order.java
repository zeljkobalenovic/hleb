package baki.api.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;   
    
    // od jave8 i jpa 2.2 moze ovako prosto (ima 8 tipova u javi koji idu na 8 tipova u hibernate kad pravi jdbc za sql bazu )
    private LocalDate date;             // hocu samo cist datum bez time dela

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "customers_id")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> ordersitem;
}