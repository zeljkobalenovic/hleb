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
@EqualsAndHashCode(onlyExplicitlyIncluded = true , callSuper = false)


@Entity
@Table(name = "customers_group")

@Audited

// soft delete preko hibernate (ciste hibernate anotacije)
// ovo ne koristim nije zgodno (zbog audita mi je bolje resenje koje sam sam rucno napisao , a radi posao)
// nisam obrisao da AKO neko zaobidje moje brisanje (npr neko cascade brisanje ili orphan removal) ipak odradi update
// Radi tako sto svaki delete izraz menja sa datim sql izrazom
// NAPOMENA : SVI NACINI DELETE (osim native sql) dodaju i polje version , zato i ja dodajem drugi parametar
// Npr. metoda repositoryja deleteById(id) automatski dodaje i version polje , pa ima u stvari DVA parametra
@SQLDelete(sql = "UPDATE customers_group SET deleted=id WHERE id= ? AND version= ?" , check = ResultCheckStyle.COUNT)
// ovo koristim ( moze se zaobici SAMO sa nativ sql query)
// automatski where deleted=false kod svakog fetcinga (znaci selecta)
// ovo je zgodno jer ne moram u repository (koji ja koristim za sve) da vodim racuna.
// Koristim uobicajene metode repoa (sve find , list, count, exist ...) sve radi kao da su zapisi stvarno obrisani
// NE ODNOSI se na metode koje modifikuju podatke ( svi save, delete )
@Where(clause = "deleted=0") 

public class CustomerGroup extends BaseEntity{
 
  @EqualsAndHashCode.Include
  private String name; 
  
    
}