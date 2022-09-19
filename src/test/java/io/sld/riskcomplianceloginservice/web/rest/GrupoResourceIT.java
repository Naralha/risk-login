package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.Grupo;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.GrupoRepository;
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
 * Integration tests for the {@link GrupoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GrupoResourceIT {

    private static final String DEFAULT_IDN_VAR_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_GRUPO = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grupos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Grupo grupo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupo createEntity(EntityManager em) {
        Grupo grupo = new Grupo()
            .idnVarGrupo(DEFAULT_IDN_VAR_GRUPO)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA);
        return grupo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupo createUpdatedEntity(EntityManager em) {
        Grupo grupo = new Grupo()
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        return grupo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Grupo.class).block();
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
        grupo = createEntity(em);
    }

    @Test
    void createGrupo() throws Exception {
        int databaseSizeBeforeCreate = grupoRepository.findAll().collectList().block().size();
        // Create the Grupo
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeCreate + 1);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    void createGrupoWithExistingId() throws Exception {
        // Create the Grupo with an existing ID
        grupo.setId(1L);

        int databaseSizeBeforeCreate = grupoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarGrupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().collectList().block().size();
        // set the field null
        grupo.setIdnVarGrupo(null);

        // Create the Grupo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().collectList().block().size();
        // set the field null
        grupo.setnVarNome(null);

        // Create the Grupo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().collectList().block().size();
        // set the field null
        grupo.setIdnVarUsuario(null);

        // Create the Grupo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().collectList().block().size();
        // set the field null
        grupo.setIdnVarEmpresa(null);

        // Create the Grupo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGruposAsStream() {
        // Initialize the database
        grupoRepository.save(grupo).block();

        List<Grupo> grupoList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Grupo.class)
            .getResponseBody()
            .filter(grupo::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(grupoList).isNotNull();
        assertThat(grupoList).hasSize(1);
        Grupo testGrupo = grupoList.get(0);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    void getAllGrupos() {
        // Initialize the database
        grupoRepository.save(grupo).block();

        // Get all the grupoList
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
            .value(hasItem(grupo.getId().intValue()))
            .jsonPath("$.[*].idnVarGrupo")
            .value(hasItem(DEFAULT_IDN_VAR_GRUPO))
            .jsonPath("$.[*].nVarNome")
            .value(hasItem(DEFAULT_N_VAR_NOME))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.[*].idnVarEmpresa")
            .value(hasItem(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    void getGrupo() {
        // Initialize the database
        grupoRepository.save(grupo).block();

        // Get the grupo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, grupo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(grupo.getId().intValue()))
            .jsonPath("$.idnVarGrupo")
            .value(is(DEFAULT_IDN_VAR_GRUPO))
            .jsonPath("$.nVarNome")
            .value(is(DEFAULT_N_VAR_NOME))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.idnVarEmpresa")
            .value(is(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    void getNonExistingGrupo() {
        // Get the grupo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGrupo() throws Exception {
        // Initialize the database
        grupoRepository.save(grupo).block();

        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();

        // Update the grupo
        Grupo updatedGrupo = grupoRepository.findById(grupo.getId()).block();
        updatedGrupo
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedGrupo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedGrupo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void putNonExistingGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();
        grupo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, grupo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();
        grupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();
        grupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGrupoWithPatch() throws Exception {
        // Initialize the database
        grupoRepository.save(grupo).block();

        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();

        // Update the grupo using partial update
        Grupo partialUpdatedGrupo = new Grupo();
        partialUpdatedGrupo.setId(grupo.getId());

        partialUpdatedGrupo.idnVarGrupo(UPDATED_IDN_VAR_GRUPO).idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGrupo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    void fullUpdateGrupoWithPatch() throws Exception {
        // Initialize the database
        grupoRepository.save(grupo).block();

        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();

        // Update the grupo using partial update
        Grupo partialUpdatedGrupo = new Grupo();
        partialUpdatedGrupo.setId(grupo.getId());

        partialUpdatedGrupo
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGrupo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    void patchNonExistingGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();
        grupo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, grupo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();
        grupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().collectList().block().size();
        grupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(grupo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGrupo() {
        // Initialize the database
        grupoRepository.save(grupo).block();

        int databaseSizeBeforeDelete = grupoRepository.findAll().collectList().block().size();

        // Delete the grupo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, grupo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Grupo> grupoList = grupoRepository.findAll().collectList().block();
        assertThat(grupoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
