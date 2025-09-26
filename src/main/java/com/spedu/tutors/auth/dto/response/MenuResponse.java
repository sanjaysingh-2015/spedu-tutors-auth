package com.spedu.tutors.auth.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponse {
    private String code;
    private String name;
    private String displayName;
    private String uiPath;
    private Integer displayOrder;
}
