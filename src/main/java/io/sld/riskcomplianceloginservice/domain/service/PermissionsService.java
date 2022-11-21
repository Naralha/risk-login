package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import io.sld.riskcomplianceloginservice.domain.repository.PermissionsRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PermissionsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Permissions}.
 */
@Service
@Transactional
public class PermissionsService {

    private final Logger log = LoggerFactory.getLogger(PermissionsService.class);

    private final PermissionsRepository permissionsRepository;

    private final PermissionsMapper permissionsMapper;

    public PermissionsService(PermissionsRepository permissionsRepository, PermissionsMapper permissionsMapper) {
        this.permissionsRepository = permissionsRepository;
        this.permissionsMapper = permissionsMapper;
    }

    /**
     * Save a permissions.
     *
     * @param permissionsDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissionsDTO save(PermissionsDTO permissionsDTO) {
        log.debug("Request to save Permissions : {}", permissionsDTO);
        Permissions permissions = permissionsMapper.toEntity(permissionsDTO);
        permissions = permissionsRepository.save(permissions);
        return permissionsMapper.toDto(permissions);
    }

    /**
     * Update a permissions.
     *
     * @param permissionsDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissionsDTO update(PermissionsDTO permissionsDTO) {
        log.debug("Request to update Permissions : {}", permissionsDTO);
        Permissions permissions = permissionsMapper.toEntity(permissionsDTO);
        permissions = permissionsRepository.save(permissions);
        return permissionsMapper.toDto(permissions);
    }

    /**
     * Partially update a permissions.
     *
     * @param permissionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PermissionsDTO> partialUpdate(PermissionsDTO permissionsDTO) {
        log.debug("Request to partially update Permissions : {}", permissionsDTO);

        return permissionsRepository
            .findById(permissionsDTO.getId())
            .map(existingPermissions -> {
                permissionsMapper.partialUpdate(existingPermissions, permissionsDTO);

                return existingPermissions;
            })
            .map(permissionsRepository::save)
            .map(permissionsMapper::toDto);
    }

    /**
     * Get all the permissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Permissions");
        return permissionsRepository.findAll(pageable).map(permissionsMapper::toDto);
    }

    /**
     * Get one permissions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PermissionsDTO> findOne(Long id) {
        log.debug("Request to get Permissions : {}", id);
        return permissionsRepository.findById(id).map(permissionsMapper::toDto);
    }

    /**
     * Delete the permissions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Permissions : {}", id);
        permissionsRepository.deleteById(id);
    }
}
