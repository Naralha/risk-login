package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.UsuarioPapelQueryService;
import io.sld.riskcomplianceloginservice.domain.service.UsuarioPapelService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.UsuarioPapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioPapelDTO;
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
 * REST controller for managing {@link UsuarioPapel}.
 */
@RestController
@RequestMapping("/api")
public class UsuarioPapelResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioPapelResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginserviceUsuarioPapel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioPapelService usuarioPapelService;

    private final UsuarioPapelRepository usuarioPapelRepository;

    private final UsuarioPapelQueryService usuarioPapelQueryService;

    public UsuarioPapelResource(
        UsuarioPapelService usuarioPapelService,
        UsuarioPapelRepository usuarioPapelRepository,
        UsuarioPapelQueryService usuarioPapelQueryService
    ) {
        this.usuarioPapelService = usuarioPapelService;
        this.usuarioPapelRepository = usuarioPapelRepository;
        this.usuarioPapelQueryService = usuarioPapelQueryService;
    }

    /**
     * {@code POST  /usuario-papels} : Create a new usuarioPapel.
     *
     * @param usuarioPapelDTO the usuarioPapelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioPapelDTO, or with status {@code 400 (Bad Request)} if the usuarioPapel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/usuario-papels")
    public ResponseEntity<UsuarioPapelDTO> createUsuarioPapel(@Valid @RequestBody UsuarioPapelDTO usuarioPapelDTO)
        throws URISyntaxException {
        log.debug("REST request to save UsuarioPapel : {}", usuarioPapelDTO);
        if (usuarioPapelDTO.getId() != null) {
            throw new BadRequestAlertException("A new usuarioPapel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UsuarioPapelDTO result = usuarioPapelService.save(usuarioPapelDTO);
        return ResponseEntity
            .created(new URI("/api/usuario-papels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /usuario-papels/:id} : Updates an existing usuarioPapel.
     *
     * @param id the id of the usuarioPapelDTO to save.
     * @param usuarioPapelDTO the usuarioPapelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioPapelDTO,
     * or with status {@code 400 (Bad Request)} if the usuarioPapelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioPapelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/usuario-papels/{id}")
    public ResponseEntity<UsuarioPapelDTO> updateUsuarioPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioPapelDTO usuarioPapelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UsuarioPapel : {}, {}", id, usuarioPapelDTO);
        if (usuarioPapelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioPapelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioPapelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UsuarioPapelDTO result = usuarioPapelService.update(usuarioPapelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioPapelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /usuario-papels/:id} : Partial updates given fields of an existing usuarioPapel, field will ignore if it is null
     *
     * @param id the id of the usuarioPapelDTO to save.
     * @param usuarioPapelDTO the usuarioPapelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioPapelDTO,
     * or with status {@code 400 (Bad Request)} if the usuarioPapelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioPapelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioPapelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/usuario-papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UsuarioPapelDTO> partialUpdateUsuarioPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioPapelDTO usuarioPapelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UsuarioPapel partially : {}, {}", id, usuarioPapelDTO);
        if (usuarioPapelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioPapelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioPapelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UsuarioPapelDTO> result = usuarioPapelService.partialUpdate(usuarioPapelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioPapelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /usuario-papels} : get all the usuarioPapels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioPapels in body.
     */
    @GetMapping("/usuario-papels")
    public ResponseEntity<List<UsuarioPapelDTO>> getAllUsuarioPapels(
        UsuarioPapelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UsuarioPapels by criteria: {}", criteria);
        Page<UsuarioPapelDTO> page = usuarioPapelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /usuario-papels/count} : count all the usuarioPapels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/usuario-papels/count")
    public ResponseEntity<Long> countUsuarioPapels(UsuarioPapelCriteria criteria) {
        log.debug("REST request to count UsuarioPapels by criteria: {}", criteria);
        return ResponseEntity.ok().body(usuarioPapelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /usuario-papels/:id} : get the "id" usuarioPapel.
     *
     * @param id the id of the usuarioPapelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioPapelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/usuario-papels/{id}")
    public ResponseEntity<UsuarioPapelDTO> getUsuarioPapel(@PathVariable Long id) {
        log.debug("REST request to get UsuarioPapel : {}", id);
        Optional<UsuarioPapelDTO> usuarioPapelDTO = usuarioPapelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usuarioPapelDTO);
    }

    /**
     * {@code DELETE  /usuario-papels/:id} : delete the "id" usuarioPapel.
     *
     * @param id the id of the usuarioPapelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/usuario-papels/{id}")
    public ResponseEntity<Void> deleteUsuarioPapel(@PathVariable Long id) {
        log.debug("REST request to delete UsuarioPapel : {}", id);
        usuarioPapelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
