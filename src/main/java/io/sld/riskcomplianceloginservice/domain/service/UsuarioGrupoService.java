package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioGrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioGrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioGrupoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UsuarioGrupo}.
 */
@Service
@Transactional
public class UsuarioGrupoService {

    private final Logger log = LoggerFactory.getLogger(UsuarioGrupoService.class);

    private final UsuarioGrupoRepository usuarioGrupoRepository;

    private final UsuarioGrupoMapper usuarioGrupoMapper;

    public UsuarioGrupoService(UsuarioGrupoRepository usuarioGrupoRepository, UsuarioGrupoMapper usuarioGrupoMapper) {
        this.usuarioGrupoRepository = usuarioGrupoRepository;
        this.usuarioGrupoMapper = usuarioGrupoMapper;
    }

    /**
     * Save a usuarioGrupo.
     *
     * @param usuarioGrupoDTO the entity to save.
     * @return the persisted entity.
     */
    public UsuarioGrupoDTO save(UsuarioGrupoDTO usuarioGrupoDTO) {
        log.debug("Request to save UsuarioGrupo : {}", usuarioGrupoDTO);
        UsuarioGrupo usuarioGrupo = usuarioGrupoMapper.toEntity(usuarioGrupoDTO);
        usuarioGrupo = usuarioGrupoRepository.save(usuarioGrupo);
        return usuarioGrupoMapper.toDto(usuarioGrupo);
    }

    /**
     * Update a usuarioGrupo.
     *
     * @param usuarioGrupoDTO the entity to save.
     * @return the persisted entity.
     */
    public UsuarioGrupoDTO update(UsuarioGrupoDTO usuarioGrupoDTO) {
        log.debug("Request to update UsuarioGrupo : {}", usuarioGrupoDTO);
        UsuarioGrupo usuarioGrupo = usuarioGrupoMapper.toEntity(usuarioGrupoDTO);
        usuarioGrupo = usuarioGrupoRepository.save(usuarioGrupo);
        return usuarioGrupoMapper.toDto(usuarioGrupo);
    }

    /**
     * Partially update a usuarioGrupo.
     *
     * @param usuarioGrupoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UsuarioGrupoDTO> partialUpdate(UsuarioGrupoDTO usuarioGrupoDTO) {
        log.debug("Request to partially update UsuarioGrupo : {}", usuarioGrupoDTO);

        return usuarioGrupoRepository
            .findById(usuarioGrupoDTO.getId())
            .map(existingUsuarioGrupo -> {
                usuarioGrupoMapper.partialUpdate(existingUsuarioGrupo, usuarioGrupoDTO);

                return existingUsuarioGrupo;
            })
            .map(usuarioGrupoRepository::save)
            .map(usuarioGrupoMapper::toDto);
    }

    /**
     * Get all the usuarioGrupos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioGrupoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UsuarioGrupos");
        return usuarioGrupoRepository.findAll(pageable).map(usuarioGrupoMapper::toDto);
    }

    /**
     * Get one usuarioGrupo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioGrupoDTO> findOne(Long id) {
        log.debug("Request to get UsuarioGrupo : {}", id);
        return usuarioGrupoRepository.findById(id).map(usuarioGrupoMapper::toDto);
    }

    /**
     * Delete the usuarioGrupo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UsuarioGrupo : {}", id);
        usuarioGrupoRepository.deleteById(id);
    }
}
