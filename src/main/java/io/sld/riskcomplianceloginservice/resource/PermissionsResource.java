package io.sld.riskcomplianceloginservice.resource;

import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import io.sld.riskcomplianceloginservice.domain.repository.PermissionsRepository;
import io.sld.riskcomplianceloginservice.domain.service.PermissionsQueryService;
import io.sld.riskcomplianceloginservice.domain.service.PermissionsService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.PermissionsCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsDTO;
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
 * REST controller for managing {@link Permissions}.
 */
@RestController
@RequestMapping("/api")
public class PermissionsResource {

    private final Logger log = LoggerFactory.getLogger(PermissionsResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginservicePermissions";

    @Value("${spring.application.name}")
    private String applicationName;

    private final PermissionsService permissionsService;

    private final PermissionsRepository permissionsRepository;

    private final PermissionsQueryService permissionsQueryService;

    public PermissionsResource(
        PermissionsService permissionsService,
        PermissionsRepository permissionsRepository,
        PermissionsQueryService permissionsQueryService
    ) {
        this.permissionsService = permissionsService;
        this.permissionsRepository = permissionsRepository;
        this.permissionsQueryService = permissionsQueryService;
    }

    /**
     * {@code POST  /permissions} : Create a new permissions.
     *
     * @param permissionsDTO the permissionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionsDTO, or with status {@code 400 (Bad Request)} if the permissions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permissions")
    public ResponseEntity<PermissionsDTO> createPermissions(@Valid @RequestBody PermissionsDTO permissionsDTO) throws URISyntaxException {
        log.debug("REST request to save Permissions : {}", permissionsDTO);
        if (permissionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new permissions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PermissionsDTO result = permissionsService.save(permissionsDTO);
        return ResponseEntity
            .created(new URI("/api/permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permissions/:id} : Updates an existing permissions.
     *
     * @param id the id of the permissionsDTO to save.
     * @param permissionsDTO the permissionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionsDTO,
     * or with status {@code 400 (Bad Request)} if the permissionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permissions/{id}")
    public ResponseEntity<PermissionsDTO> updatePermissions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PermissionsDTO permissionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Permissions : {}, {}", id, permissionsDTO);
        if (permissionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PermissionsDTO result = permissionsService.update(permissionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permissions/:id} : Partial updates given fields of an existing permissions, field will ignore if it is null
     *
     * @param id the id of the permissionsDTO to save.
     * @param permissionsDTO the permissionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionsDTO,
     * or with status {@code 400 (Bad Request)} if the permissionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permissionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermissionsDTO> partialUpdatePermissions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PermissionsDTO permissionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Permissions partially : {}, {}", id, permissionsDTO);
        if (permissionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermissionsDTO> result = permissionsService.partialUpdate(permissionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permissions} : get all the permissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissions in body.
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionsDTO>> getAllPermissions(
        PermissionsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Permissions by criteria: {}", criteria);
        Page<PermissionsDTO> page = permissionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permissions/count} : count all the permissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/permissions/count")
    public ResponseEntity<Long> countPermissions(PermissionsCriteria criteria) {
        log.debug("REST request to count Permissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(permissionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /permissions/:id} : get the "id" permissions.
     *
     * @param id the id of the permissionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions/{id}")
    public ResponseEntity<PermissionsDTO> getPermissions(@PathVariable Long id) {
        log.debug("REST request to get Permissions : {}", id);
        Optional<PermissionsDTO> permissionsDTO = permissionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionsDTO);
    }

    /**
     * {@code DELETE  /permissions/:id} : delete the "id" permissions.
     *
     * @param id the id of the permissionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermissions(@PathVariable Long id) {
        log.debug("REST request to delete Permissions : {}", id);
        permissionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
