package baki.api.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * User
 */

@Data
@Entity
// VAZNO ! Sve ovo vazi ako se baza pravi iz koda , ako baza vec postoji ovo ne vazi
@Table(name = "users" , uniqueConstraints = {
                        @UniqueConstraint(columnNames = "username" , name = "uniqueUsernameConstraint"),
                        @UniqueConstraint(columnNames = "email" , name = "uniqueEmailConstraint")
})
          
 public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean active;  

    // fetchtype EAGER set rola se UVEK ucitava sa user , LAZY set rola se ucitava kad zatreba
    // VAZNO - Zbog role based autorizacije mora EAGER , sa default tj. LAZY nece da radi
    // za diskusiju je zasto kod seta permissiona u role isto nemora nego radi sa default LAZY i nebaca exception
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles" ,
        joinColumns = @JoinColumn(name="user_id") ,
        inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles;    
}