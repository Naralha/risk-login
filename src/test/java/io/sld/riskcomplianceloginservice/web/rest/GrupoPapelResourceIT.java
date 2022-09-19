package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.GrupoPapel;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.GrupoPapelRepository;
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
 * Integration tests for the {@link GrupoPapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GrupoPapelResourceIT {

    private static final String DEFAULT_IDN_VAR_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_GRUPO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_PAPEL = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PAPEL = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grupo-papels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GrupoPapelRepository grupoPapelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private GrupoPapel grupoPapel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupoPapel createEntity(EntityManager em) {
        GrupoPapel grupoPapel = new GrupoPapel()
            .idnVarGrupo(DEFAULT_IDN_VAR_GRUPO)
            .idnVarPapel(DEFAULT_IDN_VAR_PAPEL)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA);
        return grupoPapel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupoPapel createUpdatedEntity(EntityManager em) {
        GrupoPapel grupoPapel = new GrupoPapel()
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        return grupoPapel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(GrupoPapel.class).block();
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
        grupoPapel = createEntity(em);
    }

    @Test
    void createGrupoPapel() throws Exception {
        int databaseSizeBeforeCreate = grupoPapelRepository.findAll().collectList().block().size();
        // Create the GrupoPapel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeCreate + 1);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    void createGrupoPapelWithExistingId() throws Exception {
        // Create the GrupoPapel with an existing ID
        grupoPapel.setId(1L);

        int databaseSizeBeforeCreate = grupoPapelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarGrupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().collectList().block().size();
        // set the field null
        grupoPapel.setIdnVarGrupo(null);

        // Create the GrupoPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().collectList().block().size();
        // set the field null
        grupoPapel.setIdnVarPapel(null);

        // Create the GrupoPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().collectList().block().size();
        // set the field null
        grupoPapel.setIdnVarUsuario(null);

        // Create the GrupoPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().collectList().block().size();
        // set the field null
        grupoPapel.setIdnVarEmpresa(null);

        // Create the GrupoPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGrupoPapelsAsStream() {
        // Initialize the database
        grupoPapelRepository.save(grupoPapel).block();

        List<GrupoPapel> grupoPapelList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(GrupoPapel.class)
            .getResponseBody()
            .filter(grupoPapel::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(grupoPapelList).isNotNull();
        assertThat(grupoPapelList).hasSize(1);
        GrupoPapel testGrupoPapel = grupoPapelList.get(0);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    void getAllGrupoPapels() {
        // Initialize the database
        grupoPapelRepository.save(grupoPapel).block();

        // Get all the grupoPapelList
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
            .value(hasItem(grupoPapel.getId().intValue()))
            .jsonPath("$.[*].idnVarGrupo")
            .value(hasItem(DEFAULT_IDN_VAR_GRUPO))
            .jsonPath("$.[*].idnVarPapel")
            .value(hasItem(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.[*].idnVarEmpresa")
            .value(hasItem(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    void getGrupoPapel() {
        // Initialize the database
        grupoPapelRepository.save(grupoPapel).block();

        // Get the grupoPapel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, grupoPapel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(grupoPapel.getId().intValue()))
            .jsonPath("$.idnVarGrupo")
            .value(is(DEFAULT_IDN_VAR_GRUPO))
            .jsonPath("$.idnVarPapel")
            .value(is(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.idnVarEmpresa")
            .value(is(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    void getNonExistingGrupoPapel() {
        // Get the grupoPapel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGrupoPapel() throws Exception {
        // Initialize the database
        grupoPapelRepository.save(grupoPapel).block();

        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();

        // Update the grupoPapel
        GrupoPapel updatedGrupoPapel = grupoPapelRepository.findById(grupoPapel.getId()).block();
        updatedGrupoPapel
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedGrupoPapel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedGrupoPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void putNonExistingGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();
        grupoPapel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, grupoPapel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();
        grupoPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();
        grupoPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGrupoPapelWithPatch() throws Exception {
        // Initialize the database
        grupoPapelRepository.save(grupoPapel).block();

        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();

        // Update the grupoPapel using partial update
        GrupoPapel partialUpdatedGrupoPapel = new GrupoPapel();
        partialUpdatedGrupoPapel.setId(grupoPapel.getId());

        partialUpdatedGrupoPapel.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGrupoPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupoPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void fullUpdateGrupoPapelWithPatch() throws Exception {
        // Initialize the database
        grupoPapelRepository.save(grupoPapel).block();

        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();

        // Update the grupoPapel using partial update
        GrupoPapel partialUpdatedGrupoPapel = new GrupoPapel();
        partialUpdatedGrupoPapel.setId(grupoPapel.getId());

        partialUpdatedGrupoPapel
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGrupoPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupoPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void patchNonExistingGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();
        grupoPapel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, grupoPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();
        grupoPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().collectList().block().size();
        grupoPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupoPapel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGrupoPapel() {
        // Initialize the database
        grupoPapelRepository.save(grupoPapel).block();

        int databaseSizeBeforeDelete = grupoPapelRepository.findAll().collectList().block().size();

        // Delete the grupoPapel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, grupoPapel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll().collectList().block();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
