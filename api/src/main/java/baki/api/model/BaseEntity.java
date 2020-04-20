package baki.api.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

// spring data jpa ovo zahteva kod nasledjivanja
@MappedSuperclass
// spring data audit ovo za 4 polja ko i kada menja zapise
@EntityListeners(AuditingEntityListener.class)
// hibernate enver - mora i ovde iako sama klasa nema svoju history tabelu kao ostale koje je nasledjuju
@Audited
// lombok
@Getter

public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Setter 
    private Boolean deleted = false;    // VAZNO !!! - polje je uvek false dok ga seter explicitno ne promeni     

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @CreatedDate
    @Column(name = "created_date")    
    private LocalDateTime createdDate;
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;   

}