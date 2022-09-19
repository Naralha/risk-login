package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.Empresa;
import io.sld.riskcomplianceloginservice.repository.EmpresaRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.Empresa}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EmpresaResource {

    private final Logger log = LoggerFactory.getLogger(EmpresaResource.class);

    private static final String ENTITY_NAME = "empresa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpresaRepository empresaRepository;

    public EmpresaResource(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    /**
     * {@code POST  /empresas} : Create a new empresa.
     *
     * @param empresa the empresa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empresa, or with status {@code 400 (Bad Request)} if the empresa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/empresas")
    public Mono<ResponseEntity<Empresa>> createEmpresa(@Valid @RequestBody Empresa empresa) throws URISyntaxException {
        log.debug("REST request to save Empresa : {}", empresa);
        if (empresa.getId() != null) {
            throw new BadRequestAlertException("A new empresa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return empresaRepository
            .save(empresa)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/empresas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /empresas/:id} : Updates an existing empresa.
     *
     * @param id the id of the empresa to save.
     * @param empresa the empresa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empresa,
     * or with status {@code 400 (Bad Request)} if the empresa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empresa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/empresas/{id}")
    public Mono<ResponseEntity<Empresa>> updateEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Empresa empresa
    ) throws URISyntaxException {
        log.debug("REST request to update Empresa : {}, {}", id, empresa);
        if (empresa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empresa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return empresaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return empresaRepository
                    .save(empresa)
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
     * {@code PATCH  /empresas/:id} : Partial updates given fields of an existing empresa, field will ignore if it is null
     *
     * @param id the id of the empresa to save.
     * @param empresa the empresa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empresa,
     * or with status {@code 400 (Bad Request)} if the empresa is not valid,
     * or with status {@code 404 (Not Found)} if the empresa is not found,
     * or with status {@code 500 (Internal Server Error)} if the empresa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/empresas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Empresa>> partialUpdateEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Empresa empresa
    ) throws URISyntaxException {
        log.debug("REST request to partial update Empresa partially : {}, {}", id, empresa);
        if (empresa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empresa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return empresaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Empresa> result = empresaRepository
                    .findById(empresa.getId())
                    .map(existingEmpresa -> {
                        if (empresa.getIdnVarEmpresa() != null) {
                            existingEmpresa.setIdnVarEmpresa(empresa.getIdnVarEmpresa());
                        }
                        if (empresa.getnVarNome() != null) {
                            existingEmpresa.setnVarNome(empresa.getnVarNome());
                        }
                        if (empresa.getnVarDescricao() != null) {
                            existingEmpresa.setnVarDescricao(empresa.getnVarDescricao());
                        }

                        return existingEmpresa;
                    })
                    .flatMap(empresaRepository::save);

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
     * {@code GET  /empresas} : get all the empresas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empresas in body.
     */
    @GetMapping("/empresas")
    public Mono<List<Empresa>> getAllEmpresas() {
        log.debug("REST request to get all Empresas");
        return empresaRepository.findAll().collectList();
    }

    /**
     * {@code GET  /empresas} : get all the empresas as a stream.
     * @return the {@link Flux} of empresas.
     */
    @GetMapping(value = "/empresas", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Empresa> getAllEmpresasAsStream() {
        log.debug("REST request to get all Empresas as a stream");
        return empresaRepository.findAll();
    }

    /**
     * {@code GET  /empresas/:id} : get the "id" empresa.
     *
     * @param id the id of the empresa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empresa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/empresas/{id}")
    public Mono<ResponseEntity<Empresa>> getEmpresa(@PathVariable Long id) {
        log.debug("REST request to get Empresa : {}", id);
        Mono<Empresa> empresa = empresaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(empresa);
    }

    /**
     * {@code DELETE  /empresas/:id} : delete the "id" empresa.
     *
     * @param id the id of the empresa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/empresas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEmpresa(@PathVariable Long id) {
        log.debug("REST request to delete Empresa : {}", id);
        return empresaRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
