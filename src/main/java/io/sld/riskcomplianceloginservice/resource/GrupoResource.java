package io.sld.riskcomplianceloginservice.resource;

import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.GrupoQueryService;
import io.sld.riskcomplianceloginservice.domain.service.GrupoService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.GrupoCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Grupo}.
 */
@RestController
@RequestMapping("/api")
public class GrupoResource {

    private final Logger log = LoggerFactory.getLogger(GrupoResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginserviceGrupo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GrupoService grupoService;

    private final GrupoRepository grupoRepository;

    private final GrupoQueryService grupoQueryService;

    public GrupoResource(GrupoService grupoService, GrupoRepository grupoRepository, GrupoQueryService grupoQueryService) {
        this.grupoService = grupoService;
        this.grupoRepository = grupoRepository;
        this.grupoQueryService = grupoQueryService;
    }

    /**
     * {@code POST  /grupos} : Create a new grupo.
     *
     * @param grupoDTO the grupoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupoDTO, or with status {@code 400 (Bad Request)} if the grupo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grupos")
    public ResponseEntity<GrupoDTO> createGrupo(@Valid @RequestBody GrupoDTO grupoDTO) throws URISyntaxException {
        log.debug("REST request to save Grupo : {}", grupoDTO);
        if (grupoDTO.getId() != null) {
            throw new BadRequestAlertException("A new grupo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GrupoDTO result = grupoService.save(grupoDTO);
        return ResponseEntity
            .created(new URI("/api/grupos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /grupos/:id} : Updates an existing grupo.
     *
     * @param id the id of the grupoDTO to save.
     * @param grupoDTO the grupoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoDTO,
     * or with status {@code 400 (Bad Request)} if the grupoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grupos/{id}")
    public ResponseEntity<GrupoDTO> updateGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GrupoDTO grupoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Grupo : {}, {}", id, grupoDTO);
        if (grupoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GrupoDTO result = grupoService.update(grupoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /grupos/:id} : Partial updates given fields of an existing grupo, field will ignore if it is null
     *
     * @param id the id of the grupoDTO to save.
     * @param grupoDTO the grupoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoDTO,
     * or with status {@code 400 (Bad Request)} if the grupoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the grupoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grupos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GrupoDTO> partialUpdateGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GrupoDTO grupoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Grupo partially : {}, {}", id, grupoDTO);
        if (grupoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GrupoDTO> result = grupoService.partialUpdate(grupoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, grupoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /grupos} : get all the grupos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grupos in body.
     */
    @GetMapping("/grupos")
    public ResponseEntity<List<GrupoDTO>> getAllGrupos(
        GrupoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Grupos by criteria: {}", criteria);
        Page<GrupoDTO> page = grupoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grupos/count} : count all the grupos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/grupos/count")
    public ResponseEntity<Long> countGrupos(GrupoCriteria criteria) {
        log.debug("REST request to count Grupos by criteria: {}", criteria);
        return ResponseEntity.ok().body(grupoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /grupos/:id} : get the "id" grupo.
     *
     * @param id the id of the grupoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grupos/{id}")
    public ResponseEntity<GrupoDTO> getGrupo(@PathVariable Long id) {
        log.debug("REST request to get Grupo : {}", id);
        Optional<GrupoDTO> grupoDTO = grupoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(grupoDTO);
    }

    /**
     * {@code DELETE  /grupos/:id} : delete the "id" grupo.
     *
     * @param id the id of the grupoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grupos/{id}")
    public ResponseEntity<Void> deleteGrupo(@PathVariable Long id) {
        log.debug("REST request to delete Grupo : {}", id);
        grupoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
