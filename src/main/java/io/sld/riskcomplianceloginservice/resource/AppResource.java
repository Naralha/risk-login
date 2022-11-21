package io.sld.riskcomplianceloginservice.resource;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.repository.AppRepository;
import io.sld.riskcomplianceloginservice.domain.service.AppQueryService;
import io.sld.riskcomplianceloginservice.domain.service.AppService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.AppCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
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
 * REST controller for managing {@link App}.
 */
@RestController
@RequestMapping("/api")
public class AppResource {

    private final Logger log = LoggerFactory.getLogger(AppResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginserviceApp";

    @Value("${spring.application.name}")
    private String applicationName;

    private final AppService appService;

    private final AppRepository appRepository;

    private final AppQueryService appQueryService;

    public AppResource(AppService appService, AppRepository appRepository, AppQueryService appQueryService) {
        this.appService = appService;
        this.appRepository = appRepository;
        this.appQueryService = appQueryService;
    }

    /**
     * {@code POST  /apps} : Create a new app.
     *
     * @param appDTO the appDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appDTO, or with status {@code 400 (Bad Request)} if the app has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apps")
    public ResponseEntity<AppDTO> createApp(@Valid @RequestBody AppDTO appDTO) throws URISyntaxException {
        log.debug("REST request to save App : {}", appDTO);
        if (appDTO.getId() != null) {
            throw new BadRequestAlertException("A new app cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppDTO result = appService.save(appDTO);
        return ResponseEntity
            .created(new URI("/api/apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apps/:id} : Updates an existing app.
     *
     * @param id the id of the appDTO to save.
     * @param appDTO the appDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appDTO,
     * or with status {@code 400 (Bad Request)} if the appDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apps/{id}")
    public ResponseEntity<AppDTO> updateApp(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody AppDTO appDTO)
        throws URISyntaxException {
        log.debug("REST request to update App : {}, {}", id, appDTO);
        if (appDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppDTO result = appService.update(appDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apps/:id} : Partial updates given fields of an existing app, field will ignore if it is null
     *
     * @param id the id of the appDTO to save.
     * @param appDTO the appDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appDTO,
     * or with status {@code 400 (Bad Request)} if the appDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppDTO> partialUpdateApp(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppDTO appDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update App partially : {}, {}", id, appDTO);
        if (appDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppDTO> result = appService.partialUpdate(appDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /apps} : get all the apps.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apps in body.
     */
    @GetMapping("/apps")
    public ResponseEntity<List<AppDTO>> getAllApps(AppCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Apps by criteria: {}", criteria);
        Page<AppDTO> page = appQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apps/count} : count all the apps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apps/count")
    public ResponseEntity<Long> countApps(AppCriteria criteria) {
        log.debug("REST request to count Apps by criteria: {}", criteria);
        return ResponseEntity.ok().body(appQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apps/:id} : get the "id" app.
     *
     * @param id the id of the appDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apps/{id}")
    public ResponseEntity<AppDTO> getApp(@PathVariable Long id) {
        log.debug("REST request to get App : {}", id);
        Optional<AppDTO> appDTO = appService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appDTO);
    }

    /**
     * {@code DELETE  /apps/:id} : delete the "id" app.
     *
     * @param id the id of the appDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apps/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable Long id) {
        log.debug("REST request to delete App : {}", id);
        appService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
