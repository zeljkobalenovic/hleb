package baki.api.dto.domaindto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerGroupDto {

    Long id;

    @NotBlank
    @Size(min = 6,max = 50)
    String name;

}