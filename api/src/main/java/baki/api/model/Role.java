package baki.api.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ManyToMany
    @JoinTable(name = "roles_permissions" ,
        joinColumns = @JoinColumn(name="role_id") ,
        inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;   

}
