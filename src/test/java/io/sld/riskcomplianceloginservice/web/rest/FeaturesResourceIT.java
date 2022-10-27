package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.repository.FeaturesRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.FeaturesDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.FeaturesMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.sld.riskcomplianceloginservice.resource.FeaturesResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeaturesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeaturesResourceIT {

    private static final String DEFAULT_IDN_VAR_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_FEATURES = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/features";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeaturesRepository featuresRepository;

    @Autowired
    private FeaturesMapper featuresMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeaturesMockMvc;

    private Features features;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Features createEntity(EntityManager em) {
        Features features = new Features()
            .idnVarFeatures(DEFAULT_IDN_VAR_FEATURES)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return features;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Features createUpdatedEntity(EntityManager em) {
        Features features = new Features()
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return features;
    }

    @BeforeEach
    public void initTest() {
        features = createEntity(em);
    }

    @Test
    @Transactional
    void createFeatures() throws Exception {
        int databaseSizeBeforeCreate = featuresRepository.findAll().size();
        // Create the Features
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);
        restFeaturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(featuresDTO)))
            .andExpect(status().isCreated());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeCreate + 1);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(DEFAULT_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void createFeaturesWithExistingId() throws Exception {
        // Create the Features with an existing ID
        features.setId(1L);
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        int databaseSizeBeforeCreate = featuresRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeaturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(featuresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarFeaturesIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().size();
        // set the field null
        features.setIdnVarFeatures(null);

        // Create the Features, which fails.
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        restFeaturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(featuresDTO)))
            .andExpect(status().isBadRequest());

        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().size();
        // set the field null
        features.setnVarNome(null);

        // Create the Features, which fails.
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        restFeaturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(featuresDTO)))
            .andExpect(status().isBadRequest());

        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().size();
        // set the field null
        features.setIdnVarApp(null);

        // Create the Features, which fails.
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        restFeaturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(featuresDTO)))
            .andExpect(status().isBadRequest());

        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = featuresRepository.findAll().size();
        // set the field null
        features.setIdnVarUsuario(null);

        // Create the Features, which fails.
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        restFeaturesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(featuresDTO)))
            .andExpect(status().isBadRequest());

        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeatures() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList
        restFeaturesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(features.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarFeatures").value(hasItem(DEFAULT_IDN_VAR_FEATURES)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));
    }

    @Test
    @Transactional
    void getFeatures() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get the features
        restFeaturesMockMvc
            .perform(get(ENTITY_API_URL_ID, features.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(features.getId().intValue()))
            .andExpect(jsonPath("$.idnVarFeatures").value(DEFAULT_IDN_VAR_FEATURES))
            .andExpect(jsonPath("$.nVarNome").value(DEFAULT_N_VAR_NOME))
            .andExpect(jsonPath("$.idnVarApp").value(DEFAULT_IDN_VAR_APP))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    @Transactional
    void getFeaturesByIdFiltering() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        Long id = features.getId();

        defaultFeaturesShouldBeFound("id.equals=" + id);
        defaultFeaturesShouldNotBeFound("id.notEquals=" + id);

        defaultFeaturesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFeaturesShouldNotBeFound("id.greaterThan=" + id);

        defaultFeaturesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFeaturesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarFeaturesIsEqualToSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarFeatures equals to DEFAULT_IDN_VAR_FEATURES
        defaultFeaturesShouldBeFound("idnVarFeatures.equals=" + DEFAULT_IDN_VAR_FEATURES);

        // Get all the featuresList where idnVarFeatures equals to UPDATED_IDN_VAR_FEATURES
        defaultFeaturesShouldNotBeFound("idnVarFeatures.equals=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarFeaturesIsInShouldWork() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarFeatures in DEFAULT_IDN_VAR_FEATURES or UPDATED_IDN_VAR_FEATURES
        defaultFeaturesShouldBeFound("idnVarFeatures.in=" + DEFAULT_IDN_VAR_FEATURES + "," + UPDATED_IDN_VAR_FEATURES);

        // Get all the featuresList where idnVarFeatures equals to UPDATED_IDN_VAR_FEATURES
        defaultFeaturesShouldNotBeFound("idnVarFeatures.in=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarFeaturesIsNullOrNotNull() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarFeatures is not null
        defaultFeaturesShouldBeFound("idnVarFeatures.specified=true");

        // Get all the featuresList where idnVarFeatures is null
        defaultFeaturesShouldNotBeFound("idnVarFeatures.specified=false");
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarFeaturesContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarFeatures contains DEFAULT_IDN_VAR_FEATURES
        defaultFeaturesShouldBeFound("idnVarFeatures.contains=" + DEFAULT_IDN_VAR_FEATURES);

        // Get all the featuresList where idnVarFeatures contains UPDATED_IDN_VAR_FEATURES
        defaultFeaturesShouldNotBeFound("idnVarFeatures.contains=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarFeaturesNotContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarFeatures does not contain DEFAULT_IDN_VAR_FEATURES
        defaultFeaturesShouldNotBeFound("idnVarFeatures.doesNotContain=" + DEFAULT_IDN_VAR_FEATURES);

        // Get all the featuresList where idnVarFeatures does not contain UPDATED_IDN_VAR_FEATURES
        defaultFeaturesShouldBeFound("idnVarFeatures.doesNotContain=" + UPDATED_IDN_VAR_FEATURES);
    }

    @Test
    @Transactional
    void getAllFeaturesBynVarNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where nVarNome equals to DEFAULT_N_VAR_NOME
        defaultFeaturesShouldBeFound("nVarNome.equals=" + DEFAULT_N_VAR_NOME);

        // Get all the featuresList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultFeaturesShouldNotBeFound("nVarNome.equals=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllFeaturesBynVarNomeIsInShouldWork() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where nVarNome in DEFAULT_N_VAR_NOME or UPDATED_N_VAR_NOME
        defaultFeaturesShouldBeFound("nVarNome.in=" + DEFAULT_N_VAR_NOME + "," + UPDATED_N_VAR_NOME);

        // Get all the featuresList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultFeaturesShouldNotBeFound("nVarNome.in=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllFeaturesBynVarNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where nVarNome is not null
        defaultFeaturesShouldBeFound("nVarNome.specified=true");

        // Get all the featuresList where nVarNome is null
        defaultFeaturesShouldNotBeFound("nVarNome.specified=false");
    }

    @Test
    @Transactional
    void getAllFeaturesBynVarNomeContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where nVarNome contains DEFAULT_N_VAR_NOME
        defaultFeaturesShouldBeFound("nVarNome.contains=" + DEFAULT_N_VAR_NOME);

        // Get all the featuresList where nVarNome contains UPDATED_N_VAR_NOME
        defaultFeaturesShouldNotBeFound("nVarNome.contains=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllFeaturesBynVarNomeNotContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where nVarNome does not contain DEFAULT_N_VAR_NOME
        defaultFeaturesShouldNotBeFound("nVarNome.doesNotContain=" + DEFAULT_N_VAR_NOME);

        // Get all the featuresList where nVarNome does not contain UPDATED_N_VAR_NOME
        defaultFeaturesShouldBeFound("nVarNome.doesNotContain=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarAppIsEqualToSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarApp equals to DEFAULT_IDN_VAR_APP
        defaultFeaturesShouldBeFound("idnVarApp.equals=" + DEFAULT_IDN_VAR_APP);

        // Get all the featuresList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultFeaturesShouldNotBeFound("idnVarApp.equals=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarAppIsInShouldWork() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarApp in DEFAULT_IDN_VAR_APP or UPDATED_IDN_VAR_APP
        defaultFeaturesShouldBeFound("idnVarApp.in=" + DEFAULT_IDN_VAR_APP + "," + UPDATED_IDN_VAR_APP);

        // Get all the featuresList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultFeaturesShouldNotBeFound("idnVarApp.in=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarAppIsNullOrNotNull() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarApp is not null
        defaultFeaturesShouldBeFound("idnVarApp.specified=true");

        // Get all the featuresList where idnVarApp is null
        defaultFeaturesShouldNotBeFound("idnVarApp.specified=false");
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarAppContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarApp contains DEFAULT_IDN_VAR_APP
        defaultFeaturesShouldBeFound("idnVarApp.contains=" + DEFAULT_IDN_VAR_APP);

        // Get all the featuresList where idnVarApp contains UPDATED_IDN_VAR_APP
        defaultFeaturesShouldNotBeFound("idnVarApp.contains=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarAppNotContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarApp does not contain DEFAULT_IDN_VAR_APP
        defaultFeaturesShouldNotBeFound("idnVarApp.doesNotContain=" + DEFAULT_IDN_VAR_APP);

        // Get all the featuresList where idnVarApp does not contain UPDATED_IDN_VAR_APP
        defaultFeaturesShouldBeFound("idnVarApp.doesNotContain=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultFeaturesShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the featuresList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultFeaturesShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultFeaturesShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the featuresList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultFeaturesShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarUsuario is not null
        defaultFeaturesShouldBeFound("idnVarUsuario.specified=true");

        // Get all the featuresList where idnVarUsuario is null
        defaultFeaturesShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultFeaturesShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the featuresList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultFeaturesShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllFeaturesByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        // Get all the featuresList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultFeaturesShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the featuresList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultFeaturesShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllFeaturesByPermissionsPapelIsEqualToSomething() throws Exception {
        PermissionsPapel permissionsPapel;
        if (TestUtil.findAll(em, PermissionsPapel.class).isEmpty()) {
            featuresRepository.saveAndFlush(features);
            permissionsPapel = PermissionsPapelResourceIT.createEntity(em);
        } else {
            permissionsPapel = TestUtil.findAll(em, PermissionsPapel.class).get(0);
        }
        em.persist(permissionsPapel);
        em.flush();
        features.addPermissionsPapel(permissionsPapel);
        featuresRepository.saveAndFlush(features);
        Long permissionsPapelId = permissionsPapel.getId();

        // Get all the featuresList where permissionsPapel equals to permissionsPapelId
        defaultFeaturesShouldBeFound("permissionsPapelId.equals=" + permissionsPapelId);

        // Get all the featuresList where permissionsPapel equals to (permissionsPapelId + 1)
        defaultFeaturesShouldNotBeFound("permissionsPapelId.equals=" + (permissionsPapelId + 1));
    }

    @Test
    @Transactional
    void getAllFeaturesByAppIsEqualToSomething() throws Exception {
        App app;
        if (TestUtil.findAll(em, App.class).isEmpty()) {
            featuresRepository.saveAndFlush(features);
            app = AppResourceIT.createEntity(em);
        } else {
            app = TestUtil.findAll(em, App.class).get(0);
        }
        em.persist(app);
        em.flush();
        features.setApp(app);
        featuresRepository.saveAndFlush(features);
        Long appId = app.getId();

        // Get all the featuresList where app equals to appId
        defaultFeaturesShouldBeFound("appId.equals=" + appId);

        // Get all the featuresList where app equals to (appId + 1)
        defaultFeaturesShouldNotBeFound("appId.equals=" + (appId + 1));
    }

    @Test
    @Transactional
    void getAllFeaturesByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            featuresRepository.saveAndFlush(features);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        features.setUsuario(usuario);
        featuresRepository.saveAndFlush(features);
        Long usuarioId = usuario.getId();

        // Get all the featuresList where usuario equals to usuarioId
        defaultFeaturesShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the featuresList where usuario equals to (usuarioId + 1)
        defaultFeaturesShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFeaturesShouldBeFound(String filter) throws Exception {
        restFeaturesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(features.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarFeatures").value(hasItem(DEFAULT_IDN_VAR_FEATURES)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));

        // Check, that the count call also returns 1
        restFeaturesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFeaturesShouldNotBeFound(String filter) throws Exception {
        restFeaturesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFeaturesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFeatures() throws Exception {
        // Get the features
        restFeaturesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeatures() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();

        // Update the features
        Features updatedFeatures = featuresRepository.findById(features.getId()).get();
        // Disconnect from session so that the updates on updatedFeatures are not directly saved in db
        em.detach(updatedFeatures);
        updatedFeatures
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        FeaturesDTO featuresDTO = featuresMapper.toDto(updatedFeatures);

        restFeaturesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, featuresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(featuresDTO))
            )
            .andExpect(status().isOk());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void putNonExistingFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();
        features.setId(count.incrementAndGet());

        // Create the Features
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeaturesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, featuresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(featuresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();
        features.setId(count.incrementAndGet());

        // Create the Features
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeaturesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(featuresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();
        features.setId(count.incrementAndGet());

        // Create the Features
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeaturesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(featuresDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeaturesWithPatch() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();

        // Update the features using partial update
        Features partialUpdatedFeatures = new Features();
        partialUpdatedFeatures.setId(features.getId());

        partialUpdatedFeatures.idnVarFeatures(UPDATED_IDN_VAR_FEATURES).nVarNome(UPDATED_N_VAR_NOME).idnVarApp(UPDATED_IDN_VAR_APP);

        restFeaturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatures.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeatures))
            )
            .andExpect(status().isOk());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void fullUpdateFeaturesWithPatch() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();

        // Update the features using partial update
        Features partialUpdatedFeatures = new Features();
        partialUpdatedFeatures.setId(features.getId());

        partialUpdatedFeatures
            .idnVarFeatures(UPDATED_IDN_VAR_FEATURES)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restFeaturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatures.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeatures))
            )
            .andExpect(status().isOk());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
        Features testFeatures = featuresList.get(featuresList.size() - 1);
        assertThat(testFeatures.getIdnVarFeatures()).isEqualTo(UPDATED_IDN_VAR_FEATURES);
        assertThat(testFeatures.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testFeatures.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testFeatures.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void patchNonExistingFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();
        features.setId(count.incrementAndGet());

        // Create the Features
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeaturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, featuresDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(featuresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();
        features.setId(count.incrementAndGet());

        // Create the Features
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeaturesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(featuresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeatures() throws Exception {
        int databaseSizeBeforeUpdate = featuresRepository.findAll().size();
        features.setId(count.incrementAndGet());

        // Create the Features
        FeaturesDTO featuresDTO = featuresMapper.toDto(features);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeaturesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(featuresDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Features in the database
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeatures() throws Exception {
        // Initialize the database
        featuresRepository.saveAndFlush(features);

        int databaseSizeBeforeDelete = featuresRepository.findAll().size();

        // Delete the features
        restFeaturesMockMvc
            .perform(delete(ENTITY_API_URL_ID, features.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Features> featuresList = featuresRepository.findAll();
        assertThat(featuresList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
