package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.Empresa;
import io.sld.riskcomplianceloginservice.repository.EmpresaRepository;
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
 * Integration tests for the {@link EmpresaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmpresaResourceIT {

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Empresa empresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .nVarDescricao(DEFAULT_N_VAR_DESCRICAO);
        return empresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createUpdatedEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarDescricao(UPDATED_N_VAR_DESCRICAO);
        return empresa;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Empresa.class).block();
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
        empresa = createEntity(em);
    }

    @Test
    void createEmpresa() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().collectList().block().size();
        // Create the Empresa
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate + 1);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testEmpresa.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testEmpresa.getnVarDescricao()).isEqualTo(DEFAULT_N_VAR_DESCRICAO);
    }

    @Test
    void createEmpresaWithExistingId() throws Exception {
        // Create the Empresa with an existing ID
        empresa.setId(1L);

        int databaseSizeBeforeCreate = empresaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = empresaRepository.findAll().collectList().block().size();
        // set the field null
        empresa.setIdnVarEmpresa(null);

        // Create the Empresa, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = empresaRepository.findAll().collectList().block().size();
        // set the field null
        empresa.setnVarNome(null);

        // Create the Empresa, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEmpresasAsStream() {
        // Initialize the database
        empresaRepository.save(empresa).block();

        List<Empresa> empresaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Empresa.class)
            .getResponseBody()
            .filter(empresa::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(empresaList).isNotNull();
        assertThat(empresaList).hasSize(1);
        Empresa testEmpresa = empresaList.get(0);
        assertThat(testEmpresa.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testEmpresa.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testEmpresa.getnVarDescricao()).isEqualTo(DEFAULT_N_VAR_DESCRICAO);
    }

    @Test
    void getAllEmpresas() {
        // Initialize the database
        empresaRepository.save(empresa).block();

        // Get all the empresaList
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
            .value(hasItem(empresa.getId().intValue()))
            .jsonPath("$.[*].idnVarEmpresa")
            .value(hasItem(DEFAULT_IDN_VAR_EMPRESA))
            .jsonPath("$.[*].nVarNome")
            .value(hasItem(DEFAULT_N_VAR_NOME))
            .jsonPath("$.[*].nVarDescricao")
            .value(hasItem(DEFAULT_N_VAR_DESCRICAO));
    }

    @Test
    void getEmpresa() {
        // Initialize the database
        empresaRepository.save(empresa).block();

        // Get the empresa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, empresa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(empresa.getId().intValue()))
            .jsonPath("$.idnVarEmpresa")
            .value(is(DEFAULT_IDN_VAR_EMPRESA))
            .jsonPath("$.nVarNome")
            .value(is(DEFAULT_N_VAR_NOME))
            .jsonPath("$.nVarDescricao")
            .value(is(DEFAULT_N_VAR_DESCRICAO));
    }

    @Test
    void getNonExistingEmpresa() {
        // Get the empresa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findById(empresa.getId()).block();
        updatedEmpresa.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA).nVarNome(UPDATED_N_VAR_NOME).nVarDescricao(UPDATED_N_VAR_DESCRICAO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEmpresa.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testEmpresa.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testEmpresa.getnVarDescricao()).isEqualTo(UPDATED_N_VAR_DESCRICAO);
    }

    @Test
    void putNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, empresa.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA).nVarNome(UPDATED_N_VAR_NOME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testEmpresa.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testEmpresa.getnVarDescricao()).isEqualTo(DEFAULT_N_VAR_DESCRICAO);
    }

    @Test
    void fullUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA).nVarNome(UPDATED_N_VAR_NOME).nVarDescricao(UPDATED_N_VAR_DESCRICAO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testEmpresa.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testEmpresa.getnVarDescricao()).isEqualTo(UPDATED_N_VAR_DESCRICAO);
    }

    @Test
    void patchNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, empresa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().collectList().block().size();
        empresa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(empresa))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmpresa() {
        // Initialize the database
        empresaRepository.save(empresa).block();

        int databaseSizeBeforeDelete = empresaRepository.findAll().collectList().block().size();

        // Delete the empresa
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, empresa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Empresa> empresaList = empresaRepository.findAll().collectList().block();
        assertThat(empresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
