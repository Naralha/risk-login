package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoPapelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GrupoPapel}.
 */
@Service
@Transactional
public class GrupoPapelService {

    private final Logger log = LoggerFactory.getLogger(GrupoPapelService.class);

    private final GrupoPapelRepository grupoPapelRepository;

    private final GrupoPapelMapper grupoPapelMapper;

    public GrupoPapelService(GrupoPapelRepository grupoPapelRepository, GrupoPapelMapper grupoPapelMapper) {
        this.grupoPapelRepository = grupoPapelRepository;
        this.grupoPapelMapper = grupoPapelMapper;
    }

    /**
     * Save a grupoPapel.
     *
     * @param grupoPapelDTO the entity to save.
     * @return the persisted entity.
     */
    public GrupoPapelDTO save(GrupoPapelDTO grupoPapelDTO) {
        log.debug("Request to save GrupoPapel : {}", grupoPapelDTO);
        GrupoPapel grupoPapel = grupoPapelMapper.toEntity(grupoPapelDTO);
        grupoPapel = grupoPapelRepository.save(grupoPapel);
        return grupoPapelMapper.toDto(grupoPapel);
    }

    /**
     * Update a grupoPapel.
     *
     * @param grupoPapelDTO the entity to save.
     * @return the persisted entity.
     */
    public GrupoPapelDTO update(GrupoPapelDTO grupoPapelDTO) {
        log.debug("Request to update GrupoPapel : {}", grupoPapelDTO);
        GrupoPapel grupoPapel = grupoPapelMapper.toEntity(grupoPapelDTO);
        grupoPapel = grupoPapelRepository.save(grupoPapel);
        return grupoPapelMapper.toDto(grupoPapel);
    }

    /**
     * Partially update a grupoPapel.
     *
     * @param grupoPapelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GrupoPapelDTO> partialUpdate(GrupoPapelDTO grupoPapelDTO) {
        log.debug("Request to partially update GrupoPapel : {}", grupoPapelDTO);

        return grupoPapelRepository
            .findById(grupoPapelDTO.getId())
            .map(existingGrupoPapel -> {
                grupoPapelMapper.partialUpdate(existingGrupoPapel, grupoPapelDTO);

                return existingGrupoPapel;
            })
            .map(grupoPapelRepository::save)
            .map(grupoPapelMapper::toDto);
    }

    /**
     * Get all the grupoPapels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GrupoPapelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GrupoPapels");
        return grupoPapelRepository.findAll(pageable).map(grupoPapelMapper::toDto);
    }

    /**
     * Get one grupoPapel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GrupoPapelDTO> findOne(Long id) {
        log.debug("Request to get GrupoPapel : {}", id);
        return grupoPapelRepository.findById(id).map(grupoPapelMapper::toDto);
    }

    /**
     * Delete the grupoPapel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GrupoPapel : {}", id);
        grupoPapelRepository.deleteById(id);
    }
}
