package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.PermissionsPapel;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.PermissionsPapelRepository;
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
 * Integration tests for the {@link PermissionsPapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PermissionsPapelResourceIT {

    private static final String DEFAULT_IDN_VAR_PERMISSIONS = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PERMISSIONS = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_PAPEL = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PAPEL = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_FEATURES = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permissions-papels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionsPapelRepository permissionsPapelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PermissionsPapel permissionsPapel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionsPapel createEntity(EntityManager em) {
        PermissionsPapel permissionsPapel = new PermissionsPapel()
            .idnVarPermissions(DEFAULT_IDN_VAR_PERMISSIONS)
            .idnVarPapel(DEFAULT_IDN_VAR_PAPEL)
            .idnVarFeatures(DEFAULT_IDN_VAR_FEATURES)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return permissionsPapel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionsPapel createUpdatedEntity(EntityManager em) {
        PermissionsPapel permissionsPapel = new PermissionsPapel()
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return permissionsPapel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PermissionsPapel.class).block();
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
        permissionsPapel = createEntity(em);
    }

    @Test
    void createPermissionsPapel() throws Exception {
        int databaseSizeBeforeCreate = permissionsPapelRepository.findAll().collectList().block().size();
        // Create the PermissionsPapel
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeCreate + 1);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(DEFAULT_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void createPermissionsPapelWithExistingId() throws Exception {
        // Create the PermissionsPapel with an existing ID
        permissionsPapel.setId(1L);

        int databaseSizeBeforeCreate = permissionsPapelRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarPermissionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().collectList().block().size();
        // set the field null
        permissionsPapel.setIdnVarPermissions(null);

        // Create the PermissionsPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().collectList().block().size();
        // set the field null
        permissionsPapel.setIdnVarPapel(null);

        // Create the PermissionsPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarFeaturesIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().collectList().block().size();
        // set the field null
        permissionsPapel.setIdnVarFeatures(null);

        // Create the PermissionsPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().collectList().block().size();
        // set the field null
        permissionsPapel.setIdnVarUsuario(null);

        // Create the PermissionsPapel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPermissionsPapelsAsStream() {
        // Initialize the database
        permissionsPapelRepository.save(permissionsPapel).block();

        List<PermissionsPapel> permissionsPapelList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PermissionsPapel.class)
            .getResponseBody()
            .filter(permissionsPapel::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(permissionsPapelList).isNotNull();
        assertThat(permissionsPapelList).hasSize(1);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(0);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(DEFAULT_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void getAllPermissionsPapels() {
        // Initialize the database
        permissionsPapelRepository.save(permissionsPapel).block();

        // Get all the permissionsPapelList
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
            .value(hasItem(permissionsPapel.getId().intValue()))
            .jsonPath("$.[*].idnVarPermissions")
            .value(hasItem(DEFAULT_IDN_VAR_PERMISSIONS))
            .jsonPath("$.[*].idnVarPapel")
            .value(hasItem(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.[*].idnVarFeatures")
            .value(hasItem(DEFAULT_IDN_VAR_FEATURES))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getPermissionsPapel() {
        // Initialize the database
        permissionsPapelRepository.save(permissionsPapel).block();

        // Get the permissionsPapel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, permissionsPapel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(permissionsPapel.getId().intValue()))
            .jsonPath("$.idnVarPermissions")
            .value(is(DEFAULT_IDN_VAR_PERMISSIONS))
            .jsonPath("$.idnVarPapel")
            .value(is(DEFAULT_IDN_VAR_PAPEL))
            .jsonPath("$.idnVarFeatures")
            .value(is(DEFAULT_IDN_VAR_FEATURES))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getNonExistingPermissionsPapel() {
        // Get the permissionsPapel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPermissionsPapel() throws Exception {
        // Initialize the database
        permissionsPapelRepository.save(permissionsPapel).block();

        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();

        // Update the permissionsPapel
        PermissionsPapel updatedPermissionsPapel = permissionsPapelRepository.findById(permissionsPapel.getId()).block();
        updatedPermissionsPapel
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPermissionsPapel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPermissionsPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void putNonExistingPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();
        permissionsPapel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, permissionsPapel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();
        permissionsPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();
        permissionsPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePermissionsPapelWithPatch() throws Exception {
        // Initialize the database
        permissionsPapelRepository.save(permissionsPapel).block();

        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();

        // Update the permissionsPapel using partial update
        PermissionsPapel partialUpdatedPermissionsPapel = new PermissionsPapel();
        partialUpdatedPermissionsPapel.setId(permissionsPapel.getId());

        partialUpdatedPermissionsPapel.idnVarFeatures(UPDATED_IDN_VAR_FEATURES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPermissionsPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionsPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void fullUpdatePermissionsPapelWithPatch() throws Exception {
        // Initialize the database
        permissionsPapelRepository.save(permissionsPapel).block();

        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();

        // Update the permissionsPapel using partial update
        PermissionsPapel partialUpdatedPermissionsPapel = new PermissionsPapel();
        partialUpdatedPermissionsPapel.setId(permissionsPapel.getId());

        partialUpdatedPermissionsPapel
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPermissionsPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionsPapel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void patchNonExistingPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();
        permissionsPapel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, permissionsPapel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();
        permissionsPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().collectList().block().size();
        permissionsPapel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissionsPapel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePermissionsPapel() {
        // Initialize the database
        permissionsPapelRepository.save(permissionsPapel).block();

        int databaseSizeBeforeDelete = permissionsPapelRepository.findAll().collectList().block().size();

        // Delete the permissionsPapel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, permissionsPapel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll().collectList().block();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
