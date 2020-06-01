package baki.api.model;

import javax.persistence.Entity;
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
@Table(name = "products_group")

@Audited
@SQLDelete( sql = "UPDATE products_group SET deleted=true WHERE id= ? AND version= ?" , check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=0")

public class ProductGroup extends BaseEntity {

    private String name;
}