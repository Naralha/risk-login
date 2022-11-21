package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioPapelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UsuarioPapel}.
 */
@Service
@Transactional
public class UsuarioPapelService {

    private final Logger log = LoggerFactory.getLogger(UsuarioPapelService.class);

    private final UsuarioPapelRepository usuarioPapelRepository;

    private final UsuarioPapelMapper usuarioPapelMapper;

    public UsuarioPapelService(UsuarioPapelRepository usuarioPapelRepository, UsuarioPapelMapper usuarioPapelMapper) {
        this.usuarioPapelRepository = usuarioPapelRepository;
        this.usuarioPapelMapper = usuarioPapelMapper;
    }

    /**
     * Save a usuarioPapel.
     *
     * @param usuarioPapelDTO the entity to save.
     * @return the persisted entity.
     */
    public UsuarioPapelDTO save(UsuarioPapelDTO usuarioPapelDTO) {
        log.debug("Request to save UsuarioPapel : {}", usuarioPapelDTO);
        UsuarioPapel usuarioPapel = usuarioPapelMapper.toEntity(usuarioPapelDTO);
        usuarioPapel = usuarioPapelRepository.save(usuarioPapel);
        return usuarioPapelMapper.toDto(usuarioPapel);
    }

    /**
     * Update a usuarioPapel.
     *
     * @param usuarioPapelDTO the entity to save.
     * @return the persisted entity.
     */
    public UsuarioPapelDTO update(UsuarioPapelDTO usuarioPapelDTO) {
        log.debug("Request to update UsuarioPapel : {}", usuarioPapelDTO);
        UsuarioPapel usuarioPapel = usuarioPapelMapper.toEntity(usuarioPapelDTO);
        usuarioPapel = usuarioPapelRepository.save(usuarioPapel);
        return usuarioPapelMapper.toDto(usuarioPapel);
    }

    /**
     * Partially update a usuarioPapel.
     *
     * @param usuarioPapelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UsuarioPapelDTO> partialUpdate(UsuarioPapelDTO usuarioPapelDTO) {
        log.debug("Request to partially update UsuarioPapel : {}", usuarioPapelDTO);

        return usuarioPapelRepository
            .findById(usuarioPapelDTO.getId())
            .map(existingUsuarioPapel -> {
                usuarioPapelMapper.partialUpdate(existingUsuarioPapel, usuarioPapelDTO);

                return existingUsuarioPapel;
            })
            .map(usuarioPapelRepository::save)
            .map(usuarioPapelMapper::toDto);
    }

    /**
     * Get all the usuarioPapels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioPapelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UsuarioPapels");
        return usuarioPapelRepository.findAll(pageable).map(usuarioPapelMapper::toDto);
    }

    /**
     * Get one usuarioPapel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioPapelDTO> findOne(Long id) {
        log.debug("Request to get UsuarioPapel : {}", id);
        return usuarioPapelRepository.findById(id).map(usuarioPapelMapper::toDto);
    }

    /**
     * Delete the usuarioPapel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UsuarioPapel : {}", id);
        usuarioPapelRepository.deleteById(id);
    }
}
