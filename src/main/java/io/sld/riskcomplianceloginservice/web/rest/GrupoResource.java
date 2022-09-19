package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.Grupo;
import io.sld.riskcomplianceloginservice.repository.GrupoRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.Grupo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GrupoResource {

    private final Logger log = LoggerFactory.getLogger(GrupoResource.class);

    private static final String ENTITY_NAME = "grupo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GrupoRepository grupoRepository;

    public GrupoResource(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    /**
     * {@code POST  /grupos} : Create a new grupo.
     *
     * @param grupo the grupo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupo, or with status {@code 400 (Bad Request)} if the grupo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grupos")
    public Mono<ResponseEntity<Grupo>> createGrupo(@Valid @RequestBody Grupo grupo) throws URISyntaxException {
        log.debug("REST request to save Grupo : {}", grupo);
        if (grupo.getId() != null) {
            throw new BadRequestAlertException("A new grupo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return grupoRepository
            .save(grupo)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/grupos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /grupos/:id} : Updates an existing grupo.
     *
     * @param id the id of the grupo to save.
     * @param grupo the grupo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupo,
     * or with status {@code 400 (Bad Request)} if the grupo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grupos/{id}")
    public Mono<ResponseEntity<Grupo>> updateGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Grupo grupo
    ) throws URISyntaxException {
        log.debug("REST request to update Grupo : {}, {}", id, grupo);
        if (grupo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return grupoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return grupoRepository
                    .save(grupo)
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
     * {@code PATCH  /grupos/:id} : Partial updates given fields of an existing grupo, field will ignore if it is null
     *
     * @param id the id of the grupo to save.
     * @param grupo the grupo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupo,
     * or with status {@code 400 (Bad Request)} if the grupo is not valid,
     * or with status {@code 404 (Not Found)} if the grupo is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grupos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Grupo>> partialUpdateGrupo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Grupo grupo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Grupo partially : {}, {}", id, grupo);
        if (grupo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return grupoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Grupo> result = grupoRepository
                    .findById(grupo.getId())
                    .map(existingGrupo -> {
                        if (grupo.getIdnVarGrupo() != null) {
                            existingGrupo.setIdnVarGrupo(grupo.getIdnVarGrupo());
                        }
                        if (grupo.getnVarNome() != null) {
                            existingGrupo.setnVarNome(grupo.getnVarNome());
                        }
                        if (grupo.getIdnVarUsuario() != null) {
                            existingGrupo.setIdnVarUsuario(grupo.getIdnVarUsuario());
                        }
                        if (grupo.getIdnVarEmpresa() != null) {
                            existingGrupo.setIdnVarEmpresa(grupo.getIdnVarEmpresa());
                        }

                        return existingGrupo;
                    })
                    .flatMap(grupoRepository::save);

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
     * {@code GET  /grupos} : get all the grupos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grupos in body.
     */
    @GetMapping("/grupos")
    public Mono<List<Grupo>> getAllGrupos() {
        log.debug("REST request to get all Grupos");
        return grupoRepository.findAll().collectList();
    }

    /**
     * {@code GET  /grupos} : get all the grupos as a stream.
     * @return the {@link Flux} of grupos.
     */
    @GetMapping(value = "/grupos", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Grupo> getAllGruposAsStream() {
        log.debug("REST request to get all Grupos as a stream");
        return grupoRepository.findAll();
    }

    /**
     * {@code GET  /grupos/:id} : get the "id" grupo.
     *
     * @param id the id of the grupo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grupos/{id}")
    public Mono<ResponseEntity<Grupo>> getGrupo(@PathVariable Long id) {
        log.debug("REST request to get Grupo : {}", id);
        Mono<Grupo> grupo = grupoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(grupo);
    }

    /**
     * {@code DELETE  /grupos/:id} : delete the "id" grupo.
     *
     * @param id the id of the grupo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grupos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteGrupo(@PathVariable Long id) {
        log.debug("REST request to delete Grupo : {}", id);
        return grupoRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
