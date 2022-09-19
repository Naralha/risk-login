package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.Papel;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.PapelRepository;
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
 * Integration tests for the {@link PapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PapelResourceIT {

    private static final String DEFAULT_IDN_VAR_PAPEL = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PAPEL = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/papels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Papel papel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Papel createEntity(EntityManager em) {
        Papel papel = new Papel()
            .idnVarPapel(DEFAULT_IDN_VAR_PAPEL)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return papel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Papel createUpdatedEntity(EntityManager em) {
        Papel papel = new Papel()
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return papel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Papel.class).block();
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
        papel = createEntity(em);
    }

    @Test
    void createPapel() throws Exception {
        int databaseSizeBeforeCreate = papelRepository.findAll().collectList().block().size();
        // Create the Papel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeCreate + 1);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void createPapelWithExistingId() throws Exception {
        // Create the Papel with an existing ID
        papel.setId(1L);

        int databaseSizeBeforeCreate = papelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().collectList().block().size();
        // set the field null
        papel.setIdnVarPapel(null);

        // Create the Papel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().collectList().block().size();
        // set the field null
        papel.setnVarNome(null);

        // Create the Papel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().collectList().block().size();
        // set the field null
        papel.setIdnVarApp(null);

        // Create the Papel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().collectList().block().size();
        // set the field null
        papel.setIdnVarUsuario(null);

        // Create the Papel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPapelsAsStream() {
        // Initialize the database
        papelRepository.save(papel).block();

        List<Papel> papelList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Papel.class)
            .getResponseBody()
            .filter(papel::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(papelList).isNotNull();
        assertThat(papelList).hasSize(1);
        Papel testPapel = papelList.get(0);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void getAllPapels() {
        // Initialize the database
        papelRepository.save(papel).block();

        // Get all the papelList
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
            .value(hasItem(papel.getId().intValue()))
            .jsonPath("$.[*].idnVarPapel")
            .value(hasItem(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.[*].nVarNome")
            .value(hasItem(DEFAULT_N_VAR_NOME))
            .jsonPath("$.[*].idnVarApp")
            .value(hasItem(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getPapel() {
        // Initialize the database
        papelRepository.save(papel).block();

        // Get the papel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, papel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(papel.getId().intValue()))
            .jsonPath("$.idnVarPapel")
            .value(is(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.nVarNome")
            .value(is(DEFAULT_N_VAR_NOME))
            .jsonPath("$.idnVarApp")
            .value(is(DEFAULT_IDN_VAR_APP))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getNonExistingPapel() {
        // Get the papel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPapel() throws Exception {
        // Initialize the database
        papelRepository.save(papel).block();

        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();

        // Update the papel
        Papel updatedPapel = papelRepository.findById(papel.getId()).block();
        updatedPapel
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPapel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void putNonExistingPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();
        papel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, papel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();
        papel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();
        papel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePapelWithPatch() throws Exception {
        // Initialize the database
        papelRepository.save(papel).block();

        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();

        // Update the papel using partial update
        Papel partialUpdatedPapel = new Papel();
        partialUpdatedPapel.setId(papel.getId());

        partialUpdatedPapel.idnVarPapel(UPDATED_IDN_VAR_PAPEL).nVarNome(UPDATED_N_VAR_NOME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void fullUpdatePapelWithPatch() throws Exception {
        // Initialize the database
        papelRepository.save(papel).block();

        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();

        // Update the papel using partial update
        Papel partialUpdatedPapel = new Papel();
        partialUpdatedPapel.setId(papel.getId());

        partialUpdatedPapel
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void patchNonExistingPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();
        papel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, papel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();
        papel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().collectList().block().size();
        papel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(papel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePapel() {
        // Initialize the database
        papelRepository.save(papel).block();

        int databaseSizeBeforeDelete = papelRepository.findAll().collectList().block().size();

        // Delete the papel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, papel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Papel> papelList = papelRepository.findAll().collectList().block();
        assertThat(papelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
