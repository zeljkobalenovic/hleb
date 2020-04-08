package baki.api.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    @Column(name = "street_and_number")
    private String streetAndNumber;
    private Integer postcode;
    private String city;
    private String state;
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "customers_group_id")
    private CustomerGroup customerGroup;

    @ManyToMany(mappedBy = "customers")  
    private Set<User> users;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
    

}