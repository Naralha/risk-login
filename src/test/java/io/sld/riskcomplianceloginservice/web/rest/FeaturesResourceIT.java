package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.Features;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.FeaturesRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link FeaturesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FeaturesResourceIT {

    private static final String DEFAULT_IDN_VAR_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_FEATURES = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/features";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeaturesRepository featuresRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Features features;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Features createEntity(EntityManager em) {
        Features features = new Features()
            .idnVarFeatures(DEFAULT_IDN_VAR_FEATURES)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return features;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Features createUpdatedEntity(EntityManager em) {
        Features features = new Features()
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return features;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Features.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        features = createEntity(em);
    }

    @Test
    void createFeatures() throws Exception {
        int databaseSizeBeforeCreate = featuresRepository.findAll().collectList().block().size();
        // Create the Features
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeCreate + 1);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(DEFAULT_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void createFeaturesWithExistingId() throws Exception {
        // Create the Features with an existing ID
        features.setId(1L);

        int databaseSizeBeforeCreate = featuresRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarFeaturesIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().collectList().block().size();
        // set the field null
        features.setIdnVarFeatures(null);

        // Create the Features, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().collectList().block().size();
        // set the field null
        features.setnVarNome(null);

        // Create the Features, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().collectList().block().size();
        // set the field null
        features.setIdnVarApp(null);

        // Create the Features, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().collectList().block().size();
        // set the field null
        features.setIdnVarUsuario(null);

        // Create the Features, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFeaturesAsStream() {
        // Initialize the database
        featuresRepository.save(features).block();

        List<Features> featuresList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Features.class)
            .getResponseBody()
            .filter(features::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(featuresList).isNotNull();
        assertThat(featuresList).hasSize(1);
        Features testFeatures = featuresList.get(0);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(DEFAULT_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void getAllFeatures() {
        // Initialize the database
        featuresRepository.save(features).block();

        // Get all the featuresList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(features.getId().intValue()))
            .jsonPath("$.[*].idnVarFeatures")
            .value(hasItem(DEFAULT_IDN_VAR_FEATURES))
            .jsonPath("$.[*].nVarNome")
            .value(hasItem(DEFAULT_N_VAR_NOME))
            .jsonPath("$.[*].idnVarApp")
            .value(hasItem(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getFeatures() {
        // Initialize the database
        featuresRepository.save(features).block();

        // Get the features
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, features.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(features.getId().intValue()))
            .jsonPath("$.idnVarFeatures")
            .value(is(DEFAULT_IDN_VAR_FEATURES))
            .jsonPath("$.nVarNome")
            .value(is(DEFAULT_N_VAR_NOME))
            .jsonPath("$.idnVarApp")
            .value(is(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getNonExistingFeatures() {
        // Get the features
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFeatures() throws Exception {
        // Initialize the database
        featuresRepository.save(features).block();

        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();

        // Update the features
        Features updatedFeatures = featuresRepository.findById(features.getId()).block();
        updatedFeatures
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFeatures.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFeatures))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void putNonExistingFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();
        features.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, features.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();
        features.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();
        features.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFeaturesWithPatch() throws Exception {
        // Initialize the database
        featuresRepository.save(features).block();

        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();

        // Update the features using partial update
        Features partialUpdatedFeatures = new Features();
        partialUpdatedFeatures.setId(features.getId());

        partialUpdatedFeatures.idnVarFeatures(UPDATED_IDN_VAR_FEATURES).nVarNome(UPDATED_N_VAR_NOME).idnVarApp(UPDATED_IDN_VAR_APP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeatures.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeatures))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void fullUpdateFeaturesWithPatch() throws Exception {
        // Initialize the database
        featuresRepository.save(features).block();

        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();

        // Update the features using partial update
        Features partialUpdatedFeatures = new Features();
        partialUpdatedFeatures.setId(features.getId());

        partialUpdatedFeatures
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeatures.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeatures))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void patchNonExistingFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();
        features.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, features.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();
        features.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().collectList().block().size();
        features.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(features))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFeatures() {
        // Initialize the database
        featuresRepository.save(features).block();

        int databaseSizeBeforeDelete = featuresRepository.findAll().collectList().block().size();

        // Delete the features
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, features.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Features> featuresList = featuresRepository.findAll().collectList().block();
        assertThat(featuresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
