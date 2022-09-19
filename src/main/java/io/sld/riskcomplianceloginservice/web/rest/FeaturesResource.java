package io.sld.riskcomplianceloginservice.web.rest;

import io.sld.riskcomplianceloginservice.domain.Features;
import io.sld.riskcomplianceloginservice.repository.FeaturesRepository;
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
 * REST controller for managing {@link io.sld.riskcomplianceloginservice.domain.Features}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FeaturesResource {

    private final Logger log = LoggerFactory.getLogger(FeaturesResource.class);

    private static final String ENTITY_NAME = "features";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeaturesRepository featuresRepository;

    public FeaturesResource(FeaturesRepository featuresRepository) {
        this.featuresRepository = featuresRepository;
    }

    /**
     * {@code POST  /features} : Create a new features.
     *
     * @param features the features to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new features, or with status {@code 400 (Bad Request)} if the features has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/features")
    public Mono<ResponseEntity<Features>> createFeatures(@Valid @RequestBody Features features) throws URISyntaxException {
        log.debug("REST request to save Features : {}", features);
        if (features.getId() != null) {
            throw new BadRequestAlertException("A new features cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return featuresRepository
            .save(features)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/features/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /features/:id} : Updates an existing features.
     *
     * @param id the id of the features to save.
     * @param features the features to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated features,
     * or with status {@code 400 (Bad Request)} if the features is not valid,
     * or with status {@code 500 (Internal Server Error)} if the features couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/features/{id}")
    public Mono<ResponseEntity<Features>> updateFeatures(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Features features
    ) throws URISyntaxException {
        log.debug("REST request to update Features : {}, {}", id, features);
        if (features.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, features.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return featuresRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return featuresRepository
                    .save(features)
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
     * {@code PATCH  /features/:id} : Partial updates given fields of an existing features, field will ignore if it is null
     *
     * @param id the id of the features to save.
     * @param features the features to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated features,
     * or with status {@code 400 (Bad Request)} if the features is not valid,
     * or with status {@code 404 (Not Found)} if the features is not found,
     * or with status {@code 500 (Internal Server Error)} if the features couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/features/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Features>> partialUpdateFeatures(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Features features
    ) throws URISyntaxException {
        log.debug("REST request to partial update Features partially : {}, {}", id, features);
        if (features.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, features.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return featuresRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Features> result = featuresRepository
                    .findById(features.getId())
                    .map(existingFeatures -> {
                        if (features.getIdnVarFeatures() != null) {
                            existingFeatures.setIdnVarFeatures(features.getIdnVarFeatures());
                        }
                        if (features.getnVarNome() != null) {
                            existingFeatures.setnVarNome(features.getnVarNome());
                        }
                        if (features.getIdnVarApp() != null) {
                            existingFeatures.setIdnVarApp(features.getIdnVarApp());
                        }
                        if (features.getIdnVarUsuario() != null) {
                            existingFeatures.setIdnVarUsuario(features.getIdnVarUsuario());
                        }

                        return existingFeatures;
                    })
                    .flatMap(featuresRepository::save);

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
     * {@code GET  /features} : get all the features.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of features in body.
     */
    @GetMapping("/features")
    public Mono<List<Features>> getAllFeatures() {
        log.debug("REST request to get all Features");
        return featuresRepository.findAll().collectList();
    }

    /**
     * {@code GET  /features} : get all the features as a stream.
     * @return the {@link Flux} of features.
     */
    @GetMapping(value = "/features", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Features> getAllFeaturesAsStream() {
        log.debug("REST request to get all Features as a stream");
        return featuresRepository.findAll();
    }

    /**
     * {@code GET  /features/:id} : get the "id" features.
     *
     * @param id the id of the features to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the features, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/features/{id}")
    public Mono<ResponseEntity<Features>> getFeatures(@PathVariable Long id) {
        log.debug("REST request to get Features : {}", id);
        Mono<Features> features = featuresRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(features);
    }

    /**
     * {@code DELETE  /features/:id} : delete the "id" features.
     *
     * @param id the id of the features to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/features/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFeatures(@PathVariable Long id) {
        log.debug("REST request to delete Features : {}", id);
        return featuresRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
