package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.Usuario;
import io.sld.riskcomplianceloginservice.repository.EntityManager;
import io.sld.riskcomplianceloginservice.repository.UsuarioRepository;
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
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO_CADASTRO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO_CADASTRO = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_SENHA = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_SENHA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(DEFAULT_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(DEFAULT_N_VAR_SENHA);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(UPDATED_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(UPDATED_N_VAR_SENHA);
        return usuario;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Usuario.class).block();
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
        usuario = createEntity(em);
    }

    @Test
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().collectList().block().size();
        // Create the Usuario
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(DEFAULT_N_VAR_SENHA);
    }

    @Test
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().collectList().block().size();
        // set the field null
        usuario.setIdnVarUsuario(null);

        // Create the Usuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().collectList().block().size();
        // set the field null
        usuario.setnVarNome(null);

        // Create the Usuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checknVarSenhaIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().collectList().block().size();
        // set the field null
        usuario.setnVarSenha(null);

        // Create the Usuario, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUsuariosAsStream() {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        List<Usuario> usuarioList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Usuario.class)
            .getResponseBody()
            .filter(usuario::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(usuarioList).isNotNull();
        assertThat(usuarioList).hasSize(1);
        Usuario testUsuario = usuarioList.get(0);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(DEFAULT_N_VAR_SENHA);
    }

    @Test
    void getAllUsuarios() {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        // Get all the usuarioList
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
            .value(hasItem(usuario.getId().intValue()))
            .jsonPath("$.[*].idnVarUsuario")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.[*].nVarNome")
            .value(hasItem(DEFAULT_N_VAR_NOME))
            .jsonPath("$.[*].idnVarEmpresa")
            .value(hasItem(DEFAULT_IDN_VAR_EMPRESA))
            .jsonPath("$.[*].idnVarUsuarioCadastro")
            .value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRO))
            .jsonPath("$.[*].nVarSenha")
            .value(hasItem(DEFAULT_N_VAR_SENHA));
    }

    @Test
    void getUsuario() {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        // Get the usuario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(usuario.getId().intValue()))
            .jsonPath("$.idnVarUsuario")
            .value(is(DEFAULT_IDN_VAR_USUARIO))
            .jsonPath("$.nVarNome")
            .value(is(DEFAULT_N_VAR_NOME))
            .jsonPath("$.idnVarEmpresa")
            .value(is(DEFAULT_IDN_VAR_EMPRESA))
            .jsonPath("$.idnVarUsuarioCadastro")
            .value(is(DEFAULT_IDN_VAR_USUARIO_CADASTRO))
            .jsonPath("$.nVarSenha")
            .value(is(DEFAULT_N_VAR_SENHA));
    }

    @Test
    void getNonExistingUsuario() {
        // Get the usuario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).block();
        updatedUsuario
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(UPDATED_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(UPDATED_N_VAR_SENHA);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedUsuario.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(UPDATED_N_VAR_SENHA);
    }

    @Test
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.idnVarUsuario(UPDATED_IDN_VAR_USUARIO).nVarNome(UPDATED_N_VAR_NOME).nVarSenha(UPDATED_N_VAR_SENHA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(UPDATED_N_VAR_SENHA);
    }

    @Test
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(UPDATED_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(UPDATED_N_VAR_SENHA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(UPDATED_N_VAR_SENHA);
    }

    @Test
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().collectList().block().size();
        usuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(usuario))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUsuario() {
        // Initialize the database
        usuarioRepository.save(usuario).block();

        int databaseSizeBeforeDelete = usuarioRepository.findAll().collectList().block().size();

        // Delete the usuario
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, usuario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll().collectList().block();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
