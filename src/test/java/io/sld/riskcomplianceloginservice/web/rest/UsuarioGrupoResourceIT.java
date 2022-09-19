package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.UsuarioGrupoRepository;
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
 * Integration tests for the {@link UsuarioGrupoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UsuarioGrupoResourceIT {

    private static final String DEFAULT_IDN_VAR_USUARIO_CADASTRADO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO_CADASTRADO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_GRUPO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuario-grupos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioGrupoRepository usuarioGrupoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UsuarioGrupo usuarioGrupo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioGrupo createEntity(EntityManager em) {
        UsuarioGrupo usuarioGrupo = new UsuarioGrupo()
            .idnVarUsuarioCadastrado(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(DEFAULT_IDN_VAR_GRUPO)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return usuarioGrupo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioGrupo createUpdatedEntity(EntityManager em) {
        UsuarioGrupo usuarioGrupo = new UsuarioGrupo()
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return usuarioGrupo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UsuarioGrupo.class).block();
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
        usuarioGrupo = createEntity(em);
    }

    @Test
    void createUsuarioGrupo() throws Exception {
        int databaseSizeBeforeCreate = usuarioGrupoRepository.findAll().collectList().block().size();
        // Create the UsuarioGrupo
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeCreate + 1);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void createUsuarioGrupoWithExistingId() throws Exception {
        // Create the UsuarioGrupo with an existing ID
        usuarioGrupo.setId(1L);

        int databaseSizeBeforeCreate = usuarioGrupoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarUsuarioCadastradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioGrupoRepository.findAll().collectList().block().size();
        // set the field null
        usuarioGrupo.setIdnVarUsuarioCadastrado(null);

        // Create the UsuarioGrupo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarGrupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioGrupoRepository.findAll().collectList().block().size();
        // set the field null
        usuarioGrupo.setIdnVarGrupo(null);

        // Create the UsuarioGrupo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioGrupoRepository.findAll().collectList().block().size();
        // set the field null
        usuarioGrupo.setIdnVarUsuario(null);

        // Create the UsuarioGrupo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUsuarioGruposAsStream() {
        // Initialize the database
        usuarioGrupoRepository.save(usuarioGrupo).block();

        List<UsuarioGrupo> usuarioGrupoList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UsuarioGrupo.class)
            .getResponseBody()
            .filter(usuarioGrupo::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(usuarioGrupoList).isNotNull();
        assertThat(usuarioGrupoList).hasSize(1);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(0);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void getAllUsuarioGrupos() {
        // Initialize the database
        usuarioGrupoRepository.save(usuarioGrupo).block();

        // Get all the usuarioGrupoList
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
            .value(hasItem(usuarioGrupo.getId().intValue()))
            .jsonPath("$.[*].idnVarUsuarioCadastrado")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRADO))
            .jsonPath("$.[*].idnVarGrupo")
            .value(hasItem(DEFAULT_IDN_VAR_GRUPO))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getUsuarioGrupo() {
        // Initialize the database
        usuarioGrupoRepository.save(usuarioGrupo).block();

        // Get the usuarioGrupo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, usuarioGrupo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(usuarioGrupo.getId().intValue()))
            .jsonPath("$.idnVarUsuarioCadastrado")
            .value(is(DEFAULT_IDN_VAR_USUARIO_CADASTRADO))
            .jsonPath("$.idnVarGrupo")
            .value(is(DEFAULT_IDN_VAR_GRUPO))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getNonExistingUsuarioGrupo() {
        // Get the usuarioGrupo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUsuarioGrupo() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.save(usuarioGrupo).block();

        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();

        // Update the usuarioGrupo
        UsuarioGrupo updatedUsuarioGrupo = usuarioGrupoRepository.findById(usuarioGrupo.getId()).block();
        updatedUsuarioGrupo
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUsuarioGrupo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedUsuarioGrupo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void putNonExistingUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, usuarioGrupo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUsuarioGrupoWithPatch() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.save(usuarioGrupo).block();

        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();

        // Update the usuarioGrupo using partial update
        UsuarioGrupo partialUpdatedUsuarioGrupo = new UsuarioGrupo();
        partialUpdatedUsuarioGrupo.setId(usuarioGrupo.getId());

        partialUpdatedUsuarioGrupo.idnVarGrupo(UPDATED_IDN_VAR_GRUPO).idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuarioGrupo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioGrupo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void fullUpdateUsuarioGrupoWithPatch() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.save(usuarioGrupo).block();

        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();

        // Update the usuarioGrupo using partial update
        UsuarioGrupo partialUpdatedUsuarioGrupo = new UsuarioGrupo();
        partialUpdatedUsuarioGrupo.setId(usuarioGrupo.getId());

        partialUpdatedUsuarioGrupo
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuarioGrupo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioGrupo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void patchNonExistingUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, usuarioGrupo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().collectList().block().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioGrupo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUsuarioGrupo() {
        // Initialize the database
        usuarioGrupoRepository.save(usuarioGrupo).block();

        int databaseSizeBeforeDelete = usuarioGrupoRepository.findAll().collectList().block().size();

        // Delete the usuarioGrupo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, usuarioGrupo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll().collectList().block();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
