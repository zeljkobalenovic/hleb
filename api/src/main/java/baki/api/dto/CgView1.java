package baki.api.dto;

import java.time.LocalDateTime;

public interface CgView1 {
    
    Long getId();
    String getName();
    Integer getVersion();
    Boolean getDeleted();
    LocalDateTime getLast_modified_date();
    String getLast_modified_by();

}