package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.Permissions;
import io.sld.riskcomplianceloginservice.repository.PermissionsRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.Permissions}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PermissionsResource {

    private final Logger log = LoggerFactory.getLogger(PermissionsResource.class);

    private static final String ENTITY_NAME = "permissions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionsRepository permissionsRepository;

    public PermissionsResource(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    /**
     * {@code POST  /permissions} : Create a new permissions.
     *
     * @param permissions the permissions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissions, or with status {@code 400 (Bad Request)} if the permissions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permissions")
    public Mono<ResponseEntity<Permissions>> createPermissions(@Valid @RequestBody Permissions permissions) throws URISyntaxException {
        log.debug("REST request to save Permissions : {}", permissions);
        if (permissions.getId() != null) {
            throw new BadRequestAlertException("A new permissions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return permissionsRepository
            .save(permissions)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/permissions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /permissions/:id} : Updates an existing permissions.
     *
     * @param id the id of the permissions to save.
     * @param permissions the permissions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissions,
     * or with status {@code 400 (Bad Request)} if the permissions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permissions/{id}")
    public Mono<ResponseEntity<Permissions>> updatePermissions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Permissions permissions
    ) throws URISyntaxException {
        log.debug("REST request to update Permissions : {}, {}", id, permissions);
        if (permissions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return permissionsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return permissionsRepository
                    .save(permissions)
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
     * {@code PATCH  /permissions/:id} : Partial updates given fields of an existing permissions, field will ignore if it is null
     *
     * @param id the id of the permissions to save.
     * @param permissions the permissions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissions,
     * or with status {@code 400 (Bad Request)} if the permissions is not valid,
     * or with status {@code 404 (Not Found)} if the permissions is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Permissions>> partialUpdatePermissions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Permissions permissions
    ) throws URISyntaxException {
        log.debug("REST request to partial update Permissions partially : {}, {}", id, permissions);
        if (permissions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return permissionsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Permissions> result = permissionsRepository
                    .findById(permissions.getId())
                    .map(existingPermissions -> {
                        if (permissions.getIdnVarPermissions() != null) {
                            existingPermissions.setIdnVarPermissions(permissions.getIdnVarPermissions());
                        }
                        if (permissions.getnVarNome() != null) {
                            existingPermissions.setnVarNome(permissions.getnVarNome());
                        }
                        if (permissions.getnVarTipoPermissao() != null) {
                            existingPermissions.setnVarTipoPermissao(permissions.getnVarTipoPermissao());
                        }
                        if (permissions.getIdnVarUsuario() != null) {
                            existingPermissions.setIdnVarUsuario(permissions.getIdnVarUsuario());
                        }

                        return existingPermissions;
                    })
                    .flatMap(permissionsRepository::save);

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
     * {@code GET  /permissions} : get all the permissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissions in body.
     */
    @GetMapping("/permissions")
    public Mono<List<Permissions>> getAllPermissions() {
        log.debug("REST request to get all Permissions");
        return permissionsRepository.findAll().collectList();
    }

    /**
     * {@code GET  /permissions} : get all the permissions as a stream.
     * @return the {@link Flux} of permissions.
     */
    @GetMapping(value = "/permissions", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Permissions> getAllPermissionsAsStream() {
        log.debug("REST request to get all Permissions as a stream");
        return permissionsRepository.findAll();
    }

    /**
     * {@code GET  /permissions/:id} : get the "id" permissions.
     *
     * @param id the id of the permissions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions/{id}")
    public Mono<ResponseEntity<Permissions>> getPermissions(@PathVariable Long id) {
        log.debug("REST request to get Permissions : {}", id);
        Mono<Permissions> permissions = permissionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(permissions);
    }

    /**
     * {@code DELETE  /permissions/:id} : delete the "id" permissions.
     *
     * @param id the id of the permissions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permissions/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePermissions(@PathVariable Long id) {
        log.debug("REST request to delete Permissions : {}", id);
        return permissionsRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
