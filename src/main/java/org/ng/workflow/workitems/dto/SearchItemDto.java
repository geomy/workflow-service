package org.ng.workflow.workitems.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SearchItemDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
