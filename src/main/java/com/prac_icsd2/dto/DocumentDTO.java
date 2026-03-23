package com.prac_icsd2.dto;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prac_icsd2.enums.DocType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDTO {
    @NotNull(message="customerid should not be null")
    private Integer customerId;
    
    @NotBlank(message="fileType should not be balnk")
    private String fileType;
    
    @NotNull(message="docName should not be null")
    private DocType docName;
    
    @NotBlank(message="filePath should not be blank")
    private String filePath;
    
    @NotBlank(message="fileName should not be blank")
    private String fileName;
    
    @JsonIgnore
    private LocalDate uploadDate;
    
    @Builder.Default
    private Boolean status=true; 
    
}