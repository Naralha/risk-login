package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.Papel;
import io.sld.riskcomplianceloginservice.repository.PapelRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.Papel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PapelResource {

    private final Logger log = LoggerFactory.getLogger(PapelResource.class);

    private static final String ENTITY_NAME = "papel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PapelRepository papelRepository;

    public PapelResource(PapelRepository papelRepository) {
        this.papelRepository = papelRepository;
    }

    /**
     * {@code POST  /papels} : Create a new papel.
     *
     * @param papel the papel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new papel, or with status {@code 400 (Bad Request)} if the papel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/papels")
    public Mono<ResponseEntity<Papel>> createPapel(@Valid @RequestBody Papel papel) throws URISyntaxException {
        log.debug("REST request to save Papel : {}", papel);
        if (papel.getId() != null) {
            throw new BadRequestAlertException("A new papel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return papelRepository
            .save(papel)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/papels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /papels/:id} : Updates an existing papel.
     *
     * @param id the id of the papel to save.
     * @param papel the papel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated papel,
     * or with status {@code 400 (Bad Request)} if the papel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the papel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/papels/{id}")
    public Mono<ResponseEntity<Papel>> updatePapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Papel papel
    ) throws URISyntaxException {
        log.debug("REST request to update Papel : {}, {}", id, papel);
        if (papel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, papel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return papelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return papelRepository
                    .save(papel)
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
     * {@code PATCH  /papels/:id} : Partial updates given fields of an existing papel, field will ignore if it is null
     *
     * @param id the id of the papel to save.
     * @param papel the papel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated papel,
     * or with status {@code 400 (Bad Request)} if the papel is not valid,
     * or with status {@code 404 (Not Found)} if the papel is not found,
     * or with status {@code 500 (Internal Server Error)} if the papel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Papel>> partialUpdatePapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Papel papel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Papel partially : {}, {}", id, papel);
        if (papel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, papel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return papelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Papel> result = papelRepository
                    .findById(papel.getId())
                    .map(existingPapel -> {
                        if (papel.getIdnVarPapel() != null) {
                            existingPapel.setIdnVarPapel(papel.getIdnVarPapel());
                        }
                        if (papel.getnVarNome() != null) {
                            existingPapel.setnVarNome(papel.getnVarNome());
                        }
                        if (papel.getIdnVarApp() != null) {
                            existingPapel.setIdnVarApp(papel.getIdnVarApp());
                        }
                        if (papel.getIdnVarUsuario() != null) {
                            existingPapel.setIdnVarUsuario(papel.getIdnVarUsuario());
                        }

                        return existingPapel;
                    })
                    .flatMap(papelRepository::save);

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
     * {@code GET  /papels} : get all the papels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of papels in body.
     */
    @GetMapping("/papels")
    public Mono<List<Papel>> getAllPapels() {
        log.debug("REST request to get all Papels");
        return papelRepository.findAll().collectList();
    }

    /**
     * {@code GET  /papels} : get all the papels as a stream.
     * @return the {@link Flux} of papels.
     */
    @GetMapping(value = "/papels", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Papel> getAllPapelsAsStream() {
        log.debug("REST request to get all Papels as a stream");
        return papelRepository.findAll();
    }

    /**
     * {@code GET  /papels/:id} : get the "id" papel.
     *
     * @param id the id of the papel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the papel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/papels/{id}")
    public Mono<ResponseEntity<Papel>> getPapel(@PathVariable Long id) {
        log.debug("REST request to get Papel : {}", id);
        Mono<Papel> papel = papelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(papel);
    }

    /**
     * {@code DELETE  /papels/:id} : delete the "id" papel.
     *
     * @param id the id of the papel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/papels/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePapel(@PathVariable Long id) {
        log.debug("REST request to delete Papel : {}", id);
        return papelRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
