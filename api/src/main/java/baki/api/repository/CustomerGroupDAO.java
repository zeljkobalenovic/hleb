package baki.api.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import baki.api.model.CustomerGroup;

// primer pristupa bazi bez spring data - samo jpa/hibernate

@Repository
public class CustomerGroupDAO {
    @PersistenceContext EntityManager em;  
    public CustomerGroup findCustomerGroupByName ( String name ) {        
        TypedQuery<CustomerGroup> query = em.createQuery("FROM CustomerGroup cg WHERE cg.name = ?1 ", CustomerGroup.class);
        return query.setParameter(1, name).getSingleResult();       
    }

    // .... ostale metode se slicno prave
}