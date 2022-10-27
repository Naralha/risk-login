package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.domain.repository.FeaturesRepository;
import io.sld.riskcomplianceloginservice.domain.service.FeaturesQueryService;
import io.sld.riskcomplianceloginservice.domain.service.FeaturesService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.FeaturesCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.FeaturesDTO;
import io.sld.riskcomplianceloginservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Features}.
 */
@RestController
@RequestMapping("/api")
public class FeaturesResource {

    private final Logger log = LoggerFactory.getLogger(FeaturesResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginserviceFeatures";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeaturesService featuresService;

    private final FeaturesRepository featuresRepository;

    private final FeaturesQueryService featuresQueryService;

    public FeaturesResource(
        FeaturesService featuresService,
        FeaturesRepository featuresRepository,
        FeaturesQueryService featuresQueryService
    ) {
        this.featuresService = featuresService;
        this.featuresRepository = featuresRepository;
        this.featuresQueryService = featuresQueryService;
    }

    /**
     * {@code POST  /features} : Create a new features.
     *
     * @param featuresDTO the featuresDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new featuresDTO, or with status {@code 400 (Bad Request)} if the features has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/features")
    public ResponseEntity<FeaturesDTO> createFeatures(@Valid @RequestBody FeaturesDTO featuresDTO) throws URISyntaxException {
        log.debug("REST request to save Features : {}", featuresDTO);
        if (featuresDTO.getId() != null) {
            throw new BadRequestAlertException("A new features cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeaturesDTO result = featuresService.save(featuresDTO);
        return ResponseEntity
            .created(new URI("/api/features/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /features/:id} : Updates an existing features.
     *
     * @param id the id of the featuresDTO to save.
     * @param featuresDTO the featuresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featuresDTO,
     * or with status {@code 400 (Bad Request)} if the featuresDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the featuresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/features/{id}")
    public ResponseEntity<FeaturesDTO> updateFeatures(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeaturesDTO featuresDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Features : {}, {}", id, featuresDTO);
        if (featuresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featuresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featuresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FeaturesDTO result = featuresService.update(featuresDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, featuresDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /features/:id} : Partial updates given fields of an existing features, field will ignore if it is null
     *
     * @param id the id of the featuresDTO to save.
     * @param featuresDTO the featuresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featuresDTO,
     * or with status {@code 400 (Bad Request)} if the featuresDTO is not valid,
     * or with status {@code 404 (Not Found)} if the featuresDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the featuresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/features/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeaturesDTO> partialUpdateFeatures(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeaturesDTO featuresDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Features partially : {}, {}", id, featuresDTO);
        if (featuresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featuresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featuresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeaturesDTO> result = featuresService.partialUpdate(featuresDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, featuresDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /features} : get all the features.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of features in body.
     */
    @GetMapping("/features")
    public ResponseEntity<List<FeaturesDTO>> getAllFeatures(
        FeaturesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Features by criteria: {}", criteria);
        Page<FeaturesDTO> page = featuresQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /features/count} : count all the features.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/features/count")
    public ResponseEntity<Long> countFeatures(FeaturesCriteria criteria) {
        log.debug("REST request to count Features by criteria: {}", criteria);
        return ResponseEntity.ok().body(featuresQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /features/:id} : get the "id" features.
     *
     * @param id the id of the featuresDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the featuresDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/features/{id}")
    public ResponseEntity<FeaturesDTO> getFeatures(@PathVariable Long id) {
        log.debug("REST request to get Features : {}", id);
        Optional<FeaturesDTO> featuresDTO = featuresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(featuresDTO);
    }

    /**
     * {@code DELETE  /features/:id} : delete the "id" features.
     *
     * @param id the id of the featuresDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/features/{id}")
    public ResponseEntity<Void> deleteFeatures(@PathVariable Long id) {
        log.debug("REST request to delete Features : {}", id);
        featuresService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
