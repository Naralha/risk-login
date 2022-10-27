package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.repository.PapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PapelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Papel}.
 */
@Service
@Transactional
public class PapelService {

    private final Logger log = LoggerFactory.getLogger(PapelService.class);

    private final PapelRepository papelRepository;

    private final PapelMapper papelMapper;

    public PapelService(PapelRepository papelRepository, PapelMapper papelMapper) {
        this.papelRepository = papelRepository;
        this.papelMapper = papelMapper;
    }

    /**
     * Save a papel.
     *
     * @param papelDTO the entity to save.
     * @return the persisted entity.
     */
    public PapelDTO save(PapelDTO papelDTO) {
        log.debug("Request to save Papel : {}", papelDTO);
        Papel papel = papelMapper.toEntity(papelDTO);
        papel = papelRepository.save(papel);
        return papelMapper.toDto(papel);
    }

    /**
     * Update a papel.
     *
     * @param papelDTO the entity to save.
     * @return the persisted entity.
     */
    public PapelDTO update(PapelDTO papelDTO) {
        log.debug("Request to update Papel : {}", papelDTO);
        Papel papel = papelMapper.toEntity(papelDTO);
        papel = papelRepository.save(papel);
        return papelMapper.toDto(papel);
    }

    /**
     * Partially update a papel.
     *
     * @param papelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PapelDTO> partialUpdate(PapelDTO papelDTO) {
        log.debug("Request to partially update Papel : {}", papelDTO);

        return papelRepository
            .findById(papelDTO.getId())
            .map(existingPapel -> {
                papelMapper.partialUpdate(existingPapel, papelDTO);

                return existingPapel;
            })
            .map(papelRepository::save)
            .map(papelMapper::toDto);
    }

    /**
     * Get all the papels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PapelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Papels");
        return papelRepository.findAll(pageable).map(papelMapper::toDto);
    }

    /**
     * Get one papel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PapelDTO> findOne(Long id) {
        log.debug("Request to get Papel : {}", id);
        return papelRepository.findById(id).map(papelMapper::toDto);
    }

    /**
     * Delete the papel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Papel : {}", id);
        papelRepository.deleteById(id);
    }
}
