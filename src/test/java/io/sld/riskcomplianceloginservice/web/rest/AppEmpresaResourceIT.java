package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.repository.AppEmpresaRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppEmpresaDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.AppEmpresaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.sld.riskcomplianceloginservice.resource.AppEmpresaResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppEmpresaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppEmpresaResourceIT {

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppEmpresaRepository appEmpresaRepository;

    @Autowired
    private AppEmpresaMapper appEmpresaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppEmpresaMockMvc;

    private AppEmpresa appEmpresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppEmpresa createEntity(EntityManager em) {
        AppEmpresa appEmpresa = new AppEmpresa()
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return appEmpresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppEmpresa createUpdatedEntity(EntityManager em) {
        AppEmpresa appEmpresa = new AppEmpresa()
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return appEmpresa;
    }

    @BeforeEach
    public void initTest() {
        appEmpresa = createEntity(em);
    }

    @Test
    @Transactional
    void createAppEmpresa() throws Exception {
        int databaseSizeBeforeCreate = appEmpresaRepository.findAll().size();
        // Create the AppEmpresa
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);
        restAppEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO)))
            .andExpect(status().isCreated());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeCreate + 1);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void createAppEmpresaWithExistingId() throws Exception {
        // Create the AppEmpresa with an existing ID
        appEmpresa.setId(1L);
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        int databaseSizeBeforeCreate = appEmpresaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = appEmpresaRepository.findAll().size();
        // set the field null
        appEmpresa.setIdnVarApp(null);

        // Create the AppEmpresa, which fails.
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        restAppEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO)))
            .andExpect(status().isBadRequest());

        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = appEmpresaRepository.findAll().size();
        // set the field null
        appEmpresa.setIdnVarEmpresa(null);

        // Create the AppEmpresa, which fails.
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        restAppEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO)))
            .andExpect(status().isBadRequest());

        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = appEmpresaRepository.findAll().size();
        // set the field null
        appEmpresa.setIdnVarUsuario(null);

        // Create the AppEmpresa, which fails.
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        restAppEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO)))
            .andExpect(status().isBadRequest());

        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppEmpresas() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList
        restAppEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appEmpresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));
    }

    @Test
    @Transactional
    void getAppEmpresa() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get the appEmpresa
        restAppEmpresaMockMvc
            .perform(get(ENTITY_API_URL_ID, appEmpresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appEmpresa.getId().intValue()))
            .andExpect(jsonPath("$.idnVarApp").value(DEFAULT_IDN_VAR_APP))
            .andExpect(jsonPath("$.idnVarEmpresa").value(DEFAULT_IDN_VAR_EMPRESA))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    @Transactional
    void getAppEmpresasByIdFiltering() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        Long id = appEmpresa.getId();

        defaultAppEmpresaShouldBeFound("id.equals=" + id);
        defaultAppEmpresaShouldNotBeFound("id.notEquals=" + id);

        defaultAppEmpresaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppEmpresaShouldNotBeFound("id.greaterThan=" + id);

        defaultAppEmpresaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppEmpresaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarAppIsEqualToSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarApp equals to DEFAULT_IDN_VAR_APP
        defaultAppEmpresaShouldBeFound("idnVarApp.equals=" + DEFAULT_IDN_VAR_APP);

        // Get all the appEmpresaList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultAppEmpresaShouldNotBeFound("idnVarApp.equals=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarAppIsInShouldWork() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarApp in DEFAULT_IDN_VAR_APP or UPDATED_IDN_VAR_APP
        defaultAppEmpresaShouldBeFound("idnVarApp.in=" + DEFAULT_IDN_VAR_APP + "," + UPDATED_IDN_VAR_APP);

        // Get all the appEmpresaList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultAppEmpresaShouldNotBeFound("idnVarApp.in=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarAppIsNullOrNotNull() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarApp is not null
        defaultAppEmpresaShouldBeFound("idnVarApp.specified=true");

        // Get all the appEmpresaList where idnVarApp is null
        defaultAppEmpresaShouldNotBeFound("idnVarApp.specified=false");
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarAppContainsSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarApp contains DEFAULT_IDN_VAR_APP
        defaultAppEmpresaShouldBeFound("idnVarApp.contains=" + DEFAULT_IDN_VAR_APP);

        // Get all the appEmpresaList where idnVarApp contains UPDATED_IDN_VAR_APP
        defaultAppEmpresaShouldNotBeFound("idnVarApp.contains=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarAppNotContainsSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarApp does not contain DEFAULT_IDN_VAR_APP
        defaultAppEmpresaShouldNotBeFound("idnVarApp.doesNotContain=" + DEFAULT_IDN_VAR_APP);

        // Get all the appEmpresaList where idnVarApp does not contain UPDATED_IDN_VAR_APP
        defaultAppEmpresaShouldBeFound("idnVarApp.doesNotContain=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarEmpresa equals to DEFAULT_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldBeFound("idnVarEmpresa.equals=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the appEmpresaList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldNotBeFound("idnVarEmpresa.equals=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarEmpresaIsInShouldWork() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarEmpresa in DEFAULT_IDN_VAR_EMPRESA or UPDATED_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldBeFound("idnVarEmpresa.in=" + DEFAULT_IDN_VAR_EMPRESA + "," + UPDATED_IDN_VAR_EMPRESA);

        // Get all the appEmpresaList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldNotBeFound("idnVarEmpresa.in=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarEmpresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarEmpresa is not null
        defaultAppEmpresaShouldBeFound("idnVarEmpresa.specified=true");

        // Get all the appEmpresaList where idnVarEmpresa is null
        defaultAppEmpresaShouldNotBeFound("idnVarEmpresa.specified=false");
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarEmpresaContainsSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarEmpresa contains DEFAULT_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldBeFound("idnVarEmpresa.contains=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the appEmpresaList where idnVarEmpresa contains UPDATED_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldNotBeFound("idnVarEmpresa.contains=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarEmpresaNotContainsSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarEmpresa does not contain DEFAULT_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldNotBeFound("idnVarEmpresa.doesNotContain=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the appEmpresaList where idnVarEmpresa does not contain UPDATED_IDN_VAR_EMPRESA
        defaultAppEmpresaShouldBeFound("idnVarEmpresa.doesNotContain=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultAppEmpresaShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the appEmpresaList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultAppEmpresaShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultAppEmpresaShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the appEmpresaList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultAppEmpresaShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarUsuario is not null
        defaultAppEmpresaShouldBeFound("idnVarUsuario.specified=true");

        // Get all the appEmpresaList where idnVarUsuario is null
        defaultAppEmpresaShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultAppEmpresaShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the appEmpresaList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultAppEmpresaShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        // Get all the appEmpresaList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultAppEmpresaShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the appEmpresaList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultAppEmpresaShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllAppEmpresasByAppIsEqualToSomething() throws Exception {
        App app;
        if (TestUtil.findAll(em, App.class).isEmpty()) {
            appEmpresaRepository.saveAndFlush(appEmpresa);
            app = AppResourceIT.createEntity(em);
        } else {
            app = TestUtil.findAll(em, App.class).get(0);
        }
        em.persist(app);
        em.flush();
        appEmpresa.setApp(app);
        appEmpresaRepository.saveAndFlush(appEmpresa);
        Long appId = app.getId();

        // Get all the appEmpresaList where app equals to appId
        defaultAppEmpresaShouldBeFound("appId.equals=" + appId);

        // Get all the appEmpresaList where app equals to (appId + 1)
        defaultAppEmpresaShouldNotBeFound("appId.equals=" + (appId + 1));
    }

    @Test
    @Transactional
    void getAllAppEmpresasByEmpresaIsEqualToSomething() throws Exception {
        Empresa empresa;
        if (TestUtil.findAll(em, Empresa.class).isEmpty()) {
            appEmpresaRepository.saveAndFlush(appEmpresa);
            empresa = EmpresaResourceIT.createEntity(em);
        } else {
            empresa = TestUtil.findAll(em, Empresa.class).get(0);
        }
        em.persist(empresa);
        em.flush();
        appEmpresa.setEmpresa(empresa);
        appEmpresaRepository.saveAndFlush(appEmpresa);
        Long empresaId = empresa.getId();

        // Get all the appEmpresaList where empresa equals to empresaId
        defaultAppEmpresaShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the appEmpresaList where empresa equals to (empresaId + 1)
        defaultAppEmpresaShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllAppEmpresasByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            appEmpresaRepository.saveAndFlush(appEmpresa);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        appEmpresa.setUsuario(usuario);
        appEmpresaRepository.saveAndFlush(appEmpresa);
        Long usuarioId = usuario.getId();

        // Get all the appEmpresaList where usuario equals to usuarioId
        defaultAppEmpresaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the appEmpresaList where usuario equals to (usuarioId + 1)
        defaultAppEmpresaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppEmpresaShouldBeFound(String filter) throws Exception {
        restAppEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appEmpresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));

        // Check, that the count call also returns 1
        restAppEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppEmpresaShouldNotBeFound(String filter) throws Exception {
        restAppEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppEmpresa() throws Exception {
        // Get the appEmpresa
        restAppEmpresaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppEmpresa() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();

        // Update the appEmpresa
        AppEmpresa updatedAppEmpresa = appEmpresaRepository.findById(appEmpresa.getId()).get();
        // Disconnect from session so that the updates on updatedAppEmpresa are not directly saved in db
        em.detach(updatedAppEmpresa);
        updatedAppEmpresa.idnVarApp(UPDATED_IDN_VAR_APP).idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA).idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(updatedAppEmpresa);

        restAppEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appEmpresaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void putNonExistingAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();
        appEmpresa.setId(count.incrementAndGet());

        // Create the AppEmpresa
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appEmpresaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();
        appEmpresa.setId(count.incrementAndGet());

        // Create the AppEmpresa
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();
        appEmpresa.setId(count.incrementAndGet());

        // Create the AppEmpresa
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppEmpresaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppEmpresaWithPatch() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();

        // Update the appEmpresa using partial update
        AppEmpresa partialUpdatedAppEmpresa = new AppEmpresa();
        partialUpdatedAppEmpresa.setId(appEmpresa.getId());

        partialUpdatedAppEmpresa.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        restAppEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void fullUpdateAppEmpresaWithPatch() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();

        // Update the appEmpresa using partial update
        AppEmpresa partialUpdatedAppEmpresa = new AppEmpresa();
        partialUpdatedAppEmpresa.setId(appEmpresa.getId());

        partialUpdatedAppEmpresa
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restAppEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
        AppEmpresa testAppEmpresa = appEmpresaList.get(appEmpresaList.size() - 1);
        assertThat(testAppEmpresa.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testAppEmpresa.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testAppEmpresa.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void patchNonExistingAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();
        appEmpresa.setId(count.incrementAndGet());

        // Create the AppEmpresa
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appEmpresaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();
        appEmpresa.setId(count.incrementAndGet());

        // Create the AppEmpresa
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = appEmpresaRepository.findAll().size();
        appEmpresa.setId(count.incrementAndGet());

        // Create the AppEmpresa
        AppEmpresaDTO appEmpresaDTO = appEmpresaMapper.toDto(appEmpresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appEmpresaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppEmpresa in the database
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppEmpresa() throws Exception {
        // Initialize the database
        appEmpresaRepository.saveAndFlush(appEmpresa);

        int databaseSizeBeforeDelete = appEmpresaRepository.findAll().size();

        // Delete the appEmpresa
        restAppEmpresaMockMvc
            .perform(delete(ENTITY_API_URL_ID, appEmpresa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppEmpresa> appEmpresaList = appEmpresaRepository.findAll();
        assertThat(appEmpresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
