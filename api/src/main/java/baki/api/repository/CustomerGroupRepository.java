package baki.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import baki.api.dto.domainprojection.CustomerGroupNameOnly;
import baki.api.model.CustomerGroup;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {

    Boolean existsByName(String name);

    // povrat za findall uvek ce biti listprojekcija domena ( ovde je izuzetan slucaj pa je nisam pravio posebno )
    // razlog je sto ima samo jedno polje-name , za koje vec imam projekciju (za druge domene pravim list projekcije )
    // VAZNO !!! ako hocu da mi metoda vrati projekciju mora find****By()  
    List<CustomerGroupNameOnly> findListBy();



}