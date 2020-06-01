package baki.api.dto.domaindto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerGroupDto {

    @NotBlank
    @Size(min = 6,max = 50)
    String name;

}