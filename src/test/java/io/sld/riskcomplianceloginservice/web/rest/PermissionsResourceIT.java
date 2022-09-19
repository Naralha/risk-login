package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.Permissions;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.PermissionsRepository;
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
 * Integration tests for the {@link PermissionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PermissionsResourceIT {

    private static final String DEFAULT_IDN_VAR_PERMISSIONS = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PERMISSIONS = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_TIPO_PERMISSAO = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_TIPO_PERMISSAO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Permissions permissions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permissions createEntity(EntityManager em) {
        Permissions permissions = new Permissions()
            .idnVarPermissions(DEFAULT_IDN_VAR_PERMISSIONS)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .nVarTipoPermissao(DEFAULT_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return permissions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permissions createUpdatedEntity(EntityManager em) {
        Permissions permissions = new Permissions()
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarTipoPermissao(UPDATED_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return permissions;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Permissions.class).block();
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
        permissions = createEntity(em);
    }

    @Test
    void createPermissions() throws Exception {
        int databaseSizeBeforeCreate = permissionsRepository.findAll().collectList().block().size();
        // Create the Permissions
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeCreate + 1);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(DEFAULT_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void createPermissionsWithExistingId() throws Exception {
        // Create the Permissions with an existing ID
        permissions.setId(1L);

        int databaseSizeBeforeCreate = permissionsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarPermissionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().collectList().block().size();
        // set the field null
        permissions.setIdnVarPermissions(null);

        // Create the Permissions, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().collectList().block().size();
        // set the field null
        permissions.setnVarNome(null);

        // Create the Permissions, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarTipoPermissaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().collectList().block().size();
        // set the field null
        permissions.setnVarTipoPermissao(null);

        // Create the Permissions, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().collectList().block().size();
        // set the field null
        permissions.setIdnVarUsuario(null);

        // Create the Permissions, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPermissionsAsStream() {
        // Initialize the database
        permissionsRepository.save(permissions).block();

        List<Permissions> permissionsList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Permissions.class)
            .getResponseBody()
            .filter(permissions::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(permissionsList).isNotNull();
        assertThat(permissionsList).hasSize(1);
        Permissions testPermissions = permissionsList.get(0);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(DEFAULT_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    void getAllPermissions() {
        // Initialize the database
        permissionsRepository.save(permissions).block();

        // Get all the permissionsList
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
            .value(hasItem(permissions.getId().intValue()))
            .jsonPath("$.[*].idnVarPermissions")
            .value(hasItem(DEFAULT_IDN_VAR_PERMISSIONS))
            .jsonPath("$.[*].nVarNome")
            .value(hasItem(DEFAULT_N_VAR_NOME))
            .jsonPath("$.[*].nVarTipoPermissao")
            .value(hasItem(DEFAULT_N_VAR_TIPO_PERMISSAO))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getPermissions() {
        // Initialize the database
        permissionsRepository.save(permissions).block();

        // Get the permissions
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, permissions.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(permissions.getId().intValue()))
            .jsonPath("$.idnVarPermissions")
            .value(is(DEFAULT_IDN_VAR_PERMISSIONS))
            .jsonPath("$.nVarNome")
            .value(is(DEFAULT_N_VAR_NOME))
            .jsonPath("$.nVarTipoPermissao")
            .value(is(DEFAULT_N_VAR_TIPO_PERMISSAO))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    void getNonExistingPermissions() {
        // Get the permissions
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.save(permissions).block();

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();

        // Update the permissions
        Permissions updatedPermissions = permissionsRepository.findById(permissions.getId()).block();
        updatedPermissions
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarTipoPermissao(UPDATED_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPermissions.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPermissions))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(UPDATED_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void putNonExistingPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();
        permissions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, permissions.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePermissionsWithPatch() throws Exception {
        // Initialize the database
        permissionsRepository.save(permissions).block();

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();

        // Update the permissions using partial update
        Permissions partialUpdatedPermissions = new Permissions();
        partialUpdatedPermissions.setId(permissions.getId());

        partialUpdatedPermissions
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarTipoPermissao(UPDATED_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPermissions.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissions))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(UPDATED_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void fullUpdatePermissionsWithPatch() throws Exception {
        // Initialize the database
        permissionsRepository.save(permissions).block();

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();

        // Update the permissions using partial update
        Permissions partialUpdatedPermissions = new Permissions();
        partialUpdatedPermissions.setId(permissions.getId());

        partialUpdatedPermissions
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarTipoPermissao(UPDATED_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPermissions.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissions))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(UPDATED_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    void patchNonExistingPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();
        permissions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, permissions.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().collectList().block().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(permissions))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePermissions() {
        // Initialize the database
        permissionsRepository.save(permissions).block();

        int databaseSizeBeforeDelete = permissionsRepository.findAll().collectList().block().size();

        // Delete the permissions
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, permissions.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Permissions> permissionsList = permissionsRepository.findAll().collectList().block();
        assertThat(permissionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
