package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Grupo}.
 */
@Service
@Transactional
public class GrupoService {

    private final Logger log = LoggerFactory.getLogger(GrupoService.class);

    private final GrupoRepository grupoRepository;

    private final GrupoMapper grupoMapper;

    public GrupoService(GrupoRepository grupoRepository, GrupoMapper grupoMapper) {
        this.grupoRepository = grupoRepository;
        this.grupoMapper = grupoMapper;
    }

    /**
     * Save a grupo.
     *
     * @param grupoDTO the entity to save.
     * @return the persisted entity.
     */
    public GrupoDTO save(GrupoDTO grupoDTO) {
        log.debug("Request to save Grupo : {}", grupoDTO);
        Grupo grupo = grupoMapper.toEntity(grupoDTO);
        grupo = grupoRepository.save(grupo);
        return grupoMapper.toDto(grupo);
    }

    /**
     * Update a grupo.
     *
     * @param grupoDTO the entity to save.
     * @return the persisted entity.
     */
    public GrupoDTO update(GrupoDTO grupoDTO) {
        log.debug("Request to update Grupo : {}", grupoDTO);
        Grupo grupo = grupoMapper.toEntity(grupoDTO);
        grupo = grupoRepository.save(grupo);
        return grupoMapper.toDto(grupo);
    }

    /**
     * Partially update a grupo.
     *
     * @param grupoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GrupoDTO> partialUpdate(GrupoDTO grupoDTO) {
        log.debug("Request to partially update Grupo : {}", grupoDTO);

        return grupoRepository
            .findById(grupoDTO.getId())
            .map(existingGrupo -> {
                grupoMapper.partialUpdate(existingGrupo, grupoDTO);

                return existingGrupo;
            })
            .map(grupoRepository::save)
            .map(grupoMapper::toDto);
    }

    /**
     * Get all the grupos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GrupoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Grupos");
        return grupoRepository.findAll(pageable).map(grupoMapper::toDto);
    }

    /**
     * Get one grupo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GrupoDTO> findOne(Long id) {
        log.debug("Request to get Grupo : {}", id);
        return grupoRepository.findById(id).map(grupoMapper::toDto);
    }

    /**
     * Delete the grupo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Grupo : {}", id);
        grupoRepository.deleteById(id);
    }
}
