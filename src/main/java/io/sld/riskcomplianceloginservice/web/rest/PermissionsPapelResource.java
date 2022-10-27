package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.repository.PermissionsPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.PermissionsPapelQueryService;
import io.sld.riskcomplianceloginservice.domain.service.PermissionsPapelService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.PermissionsPapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsPapelDTO;
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
 * REST controller for managing {@link PermissionsPapel}.
 */
@RestController
@RequestMapping("/api")
public class PermissionsPapelResource {

    private final Logger log = LoggerFactory.getLogger(PermissionsPapelResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginservicePermissionsPapel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionsPapelService permissionsPapelService;

    private final PermissionsPapelRepository permissionsPapelRepository;

    private final PermissionsPapelQueryService permissionsPapelQueryService;

    public PermissionsPapelResource(
        PermissionsPapelService permissionsPapelService,
        PermissionsPapelRepository permissionsPapelRepository,
        PermissionsPapelQueryService permissionsPapelQueryService
    ) {
        this.permissionsPapelService = permissionsPapelService;
        this.permissionsPapelRepository = permissionsPapelRepository;
        this.permissionsPapelQueryService = permissionsPapelQueryService;
    }

    /**
     * {@code POST  /permissions-papels} : Create a new permissionsPapel.
     *
     * @param permissionsPapelDTO the permissionsPapelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionsPapelDTO, or with status {@code 400 (Bad Request)} if the permissionsPapel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permissions-papels")
    public ResponseEntity<PermissionsPapelDTO> createPermissionsPapel(@Valid @RequestBody PermissionsPapelDTO permissionsPapelDTO)
        throws URISyntaxException {
        log.debug("REST request to save PermissionsPapel : {}", permissionsPapelDTO);
        if (permissionsPapelDTO.getId() != null) {
            throw new BadRequestAlertException("A new permissionsPapel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PermissionsPapelDTO result = permissionsPapelService.save(permissionsPapelDTO);
        return ResponseEntity
            .created(new URI("/api/permissions-papels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permissions-papels/:id} : Updates an existing permissionsPapel.
     *
     * @param id the id of the permissionsPapelDTO to save.
     * @param permissionsPapelDTO the permissionsPapelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionsPapelDTO,
     * or with status {@code 400 (Bad Request)} if the permissionsPapelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionsPapelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permissions-papels/{id}")
    public ResponseEntity<PermissionsPapelDTO> updatePermissionsPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PermissionsPapelDTO permissionsPapelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PermissionsPapel : {}, {}", id, permissionsPapelDTO);
        if (permissionsPapelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionsPapelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionsPapelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PermissionsPapelDTO result = permissionsPapelService.update(permissionsPapelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionsPapelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permissions-papels/:id} : Partial updates given fields of an existing permissionsPapel, field will ignore if it is null
     *
     * @param id the id of the permissionsPapelDTO to save.
     * @param permissionsPapelDTO the permissionsPapelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionsPapelDTO,
     * or with status {@code 400 (Bad Request)} if the permissionsPapelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permissionsPapelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissionsPapelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permissions-papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermissionsPapelDTO> partialUpdatePermissionsPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PermissionsPapelDTO permissionsPapelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PermissionsPapel partially : {}, {}", id, permissionsPapelDTO);
        if (permissionsPapelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionsPapelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionsPapelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermissionsPapelDTO> result = permissionsPapelService.partialUpdate(permissionsPapelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionsPapelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permissions-papels} : get all the permissionsPapels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissionsPapels in body.
     */
    @GetMapping("/permissions-papels")
    public ResponseEntity<List<PermissionsPapelDTO>> getAllPermissionsPapels(
        PermissionsPapelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PermissionsPapels by criteria: {}", criteria);
        Page<PermissionsPapelDTO> page = permissionsPapelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permissions-papels/count} : count all the permissionsPapels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/permissions-papels/count")
    public ResponseEntity<Long> countPermissionsPapels(PermissionsPapelCriteria criteria) {
        log.debug("REST request to count PermissionsPapels by criteria: {}", criteria);
        return ResponseEntity.ok().body(permissionsPapelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /permissions-papels/:id} : get the "id" permissionsPapel.
     *
     * @param id the id of the permissionsPapelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionsPapelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions-papels/{id}")
    public ResponseEntity<PermissionsPapelDTO> getPermissionsPapel(@PathVariable Long id) {
        log.debug("REST request to get PermissionsPapel : {}", id);
        Optional<PermissionsPapelDTO> permissionsPapelDTO = permissionsPapelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionsPapelDTO);
    }

    /**
     * {@code DELETE  /permissions-papels/:id} : delete the "id" permissionsPapel.
     *
     * @param id the id of the permissionsPapelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permissions-papels/{id}")
    public ResponseEntity<Void> deletePermissionsPapel(@PathVariable Long id) {
        log.debug("REST request to delete PermissionsPapel : {}", id);
        permissionsPapelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
