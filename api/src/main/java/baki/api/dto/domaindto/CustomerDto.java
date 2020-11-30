package baki.api.dto.domaindto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
// VAZNO !!! ako hocu da mi se automatski vraca dto kao tip u repository MORA konstruktor full sa svim poljima
// MORA BITI SAMO JEDAN KONSTRUKTOR 
// @NoArgsConstructor   ovo baca gresku jer spring data nezna koji konstruktor pa javi da nemoze konvertovati
// rezultat tipa Customer u CustomerDto
// Moze peske tj. ostavimo da metoda repoa vrati Customer , pa ga sa beanutil prabacimo u CustomerDto i saljemo
@AllArgsConstructor
public class CustomerDto {

    Long id;

    @NotBlank
    @Size(min = 6,max = 50)
    String name;
    
    @NotBlank
    @Size(min = 6,max = 50)
    String code;
   
    @Size(min = 6,max = 50)
    String streetAndNumber;
   
    @Min(10000)
    @Max(99999)
    Integer postcode;
   
    @Size(min = 6,max = 50)
    String city;
    
    @NotNull
    Long customerGroupId;

}