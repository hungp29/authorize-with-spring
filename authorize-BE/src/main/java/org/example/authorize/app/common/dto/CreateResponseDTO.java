package org.example.authorize.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Create response DTO.
 */
@Data
public class CreateResponseDTO {

    private String id;

    private LocalDateTime createAt;
}
