package io.sld.riskcomplianceloginservice.resource;

import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.GrupoPapelQueryService;
import io.sld.riskcomplianceloginservice.domain.service.GrupoPapelService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.GrupoPapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoPapelDTO;
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
 * REST controller for managing {@link GrupoPapel}.
 */
@RestController
@RequestMapping("/api")
public class GrupoPapelResource {

    private final Logger log = LoggerFactory.getLogger(GrupoPapelResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginserviceGrupoPapel";

    @Value("${spring.application.name}")
    private String applicationName;

    private final GrupoPapelService grupoPapelService;

    private final GrupoPapelRepository grupoPapelRepository;

    private final GrupoPapelQueryService grupoPapelQueryService;

    public GrupoPapelResource(
        GrupoPapelService grupoPapelService,
        GrupoPapelRepository grupoPapelRepository,
        GrupoPapelQueryService grupoPapelQueryService
    ) {
        this.grupoPapelService = grupoPapelService;
        this.grupoPapelRepository = grupoPapelRepository;
        this.grupoPapelQueryService = grupoPapelQueryService;
    }

    /**
     * {@code POST  /grupo-papels} : Create a new grupoPapel.
     *
     * @param grupoPapelDTO the grupoPapelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupoPapelDTO, or with status {@code 400 (Bad Request)} if the grupoPapel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grupo-papels")
    public ResponseEntity<GrupoPapelDTO> createGrupoPapel(@Valid @RequestBody GrupoPapelDTO grupoPapelDTO) throws URISyntaxException {
        log.debug("REST request to save GrupoPapel : {}", grupoPapelDTO);
        if (grupoPapelDTO.getId() != null) {
            throw new BadRequestAlertException("A new grupoPapel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GrupoPapelDTO result = grupoPapelService.save(grupoPapelDTO);
        return ResponseEntity
            .created(new URI("/api/grupo-papels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /grupo-papels/:id} : Updates an existing grupoPapel.
     *
     * @param id the id of the grupoPapelDTO to save.
     * @param grupoPapelDTO the grupoPapelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoPapelDTO,
     * or with status {@code 400 (Bad Request)} if the grupoPapelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupoPapelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grupo-papels/{id}")
    public ResponseEntity<GrupoPapelDTO> updateGrupoPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GrupoPapelDTO grupoPapelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GrupoPapel : {}, {}", id, grupoPapelDTO);
        if (grupoPapelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoPapelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoPapelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GrupoPapelDTO result = grupoPapelService.update(grupoPapelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupoPapelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /grupo-papels/:id} : Partial updates given fields of an existing grupoPapel, field will ignore if it is null
     *
     * @param id the id of the grupoPapelDTO to save.
     * @param grupoPapelDTO the grupoPapelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoPapelDTO,
     * or with status {@code 400 (Bad Request)} if the grupoPapelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the grupoPapelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupoPapelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grupo-papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GrupoPapelDTO> partialUpdateGrupoPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GrupoPapelDTO grupoPapelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GrupoPapel partially : {}, {}", id, grupoPapelDTO);
        if (grupoPapelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoPapelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoPapelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GrupoPapelDTO> result = grupoPapelService.partialUpdate(grupoPapelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupoPapelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /grupo-papels} : get all the grupoPapels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grupoPapels in body.
     */
    @GetMapping("/grupo-papels")
    public ResponseEntity<List<GrupoPapelDTO>> getAllGrupoPapels(
        GrupoPapelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GrupoPapels by criteria: {}", criteria);
        Page<GrupoPapelDTO> page = grupoPapelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grupo-papels/count} : count all the grupoPapels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/grupo-papels/count")
    public ResponseEntity<Long> countGrupoPapels(GrupoPapelCriteria criteria) {
        log.debug("REST request to count GrupoPapels by criteria: {}", criteria);
        return ResponseEntity.ok().body(grupoPapelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /grupo-papels/:id} : get the "id" grupoPapel.
     *
     * @param id the id of the grupoPapelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupoPapelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grupo-papels/{id}")
    public ResponseEntity<GrupoPapelDTO> getGrupoPapel(@PathVariable Long id) {
        log.debug("REST request to get GrupoPapel : {}", id);
        Optional<GrupoPapelDTO> grupoPapelDTO = grupoPapelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(grupoPapelDTO);
    }

    /**
     * {@code DELETE  /grupo-papels/:id} : delete the "id" grupoPapel.
     *
     * @param id the id of the grupoPapelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grupo-papels/{id}")
    public ResponseEntity<Void> deleteGrupoPapel(@PathVariable Long id) {
        log.debug("REST request to delete GrupoPapel : {}", id);
        grupoPapelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
