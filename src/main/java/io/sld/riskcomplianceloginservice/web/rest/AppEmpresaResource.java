package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.AppEmpresa;
import io.sld.riskcomplianceloginservice.repository.AppEmpresaRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.AppEmpresa}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AppEmpresaResource {

    private final Logger log = LoggerFactory.getLogger(AppEmpresaResource.class);

    private static final String ENTITY_NAME = "appEmpresa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppEmpresaRepository appEmpresaRepository;

    public AppEmpresaResource(AppEmpresaRepository appEmpresaRepository) {
        this.appEmpresaRepository = appEmpresaRepository;
    }

    /**
     * {@code POST  /app-empresas} : Create a new appEmpresa.
     *
     * @param appEmpresa the appEmpresa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appEmpresa, or with status {@code 400 (Bad Request)} if the appEmpresa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-empresas")
    public Mono<ResponseEntity<AppEmpresa>> createAppEmpresa(@Valid @RequestBody AppEmpresa appEmpresa) throws URISyntaxException {
        log.debug("REST request to save AppEmpresa : {}", appEmpresa);
        if (appEmpresa.getId() != null) {
            throw new BadRequestAlertException("A new appEmpresa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return appEmpresaRepository
            .save(appEmpresa)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/app-empresas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /app-empresas/:id} : Updates an existing appEmpresa.
     *
     * @param id the id of the appEmpresa to save.
     * @param appEmpresa the appEmpresa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appEmpresa,
     * or with status {@code 400 (Bad Request)} if the appEmpresa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appEmpresa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-empresas/{id}")
    public Mono<ResponseEntity<AppEmpresa>> updateAppEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppEmpresa appEmpresa
    ) throws URISyntaxException {
        log.debug("REST request to update AppEmpresa : {}, {}", id, appEmpresa);
        if (appEmpresa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appEmpresa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appEmpresaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return appEmpresaRepository
                    .save(appEmpresa)
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
     * {@code PATCH  /app-empresas/:id} : Partial updates given fields of an existing appEmpresa, field will ignore if it is null
     *
     * @param id the id of the appEmpresa to save.
     * @param appEmpresa the appEmpresa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appEmpresa,
     * or with status {@code 400 (Bad Request)} if the appEmpresa is not valid,
     * or with status {@code 404 (Not Found)} if the appEmpresa is not found,
     * or with status {@code 500 (Internal Server Error)} if the appEmpresa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/app-empresas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AppEmpresa>> partialUpdateAppEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppEmpresa appEmpresa
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppEmpresa partially : {}, {}", id, appEmpresa);
        if (appEmpresa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appEmpresa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appEmpresaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AppEmpresa> result = appEmpresaRepository
                    .findById(appEmpresa.getId())
                    .map(existingAppEmpresa -> {
                        if (appEmpresa.getIdnVarApp() != null) {
                            existingAppEmpresa.setIdnVarApp(appEmpresa.getIdnVarApp());
                        }
                        if (appEmpresa.getIdnVarEmpresa() != null) {
                            existingAppEmpresa.setIdnVarEmpresa(appEmpresa.getIdnVarEmpresa());
                        }
                        if (appEmpresa.getIdnVarUsuario() != null) {
                            existingAppEmpresa.setIdnVarUsuario(appEmpresa.getIdnVarUsuario());
                        }

                        return existingAppEmpresa;
                    })
                    .flatMap(appEmpresaRepository::save);

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
     * {@code GET  /app-empresas} : get all the appEmpresas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appEmpresas in body.
     */
    @GetMapping("/app-empresas")
    public Mono<List<AppEmpresa>> getAllAppEmpresas() {
        log.debug("REST request to get all AppEmpresas");
        return appEmpresaRepository.findAll().collectList();
    }

    /**
     * {@code GET  /app-empresas} : get all the appEmpresas as a stream.
     * @return the {@link Flux} of appEmpresas.
     */
    @GetMapping(value = "/app-empresas", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AppEmpresa> getAllAppEmpresasAsStream() {
        log.debug("REST request to get all AppEmpresas as a stream");
        return appEmpresaRepository.findAll();
    }

    /**
     * {@code GET  /app-empresas/:id} : get the "id" appEmpresa.
     *
     * @param id the id of the appEmpresa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appEmpresa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-empresas/{id}")
    public Mono<ResponseEntity<AppEmpresa>> getAppEmpresa(@PathVariable Long id) {
        log.debug("REST request to get AppEmpresa : {}", id);
        Mono<AppEmpresa> appEmpresa = appEmpresaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appEmpresa);
    }

    /**
     * {@code DELETE  /app-empresas/:id} : delete the "id" appEmpresa.
     *
     * @param id the id of the appEmpresa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-empresas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAppEmpresa(@PathVariable Long id) {
        log.debug("REST request to delete AppEmpresa : {}", id);
        return appEmpresaRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
