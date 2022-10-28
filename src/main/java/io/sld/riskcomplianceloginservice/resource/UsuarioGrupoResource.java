package io.sld.riskcomplianceloginservice.resource;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioGrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.UsuarioGrupoQueryService;
import io.sld.riskcomplianceloginservice.domain.service.UsuarioGrupoService;
import io.sld.riskcomplianceloginservice.domain.service.criteria.UsuarioGrupoCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioGrupoDTO;
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
 * REST controller for managing {@link UsuarioGrupo}.
 */
@RestController
@RequestMapping("/api")
public class UsuarioGrupoResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioGrupoResource.class);

    private static final String ENTITY_NAME = "riskcomplianceloginserviceUsuarioGrupo";

    @Value("${spring.application.name}")
    private String applicationName;

    private final UsuarioGrupoService usuarioGrupoService;

    private final UsuarioGrupoRepository usuarioGrupoRepository;

    private final UsuarioGrupoQueryService usuarioGrupoQueryService;

    public UsuarioGrupoResource(
        UsuarioGrupoService usuarioGrupoService,
        UsuarioGrupoRepository usuarioGrupoRepository,
        UsuarioGrupoQueryService usuarioGrupoQueryService
    ) {
        this.usuarioGrupoService = usuarioGrupoService;
        this.usuarioGrupoRepository = usuarioGrupoRepository;
        this.usuarioGrupoQueryService = usuarioGrupoQueryService;
    }

    /**
     * {@code POST  /usuario-grupos} : Create a new usuarioGrupo.
     *
     * @param usuarioGrupoDTO the usuarioGrupoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioGrupoDTO, or with status {@code 400 (Bad Request)} if the usuarioGrupo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/usuario-grupos")
    public ResponseEntity<UsuarioGrupoDTO> createUsuarioGrupo(@Valid @RequestBody UsuarioGrupoDTO usuarioGrupoDTO)
        throws URISyntaxException {
        log.debug("REST request to save UsuarioGrupo : {}", usuarioGrupoDTO);
        if (usuarioGrupoDTO.getId() != null) {
            throw new BadRequestAlertException("A new usuarioGrupo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UsuarioGrupoDTO result = usuarioGrupoService.save(usuarioGrupoDTO);
        return ResponseEntity
            .created(new URI("/api/usuario-grupos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /usuario-grupos/:id} : Updates an existing usuarioGrupo.
     *
     * @param id the id of the usuarioGrupoDTO to save.
     * @param usuarioGrupoDTO the usuarioGrupoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioGrupoDTO,
     * or with status {@code 400 (Bad Request)} if the usuarioGrupoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioGrupoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/usuario-grupos/{id}")
    public ResponseEntity<UsuarioGrupoDTO> updateUsuarioGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioGrupoDTO usuarioGrupoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UsuarioGrupo : {}, {}", id, usuarioGrupoDTO);
        if (usuarioGrupoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioGrupoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioGrupoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UsuarioGrupoDTO result = usuarioGrupoService.update(usuarioGrupoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioGrupoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /usuario-grupos/:id} : Partial updates given fields of an existing usuarioGrupo, field will ignore if it is null
     *
     * @param id the id of the usuarioGrupoDTO to save.
     * @param usuarioGrupoDTO the usuarioGrupoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioGrupoDTO,
     * or with status {@code 400 (Bad Request)} if the usuarioGrupoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioGrupoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioGrupoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/usuario-grupos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UsuarioGrupoDTO> partialUpdateUsuarioGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioGrupoDTO usuarioGrupoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UsuarioGrupo partially : {}, {}", id, usuarioGrupoDTO);
        if (usuarioGrupoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioGrupoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioGrupoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UsuarioGrupoDTO> result = usuarioGrupoService.partialUpdate(usuarioGrupoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuarioGrupoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /usuario-grupos} : get all the usuarioGrupos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioGrupos in body.
     */
    @GetMapping("/usuario-grupos")
    public ResponseEntity<List<UsuarioGrupoDTO>> getAllUsuarioGrupos(
        UsuarioGrupoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UsuarioGrupos by criteria: {}", criteria);
        Page<UsuarioGrupoDTO> page = usuarioGrupoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /usuario-grupos/count} : count all the usuarioGrupos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/usuario-grupos/count")
    public ResponseEntity<Long> countUsuarioGrupos(UsuarioGrupoCriteria criteria) {
        log.debug("REST request to count UsuarioGrupos by criteria: {}", criteria);
        return ResponseEntity.ok().body(usuarioGrupoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /usuario-grupos/:id} : get the "id" usuarioGrupo.
     *
     * @param id the id of the usuarioGrupoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioGrupoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/usuario-grupos/{id}")
    public ResponseEntity<UsuarioGrupoDTO> getUsuarioGrupo(@PathVariable Long id) {
        log.debug("REST request to get UsuarioGrupo : {}", id);
        Optional<UsuarioGrupoDTO> usuarioGrupoDTO = usuarioGrupoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(usuarioGrupoDTO);
    }

    /**
     * {@code DELETE  /usuario-grupos/:id} : delete the "id" usuarioGrupo.
     *
     * @param id the id of the usuarioGrupoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/usuario-grupos/{id}")
    public ResponseEntity<Void> deleteUsuarioGrupo(@PathVariable Long id) {
        log.debug("REST request to delete UsuarioGrupo : {}", id);
        usuarioGrupoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
