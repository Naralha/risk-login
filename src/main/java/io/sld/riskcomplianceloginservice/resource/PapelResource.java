package io.sld.riskcomplianceloginservice.resource;

import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.repository.PapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.PapelQueryService;
import io.sld.riskcomplianceloginservice.domain.service.PapelService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.PapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.resource.errors.BadRequestAlertException;
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
import io.sld.riskcomplianceloginservice.resource.utils.HeaderUtil;
import io.sld.riskcomplianceloginservice.resource.utils.PaginationUtil;
import io.sld.riskcomplianceloginservice.resource.utils.ResponseUtil;

/**
 * REST controller for managing {@link Papel}.
 */
@RestController
@RequestMapping("/api")
public class PapelResource {

    private final Logger log = LoggerFactory.getLogger(PapelResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginservicePapel";

    @Value("${spring.application.name}")
    private String applicationName;

    private final PapelService papelService;

    private final PapelRepository papelRepository;

    private final PapelQueryService papelQueryService;

    public PapelResource(PapelService papelService, PapelRepository papelRepository, PapelQueryService papelQueryService) {
        this.papelService = papelService;
        this.papelRepository = papelRepository;
        this.papelQueryService = papelQueryService;
    }

    /**
     * {@code POST  /papels} : Create a new papel.
     *
     * @param papelDTO the papelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new papelDTO, or with status {@code 400 (Bad Request)} if the papel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/papels")
    public ResponseEntity<PapelDTO> createPapel(@Valid @RequestBody PapelDTO papelDTO) throws URISyntaxException {
        log.debug("REST request to save Papel : {}", papelDTO);
        if (papelDTO.getId() != null) {
            throw new BadRequestAlertException("A new papel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PapelDTO result = papelService.save(papelDTO);
        return ResponseEntity
            .created(new URI("/api/papels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /papels/:id} : Updates an existing papel.
     *
     * @param id the id of the papelDTO to save.
     * @param papelDTO the papelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated papelDTO,
     * or with status {@code 400 (Bad Request)} if the papelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the papelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/papels/{id}")
    public ResponseEntity<PapelDTO> updatePapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PapelDTO papelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Papel : {}, {}", id, papelDTO);
        if (papelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, papelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!papelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PapelDTO result = papelService.update(papelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, papelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /papels/:id} : Partial updates given fields of an existing papel, field will ignore if it is null
     *
     * @param id the id of the papelDTO to save.
     * @param papelDTO the papelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated papelDTO,
     * or with status {@code 400 (Bad Request)} if the papelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the papelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the papelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PapelDTO> partialUpdatePapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PapelDTO papelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Papel partially : {}, {}", id, papelDTO);
        if (papelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, papelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!papelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PapelDTO> result = papelService.partialUpdate(papelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, papelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /papels} : get all the papels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of papels in body.
     */
    @GetMapping("/papels")
    public ResponseEntity<List<PapelDTO>> getAllPapels(
        PapelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Papels by criteria: {}", criteria);
        Page<PapelDTO> page = papelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /papels/count} : count all the papels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/papels/count")
    public ResponseEntity<Long> countPapels(PapelCriteria criteria) {
        log.debug("REST request to count Papels by criteria: {}", criteria);
        return ResponseEntity.ok().body(papelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /papels/:id} : get the "id" papel.
     *
     * @param id the id of the papelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the papelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/papels/{id}")
    public ResponseEntity<PapelDTO> getPapel(@PathVariable Long id) {
        log.debug("REST request to get Papel : {}", id);
        Optional<PapelDTO> papelDTO = papelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(papelDTO);
    }

    /**
     * {@code DELETE  /papels/:id} : delete the "id" papel.
     *
     * @param id the id of the papelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/papels/{id}")
    public ResponseEntity<Void> deletePapel(@PathVariable Long id) {
        log.debug("REST request to delete Papel : {}", id);
        papelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
