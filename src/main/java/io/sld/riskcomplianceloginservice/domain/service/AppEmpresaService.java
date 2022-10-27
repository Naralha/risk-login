package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import io.sld.riskcomplianceloginservice.domain.repository.AppEmpresaRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppEmpresaDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.AppEmpresaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppEmpresa}.
 */
@Service
@Transactional
public class AppEmpresaService {

    private final Logger log = LoggerFactory.getLogger(AppEmpresaService.class);

    private final AppEmpresaRepository appEmpresaRepository;

    private final AppEmpresaMapper appEmpresaMapper;

    public AppEmpresaService(AppEmpresaRepository appEmpresaRepository, AppEmpresaMapper appEmpresaMapper) {
        this.appEmpresaRepository = appEmpresaRepository;
        this.appEmpresaMapper = appEmpresaMapper;
    }

    /**
     * Save a appEmpresa.
     *
     * @param appEmpresaDTO the entity to save.
     * @return the persisted entity.
     */
    public AppEmpresaDTO save(AppEmpresaDTO appEmpresaDTO) {
        log.debug("Request to save AppEmpresa : {}", appEmpresaDTO);
        AppEmpresa appEmpresa = appEmpresaMapper.toEntity(appEmpresaDTO);
        appEmpresa = appEmpresaRepository.save(appEmpresa);
        return appEmpresaMapper.toDto(appEmpresa);
    }

    /**
     * Update a appEmpresa.
     *
     * @param appEmpresaDTO the entity to save.
     * @return the persisted entity.
     */
    public AppEmpresaDTO update(AppEmpresaDTO appEmpresaDTO) {
        log.debug("Request to update AppEmpresa : {}", appEmpresaDTO);
        AppEmpresa appEmpresa = appEmpresaMapper.toEntity(appEmpresaDTO);
        appEmpresa = appEmpresaRepository.save(appEmpresa);
        return appEmpresaMapper.toDto(appEmpresa);
    }

    /**
     * Partially update a appEmpresa.
     *
     * @param appEmpresaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppEmpresaDTO> partialUpdate(AppEmpresaDTO appEmpresaDTO) {
        log.debug("Request to partially update AppEmpresa : {}", appEmpresaDTO);

        return appEmpresaRepository
            .findById(appEmpresaDTO.getId())
            .map(existingAppEmpresa -> {
                appEmpresaMapper.partialUpdate(existingAppEmpresa, appEmpresaDTO);

                return existingAppEmpresa;
            })
            .map(appEmpresaRepository::save)
            .map(appEmpresaMapper::toDto);
    }

    /**
     * Get all the appEmpresas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppEmpresaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppEmpresas");
        return appEmpresaRepository.findAll(pageable).map(appEmpresaMapper::toDto);
    }

    /**
     * Get one appEmpresa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppEmpresaDTO> findOne(Long id) {
        log.debug("Request to get AppEmpresa : {}", id);
        return appEmpresaRepository.findById(id).map(appEmpresaMapper::toDto);
    }

    /**
     * Delete the appEmpresa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppEmpresa : {}", id);
        appEmpresaRepository.deleteById(id);
    }
}
