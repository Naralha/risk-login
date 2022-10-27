package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.repository.PapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PapelMapper;
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
 * Integration tests for the {@link PapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PapelResourceIT {

    private static final String DEFAULT_IDN_VAR_PAPEL = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PAPEL = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_APP = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_APP = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/papels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private PapelMapper papelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPapelMockMvc;

    private Papel papel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Papel createEntity(EntityManager em) {
        Papel papel = new Papel()
            .idnVarPapel(DEFAULT_IDN_VAR_PAPEL)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarApp(DEFAULT_IDN_VAR_APP)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return papel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Papel createUpdatedEntity(EntityManager em) {
        Papel papel = new Papel()
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return papel;
    }

    @BeforeEach
    public void initTest() {
        papel = createEntity(em);
    }

    @Test
    @Transactional
    void createPapel() throws Exception {
        int databaseSizeBeforeCreate = papelRepository.findAll().size();
        // Create the Papel
        PapelDTO papelDTO = papelMapper.toDto(papel);
        restPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isCreated());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeCreate + 1);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void createPapelWithExistingId() throws Exception {
        // Create the Papel with an existing ID
        papel.setId(1L);
        PapelDTO papelDTO = papelMapper.toDto(papel);

        int databaseSizeBeforeCreate = papelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().size();
        // set the field null
        papel.setIdnVarPapel(null);

        // Create the Papel, which fails.
        PapelDTO papelDTO = papelMapper.toDto(papel);

        restPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isBadRequest());

        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().size();
        // set the field null
        papel.setnVarNome(null);

        // Create the Papel, which fails.
        PapelDTO papelDTO = papelMapper.toDto(papel);

        restPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isBadRequest());

        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarAppIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().size();
        // set the field null
        papel.setIdnVarApp(null);

        // Create the Papel, which fails.
        PapelDTO papelDTO = papelMapper.toDto(papel);

        restPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isBadRequest());

        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = papelRepository.findAll().size();
        // set the field null
        papel.setIdnVarUsuario(null);

        // Create the Papel, which fails.
        PapelDTO papelDTO = papelMapper.toDto(papel);

        restPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isBadRequest());

        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPapels() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList
        restPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(papel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));
    }

    @Test
    @Transactional
    void getPapel() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get the papel
        restPapelMockMvc
            .perform(get(ENTITY_API_URL_ID, papel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(papel.getId().intValue()))
            .andExpect(jsonPath("$.idnVarPapel").value(DEFAULT_IDN_VAR_PAPEL))
            .andExpect(jsonPath("$.nVarNome").value(DEFAULT_N_VAR_NOME))
            .andExpect(jsonPath("$.idnVarApp").value(DEFAULT_IDN_VAR_APP))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    @Transactional
    void getPapelsByIdFiltering() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        Long id = papel.getId();

        defaultPapelShouldBeFound("id.equals=" + id);
        defaultPapelShouldNotBeFound("id.notEquals=" + id);

        defaultPapelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPapelShouldNotBeFound("id.greaterThan=" + id);

        defaultPapelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPapelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarPapelIsEqualToSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarPapel equals to DEFAULT_IDN_VAR_PAPEL
        defaultPapelShouldBeFound("idnVarPapel.equals=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the papelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultPapelShouldNotBeFound("idnVarPapel.equals=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarPapelIsInShouldWork() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarPapel in DEFAULT_IDN_VAR_PAPEL or UPDATED_IDN_VAR_PAPEL
        defaultPapelShouldBeFound("idnVarPapel.in=" + DEFAULT_IDN_VAR_PAPEL + "," + UPDATED_IDN_VAR_PAPEL);

        // Get all the papelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultPapelShouldNotBeFound("idnVarPapel.in=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarPapelIsNullOrNotNull() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarPapel is not null
        defaultPapelShouldBeFound("idnVarPapel.specified=true");

        // Get all the papelList where idnVarPapel is null
        defaultPapelShouldNotBeFound("idnVarPapel.specified=false");
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarPapelContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarPapel contains DEFAULT_IDN_VAR_PAPEL
        defaultPapelShouldBeFound("idnVarPapel.contains=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the papelList where idnVarPapel contains UPDATED_IDN_VAR_PAPEL
        defaultPapelShouldNotBeFound("idnVarPapel.contains=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarPapelNotContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarPapel does not contain DEFAULT_IDN_VAR_PAPEL
        defaultPapelShouldNotBeFound("idnVarPapel.doesNotContain=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the papelList where idnVarPapel does not contain UPDATED_IDN_VAR_PAPEL
        defaultPapelShouldBeFound("idnVarPapel.doesNotContain=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllPapelsBynVarNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where nVarNome equals to DEFAULT_N_VAR_NOME
        defaultPapelShouldBeFound("nVarNome.equals=" + DEFAULT_N_VAR_NOME);

        // Get all the papelList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultPapelShouldNotBeFound("nVarNome.equals=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPapelsBynVarNomeIsInShouldWork() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where nVarNome in DEFAULT_N_VAR_NOME or UPDATED_N_VAR_NOME
        defaultPapelShouldBeFound("nVarNome.in=" + DEFAULT_N_VAR_NOME + "," + UPDATED_N_VAR_NOME);

        // Get all the papelList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultPapelShouldNotBeFound("nVarNome.in=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPapelsBynVarNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where nVarNome is not null
        defaultPapelShouldBeFound("nVarNome.specified=true");

        // Get all the papelList where nVarNome is null
        defaultPapelShouldNotBeFound("nVarNome.specified=false");
    }

    @Test
    @Transactional
    void getAllPapelsBynVarNomeContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where nVarNome contains DEFAULT_N_VAR_NOME
        defaultPapelShouldBeFound("nVarNome.contains=" + DEFAULT_N_VAR_NOME);

        // Get all the papelList where nVarNome contains UPDATED_N_VAR_NOME
        defaultPapelShouldNotBeFound("nVarNome.contains=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPapelsBynVarNomeNotContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where nVarNome does not contain DEFAULT_N_VAR_NOME
        defaultPapelShouldNotBeFound("nVarNome.doesNotContain=" + DEFAULT_N_VAR_NOME);

        // Get all the papelList where nVarNome does not contain UPDATED_N_VAR_NOME
        defaultPapelShouldBeFound("nVarNome.doesNotContain=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarAppIsEqualToSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarApp equals to DEFAULT_IDN_VAR_APP
        defaultPapelShouldBeFound("idnVarApp.equals=" + DEFAULT_IDN_VAR_APP);

        // Get all the papelList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultPapelShouldNotBeFound("idnVarApp.equals=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarAppIsInShouldWork() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarApp in DEFAULT_IDN_VAR_APP or UPDATED_IDN_VAR_APP
        defaultPapelShouldBeFound("idnVarApp.in=" + DEFAULT_IDN_VAR_APP + "," + UPDATED_IDN_VAR_APP);

        // Get all the papelList where idnVarApp equals to UPDATED_IDN_VAR_APP
        defaultPapelShouldNotBeFound("idnVarApp.in=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarAppIsNullOrNotNull() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarApp is not null
        defaultPapelShouldBeFound("idnVarApp.specified=true");

        // Get all the papelList where idnVarApp is null
        defaultPapelShouldNotBeFound("idnVarApp.specified=false");
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarAppContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarApp contains DEFAULT_IDN_VAR_APP
        defaultPapelShouldBeFound("idnVarApp.contains=" + DEFAULT_IDN_VAR_APP);

        // Get all the papelList where idnVarApp contains UPDATED_IDN_VAR_APP
        defaultPapelShouldNotBeFound("idnVarApp.contains=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarAppNotContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarApp does not contain DEFAULT_IDN_VAR_APP
        defaultPapelShouldNotBeFound("idnVarApp.doesNotContain=" + DEFAULT_IDN_VAR_APP);

        // Get all the papelList where idnVarApp does not contain UPDATED_IDN_VAR_APP
        defaultPapelShouldBeFound("idnVarApp.doesNotContain=" + UPDATED_IDN_VAR_APP);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultPapelShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the papelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultPapelShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultPapelShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the papelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultPapelShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarUsuario is not null
        defaultPapelShouldBeFound("idnVarUsuario.specified=true");

        // Get all the papelList where idnVarUsuario is null
        defaultPapelShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultPapelShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the papelList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultPapelShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPapelsByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        // Get all the papelList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultPapelShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the papelList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultPapelShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllPapelsByGrupoPapelIsEqualToSomething() throws Exception {
        GrupoPapel grupoPapel;
        if (TestUtil.findAll(em, GrupoPapel.class).isEmpty()) {
            papelRepository.saveAndFlush(papel);
            grupoPapel = GrupoPapelResourceIT.createEntity(em);
        } else {
            grupoPapel = TestUtil.findAll(em, GrupoPapel.class).get(0);
        }
        em.persist(grupoPapel);
        em.flush();
        papel.addGrupoPapel(grupoPapel);
        papelRepository.saveAndFlush(papel);
        Long grupoPapelId = grupoPapel.getId();

        // Get all the papelList where grupoPapel equals to grupoPapelId
        defaultPapelShouldBeFound("grupoPapelId.equals=" + grupoPapelId);

        // Get all the papelList where grupoPapel equals to (grupoPapelId + 1)
        defaultPapelShouldNotBeFound("grupoPapelId.equals=" + (grupoPapelId + 1));
    }

    @Test
    @Transactional
    void getAllPapelsByPermissionsPapelIsEqualToSomething() throws Exception {
        PermissionsPapel permissionsPapel;
        if (TestUtil.findAll(em, PermissionsPapel.class).isEmpty()) {
            papelRepository.saveAndFlush(papel);
            permissionsPapel = PermissionsPapelResourceIT.createEntity(em);
        } else {
            permissionsPapel = TestUtil.findAll(em, PermissionsPapel.class).get(0);
        }
        em.persist(permissionsPapel);
        em.flush();
        papel.addPermissionsPapel(permissionsPapel);
        papelRepository.saveAndFlush(papel);
        Long permissionsPapelId = permissionsPapel.getId();

        // Get all the papelList where permissionsPapel equals to permissionsPapelId
        defaultPapelShouldBeFound("permissionsPapelId.equals=" + permissionsPapelId);

        // Get all the papelList where permissionsPapel equals to (permissionsPapelId + 1)
        defaultPapelShouldNotBeFound("permissionsPapelId.equals=" + (permissionsPapelId + 1));
    }

    @Test
    @Transactional
    void getAllPapelsByUsuarioPapelIsEqualToSomething() throws Exception {
        UsuarioPapel usuarioPapel;
        if (TestUtil.findAll(em, UsuarioPapel.class).isEmpty()) {
            papelRepository.saveAndFlush(papel);
            usuarioPapel = UsuarioPapelResourceIT.createEntity(em);
        } else {
            usuarioPapel = TestUtil.findAll(em, UsuarioPapel.class).get(0);
        }
        em.persist(usuarioPapel);
        em.flush();
        papel.addUsuarioPapel(usuarioPapel);
        papelRepository.saveAndFlush(papel);
        Long usuarioPapelId = usuarioPapel.getId();

        // Get all the papelList where usuarioPapel equals to usuarioPapelId
        defaultPapelShouldBeFound("usuarioPapelId.equals=" + usuarioPapelId);

        // Get all the papelList where usuarioPapel equals to (usuarioPapelId + 1)
        defaultPapelShouldNotBeFound("usuarioPapelId.equals=" + (usuarioPapelId + 1));
    }

    @Test
    @Transactional
    void getAllPapelsByAppIsEqualToSomething() throws Exception {
        App app;
        if (TestUtil.findAll(em, App.class).isEmpty()) {
            papelRepository.saveAndFlush(papel);
            app = AppResourceIT.createEntity(em);
        } else {
            app = TestUtil.findAll(em, App.class).get(0);
        }
        em.persist(app);
        em.flush();
        papel.setApp(app);
        papelRepository.saveAndFlush(papel);
        Long appId = app.getId();

        // Get all the papelList where app equals to appId
        defaultPapelShouldBeFound("appId.equals=" + appId);

        // Get all the papelList where app equals to (appId + 1)
        defaultPapelShouldNotBeFound("appId.equals=" + (appId + 1));
    }

    @Test
    @Transactional
    void getAllPapelsByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            papelRepository.saveAndFlush(papel);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        papel.setUsuario(usuario);
        papelRepository.saveAndFlush(papel);
        Long usuarioId = usuario.getId();

        // Get all the papelList where usuario equals to usuarioId
        defaultPapelShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the papelList where usuario equals to (usuarioId + 1)
        defaultPapelShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPapelShouldBeFound(String filter) throws Exception {
        restPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(papel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarApp").value(hasItem(DEFAULT_IDN_VAR_APP)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));

        // Check, that the count call also returns 1
        restPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPapelShouldNotBeFound(String filter) throws Exception {
        restPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPapel() throws Exception {
        // Get the papel
        restPapelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPapel() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        int databaseSizeBeforeUpdate = papelRepository.findAll().size();

        // Update the papel
        Papel updatedPapel = papelRepository.findById(papel.getId()).get();
        // Disconnect from session so that the updates on updatedPapel are not directly saved in db
        em.detach(updatedPapel);
        updatedPapel
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        PapelDTO papelDTO = papelMapper.toDto(updatedPapel);

        restPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, papelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(papelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void putNonExistingPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().size();
        papel.setId(count.incrementAndGet());

        // Create the Papel
        PapelDTO papelDTO = papelMapper.toDto(papel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, papelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(papelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().size();
        papel.setId(count.incrementAndGet());

        // Create the Papel
        PapelDTO papelDTO = papelMapper.toDto(papel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(papelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().size();
        papel.setId(count.incrementAndGet());

        // Create the Papel
        PapelDTO papelDTO = papelMapper.toDto(papel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPapelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePapelWithPatch() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        int databaseSizeBeforeUpdate = papelRepository.findAll().size();

        // Update the papel using partial update
        Papel partialUpdatedPapel = new Papel();
        partialUpdatedPapel.setId(papel.getId());

        partialUpdatedPapel.idnVarPapel(UPDATED_IDN_VAR_PAPEL).nVarNome(UPDATED_N_VAR_NOME);

        restPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPapel))
            )
            .andExpect(status().isOk());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(DEFAULT_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void fullUpdatePapelWithPatch() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        int databaseSizeBeforeUpdate = papelRepository.findAll().size();

        // Update the papel using partial update
        Papel partialUpdatedPapel = new Papel();
        partialUpdatedPapel.setId(papel.getId());

        partialUpdatedPapel
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarApp(UPDATED_IDN_VAR_APP)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPapel))
            )
            .andExpect(status().isOk());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
        Papel testPapel = papelList.get(papelList.size() - 1);
        assertThat(testPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testPapel.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testPapel.getIdnVarApp()).isEqualTo(UPDATED_IDN_VAR_APP);
        assertThat(testPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void patchNonExistingPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().size();
        papel.setId(count.incrementAndGet());

        // Create the Papel
        PapelDTO papelDTO = papelMapper.toDto(papel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, papelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(papelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().size();
        papel.setId(count.incrementAndGet());

        // Create the Papel
        PapelDTO papelDTO = papelMapper.toDto(papel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(papelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPapel() throws Exception {
        int databaseSizeBeforeUpdate = papelRepository.findAll().size();
        papel.setId(count.incrementAndGet());

        // Create the Papel
        PapelDTO papelDTO = papelMapper.toDto(papel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPapelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(papelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Papel in the database
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePapel() throws Exception {
        // Initialize the database
        papelRepository.saveAndFlush(papel);

        int databaseSizeBeforeDelete = papelRepository.findAll().size();

        // Delete the papel
        restPapelMockMvc
            .perform(delete(ENTITY_API_URL_ID, papel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Papel> papelList = papelRepository.findAll();
        assertThat(papelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
