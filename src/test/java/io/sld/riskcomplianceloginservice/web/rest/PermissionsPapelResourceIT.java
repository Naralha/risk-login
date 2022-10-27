package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.repository.PermissionsPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PermissionsPapelMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.sld.riskcomplianceloginservice.resource.PermissionsPapelResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PermissionsPapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private PermissionsPapelMapper permissionsPapelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionsPapelMockMvc;

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

    @BeforeEach
    public void initTest() {
        permissionsPapel = createEntity(em);
    }

    @Test
    @Transactional
    void createPermissionsPapel() throws Exception {
        int databaseSizeBeforeCreate = permissionsPapelRepository.findAll().size();
        // Create the PermissionsPapel
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);
        restPermissionsPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeCreate + 1);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(DEFAULT_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void createPermissionsPapelWithExistingId() throws Exception {
        // Create the PermissionsPapel with an existing ID
        permissionsPapel.setId(1L);
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        int databaseSizeBeforeCreate = permissionsPapelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionsPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarPermissionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().size();
        // set the field null
        permissionsPapel.setIdnVarPermissions(null);

        // Create the PermissionsPapel, which fails.
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        restPermissionsPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().size();
        // set the field null
        permissionsPapel.setIdnVarPapel(null);

        // Create the PermissionsPapel, which fails.
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        restPermissionsPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarFeaturesIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().size();
        // set the field null
        permissionsPapel.setIdnVarFeatures(null);

        // Create the PermissionsPapel, which fails.
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        restPermissionsPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsPapelRepository.findAll().size();
        // set the field null
        permissionsPapel.setIdnVarUsuario(null);

        // Create the PermissionsPapel, which fails.
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        restPermissionsPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissionsPapels() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList
        restPermissionsPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionsPapel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarPermissions").value(hasItem(DEFAULT_IDN_VAR_PERMISSIONS)))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].idnVarFeatures").value(hasItem(DEFAULT_IDN_VAR_FEATURES)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));
    }

    @Test
    @Transactional
    void getPermissionsPapel() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get the permissionsPapel
        restPermissionsPapelMockMvc
            .perform(get(ENTITY_API_URL_ID, permissionsPapel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissionsPapel.getId().intValue()))
            .andExpect(jsonPath("$.idnVarPermissions").value(DEFAULT_IDN_VAR_PERMISSIONS))
            .andExpect(jsonPath("$.idnVarPapel").value(DEFAULT_IDN_VAR_PAPEL))
            .andExpect(jsonPath("$.idnVarFeatures").value(DEFAULT_IDN_VAR_FEATURES))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    @Transactional
    void getPermissionsPapelsByIdFiltering() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        Long id = permissionsPapel.getId();

        defaultPermissionsPapelShouldBeFound("id.equals=" + id);
        defaultPermissionsPapelShouldNotBeFound("id.notEquals=" + id);

        defaultPermissionsPapelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermissionsPapelShouldNotBeFound("id.greaterThan=" + id);

        defaultPermissionsPapelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermissionsPapelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPermissionsIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPermissions equals to DEFAULT_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldBeFound("idnVarPermissions.equals=" + DEFAULT_IDN_VAR_PERMISSIONS);

        // Get all the permissionsPapelList where idnVarPermissions equals to UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldNotBeFound("idnVarPermissions.equals=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPermissionsIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPermissions in DEFAULT_IDN_VAR_PERMISSIONS or UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldBeFound("idnVarPermissions.in=" + DEFAULT_IDN_VAR_PERMISSIONS + "," + UPDATED_IDN_VAR_PERMISSIONS);

        // Get all the permissionsPapelList where idnVarPermissions equals to UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldNotBeFound("idnVarPermissions.in=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPermissionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPermissions is not null
        defaultPermissionsPapelShouldBeFound("idnVarPermissions.specified=true");

        // Get all the permissionsPapelList where idnVarPermissions is null
        defaultPermissionsPapelShouldNotBeFound("idnVarPermissions.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPermissionsContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPermissions contains DEFAULT_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldBeFound("idnVarPermissions.contains=" + DEFAULT_IDN_VAR_PERMISSIONS);

        // Get all the permissionsPapelList where idnVarPermissions contains UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldNotBeFound("idnVarPermissions.contains=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPermissionsNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPermissions does not contain DEFAULT_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldNotBeFound("idnVarPermissions.doesNotContain=" + DEFAULT_IDN_VAR_PERMISSIONS);

        // Get all the permissionsPapelList where idnVarPermissions does not contain UPDATED_IDN_VAR_PERMISSIONS
        defaultPermissionsPapelShouldBeFound("idnVarPermissions.doesNotContain=" + UPDATED_IDN_VAR_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPapelIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPapel equals to DEFAULT_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldBeFound("idnVarPapel.equals=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the permissionsPapelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldNotBeFound("idnVarPapel.equals=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPapelIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPapel in DEFAULT_IDN_VAR_PAPEL or UPDATED_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldBeFound("idnVarPapel.in=" + DEFAULT_IDN_VAR_PAPEL + "," + UPDATED_IDN_VAR_PAPEL);

        // Get all the permissionsPapelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldNotBeFound("idnVarPapel.in=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPapelIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPapel is not null
        defaultPermissionsPapelShouldBeFound("idnVarPapel.specified=true");

        // Get all the permissionsPapelList where idnVarPapel is null
        defaultPermissionsPapelShouldNotBeFound("idnVarPapel.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPapelContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPapel contains DEFAULT_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldBeFound("idnVarPapel.contains=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the permissionsPapelList where idnVarPapel contains UPDATED_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldNotBeFound("idnVarPapel.contains=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarPapelNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarPapel does not contain DEFAULT_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldNotBeFound("idnVarPapel.doesNotContain=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the permissionsPapelList where idnVarPapel does not contain UPDATED_IDN_VAR_PAPEL
        defaultPermissionsPapelShouldBeFound("idnVarPapel.doesNotContain=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarFeaturesIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarFeatures equals to DEFAULT_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldBeFound("idnVarFeatures.equals=" + DEFAULT_IDN_VAR_FEATURES);

        // Get all the permissionsPapelList where idnVarFeatures equals to UPDATED_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldNotBeFound("idnVarFeatures.equals=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarFeaturesIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarFeatures in DEFAULT_IDN_VAR_FEATURES or UPDATED_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldBeFound("idnVarFeatures.in=" + DEFAULT_IDN_VAR_FEATURES + "," + UPDATED_IDN_VAR_FEATURES);

        // Get all the permissionsPapelList where idnVarFeatures equals to UPDATED_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldNotBeFound("idnVarFeatures.in=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarFeaturesIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarFeatures is not null
        defaultPermissionsPapelShouldBeFound("idnVarFeatures.specified=true");

        // Get all the permissionsPapelList where idnVarFeatures is null
        defaultPermissionsPapelShouldNotBeFound("idnVarFeatures.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarFeaturesContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarFeatures contains DEFAULT_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldBeFound("idnVarFeatures.contains=" + DEFAULT_IDN_VAR_FEATURES);

        // Get all the permissionsPapelList where idnVarFeatures contains UPDATED_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldNotBeFound("idnVarFeatures.contains=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarFeaturesNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarFeatures does not contain DEFAULT_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldNotBeFound("idnVarFeatures.doesNotContain=" + DEFAULT_IDN_VAR_FEATURES);

        // Get all the permissionsPapelList where idnVarFeatures does not contain UPDATED_IDN_VAR_FEATURES
        defaultPermissionsPapelShouldBeFound("idnVarFeatures.doesNotContain=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the permissionsPapelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the permissionsPapelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarUsuario is not null
        defaultPermissionsPapelShouldBeFound("idnVarUsuario.specified=true");

        // Get all the permissionsPapelList where idnVarUsuario is null
        defaultPermissionsPapelShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the permissionsPapelList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        // Get all the permissionsPapelList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the permissionsPapelList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultPermissionsPapelShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByPapelIsEqualToSomething() throws Exception {
        Papel papel;
        if (TestUtil.findAll(em, Papel.class).isEmpty()) {
            permissionsPapelRepository.saveAndFlush(permissionsPapel);
            papel = PapelResourceIT.createEntity(em);
        } else {
            papel = TestUtil.findAll(em, Papel.class).get(0);
        }
        em.persist(papel);
        em.flush();
        permissionsPapel.setPapel(papel);
        permissionsPapelRepository.saveAndFlush(permissionsPapel);
        Long papelId = papel.getId();

        // Get all the permissionsPapelList where papel equals to papelId
        defaultPermissionsPapelShouldBeFound("papelId.equals=" + papelId);

        // Get all the permissionsPapelList where papel equals to (papelId + 1)
        defaultPermissionsPapelShouldNotBeFound("papelId.equals=" + (papelId + 1));
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByPermissionsIsEqualToSomething() throws Exception {
        Permissions permissions;
        if (TestUtil.findAll(em, Permissions.class).isEmpty()) {
            permissionsPapelRepository.saveAndFlush(permissionsPapel);
            permissions = PermissionsResourceIT.createEntity(em);
        } else {
            permissions = TestUtil.findAll(em, Permissions.class).get(0);
        }
        em.persist(permissions);
        em.flush();
        permissionsPapel.setPermissions(permissions);
        permissionsPapelRepository.saveAndFlush(permissionsPapel);
        Long permissionsId = permissions.getId();

        // Get all the permissionsPapelList where permissions equals to permissionsId
        defaultPermissionsPapelShouldBeFound("permissionsId.equals=" + permissionsId);

        // Get all the permissionsPapelList where permissions equals to (permissionsId + 1)
        defaultPermissionsPapelShouldNotBeFound("permissionsId.equals=" + (permissionsId + 1));
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByFeaturesIsEqualToSomething() throws Exception {
        Features features;
        if (TestUtil.findAll(em, Features.class).isEmpty()) {
            permissionsPapelRepository.saveAndFlush(permissionsPapel);
            features = FeaturesResourceIT.createEntity(em);
        } else {
            features = TestUtil.findAll(em, Features.class).get(0);
        }
        em.persist(features);
        em.flush();
        permissionsPapel.setFeatures(features);
        permissionsPapelRepository.saveAndFlush(permissionsPapel);
        Long featuresId = features.getId();

        // Get all the permissionsPapelList where features equals to featuresId
        defaultPermissionsPapelShouldBeFound("featuresId.equals=" + featuresId);

        // Get all the permissionsPapelList where features equals to (featuresId + 1)
        defaultPermissionsPapelShouldNotBeFound("featuresId.equals=" + (featuresId + 1));
    }

    @Test
    @Transactional
    void getAllPermissionsPapelsByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            permissionsPapelRepository.saveAndFlush(permissionsPapel);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        permissionsPapel.setUsuario(usuario);
        permissionsPapelRepository.saveAndFlush(permissionsPapel);
        Long usuarioId = usuario.getId();

        // Get all the permissionsPapelList where usuario equals to usuarioId
        defaultPermissionsPapelShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the permissionsPapelList where usuario equals to (usuarioId + 1)
        defaultPermissionsPapelShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionsPapelShouldBeFound(String filter) throws Exception {
        restPermissionsPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionsPapel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarPermissions").value(hasItem(DEFAULT_IDN_VAR_PERMISSIONS)))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].idnVarFeatures").value(hasItem(DEFAULT_IDN_VAR_FEATURES)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));

        // Check, that the count call also returns 1
        restPermissionsPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionsPapelShouldNotBeFound(String filter) throws Exception {
        restPermissionsPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionsPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPermissionsPapel() throws Exception {
        // Get the permissionsPapel
        restPermissionsPapelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissionsPapel() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();

        // Update the permissionsPapel
        PermissionsPapel updatedPermissionsPapel = permissionsPapelRepository.findById(permissionsPapel.getId()).get();
        // Disconnect from session so that the updates on updatedPermissionsPapel are not directly saved in db
        em.detach(updatedPermissionsPapel);
        updatedPermissionsPapel
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(updatedPermissionsPapel);

        restPermissionsPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionsPapelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isOk());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void putNonExistingPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();
        permissionsPapel.setId(count.incrementAndGet());

        // Create the PermissionsPapel
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionsPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionsPapelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();
        permissionsPapel.setId(count.incrementAndGet());

        // Create the PermissionsPapel
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();
        permissionsPapel.setId(count.incrementAndGet());

        // Create the PermissionsPapel
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsPapelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionsPapelWithPatch() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();

        // Update the permissionsPapel using partial update
        PermissionsPapel partialUpdatedPermissionsPapel = new PermissionsPapel();
        partialUpdatedPermissionsPapel.setId(permissionsPapel.getId());

        partialUpdatedPermissionsPapel.idnVarFeatures(UPDATED_IDN_VAR_FEATURES);

        restPermissionsPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionsPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionsPapel))
            )
            .andExpect(status().isOk());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(DEFAULT_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void fullUpdatePermissionsPapelWithPatch() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();

        // Update the permissionsPapel using partial update
        PermissionsPapel partialUpdatedPermissionsPapel = new PermissionsPapel();
        partialUpdatedPermissionsPapel.setId(permissionsPapel.getId());

        partialUpdatedPermissionsPapel
            .idnVarPermissions(UPDATED_IDN_VAR_PERMISSIONS)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restPermissionsPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionsPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionsPapel))
            )
            .andExpect(status().isOk());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
        PermissionsPapel testPermissionsPapel = permissionsPapelList.get(permissionsPapelList.size() - 1);
        assertThat(testPermissionsPapel.getIdnVarPermissions()).isEqualTo(UPDATED_IDN_VAR_PERMISSIONS);
        assertThat(testPermissionsPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPermissionsPapel.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testPermissionsPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void patchNonExistingPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();
        permissionsPapel.setId(count.incrementAndGet());

        // Create the PermissionsPapel
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionsPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionsPapelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();
        permissionsPapel.setId(count.incrementAndGet());

        // Create the PermissionsPapel
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissionsPapel() throws Exception {
        int databaseSizeBeforeUpdate = permissionsPapelRepository.findAll().size();
        permissionsPapel.setId(count.incrementAndGet());

        // Create the PermissionsPapel
        PermissionsPapelDTO permissionsPapelDTO = permissionsPapelMapper.toDto(permissionsPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsPapelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionsPapelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionsPapel in the database
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissionsPapel() throws Exception {
        // Initialize the database
        permissionsPapelRepository.saveAndFlush(permissionsPapel);

        int databaseSizeBeforeDelete = permissionsPapelRepository.findAll().size();

        // Delete the permissionsPapel
        restPermissionsPapelMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissionsPapel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PermissionsPapel> permissionsPapelList = permissionsPapelRepository.findAll();
        assertThat(permissionsPapelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
