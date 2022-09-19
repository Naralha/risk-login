package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.PermissionsPapel;
import io.sld.riskcomplianceloginservice.repository.PermissionsPapelRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.PermissionsPapel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PermissionsPapelResource {

    private final Logger log = LoggerFactory.getLogger(PermissionsPapelResource.class);

    private static final String ENTITY_NAME = "permissionsPapel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionsPapelRepository permissionsPapelRepository;

    public PermissionsPapelResource(PermissionsPapelRepository permissionsPapelRepository) {
        this.permissionsPapelRepository = permissionsPapelRepository;
    }

    /**
     * {@code POST  /permissions-papels} : Create a new permissionsPapel.
     *
     * @param permissionsPapel the permissionsPapel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionsPapel, or with status {@code 400 (Bad Request)} if the permissionsPapel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permissions-papels")
    public Mono<ResponseEntity<PermissionsPapel>> createPermissionsPapel(@Valid @RequestBody PermissionsPapel permissionsPapel)
        throws URISyntaxException {
        log.debug("REST request to save PermissionsPapel : {}", permissionsPapel);
        if (permissionsPapel.getId() != null) {
            throw new BadRequestAlertException("A new permissionsPapel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return permissionsPapelRepository
            .save(permissionsPapel)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/permissions-papels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /permissions-papels/:id} : Updates an existing permissionsPapel.
     *
     * @param id the id of the permissionsPapel to save.
     * @param permissionsPapel the permissionsPapel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionsPapel,
     * or with status {@code 400 (Bad Request)} if the permissionsPapel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionsPapel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permissions-papels/{id}")
    public Mono<ResponseEntity<PermissionsPapel>> updatePermissionsPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PermissionsPapel permissionsPapel
    ) throws URISyntaxException {
        log.debug("REST request to update PermissionsPapel : {}, {}", id, permissionsPapel);
        if (permissionsPapel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionsPapel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return permissionsPapelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return permissionsPapelRepository
                    .save(permissionsPapel)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /permissions-papels/:id} : Partial updates given fields of an existing permissionsPapel, field will ignore if it is null
     *
     * @param id the id of the permissionsPapel to save.
     * @param permissionsPapel the permissionsPapel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionsPapel,
     * or with status {@code 400 (Bad Request)} if the permissionsPapel is not valid,
     * or with status {@code 404 (Not Found)} if the permissionsPapel is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissionsPapel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permissions-papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PermissionsPapel>> partialUpdatePermissionsPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PermissionsPapel permissionsPapel
    ) throws URISyntaxException {
        log.debug("REST request to partial update PermissionsPapel partially : {}, {}", id, permissionsPapel);
        if (permissionsPapel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionsPapel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return permissionsPapelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PermissionsPapel> result = permissionsPapelRepository
                    .findById(permissionsPapel.getId())
                    .map(existingPermissionsPapel -> {
                        if (permissionsPapel.getIdnVarPermissions() != null) {
                            existingPermissionsPapel.setIdnVarPermissions(permissionsPapel.getIdnVarPermissions());
                        }
                        if (permissionsPapel.getIdnVarPapel() != null) {
                            existingPermissionsPapel.setIdnVarPapel(permissionsPapel.getIdnVarPapel());
                        }
                        if (permissionsPapel.getIdnVarFeatures() != null) {
                            existingPermissionsPapel.setIdnVarFeatures(permissionsPapel.getIdnVarFeatures());
                        }
                        if (permissionsPapel.getIdnVarUsuario() != null) {
                            existingPermissionsPapel.setIdnVarUsuario(permissionsPapel.getIdnVarUsuario());
                        }

                        return existingPermissionsPapel;
                    })
                    .flatMap(permissionsPapelRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /permissions-papels} : get all the permissionsPapels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissionsPapels in body.
     */
    @GetMapping("/permissions-papels")
    public Mono<List<PermissionsPapel>> getAllPermissionsPapels() {
        log.debug("REST request to get all PermissionsPapels");
        return permissionsPapelRepository.findAll().collectList();
    }

    /**
     * {@code GET  /permissions-papels} : get all the permissionsPapels as a stream.
     * @return the {@link Flux} of permissionsPapels.
     */
    @GetMapping(value = "/permissions-papels", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PermissionsPapel> getAllPermissionsPapelsAsStream() {
        log.debug("REST request to get all PermissionsPapels as a stream");
        return permissionsPapelRepository.findAll();
    }

    /**
     * {@code GET  /permissions-papels/:id} : get the "id" permissionsPapel.
     *
     * @param id the id of the permissionsPapel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionsPapel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions-papels/{id}")
    public Mono<ResponseEntity<PermissionsPapel>> getPermissionsPapel(@PathVariable Long id) {
        log.debug("REST request to get PermissionsPapel : {}", id);
        Mono<PermissionsPapel> permissionsPapel = permissionsPapelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(permissionsPapel);
    }

    /**
     * {@code DELETE  /permissions-papels/:id} : delete the "id" permissionsPapel.
     *
     * @param id the id of the permissionsPapel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permissions-papels/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePermissionsPapel(@PathVariable Long id) {
        log.debug("REST request to delete PermissionsPapel : {}", id);
        return permissionsPapelRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
