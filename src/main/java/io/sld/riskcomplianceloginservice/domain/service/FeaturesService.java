package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.domain.repository.FeaturesRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.FeaturesDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.FeaturesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Features}.
 */
@Service
@Transactional
public class FeaturesService {

    private final Logger log = LoggerFactory.getLogger(FeaturesService.class);

    private final FeaturesRepository featuresRepository;

    private final FeaturesMapper featuresMapper;

    public FeaturesService(FeaturesRepository featuresRepository, FeaturesMapper featuresMapper) {
        this.featuresRepository = featuresRepository;
        this.featuresMapper = featuresMapper;
    }

    /**
     * Save a features.
     *
     * @param featuresDTO the entity to save.
     * @return the persisted entity.
     */
    public FeaturesDTO save(FeaturesDTO featuresDTO) {
        log.debug("Request to save Features : {}", featuresDTO);
        Features features = featuresMapper.toEntity(featuresDTO);
        features = featuresRepository.save(features);
        return featuresMapper.toDto(features);
    }

    /**
     * Update a features.
     *
     * @param featuresDTO the entity to save.
     * @return the persisted entity.
     */
    public FeaturesDTO update(FeaturesDTO featuresDTO) {
        log.debug("Request to update Features : {}", featuresDTO);
        Features features = featuresMapper.toEntity(featuresDTO);
        features = featuresRepository.save(features);
        return featuresMapper.toDto(features);
    }

    /**
     * Partially update a features.
     *
     * @param featuresDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FeaturesDTO> partialUpdate(FeaturesDTO featuresDTO) {
        log.debug("Request to partially update Features : {}", featuresDTO);

        return featuresRepository
            .findById(featuresDTO.getId())
            .map(existingFeatures -> {
                featuresMapper.partialUpdate(existingFeatures, featuresDTO);

                return existingFeatures;
            })
            .map(featuresRepository::save)
            .map(featuresMapper::toDto);
    }

    /**
     * Get all the features.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FeaturesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Features");
        return featuresRepository.findAll(pageable).map(featuresMapper::toDto);
    }

    /**
     * Get one features by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FeaturesDTO> findOne(Long id) {
        log.debug("Request to get Features : {}", id);
        return featuresRepository.findById(id).map(featuresMapper::toDto);
    }

    /**
     * Delete the features by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Features : {}", id);
        featuresRepository.deleteById(id);
    }
}
