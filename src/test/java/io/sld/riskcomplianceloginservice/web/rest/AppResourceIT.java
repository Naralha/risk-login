package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.repository.AppRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.AppMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppResourceIT {

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppMockMvc;

    private App app;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createEntity(EntityManager em) {
        App app = new App()
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA);
        return app;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createUpdatedEntity(EntityManager em) {
        App app = new App()
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        return app;
    }

    @BeforeEach
    public void initTest() {
        app = createEntity(em);
    }

    @Test
    @Transactional
    void createApp() throws Exception {
        int databaseSizeBeforeCreate = appRepository.findAll().size();
        // Create the App
        AppDTO appDTO = appMapper.toDto(app);
        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isCreated());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate + 1);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void createAppWithExistingId() throws Exception {
        // Create the App with an existing ID
        app.setId(1L);
        AppDTO appDTO = appMapper.toDto(app);

        int databaseSizeBeforeCreate = appRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setIdnVarApp(null);

        // Create the App, which fails.
        AppDTO appDTO = appMapper.toDto(app);

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setnVarNome(null);

        // Create the App, which fails.
        AppDTO appDTO = appMapper.toDto(app);

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setIdnVarUsuario(null);

        // Create the App, which fails.
        AppDTO appDTO = appMapper.toDto(app);

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = appRepository.findAll().size();
        // set the field null
        app.setIdnVarEmpresa(null);

        // Create the App, which fails.
        AppDTO appDTO = appMapper.toDto(app);

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isBadRequest());

        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApps() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)));
    }

    @Test
    @Transactional
    void getApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get the app
        restAppMockMvc
            .perform(get(ENTITY_API_URL_ID, app.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(app.getId().intValue()))
            .andExpect(jsonPath("$.idnVarApp").value(DEFAULT_IDN_VAR_APP))
            .andExpect(jsonPath("$.nVarNome").value(DEFAULT_N_VAR_NOME))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO))
            .andExpect(jsonPath("$.idnVarEmpresa").value(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    @Transactional
    void getAppsByIdFiltering() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        Long id = app.getId();

        defaultAppShouldBeFound("id.equals=" + id);
        defaultAppShouldNotBeFound("id.notEquals=" + id);

        defaultAppShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppShouldNotBeFound("id.greaterThan=" + id);

        defaultAppShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarAppIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarApp equals to DEFAULT_IDN_VAR_APP
        defaultAppShouldBeFound("idnVarApp.equals=" + DEFAULT_IDN_VAR_APP);

        // Get all the appList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultAppShouldNotBeFound("idnVarApp.equals=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarAppIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarApp in DEFAULT_IDN_VAR_APP or UPDATED_IDN_VAR_APP
        defaultAppShouldBeFound("idnVarApp.in=" + DEFAULT_IDN_VAR_APP + "," + UPDATED_IDN_VAR_APP);

        // Get all the appList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultAppShouldNotBeFound("idnVarApp.in=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarAppIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarApp is not null
        defaultAppShouldBeFound("idnVarApp.specified=true");

        // Get all the appList where idnVarApp is null
        defaultAppShouldNotBeFound("idnVarApp.specified=false");
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarAppContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarApp contains DEFAULT_IDN_VAR_APP
        defaultAppShouldBeFound("idnVarApp.contains=" + DEFAULT_IDN_VAR_APP);

        // Get all the appList where idnVarApp contains UPDATED_IDN_VAR_APP
        defaultAppShouldNotBeFound("idnVarApp.contains=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarAppNotContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarApp does not contain DEFAULT_IDN_VAR_APP
        defaultAppShouldNotBeFound("idnVarApp.doesNotContain=" + DEFAULT_IDN_VAR_APP);

        // Get all the appList where idnVarApp does not contain UPDATED_IDN_VAR_APP
        defaultAppShouldBeFound("idnVarApp.doesNotContain=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppsBynVarNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nVarNome equals to DEFAULT_N_VAR_NOME
        defaultAppShouldBeFound("nVarNome.equals=" + DEFAULT_N_VAR_NOME);

        // Get all the appList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultAppShouldNotBeFound("nVarNome.equals=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllAppsBynVarNomeIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nVarNome in DEFAULT_N_VAR_NOME or UPDATED_N_VAR_NOME
        defaultAppShouldBeFound("nVarNome.in=" + DEFAULT_N_VAR_NOME + "," + UPDATED_N_VAR_NOME);

        // Get all the appList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultAppShouldNotBeFound("nVarNome.in=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllAppsBynVarNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nVarNome is not null
        defaultAppShouldBeFound("nVarNome.specified=true");

        // Get all the appList where nVarNome is null
        defaultAppShouldNotBeFound("nVarNome.specified=false");
    }

    @Test
    @Transactional
    void getAllAppsBynVarNomeContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nVarNome contains DEFAULT_N_VAR_NOME
        defaultAppShouldBeFound("nVarNome.contains=" + DEFAULT_N_VAR_NOME);

        // Get all the appList where nVarNome contains UPDATED_N_VAR_NOME
        defaultAppShouldNotBeFound("nVarNome.contains=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllAppsBynVarNomeNotContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where nVarNome does not contain DEFAULT_N_VAR_NOME
        defaultAppShouldNotBeFound("nVarNome.doesNotContain=" + DEFAULT_N_VAR_NOME);

        // Get all the appList where nVarNome does not contain UPDATED_N_VAR_NOME
        defaultAppShouldBeFound("nVarNome.doesNotContain=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultAppShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the appList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultAppShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultAppShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the appList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultAppShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarUsuario is not null
        defaultAppShouldBeFound("idnVarUsuario.specified=true");

        // Get all the appList where idnVarUsuario is null
        defaultAppShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultAppShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the appList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultAppShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultAppShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the appList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultAppShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarEmpresa equals to DEFAULT_IDN_VAR_EMPRESA
        defaultAppShouldBeFound("idnVarEmpresa.equals=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the appList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultAppShouldNotBeFound("idnVarEmpresa.equals=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarEmpresaIsInShouldWork() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarEmpresa in DEFAULT_IDN_VAR_EMPRESA or UPDATED_IDN_VAR_EMPRESA
        defaultAppShouldBeFound("idnVarEmpresa.in=" + DEFAULT_IDN_VAR_EMPRESA + "," + UPDATED_IDN_VAR_EMPRESA);

        // Get all the appList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultAppShouldNotBeFound("idnVarEmpresa.in=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarEmpresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarEmpresa is not null
        defaultAppShouldBeFound("idnVarEmpresa.specified=true");

        // Get all the appList where idnVarEmpresa is null
        defaultAppShouldNotBeFound("idnVarEmpresa.specified=false");
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarEmpresaContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarEmpresa contains DEFAULT_IDN_VAR_EMPRESA
        defaultAppShouldBeFound("idnVarEmpresa.contains=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the appList where idnVarEmpresa contains UPDATED_IDN_VAR_EMPRESA
        defaultAppShouldNotBeFound("idnVarEmpresa.contains=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppsByIdnVarEmpresaNotContainsSomething() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        // Get all the appList where idnVarEmpresa does not contain DEFAULT_IDN_VAR_EMPRESA
        defaultAppShouldNotBeFound("idnVarEmpresa.doesNotContain=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the appList where idnVarEmpresa does not contain UPDATED_IDN_VAR_EMPRESA
        defaultAppShouldBeFound("idnVarEmpresa.doesNotContain=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppsByAppEmpresaIsEqualToSomething() throws Exception {
        AppEmpresa appEmpresa;
        if (TestUtil.findAll(em, AppEmpresa.class).isEmpty()) {
            appRepository.saveAndFlush(app);
            appEmpresa = AppEmpresaResourceIT.createEntity(em);
        } else {
            appEmpresa = TestUtil.findAll(em, AppEmpresa.class).get(0);
        }
        em.persist(appEmpresa);
        em.flush();
        app.addAppEmpresa(appEmpresa);
        appRepository.saveAndFlush(app);
        Long appEmpresaId = appEmpresa.getId();

        // Get all the appList where appEmpresa equals to appEmpresaId
        defaultAppShouldBeFound("appEmpresaId.equals=" + appEmpresaId);

        // Get all the appList where appEmpresa equals to (appEmpresaId + 1)
        defaultAppShouldNotBeFound("appEmpresaId.equals=" + (appEmpresaId + 1));
    }

    @Test
    @Transactional
    void getAllAppsByFeaturesIsEqualToSomething() throws Exception {
        Features features;
        if (TestUtil.findAll(em, Features.class).isEmpty()) {
            appRepository.saveAndFlush(app);
            features = FeaturesResourceIT.createEntity(em);
        } else {
            features = TestUtil.findAll(em, Features.class).get(0);
        }
        em.persist(features);
        em.flush();
        app.addFeatures(features);
        appRepository.saveAndFlush(app);
        Long featuresId = features.getId();

        // Get all the appList where features equals to featuresId
        defaultAppShouldBeFound("featuresId.equals=" + featuresId);

        // Get all the appList where features equals to (featuresId + 1)
        defaultAppShouldNotBeFound("featuresId.equals=" + (featuresId + 1));
    }

    @Test
    @Transactional
    void getAllAppsByPapelIsEqualToSomething() throws Exception {
        Papel papel;
        if (TestUtil.findAll(em, Papel.class).isEmpty()) {
            appRepository.saveAndFlush(app);
            papel = PapelResourceIT.createEntity(em);
        } else {
            papel = TestUtil.findAll(em, Papel.class).get(0);
        }
        em.persist(papel);
        em.flush();
        app.addPapel(papel);
        appRepository.saveAndFlush(app);
        Long papelId = papel.getId();

        // Get all the appList where papel equals to papelId
        defaultAppShouldBeFound("papelId.equals=" + papelId);

        // Get all the appList where papel equals to (papelId + 1)
        defaultAppShouldNotBeFound("papelId.equals=" + (papelId + 1));
    }

    @Test
    @Transactional
    void getAllAppsByEmpresaIsEqualToSomething() throws Exception {
        Empresa empresa;
        if (TestUtil.findAll(em, Empresa.class).isEmpty()) {
            appRepository.saveAndFlush(app);
            empresa = EmpresaResourceIT.createEntity(em);
        } else {
            empresa = TestUtil.findAll(em, Empresa.class).get(0);
        }
        em.persist(empresa);
        em.flush();
        app.setEmpresa(empresa);
        appRepository.saveAndFlush(app);
        Long empresaId = empresa.getId();

        // Get all the appList where empresa equals to empresaId
        defaultAppShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the appList where empresa equals to (empresaId + 1)
        defaultAppShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllAppsByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            appRepository.saveAndFlush(app);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        app.setUsuario(usuario);
        appRepository.saveAndFlush(app);
        Long usuarioId = usuario.getId();

        // Get all the appList where usuario equals to usuarioId
        defaultAppShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the appList where usuario equals to (usuarioId + 1)
        defaultAppShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppShouldBeFound(String filter) throws Exception {
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)));

        // Check, that the count call also returns 1
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppShouldNotBeFound(String filter) throws Exception {
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApp() throws Exception {
        // Get the app
        restAppMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app
        App updatedApp = appRepository.findById(app.getId()).get();
        // Disconnect from session so that the updates on updatedApp are not directly saved in db
        em.detach(updatedApp);
        updatedApp
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        AppDTO appDTO = appMapper.toDto(updatedApp);

        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void putNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void fullUpdateAppWithPatch() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeUpdate = appRepository.findAll().size();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
        App testApp = appList.get(appList.size() - 1);
        assertThat(testApp.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testApp.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testApp.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testApp.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void patchNonExistingApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApp() throws Exception {
        int databaseSizeBeforeUpdate = appRepository.findAll().size();
        app.setId(count.incrementAndGet());

        // Create the App
        AppDTO appDTO = appMapper.toDto(app);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApp() throws Exception {
        // Initialize the database
        appRepository.saveAndFlush(app);

        int databaseSizeBeforeDelete = appRepository.findAll().size();

        // Delete the app
        restAppMockMvc.perform(delete(ENTITY_API_URL_ID, app.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<App> appList = appRepository.findAll();
        assertThat(appList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
