package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.GrupoPapel;
import io.sld.riskcomplianceloginservice.repository.GrupoPapelRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.GrupoPapel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GrupoPapelResource {

    private final Logger log = LoggerFactory.getLogger(GrupoPapelResource.class);

    private static final String ENTITY_NAME = "grupoPapel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GrupoPapelRepository grupoPapelRepository;

    public GrupoPapelResource(GrupoPapelRepository grupoPapelRepository) {
        this.grupoPapelRepository = grupoPapelRepository;
    }

    /**
     * {@code POST  /grupo-papels} : Create a new grupoPapel.
     *
     * @param grupoPapel the grupoPapel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupoPapel, or with status {@code 400 (Bad Request)} if the grupoPapel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grupo-papels")
    public Mono<ResponseEntity<GrupoPapel>> createGrupoPapel(@Valid @RequestBody GrupoPapel grupoPapel) throws URISyntaxException {
        log.debug("REST request to save GrupoPapel : {}", grupoPapel);
        if (grupoPapel.getId() != null) {
            throw new BadRequestAlertException("A new grupoPapel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return grupoPapelRepository
            .save(grupoPapel)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/grupo-papels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /grupo-papels/:id} : Updates an existing grupoPapel.
     *
     * @param id the id of the grupoPapel to save.
     * @param grupoPapel the grupoPapel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoPapel,
     * or with status {@code 400 (Bad Request)} if the grupoPapel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupoPapel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grupo-papels/{id}")
    public Mono<ResponseEntity<GrupoPapel>> updateGrupoPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GrupoPapel grupoPapel
    ) throws URISyntaxException {
        log.debug("REST request to update GrupoPapel : {}, {}", id, grupoPapel);
        if (grupoPapel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoPapel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return grupoPapelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return grupoPapelRepository
                    .save(grupoPapel)
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
     * {@code PATCH  /grupo-papels/:id} : Partial updates given fields of an existing grupoPapel, field will ignore if it is null
     *
     * @param id the id of the grupoPapel to save.
     * @param grupoPapel the grupoPapel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoPapel,
     * or with status {@code 400 (Bad Request)} if the grupoPapel is not valid,
     * or with status {@code 404 (Not Found)} if the grupoPapel is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupoPapel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grupo-papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<GrupoPapel>> partialUpdateGrupoPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GrupoPapel grupoPapel
    ) throws URISyntaxException {
        log.debug("REST request to partial update GrupoPapel partially : {}, {}", id, grupoPapel);
        if (grupoPapel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoPapel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return grupoPapelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<GrupoPapel> result = grupoPapelRepository
                    .findById(grupoPapel.getId())
                    .map(existingGrupoPapel -> {
                        if (grupoPapel.getIdnVarGrupo() != null) {
                            existingGrupoPapel.setIdnVarGrupo(grupoPapel.getIdnVarGrupo());
                        }
                        if (grupoPapel.getIdnVarPapel() != null) {
                            existingGrupoPapel.setIdnVarPapel(grupoPapel.getIdnVarPapel());
                        }
                        if (grupoPapel.getIdnVarUsuario() != null) {
                            existingGrupoPapel.setIdnVarUsuario(grupoPapel.getIdnVarUsuario());
                        }
                        if (grupoPapel.getIdnVarEmpresa() != null) {
                            existingGrupoPapel.setIdnVarEmpresa(grupoPapel.getIdnVarEmpresa());
                        }

                        return existingGrupoPapel;
                    })
                    .flatMap(grupoPapelRepository::save);

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
     * {@code GET  /grupo-papels} : get all the grupoPapels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grupoPapels in body.
     */
    @GetMapping("/grupo-papels")
    public Mono<List<GrupoPapel>> getAllGrupoPapels() {
        log.debug("REST request to get all GrupoPapels");
        return grupoPapelRepository.findAll().collectList();
    }

    /**
     * {@code GET  /grupo-papels} : get all the grupoPapels as a stream.
     * @return the {@link Flux} of grupoPapels.
     */
    @GetMapping(value = "/grupo-papels", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<GrupoPapel> getAllGrupoPapelsAsStream() {
        log.debug("REST request to get all GrupoPapels as a stream");
        return grupoPapelRepository.findAll();
    }

    /**
     * {@code GET  /grupo-papels/:id} : get the "id" grupoPapel.
     *
     * @param id the id of the grupoPapel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupoPapel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grupo-papels/{id}")
    public Mono<ResponseEntity<GrupoPapel>> getGrupoPapel(@PathVariable Long id) {
        log.debug("REST request to get GrupoPapel : {}", id);
        Mono<GrupoPapel> grupoPapel = grupoPapelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(grupoPapel);
    }

    /**
     * {@code DELETE  /grupo-papels/:id} : delete the "id" grupoPapel.
     *
     * @param id the id of the grupoPapel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grupo-papels/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteGrupoPapel(@PathVariable Long id) {
        log.debug("REST request to delete GrupoPapel : {}", id);
        return grupoPapelRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
