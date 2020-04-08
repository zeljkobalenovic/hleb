package baki.api.model;

import java.util.Set;

import javax.persistence.Entity;
// import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;



import lombok.Data;

/**
 * User
 */

@Data
@Entity
/* VAZNO ! Sve ovo vazi ako se baza pravi iz koda , ako baza vec postoji ovo ne vazi
od KORAK 3 sve ide iz workbencha pa ovako vise netreba
@Table(name = "users" , uniqueConstraints = {
                        @UniqueConstraint(columnNames = "username" , name = "uniqueUsernameConstraint"),
                        @UniqueConstraint(columnNames = "email" , name = "uniqueEmailConstraint")
})  */
 @Table(name = "users")         
 public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean active; 
    
    @ManyToMany  
    @JoinTable(name = "users_has_roles" ,
        joinColumns = @JoinColumn(name="users_id") ,
        inverseJoinColumns = @JoinColumn(name="roles_id"))
    private Set<Role> roles;  
    
    @ManyToMany  
    @JoinTable(name = "users_has_customers" ,
        joinColumns = @JoinColumn(name="users_id") ,
        inverseJoinColumns = @JoinColumn(name="customers_id"))
    private Set<Customer> customers;
    
    

}