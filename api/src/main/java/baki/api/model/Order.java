package baki.api.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
@Table(name = "orders")

@Audited
@SQLDelete( sql = "UPDATE orders SET deleted=true WHERE id= ?" , check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=false")

public class Order extends BaseEntity {
    
    private String code;      
    // od jave8 i jpa 2.2 moze ovako prosto (ima 8 tipova u javi koji idu na 8 tipova u hibernate kad pravi jdbc za sql bazu )
    private LocalDate date;             // hocu samo cist datum bez time dela

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // korak 4+5 jednosmerna sa customerom. tamo nista
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customers_id")
    private Customer customer;

    // korak 4+5 dvosmerna sa order itemima, mapirana je i tamo.
    @OneToMany(mappedBy = "order")
    private List<OrderItem> ordersitem;
}