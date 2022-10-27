package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.repository.PermissionsPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PermissionsPapelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PermissionsPapel}.
 */
@Service
@Transactional
public class PermissionsPapelService {

    private final Logger log = LoggerFactory.getLogger(PermissionsPapelService.class);

    private final PermissionsPapelRepository permissionsPapelRepository;

    private final PermissionsPapelMapper permissionsPapelMapper;

    public PermissionsPapelService(PermissionsPapelRepository permissionsPapelRepository, PermissionsPapelMapper permissionsPapelMapper) {
        this.permissionsPapelRepository = permissionsPapelRepository;
        this.permissionsPapelMapper = permissionsPapelMapper;
    }

    /**
     * Save a permissionsPapel.
     *
     * @param permissionsPapelDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissionsPapelDTO save(PermissionsPapelDTO permissionsPapelDTO) {
        log.debug("Request to save PermissionsPapel : {}", permissionsPapelDTO);
        PermissionsPapel permissionsPapel = permissionsPapelMapper.toEntity(permissionsPapelDTO);
        permissionsPapel = permissionsPapelRepository.save(permissionsPapel);
        return permissionsPapelMapper.toDto(permissionsPapel);
    }

    /**
     * Update a permissionsPapel.
     *
     * @param permissionsPapelDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissionsPapelDTO update(PermissionsPapelDTO permissionsPapelDTO) {
        log.debug("Request to update PermissionsPapel : {}", permissionsPapelDTO);
        PermissionsPapel permissionsPapel = permissionsPapelMapper.toEntity(permissionsPapelDTO);
        permissionsPapel = permissionsPapelRepository.save(permissionsPapel);
        return permissionsPapelMapper.toDto(permissionsPapel);
    }

    /**
     * Partially update a permissionsPapel.
     *
     * @param permissionsPapelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PermissionsPapelDTO> partialUpdate(PermissionsPapelDTO permissionsPapelDTO) {
        log.debug("Request to partially update PermissionsPapel : {}", permissionsPapelDTO);

        return permissionsPapelRepository
            .findById(permissionsPapelDTO.getId())
            .map(existingPermissionsPapel -> {
                permissionsPapelMapper.partialUpdate(existingPermissionsPapel, permissionsPapelDTO);

                return existingPermissionsPapel;
            })
            .map(permissionsPapelRepository::save)
            .map(permissionsPapelMapper::toDto);
    }

    /**
     * Get all the permissionsPapels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissionsPapelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PermissionsPapels");
        return permissionsPapelRepository.findAll(pageable).map(permissionsPapelMapper::toDto);
    }

    /**
     * Get one permissionsPapel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PermissionsPapelDTO> findOne(Long id) {
        log.debug("Request to get PermissionsPapel : {}", id);
        return permissionsPapelRepository.findById(id).map(permissionsPapelMapper::toDto);
    }

    /**
     * Delete the permissionsPapel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PermissionsPapel : {}", id);
        permissionsPapelRepository.deleteById(id);
    }
}
