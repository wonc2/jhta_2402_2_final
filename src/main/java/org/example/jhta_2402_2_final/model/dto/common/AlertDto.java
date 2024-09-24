package org.example.jhta_2402_2_final.model.dto.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDto {
    private String title;
    private String text;
    private String icon;
}
