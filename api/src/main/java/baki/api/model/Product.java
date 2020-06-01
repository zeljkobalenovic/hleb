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
@Table(name = "products")

@Audited
@SQLDelete( sql = "UPDATE products SET deleted=true WHERE id= ? AND version= ?" , check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=0")

public class Product extends BaseEntity {

    private String name;
    private String code;
    private Double price;
    private String description;
    private String picture;
    
    // korak 4+5 relacija sa product group jednosmerna. sve isto kao i customer - customer_group
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_group_id")
    private ProductGroup productGroup;

}