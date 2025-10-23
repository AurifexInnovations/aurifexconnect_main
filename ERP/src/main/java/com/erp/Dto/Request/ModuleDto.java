package com.erp.Dto.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private String name;

    private String description;

}
