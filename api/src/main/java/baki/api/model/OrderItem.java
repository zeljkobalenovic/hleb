package baki.api.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "orders_items")

@Audited
@SQLDelete( sql = "UPDATE orders_items SET deleted=true WHERE id= ? AND version= ?" , check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=0")
public class OrderItem extends BaseEntity {
    
    private Double quantity;
    private Double price;
    
    // korak 4+5 relacija ka product jednosmerna , tamo nista
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private Product product;

    // korak 4+5 relacija ka order dvosmerna , ima je i u item. Ova strana je owner 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

}