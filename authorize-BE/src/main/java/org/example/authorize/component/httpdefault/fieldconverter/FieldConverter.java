package org.example.authorize.component.httpdefault.fieldconverter;

/**
 * Interface converter between Entity and DTO.
 *
 * @param <ENTITY> Generic Entity
 * @param <DTO>    Generic DTO
 */
public interface FieldConverter<ENTITY, DTO> {

    /**
     * Convert field value from entity to DTO.
     *
     * @param entityField field value of entity
     * @return field value of DTO
     */
    DTO convertFieldEntityToDTO(ENTITY entityField);

    /**
     * Convert field value from DTO to entity.
     *
     * @param dtoField field value of DTO
     * @return field value of entity
     */
    ENTITY convertFieldDTOToEntity(DTO dtoField);
}
