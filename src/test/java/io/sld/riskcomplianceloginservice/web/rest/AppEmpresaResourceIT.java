package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.AppEmpresa;
import io.sld.riskcomplianceloginservice.repository.AppEmpresaRepository;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
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
 * Integration tests for the {@link AppEmpresaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppEmpresaResourceIT {

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppEmpresaRepository appEmpresaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AppEmpresa appEmpresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppEmpresa createEntity(EntityManager em) {
        AppEmpresa appEmpresa = new AppEmpresa()
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return appEmpresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppEmpresa createUpdatedEntity(EntityManager em) {
        AppEmpresa appEmpresa = new AppEmpresa()
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return appEmpresa;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AppEmpresa.class).block();
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
        appEmpresa = createEntity(em);
    }

    @Test
    void createAppEmpresa() throws Exception {
        int databaseSizeBeforeCreate = appEmpresaRepository.findAll().collectList().block().size();
        // Create the AppEmpresa
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeCreate + 1);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void createAppEmpresaWithExistingId() throws Exception {
        // Create the AppEmpresa with an existing ID
        appEmpresa.setId(1L);

        int databaseSizeBeforeCreate = appEmpresaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = appEmpresaRepository.findAll().collectList().block().size();
        // set the field null
        appEmpresa.setIdnVarApp(null);

        // Create the AppEmpresa, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = appEmpresaRepository.findAll().collectList().block().size();
        // set the field null
        appEmpresa.setIdnVarEmpresa(null);

        // Create the AppEmpresa, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = appEmpresaRepository.findAll().collectList().block().size();
        // set the field null
        appEmpresa.setIdnVarUsuario(null);

        // Create the AppEmpresa, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAppEmpresasAsStream() {
        // Initialize the database
        appEmpresaRepository.save(appEmpresa).block();

        List<AppEmpresa> appEmpresaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AppEmpresa.class)
            .getResponseBody()
            .filter(appEmpresa::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(appEmpresaList).isNotNull();
        assertThat(appEmpresaList).hasSize(1);
        AppEmpresa testAppEmpresa = appEmpresaList.get(0);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void getAllAppEmpresas() {
        // Initialize the database
        appEmpresaRepository.save(appEmpresa).block();

        // Get all the appEmpresaList
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
            .value(hasItem(appEmpresa.getId().intValue()))
            .jsonPath("$.[*].idnVarApp")
            .value(hasItem(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.[*].idnVarEmpresa")
            .value(hasItem(DEFAULT_IDN_VAR_EMPRESA))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getAppEmpresa() {
        // Initialize the database
        appEmpresaRepository.save(appEmpresa).block();

        // Get the appEmpresa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, appEmpresa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(appEmpresa.getId().intValue()))
            .jsonPath("$.idnVarApp")
            .value(is(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.idnVarEmpresa")
            .value(is(DEFAULT_IDN_VAR_EMPRESA))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getNonExistingAppEmpresa() {
        // Get the appEmpresa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAppEmpresa() throws Exception {
        // Initialize the database
        appEmpresaRepository.save(appEmpresa).block();

        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();

        // Update the appEmpresa
        AppEmpresa updatedAppEmpresa = appEmpresaRepository.findById(appEmpresa.getId()).block();
        updatedAppEmpresa.idnVarApp(UPDATED_IDN_VAR_APP).idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA).idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAppEmpresa.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAppEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void putNonExistingAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();
        appEmpresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appEmpresa.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();
        appEmpresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();
        appEmpresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppEmpresaWithPatch() throws Exception {
        // Initialize the database
        appEmpresaRepository.save(appEmpresa).block();

        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();

        // Update the appEmpresa using partial update
        AppEmpresa partialUpdatedAppEmpresa = new AppEmpresa();
        partialUpdatedAppEmpresa.setId(appEmpresa.getId());

        partialUpdatedAppEmpresa.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppEmpresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAppEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void fullUpdateAppEmpresaWithPatch() throws Exception {
        // Initialize the database
        appEmpresaRepository.save(appEmpresa).block();

        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();

        // Update the appEmpresa using partial update
        AppEmpresa partialUpdatedAppEmpresa = new AppEmpresa();
        partialUpdatedAppEmpresa.setId(appEmpresa.getId());

        partialUpdatedAppEmpresa
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppEmpresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAppEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void patchNonExistingAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();
        appEmpresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, appEmpresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();
        appEmpresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().collectList().block().size();
        appEmpresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(appEmpresa))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppEmpresa() {
        // Initialize the database
        appEmpresaRepository.save(appEmpresa).block();

        int databaseSizeBeforeDelete = appEmpresaRepository.findAll().collectList().block().size();

        // Delete the appEmpresa
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, appEmpresa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll().collectList().block();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
