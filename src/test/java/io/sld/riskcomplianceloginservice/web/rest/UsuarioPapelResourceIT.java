package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.UsuarioPapel;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.UsuarioPapelRepository;
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
 * Integration tests for the {@link UsuarioPapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UsuarioPapelResourceIT {

    private static final String DEFAULT_IDN_VAR_USUARIO_CADASTRADO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO_CADASTRADO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_PAPEL = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PAPEL = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuario-papels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioPapelRepository usuarioPapelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UsuarioPapel usuarioPapel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioPapel createEntity(EntityManager em) {
        UsuarioPapel usuarioPapel = new UsuarioPapel()
            .idnVarUsuarioCadastrado(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(DEFAULT_IDN_VAR_PAPEL)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return usuarioPapel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioPapel createUpdatedEntity(EntityManager em) {
        UsuarioPapel usuarioPapel = new UsuarioPapel()
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return usuarioPapel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UsuarioPapel.class).block();
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
        usuarioPapel = createEntity(em);
    }

    @Test
    void createUsuarioPapel() throws Exception {
        int databaseSizeBeforeCreate = usuarioPapelRepository.findAll().collectList().block().size();
        // Create the UsuarioPapel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeCreate + 1);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void createUsuarioPapelWithExistingId() throws Exception {
        // Create the UsuarioPapel with an existing ID
        usuarioPapel.setId(1L);

        int databaseSizeBeforeCreate = usuarioPapelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarUsuarioCadastradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioPapelRepository.findAll().collectList().block().size();
        // set the field null
        usuarioPapel.setIdnVarUsuarioCadastrado(null);

        // Create the UsuarioPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioPapelRepository.findAll().collectList().block().size();
        // set the field null
        usuarioPapel.setIdnVarPapel(null);

        // Create the UsuarioPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioPapelRepository.findAll().collectList().block().size();
        // set the field null
        usuarioPapel.setIdnVarUsuario(null);

        // Create the UsuarioPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUsuarioPapelsAsStream() {
        // Initialize the database
        usuarioPapelRepository.save(usuarioPapel).block();

        List<UsuarioPapel> usuarioPapelList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UsuarioPapel.class)
            .getResponseBody()
            .filter(usuarioPapel::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(usuarioPapelList).isNotNull();
        assertThat(usuarioPapelList).hasSize(1);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(0);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void getAllUsuarioPapels() {
        // Initialize the database
        usuarioPapelRepository.save(usuarioPapel).block();

        // Get all the usuarioPapelList
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
            .value(hasItem(usuarioPapel.getId().intValue()))
            .jsonPath("$.[*].idnVarUsuarioCadastrado")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRADO))
            .jsonPath("$.[*].idnVarPapel")
            .value(hasItem(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getUsuarioPapel() {
        // Initialize the database
        usuarioPapelRepository.save(usuarioPapel).block();

        // Get the usuarioPapel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, usuarioPapel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(usuarioPapel.getId().intValue()))
            .jsonPath("$.idnVarUsuarioCadastrado")
            .value(is(DEFAULT_IDN_VAR_USUARIO_CADASTRADO))
            .jsonPath("$.idnVarPapel")
            .value(is(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getNonExistingUsuarioPapel() {
        // Get the usuarioPapel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUsuarioPapel() throws Exception {
        // Initialize the database
        usuarioPapelRepository.save(usuarioPapel).block();

        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();

        // Update the usuarioPapel
        UsuarioPapel updatedUsuarioPapel = usuarioPapelRepository.findById(usuarioPapel.getId()).block();
        updatedUsuarioPapel
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUsuarioPapel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedUsuarioPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void putNonExistingUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();
        usuarioPapel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, usuarioPapel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();
        usuarioPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();
        usuarioPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUsuarioPapelWithPatch() throws Exception {
        // Initialize the database
        usuarioPapelRepository.save(usuarioPapel).block();

        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();

        // Update the usuarioPapel using partial update
        UsuarioPapel partialUpdatedUsuarioPapel = new UsuarioPapel();
        partialUpdatedUsuarioPapel.setId(usuarioPapel.getId());

        partialUpdatedUsuarioPapel.idnVarPapel(UPDATED_IDN_VAR_PAPEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuarioPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void fullUpdateUsuarioPapelWithPatch() throws Exception {
        // Initialize the database
        usuarioPapelRepository.save(usuarioPapel).block();

        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();

        // Update the usuarioPapel using partial update
        UsuarioPapel partialUpdatedUsuarioPapel = new UsuarioPapel();
        partialUpdatedUsuarioPapel.setId(usuarioPapel.getId());

        partialUpdatedUsuarioPapel
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuarioPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void patchNonExistingUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();
        usuarioPapel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, usuarioPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();
        usuarioPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().collectList().block().size();
        usuarioPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuarioPapel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUsuarioPapel() {
        // Initialize the database
        usuarioPapelRepository.save(usuarioPapel).block();

        int databaseSizeBeforeDelete = usuarioPapelRepository.findAll().collectList().block().size();

        // Delete the usuarioPapel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, usuarioPapel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll().collectList().block();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
