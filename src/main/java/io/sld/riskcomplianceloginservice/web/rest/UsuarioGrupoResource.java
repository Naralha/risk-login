package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.repository.UsuarioGrupoRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.UsuarioGrupo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UsuarioGrupoResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioGrupoResource.class);

    private static final String ENTITY_NAME = "usuarioGrupo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioGrupoRepository usuarioGrupoRepository;

    public UsuarioGrupoResource(UsuarioGrupoRepository usuarioGrupoRepository) {
        this.usuarioGrupoRepository = usuarioGrupoRepository;
    }

    /**
     * {@code POST  /usuario-grupos} : Create a new usuarioGrupo.
     *
     * @param usuarioGrupo the usuarioGrupo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioGrupo, or with status {@code 400 (Bad Request)} if the usuarioGrupo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/usuario-grupos")
    public Mono<ResponseEntity<UsuarioGrupo>> createUsuarioGrupo(@Valid @RequestBody UsuarioGrupo usuarioGrupo) throws URISyntaxException {
        log.debug("REST request to save UsuarioGrupo : {}", usuarioGrupo);
        if (usuarioGrupo.getId() != null) {
            throw new BadRequestAlertException("A new usuarioGrupo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return usuarioGrupoRepository
            .save(usuarioGrupo)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/usuario-grupos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /usuario-grupos/:id} : Updates an existing usuarioGrupo.
     *
     * @param id the id of the usuarioGrupo to save.
     * @param usuarioGrupo the usuarioGrupo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioGrupo,
     * or with status {@code 400 (Bad Request)} if the usuarioGrupo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioGrupo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/usuario-grupos/{id}")
    public Mono<ResponseEntity<UsuarioGrupo>> updateUsuarioGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioGrupo usuarioGrupo
    ) throws URISyntaxException {
        log.debug("REST request to update UsuarioGrupo : {}, {}", id, usuarioGrupo);
        if (usuarioGrupo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioGrupo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return usuarioGrupoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return usuarioGrupoRepository
                    .save(usuarioGrupo)
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
     * {@code PATCH  /usuario-grupos/:id} : Partial updates given fields of an existing usuarioGrupo, field will ignore if it is null
     *
     * @param id the id of the usuarioGrupo to save.
     * @param usuarioGrupo the usuarioGrupo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioGrupo,
     * or with status {@code 400 (Bad Request)} if the usuarioGrupo is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioGrupo is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioGrupo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/usuario-grupos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UsuarioGrupo>> partialUpdateUsuarioGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioGrupo usuarioGrupo
    ) throws URISyntaxException {
        log.debug("REST request to partial update UsuarioGrupo partially : {}, {}", id, usuarioGrupo);
        if (usuarioGrupo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioGrupo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return usuarioGrupoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UsuarioGrupo> result = usuarioGrupoRepository
                    .findById(usuarioGrupo.getId())
                    .map(existingUsuarioGrupo -> {
                        if (usuarioGrupo.getIdnVarUsuarioCadastrado() != null) {
                            existingUsuarioGrupo.setIdnVarUsuarioCadastrado(usuarioGrupo.getIdnVarUsuarioCadastrado());
                        }
                        if (usuarioGrupo.getIdnVarGrupo() != null) {
                            existingUsuarioGrupo.setIdnVarGrupo(usuarioGrupo.getIdnVarGrupo());
                        }
                        if (usuarioGrupo.getIdnVarUsuario() != null) {
                            existingUsuarioGrupo.setIdnVarUsuario(usuarioGrupo.getIdnVarUsuario());
                        }

                        return existingUsuarioGrupo;
                    })
                    .flatMap(usuarioGrupoRepository::save);

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
     * {@code GET  /usuario-grupos} : get all the usuarioGrupos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioGrupos in body.
     */
    @GetMapping("/usuario-grupos")
    public Mono<List<UsuarioGrupo>> getAllUsuarioGrupos() {
        log.debug("REST request to get all UsuarioGrupos");
        return usuarioGrupoRepository.findAll().collectList();
    }

    /**
     * {@code GET  /usuario-grupos} : get all the usuarioGrupos as a stream.
     * @return the {@link Flux} of usuarioGrupos.
     */
    @GetMapping(value = "/usuario-grupos", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UsuarioGrupo> getAllUsuarioGruposAsStream() {
        log.debug("REST request to get all UsuarioGrupos as a stream");
        return usuarioGrupoRepository.findAll();
    }

    /**
     * {@code GET  /usuario-grupos/:id} : get the "id" usuarioGrupo.
     *
     * @param id the id of the usuarioGrupo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioGrupo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/usuario-grupos/{id}")
    public Mono<ResponseEntity<UsuarioGrupo>> getUsuarioGrupo(@PathVariable Long id) {
        log.debug("REST request to get UsuarioGrupo : {}", id);
        Mono<UsuarioGrupo> usuarioGrupo = usuarioGrupoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(usuarioGrupo);
    }

    /**
     * {@code DELETE  /usuario-grupos/:id} : delete the "id" usuarioGrupo.
     *
     * @param id the id of the usuarioGrupo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/usuario-grupos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteUsuarioGrupo(@PathVariable Long id) {
        log.debug("REST request to delete UsuarioGrupo : {}", id);
        return usuarioGrupoRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
