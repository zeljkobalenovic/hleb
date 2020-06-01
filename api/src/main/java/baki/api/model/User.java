package baki.api.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User
 */

// ove anotacije su jednake za svih 7 tabela koje nasledjuju BaseEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity
@Table(name = "users") 
@Audited

// Samo kod usera je upitno dali koristiti softdelete ili samo active
@SQLDelete( sql = "UPDATE users SET deleted=true WHERE id= ? AND version= ?" , check = ResultCheckStyle.COUNT)
@Where(clause = "deleted=0")

/*
VAZNO ! Sve ovo vazi ako se baza pravi iz koda , ako baza vec postoji ovo ne vazi
od KORAK 3 sve ide iz workbencha pa ovako vise netreba
@Table(name = "users" , uniqueConstraints = {
                        @UniqueConstraint(columnNames = "username" , name = "uniqueUsernameConstraint"),
                        @UniqueConstraint(columnNames = "email" , name = "uniqueEmailConstraint")
})
*/

public class User extends BaseEntity {
    
    private String username;
    private String email;
    private String password;
    private boolean active; 

    // do koraka 3 - postojeca relacija ( jednosmerna u rolama nista zato je jednosmerna )
    @NotAudited
    @ManyToMany  
    @JoinTable(name = "users_has_roles" ,
        joinColumns = @JoinColumn(name="users_id") ,
        inverseJoinColumns = @JoinColumn(name="roles_id"))
    private Set<Role> roles;  
    
    // korak 4+5 - dodatna relacija ( dvosmerna u customer isto dodajem )
    @ManyToMany  
    @JoinTable(name = "users_has_customers" ,
        joinColumns = @JoinColumn(name="users_id") ,
        inverseJoinColumns = @JoinColumn(name="customers_id"))
    private Set<Customer> customers;
    
    

}