package baki.api.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
@Table(name = "customers")

@Audited
@SQLDelete( sql = "UPDATE customers SET deleted=true WHERE id= ?" , check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=false")


public class Customer extends BaseEntity {
    
    private String name;
    private String code;
    @Column(name = "street_and_number")
    private String streetAndNumber;
    private Integer postcode;
    private String city;
    
    // korak 4+5 jednosmerna ka customer_group , tamo nista. Takodje fetch na lazy - po defaultu je EAGER
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "customers_group_id")
    private CustomerGroup customerGroup;

    // KORAK 4+5 dvosmerna ka user , tamo isto. Posto su tamo izmapirane join i inverse join kolone ovde nista
    // relacija je mapirana u user (ta strana je owner , a ovde to samo konstatujemo)  
    // moglo je i sve obrnuto, a moglo je i samo u jednoj klasi - pa jednosmerna  
    @ManyToMany(mappedBy = "customers")  
    private Set<User> users;
    
}