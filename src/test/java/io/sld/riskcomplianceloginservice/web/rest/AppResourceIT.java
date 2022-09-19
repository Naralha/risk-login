package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.App;
import io.sld.riskcomplianceloginservice.repository.AppRepository;
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
 * Integration tests for the {@link AppResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppResourceIT {

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private App app;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createEntity(EntityManager em) {
        App app = new App()
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA);
        return app;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createUpdatedEntity(EntityManager em) {
        App app = new App()
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        return app;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(App.class).block();
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
        app = createEntity(em);
    }

    @Test
    void createApp() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().collectList().block().size();
        // Create the App
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeCreate + 1);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    void createAppWithExistingId() throws Exception {
        // Create the App with an existing ID
        app.setId(1L);

        int databaseSizeBeforeCreate = appRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().collectList().block().size();
        // set the field null
        app.setIdnVarApp(null);

        // Create the App, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().collectList().block().size();
        // set the field null
        app.setnVarNome(null);

        // Create the App, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().collectList().block().size();
        // set the field null
        app.setIdnVarUsuario(null);

        // Create the App, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().collectList().block().size();
        // set the field null
        app.setIdnVarEmpresa(null);

        // Create the App, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAppsAsStream() {
        // Initialize the database
        appRepository.save(app).block();

        List<App> appList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(App.class)
            .getResponseBody()
            .filter(app::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(appList).isNotNull();
        assertThat(appList).hasSize(1);
        App testApp = appList.get(0);
        assertThat(testApp.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    void getAllApps() {
        // Initialize the database
        appRepository.save(app).block();

        // Get all the appList
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
            .value(hasItem(app.getId().intValue()))
            .jsonPath("$.[*].idnVarApp")
            .value(hasItem(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.[*].nVarNome")
            .value(hasItem(DEFAULT_N_VAR_NOME))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.[*].idnVarEmpresa")
            .value(hasItem(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    void getApp() {
        // Initialize the database
        appRepository.save(app).block();

        // Get the app
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, app.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(app.getId().intValue()))
            .jsonPath("$.idnVarApp")
            .value(is(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.nVarNome")
            .value(is(DEFAULT_N_VAR_NOME))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.idnVarEmpresa")
            .value(is(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    void getNonExistingApp() {
        // Get the app
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewApp() throws Exception {
        // Initialize the database
        appRepository.save(app).block();

        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();

        // Update the app
        App updatedApp = appRepository.findById(app.getId()).block();
        updatedApp
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedApp.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedApp))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void putNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();
        app.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, app.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.save(app).block();

        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApp.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void fullUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.save(app).block();

        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApp.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void patchNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();
        app.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, app.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().collectList().block().size();
        app.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(app))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the App in the database
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApp() {
        // Initialize the database
        appRepository.save(app).block();

        int databaseSizeBeforeDelete = appRepository.findAll().collectList().block().size();

        // Delete the app
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, app.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<App> appList = appRepository.findAll().collectList().block();
        assertThat(appList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
