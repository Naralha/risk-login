package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.UsuarioPapel;
import io.sld.riskcomplianceloginservice.repository.UsuarioPapelRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.UsuarioPapel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UsuarioPapelResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioPapelResource.class);

    private static final String ENTITY_NAME = "usuarioPapel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioPapelRepository usuarioPapelRepository;

    public UsuarioPapelResource(UsuarioPapelRepository usuarioPapelRepository) {
        this.usuarioPapelRepository = usuarioPapelRepository;
    }

    /**
     * {@code POST  /usuario-papels} : Create a new usuarioPapel.
     *
     * @param usuarioPapel the usuarioPapel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuarioPapel, or with status {@code 400 (Bad Request)} if the usuarioPapel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/usuario-papels")
    public Mono<ResponseEntity<UsuarioPapel>> createUsuarioPapel(@Valid @RequestBody UsuarioPapel usuarioPapel) throws URISyntaxException {
        log.debug("REST request to save UsuarioPapel : {}", usuarioPapel);
        if (usuarioPapel.getId() != null) {
            throw new BadRequestAlertException("A new usuarioPapel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return usuarioPapelRepository
            .save(usuarioPapel)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/usuario-papels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /usuario-papels/:id} : Updates an existing usuarioPapel.
     *
     * @param id the id of the usuarioPapel to save.
     * @param usuarioPapel the usuarioPapel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioPapel,
     * or with status {@code 400 (Bad Request)} if the usuarioPapel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuarioPapel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/usuario-papels/{id}")
    public Mono<ResponseEntity<UsuarioPapel>> updateUsuarioPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UsuarioPapel usuarioPapel
    ) throws URISyntaxException {
        log.debug("REST request to update UsuarioPapel : {}, {}", id, usuarioPapel);
        if (usuarioPapel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioPapel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return usuarioPapelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return usuarioPapelRepository
                    .save(usuarioPapel)
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
     * {@code PATCH  /usuario-papels/:id} : Partial updates given fields of an existing usuarioPapel, field will ignore if it is null
     *
     * @param id the id of the usuarioPapel to save.
     * @param usuarioPapel the usuarioPapel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuarioPapel,
     * or with status {@code 400 (Bad Request)} if the usuarioPapel is not valid,
     * or with status {@code 404 (Not Found)} if the usuarioPapel is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuarioPapel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/usuario-papels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UsuarioPapel>> partialUpdateUsuarioPapel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UsuarioPapel usuarioPapel
    ) throws URISyntaxException {
        log.debug("REST request to partial update UsuarioPapel partially : {}, {}", id, usuarioPapel);
        if (usuarioPapel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuarioPapel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return usuarioPapelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UsuarioPapel> result = usuarioPapelRepository
                    .findById(usuarioPapel.getId())
                    .map(existingUsuarioPapel -> {
                        if (usuarioPapel.getIdnVarUsuarioCadastrado() != null) {
                            existingUsuarioPapel.setIdnVarUsuarioCadastrado(usuarioPapel.getIdnVarUsuarioCadastrado());
                        }
                        if (usuarioPapel.getIdnVarPapel() != null) {
                            existingUsuarioPapel.setIdnVarPapel(usuarioPapel.getIdnVarPapel());
                        }
                        if (usuarioPapel.getIdnVarUsuario() != null) {
                            existingUsuarioPapel.setIdnVarUsuario(usuarioPapel.getIdnVarUsuario());
                        }

                        return existingUsuarioPapel;
                    })
                    .flatMap(usuarioPapelRepository::save);

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
     * {@code GET  /usuario-papels} : get all the usuarioPapels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarioPapels in body.
     */
    @GetMapping("/usuario-papels")
    public Mono<List<UsuarioPapel>> getAllUsuarioPapels() {
        log.debug("REST request to get all UsuarioPapels");
        return usuarioPapelRepository.findAll().collectList();
    }

    /**
     * {@code GET  /usuario-papels} : get all the usuarioPapels as a stream.
     * @return the {@link Flux} of usuarioPapels.
     */
    @GetMapping(value = "/usuario-papels", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UsuarioPapel> getAllUsuarioPapelsAsStream() {
        log.debug("REST request to get all UsuarioPapels as a stream");
        return usuarioPapelRepository.findAll();
    }

    /**
     * {@code GET  /usuario-papels/:id} : get the "id" usuarioPapel.
     *
     * @param id the id of the usuarioPapel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuarioPapel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/usuario-papels/{id}")
    public Mono<ResponseEntity<UsuarioPapel>> getUsuarioPapel(@PathVariable Long id) {
        log.debug("REST request to get UsuarioPapel : {}", id);
        Mono<UsuarioPapel> usuarioPapel = usuarioPapelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(usuarioPapel);
    }

    /**
     * {@code DELETE  /usuario-papels/:id} : delete the "id" usuarioPapel.
     *
     * @param id the id of the usuarioPapel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/usuario-papels/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteUsuarioPapel(@PathVariable Long id) {
        log.debug("REST request to delete UsuarioPapel : {}", id);
        return usuarioPapelRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
