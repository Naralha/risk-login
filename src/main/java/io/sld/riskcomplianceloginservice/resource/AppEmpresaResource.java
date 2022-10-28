package io.sld.riskcomplianceloginservice.resource;

import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import io.sld.riskcomplianceloginservice.domain.repository.AppEmpresaRepository;
import io.sld.riskcomplianceloginservice.domain.service.AppEmpresaQueryService;
import io.sld.riskcomplianceloginservice.domain.service.AppEmpresaService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.AppEmpresaCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppEmpresaDTO;
import io.sld.riskcomplianceloginservice.resource.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.sld.riskcomplianceloginservice.resource.utils.HeaderUtil;
import io.sld.riskcomplianceloginservice.resource.utils.PaginationUtil;
import io.sld.riskcomplianceloginservice.resource.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


/**
 * REST controller for managing {@link AppEmpresa}.
 */
@RestController
@RequestMapping("/api")
public class AppEmpresaResource {

    private final Logger log = LoggerFactory.getLogger(AppEmpresaResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginserviceAppEmpresa";

    @Value("${spring.application.name}")
    private String applicationName;

    private final AppEmpresaService appEmpresaService;

    private final AppEmpresaRepository appEmpresaRepository;

    private final AppEmpresaQueryService appEmpresaQueryService;

    public AppEmpresaResource(
        AppEmpresaService appEmpresaService,
        AppEmpresaRepository appEmpresaRepository,
        AppEmpresaQueryService appEmpresaQueryService
    ) {
        this.appEmpresaService = appEmpresaService;
        this.appEmpresaRepository = appEmpresaRepository;
        this.appEmpresaQueryService = appEmpresaQueryService;
    }

    /**
     * {@code POST  /app-empresas} : Create a new appEmpresa.
     *
     * @param appEmpresaDTO the appEmpresaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appEmpresaDTO, or with status {@code 400 (Bad Request)} if the appEmpresa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-empresas")
    public ResponseEntity<AppEmpresaDTO> createAppEmpresa(@Valid @RequestBody AppEmpresaDTO appEmpresaDTO) throws URISyntaxException {
        log.debug("REST request to save AppEmpresa : {}", appEmpresaDTO);
        if (appEmpresaDTO.getId() != null) {
            throw new BadRequestAlertException("A new appEmpresa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppEmpresaDTO result = appEmpresaService.save(appEmpresaDTO);
        return ResponseEntity
            .created(new URI("/api/app-empresas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-empresas/:id} : Updates an existing appEmpresa.
     *
     * @param id the id of the appEmpresaDTO to save.
     * @param appEmpresaDTO the appEmpresaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appEmpresaDTO,
     * or with status {@code 400 (Bad Request)} if the appEmpresaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appEmpresaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-empresas/{id}")
    public ResponseEntity<AppEmpresaDTO> updateAppEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppEmpresaDTO appEmpresaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppEmpresa : {}, {}", id, appEmpresaDTO);
        if (appEmpresaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appEmpresaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appEmpresaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppEmpresaDTO result = appEmpresaService.update(appEmpresaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appEmpresaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /app-empresas/:id} : Partial updates given fields of an existing appEmpresa, field will ignore if it is null
     *
     * @param id the id of the appEmpresaDTO to save.
     * @param appEmpresaDTO the appEmpresaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appEmpresaDTO,
     * or with status {@code 400 (Bad Request)} if the appEmpresaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appEmpresaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appEmpresaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/app-empresas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppEmpresaDTO> partialUpdateAppEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppEmpresaDTO appEmpresaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppEmpresa partially : {}, {}", id, appEmpresaDTO);
        if (appEmpresaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appEmpresaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appEmpresaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppEmpresaDTO> result = appEmpresaService.partialUpdate(appEmpresaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appEmpresaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /app-empresas} : get all the appEmpresas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appEmpresas in body.
     */
    @GetMapping("/app-empresas")
    public ResponseEntity<List<AppEmpresaDTO>> getAllAppEmpresas(
        AppEmpresaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AppEmpresas by criteria: {}", criteria);
        Page<AppEmpresaDTO> page = appEmpresaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-empresas/count} : count all the appEmpresas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/app-empresas/count")
    public ResponseEntity<Long> countAppEmpresas(AppEmpresaCriteria criteria) {
        log.debug("REST request to count AppEmpresas by criteria: {}", criteria);
        return ResponseEntity.ok().body(appEmpresaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-empresas/:id} : get the "id" appEmpresa.
     *
     * @param id the id of the appEmpresaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appEmpresaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-empresas/{id}")
    public ResponseEntity<AppEmpresaDTO> getAppEmpresa(@PathVariable Long id) {
        log.debug("REST request to get AppEmpresa : {}", id);
        Optional<AppEmpresaDTO> appEmpresaDTO = appEmpresaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appEmpresaDTO);
    }

    /**
     * {@code DELETE  /app-empresas/:id} : delete the "id" appEmpresa.
     *
     * @param id the id of the appEmpresaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-empresas/{id}")
    public ResponseEntity<Void> deleteAppEmpresa(@PathVariable Long id) {
        log.debug("REST request to delete AppEmpresa : {}", id);
        appEmpresaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
