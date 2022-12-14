package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.repository.AppRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.AppMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link App}.
 */
@Service
@Transactional
public class AppService {

    private final Logger log = LoggerFactory.getLogger(AppService.class);

    private final AppRepository appRepository;

    private final AppMapper appMapper;

    public AppService(AppRepository appRepository, AppMapper appMapper) {
        this.appRepository = appRepository;
        this.appMapper = appMapper;
    }

    /**
     * Save a app.
     *
     * @param appDTO the entity to save.
     * @return the persisted entity.
     */
    public AppDTO save(AppDTO appDTO) {
        log.debug("Request to save App : {}", appDTO);
        App app = appMapper.toEntity(appDTO);
        app = appRepository.save(app);
        return appMapper.toDto(app);
    }

    /**
     * Update a app.
     *
     * @param appDTO the entity to save.
     * @return the persisted entity.
     */
    public AppDTO update(AppDTO appDTO) {
        log.debug("Request to update App : {}", appDTO);
        App app = appMapper.toEntity(appDTO);
        app = appRepository.save(app);
        return appMapper.toDto(app);
    }

    /**
     * Partially update a app.
     *
     * @param appDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppDTO> partialUpdate(AppDTO appDTO) {
        log.debug("Request to partially update App : {}", appDTO);

        return appRepository
            .findById(appDTO.getId())
            .map(existingApp -> {
                appMapper.partialUpdate(existingApp, appDTO);

                return existingApp;
            })
            .map(appRepository::save)
            .map(appMapper::toDto);
    }

    /**
     * Get all the apps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Apps");
        return appRepository.findAll(pageable).map(appMapper::toDto);
    }

    /**
     * Get one app by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppDTO> findOne(Long id) {
        log.debug("Request to get App : {}", id);
        return appRepository.findById(id).map(appMapper::toDto);
    }

    /**
     * Delete the app by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete App : {}", id);
        appRepository.deleteById(id);
    }
}
