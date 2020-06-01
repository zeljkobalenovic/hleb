package baki.api.dto.domaindto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerDto {

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