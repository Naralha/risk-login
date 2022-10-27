package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.repository.PermissionsRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PermissionsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.sld.riskcomplianceloginservice.resource.PermissionsResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PermissionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private PermissionsMapper permissionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionsMockMvc;

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

    @BeforeEach
    public void initTest() {
        permissions = createEntity(em);
    }

    @Test
    @Transactional
    void createPermissions() throws Exception {
        int databaseSizeBeforeCreate = permissionsRepository.findAll().size();
        // Create the Permissions
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);
        restPermissionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeCreate + 1);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(DEFAULT_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void createPermissionsWithExistingId() throws Exception {
        // Create the Permissions with an existing ID
        permissions.setId(1L);
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        int databaseSizeBeforeCreate = permissionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarPermissionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().size();
        // set the field null
        permissions.setIdnVarPermissions(null);

        // Create the Permissions, which fails.
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        restPermissionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().size();
        // set the field null
        permissions.setnVarNome(null);

        // Create the Permissions, which fails.
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        restPermissionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarTipoPermissaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().size();
        // set the field null
        permissions.setnVarTipoPermissao(null);

        // Create the Permissions, which fails.
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        restPermissionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().size();
        // set the field null
        permissions.setIdnVarUsuario(null);

        // Create the Permissions, which fails.
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        restPermissionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarPermissions").value(hasItem(DEFAULT_IDN_VAR_PERMISSIONS)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].nVarTipoPermissao").value(hasItem(DEFAULT_N_VAR_TIPO_PERMISSAO)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));
    }

    @Test
    @Transactional
    void getPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get the permissions
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL_ID, permissions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissions.getId().intValue()))
            .andExpect(jsonPath("$.idnVarPermissions").value(DEFAULT_IDN_VAR_PERMISSIONS))
            .andExpect(jsonPath("$.nVarNome").value(DEFAULT_N_VAR_NOME))
            .andExpect(jsonPath("$.nVarTipoPermissao").value(DEFAULT_N_VAR_TIPO_PERMISSAO))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    @Transactional
    void getPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        Long id = permissions.getId();

        defaultPermissionsShouldBeFound("id.equals=" + id);
        defaultPermissionsShouldNotBeFound("id.notEquals=" + id);

        defaultPermissionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermissionsShouldNotBeFound("id.greaterThan=" + id);

        defaultPermissionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermissionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarPermissionsIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarPermissions equals to DEFAULT_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldBeFound("idnVarPermissions.equals=" + DEFAULT_IDN_VAR_PERMISSIONS);

        // Get all the permissionsList where idnVarPermissions equals to UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldNotBeFound("idnVarPermissions.equals=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarPermissionsIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarPermissions in DEFAULT_IDN_VAR_PERMISSIONS or UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldBeFound("idnVarPermissions.in=" + DEFAULT_IDN_VAR_PERMISSIONS + "," + UPDATED_IDN_VAR_PERMISSIONS);

        // Get all the permissionsList where idnVarPermissions equals to UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldNotBeFound("idnVarPermissions.in=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarPermissionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarPermissions is not null
        defaultPermissionsShouldBeFound("idnVarPermissions.specified=true");

        // Get all the permissionsList where idnVarPermissions is null
        defaultPermissionsShouldNotBeFound("idnVarPermissions.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarPermissionsContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarPermissions contains DEFAULT_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldBeFound("idnVarPermissions.contains=" + DEFAULT_IDN_VAR_PERMISSIONS);

        // Get all the permissionsList where idnVarPermissions contains UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldNotBeFound("idnVarPermissions.contains=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarPermissionsNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarPermissions does not contain DEFAULT_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldNotBeFound("idnVarPermissions.doesNotContain=" + DEFAULT_IDN_VAR_PERMISSIONS);

        // Get all the permissionsList where idnVarPermissions does not contain UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsShouldBeFound("idnVarPermissions.doesNotContain=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarNome equals to DEFAULT_N_VAR_NOME
        defaultPermissionsShouldBeFound("nVarNome.equals=" + DEFAULT_N_VAR_NOME);

        // Get all the permissionsList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultPermissionsShouldNotBeFound("nVarNome.equals=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarNomeIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarNome in DEFAULT_N_VAR_NOME or UPDATED_N_VAR_NOME
        defaultPermissionsShouldBeFound("nVarNome.in=" + DEFAULT_N_VAR_NOME + "," + UPDATED_N_VAR_NOME);

        // Get all the permissionsList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultPermissionsShouldNotBeFound("nVarNome.in=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarNome is not null
        defaultPermissionsShouldBeFound("nVarNome.specified=true");

        // Get all the permissionsList where nVarNome is null
        defaultPermissionsShouldNotBeFound("nVarNome.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarNomeContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarNome contains DEFAULT_N_VAR_NOME
        defaultPermissionsShouldBeFound("nVarNome.contains=" + DEFAULT_N_VAR_NOME);

        // Get all the permissionsList where nVarNome contains UPDATED_N_VAR_NOME
        defaultPermissionsShouldNotBeFound("nVarNome.contains=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarNomeNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarNome does not contain DEFAULT_N_VAR_NOME
        defaultPermissionsShouldNotBeFound("nVarNome.doesNotContain=" + DEFAULT_N_VAR_NOME);

        // Get all the permissionsList where nVarNome does not contain UPDATED_N_VAR_NOME
        defaultPermissionsShouldBeFound("nVarNome.doesNotContain=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarTipoPermissaoIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarTipoPermissao equals to DEFAULT_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldBeFound("nVarTipoPermissao.equals=" + DEFAULT_N_VAR_TIPO_PERMISSAO);

        // Get all the permissionsList where nVarTipoPermissao equals to UPDATED_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldNotBeFound("nVarTipoPermissao.equals=" + UPDATED_N_VAR_TIPO_PERMISSAO);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarTipoPermissaoIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarTipoPermissao in DEFAULT_N_VAR_TIPO_PERMISSAO or UPDATED_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldBeFound("nVarTipoPermissao.in=" + DEFAULT_N_VAR_TIPO_PERMISSAO + "," + UPDATED_N_VAR_TIPO_PERMISSAO);

        // Get all the permissionsList where nVarTipoPermissao equals to UPDATED_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldNotBeFound("nVarTipoPermissao.in=" + UPDATED_N_VAR_TIPO_PERMISSAO);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarTipoPermissaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarTipoPermissao is not null
        defaultPermissionsShouldBeFound("nVarTipoPermissao.specified=true");

        // Get all the permissionsList where nVarTipoPermissao is null
        defaultPermissionsShouldNotBeFound("nVarTipoPermissao.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarTipoPermissaoContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarTipoPermissao contains DEFAULT_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldBeFound("nVarTipoPermissao.contains=" + DEFAULT_N_VAR_TIPO_PERMISSAO);

        // Get all the permissionsList where nVarTipoPermissao contains UPDATED_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldNotBeFound("nVarTipoPermissao.contains=" + UPDATED_N_VAR_TIPO_PERMISSAO);
    }

    @Test
    @Transactional
    void getAllPermissionsBynVarTipoPermissaoNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where nVarTipoPermissao does not contain DEFAULT_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldNotBeFound("nVarTipoPermissao.doesNotContain=" + DEFAULT_N_VAR_TIPO_PERMISSAO);

        // Get all the permissionsList where nVarTipoPermissao does not contain UPDATED_N_VAR_TIPO_PERMISSAO
        defaultPermissionsShouldBeFound("nVarTipoPermissao.doesNotContain=" + UPDATED_N_VAR_TIPO_PERMISSAO);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultPermissionsShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the permissionsList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultPermissionsShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultPermissionsShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the permissionsList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultPermissionsShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarUsuario is not null
        defaultPermissionsShouldBeFound("idnVarUsuario.specified=true");

        // Get all the permissionsList where idnVarUsuario is null
        defaultPermissionsShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultPermissionsShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the permissionsList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultPermissionsShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultPermissionsShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the permissionsList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultPermissionsShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionsPapelIsEqualToSomething() throws Exception {
        PermissionsPapel permissionsPapel;
        if (TestUtil.findAll(em, PermissionsPapel.class).isEmpty()) {
            permissionsRepository.saveAndFlush(permissions);
            permissionsPapel = PermissionsPapelResourceIT.createEntity(em);
        } else {
            permissionsPapel = TestUtil.findAll(em, PermissionsPapel.class).get(0);
        }
        em.persist(permissionsPapel);
        em.flush();
        permissions.addPermissionsPapel(permissionsPapel);
        permissionsRepository.saveAndFlush(permissions);
        Long permissionsPapelId = permissionsPapel.getId();

        // Get all the permissionsList where permissionsPapel equals to permissionsPapelId
        defaultPermissionsShouldBeFound("permissionsPapelId.equals=" + permissionsPapelId);

        // Get all the permissionsList where permissionsPapel equals to (permissionsPapelId + 1)
        defaultPermissionsShouldNotBeFound("permissionsPapelId.equals=" + (permissionsPapelId + 1));
    }

    @Test
    @Transactional
    void getAllPermissionsByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            permissionsRepository.saveAndFlush(permissions);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        permissions.setUsuario(usuario);
        permissionsRepository.saveAndFlush(permissions);
        Long usuarioId = usuario.getId();

        // Get all the permissionsList where usuario equals to usuarioId
        defaultPermissionsShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the permissionsList where usuario equals to (usuarioId + 1)
        defaultPermissionsShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionsShouldBeFound(String filter) throws Exception {
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarPermissions").value(hasItem(DEFAULT_IDN_VAR_PERMISSIONS)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].nVarTipoPermissao").value(hasItem(DEFAULT_N_VAR_TIPO_PERMISSAO)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));

        // Check, that the count call also returns 1
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionsShouldNotBeFound(String filter) throws Exception {
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPermissions() throws Exception {
        // Get the permissions
        restPermissionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();

        // Update the permissions
        Permissions updatedPermissions = permissionsRepository.findById(permissions.getId()).get();
        // Disconnect from session so that the updates on updatedPermissions are not directly saved in db
        em.detach(updatedPermissions);
        updatedPermissions
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarTipoPermissao(UPDATED_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(updatedPermissions);

        restPermissionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(UPDATED_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void putNonExistingPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // Create the Permissions
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // Create the Permissions
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // Create the Permissions
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionsWithPatch() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();

        // Update the permissions using partial update
        Permissions partialUpdatedPermissions = new Permissions();
        partialUpdatedPermissions.setId(permissions.getId());

        partialUpdatedPermissions
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarTipoPermissao(UPDATED_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissions))
            )
            .andExpect(status().isOk());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(UPDATED_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void fullUpdatePermissionsWithPatch() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();

        // Update the permissions using partial update
        Permissions partialUpdatedPermissions = new Permissions();
        partialUpdatedPermissions.setId(permissions.getId());

        partialUpdatedPermissions
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .nVarNome(UPDATED_N_VAR_NOME)
            .nVarTipoPermissao(UPDATED_N_VAR_TIPO_PERMISSAO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissions))
            )
            .andExpect(status().isOk());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissions.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPermissions.getnVarTipoPermissao()).isEqualTo(UPDATED_N_VAR_TIPO_PERMISSAO);
        assertThat(testPermissions.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void patchNonExistingPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // Create the Permissions
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // Create the Permissions
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // Create the Permissions
        PermissionsDTO permissionsDTO = permissionsMapper.toDto(permissions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(permissionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeDelete = permissionsRepository.findAll().size();

        // Delete the permissions
        restPermissionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
