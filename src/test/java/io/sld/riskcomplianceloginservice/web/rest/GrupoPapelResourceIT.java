package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoPapelMapper;
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
 * Integration tests for the {@link GrupoPapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GrupoPapelResourceIT {

    private static final String DEFAULT_IDN_VAR_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_GRUPO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_PAPEL = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PAPEL = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grupo-papels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GrupoPapelRepository grupoPapelRepository;

    @Autowired
    private GrupoPapelMapper grupoPapelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGrupoPapelMockMvc;

    private GrupoPapel grupoPapel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupoPapel createEntity(EntityManager em) {
        GrupoPapel grupoPapel = new GrupoPapel()
            .idnVarGrupo(DEFAULT_IDN_VAR_GRUPO)
            .idnVarPapel(DEFAULT_IDN_VAR_PAPEL)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA);
        return grupoPapel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupoPapel createUpdatedEntity(EntityManager em) {
        GrupoPapel grupoPapel = new GrupoPapel()
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        return grupoPapel;
    }

    @BeforeEach
    public void initTest() {
        grupoPapel = createEntity(em);
    }

    @Test
    @Transactional
    void createGrupoPapel() throws Exception {
        int databaseSizeBeforeCreate = grupoPapelRepository.findAll().size();
        // Create the GrupoPapel
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);
        restGrupoPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO)))
            .andExpect(status().isCreated());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeCreate + 1);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void createGrupoPapelWithExistingId() throws Exception {
        // Create the GrupoPapel with an existing ID
        grupoPapel.setId(1L);
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        int databaseSizeBeforeCreate = grupoPapelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGrupoPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarGrupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().size();
        // set the field null
        grupoPapel.setIdnVarGrupo(null);

        // Create the GrupoPapel, which fails.
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        restGrupoPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO)))
            .andExpect(status().isBadRequest());

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().size();
        // set the field null
        grupoPapel.setIdnVarPapel(null);

        // Create the GrupoPapel, which fails.
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        restGrupoPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO)))
            .andExpect(status().isBadRequest());

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().size();
        // set the field null
        grupoPapel.setIdnVarUsuario(null);

        // Create the GrupoPapel, which fails.
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        restGrupoPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO)))
            .andExpect(status().isBadRequest());

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoPapelRepository.findAll().size();
        // set the field null
        grupoPapel.setIdnVarEmpresa(null);

        // Create the GrupoPapel, which fails.
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        restGrupoPapelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO)))
            .andExpect(status().isBadRequest());

        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGrupoPapels() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList
        restGrupoPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupoPapel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarGrupo").value(hasItem(DEFAULT_IDN_VAR_GRUPO)))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)));
    }

    @Test
    @Transactional
    void getGrupoPapel() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get the grupoPapel
        restGrupoPapelMockMvc
            .perform(get(ENTITY_API_URL_ID, grupoPapel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grupoPapel.getId().intValue()))
            .andExpect(jsonPath("$.idnVarGrupo").value(DEFAULT_IDN_VAR_GRUPO))
            .andExpect(jsonPath("$.idnVarPapel").value(DEFAULT_IDN_VAR_PAPEL))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO))
            .andExpect(jsonPath("$.idnVarEmpresa").value(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    @Transactional
    void getGrupoPapelsByIdFiltering() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        Long id = grupoPapel.getId();

        defaultGrupoPapelShouldBeFound("id.equals=" + id);
        defaultGrupoPapelShouldNotBeFound("id.notEquals=" + id);

        defaultGrupoPapelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGrupoPapelShouldNotBeFound("id.greaterThan=" + id);

        defaultGrupoPapelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGrupoPapelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarGrupoIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarGrupo equals to DEFAULT_IDN_VAR_GRUPO
        defaultGrupoPapelShouldBeFound("idnVarGrupo.equals=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the grupoPapelList where idnVarGrupo equals to UPDATED_IDN_VAR_GRUPO
        defaultGrupoPapelShouldNotBeFound("idnVarGrupo.equals=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarGrupoIsInShouldWork() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarGrupo in DEFAULT_IDN_VAR_GRUPO or UPDATED_IDN_VAR_GRUPO
        defaultGrupoPapelShouldBeFound("idnVarGrupo.in=" + DEFAULT_IDN_VAR_GRUPO + "," + UPDATED_IDN_VAR_GRUPO);

        // Get all the grupoPapelList where idnVarGrupo equals to UPDATED_IDN_VAR_GRUPO
        defaultGrupoPapelShouldNotBeFound("idnVarGrupo.in=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarGrupoIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarGrupo is not null
        defaultGrupoPapelShouldBeFound("idnVarGrupo.specified=true");

        // Get all the grupoPapelList where idnVarGrupo is null
        defaultGrupoPapelShouldNotBeFound("idnVarGrupo.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarGrupoContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarGrupo contains DEFAULT_IDN_VAR_GRUPO
        defaultGrupoPapelShouldBeFound("idnVarGrupo.contains=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the grupoPapelList where idnVarGrupo contains UPDATED_IDN_VAR_GRUPO
        defaultGrupoPapelShouldNotBeFound("idnVarGrupo.contains=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarGrupoNotContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarGrupo does not contain DEFAULT_IDN_VAR_GRUPO
        defaultGrupoPapelShouldNotBeFound("idnVarGrupo.doesNotContain=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the grupoPapelList where idnVarGrupo does not contain UPDATED_IDN_VAR_GRUPO
        defaultGrupoPapelShouldBeFound("idnVarGrupo.doesNotContain=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarPapelIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarPapel equals to DEFAULT_IDN_VAR_PAPEL
        defaultGrupoPapelShouldBeFound("idnVarPapel.equals=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the grupoPapelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultGrupoPapelShouldNotBeFound("idnVarPapel.equals=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarPapelIsInShouldWork() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarPapel in DEFAULT_IDN_VAR_PAPEL or UPDATED_IDN_VAR_PAPEL
        defaultGrupoPapelShouldBeFound("idnVarPapel.in=" + DEFAULT_IDN_VAR_PAPEL + "," + UPDATED_IDN_VAR_PAPEL);

        // Get all the grupoPapelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultGrupoPapelShouldNotBeFound("idnVarPapel.in=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarPapelIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarPapel is not null
        defaultGrupoPapelShouldBeFound("idnVarPapel.specified=true");

        // Get all the grupoPapelList where idnVarPapel is null
        defaultGrupoPapelShouldNotBeFound("idnVarPapel.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarPapelContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarPapel contains DEFAULT_IDN_VAR_PAPEL
        defaultGrupoPapelShouldBeFound("idnVarPapel.contains=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the grupoPapelList where idnVarPapel contains UPDATED_IDN_VAR_PAPEL
        defaultGrupoPapelShouldNotBeFound("idnVarPapel.contains=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarPapelNotContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarPapel does not contain DEFAULT_IDN_VAR_PAPEL
        defaultGrupoPapelShouldNotBeFound("idnVarPapel.doesNotContain=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the grupoPapelList where idnVarPapel does not contain UPDATED_IDN_VAR_PAPEL
        defaultGrupoPapelShouldBeFound("idnVarPapel.doesNotContain=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultGrupoPapelShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the grupoPapelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultGrupoPapelShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultGrupoPapelShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the grupoPapelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultGrupoPapelShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarUsuario is not null
        defaultGrupoPapelShouldBeFound("idnVarUsuario.specified=true");

        // Get all the grupoPapelList where idnVarUsuario is null
        defaultGrupoPapelShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultGrupoPapelShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the grupoPapelList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultGrupoPapelShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultGrupoPapelShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the grupoPapelList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultGrupoPapelShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarEmpresa equals to DEFAULT_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldBeFound("idnVarEmpresa.equals=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the grupoPapelList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldNotBeFound("idnVarEmpresa.equals=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarEmpresaIsInShouldWork() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarEmpresa in DEFAULT_IDN_VAR_EMPRESA or UPDATED_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldBeFound("idnVarEmpresa.in=" + DEFAULT_IDN_VAR_EMPRESA + "," + UPDATED_IDN_VAR_EMPRESA);

        // Get all the grupoPapelList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldNotBeFound("idnVarEmpresa.in=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarEmpresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarEmpresa is not null
        defaultGrupoPapelShouldBeFound("idnVarEmpresa.specified=true");

        // Get all the grupoPapelList where idnVarEmpresa is null
        defaultGrupoPapelShouldNotBeFound("idnVarEmpresa.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarEmpresaContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarEmpresa contains DEFAULT_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldBeFound("idnVarEmpresa.contains=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the grupoPapelList where idnVarEmpresa contains UPDATED_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldNotBeFound("idnVarEmpresa.contains=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByIdnVarEmpresaNotContainsSomething() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        // Get all the grupoPapelList where idnVarEmpresa does not contain DEFAULT_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldNotBeFound("idnVarEmpresa.doesNotContain=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the grupoPapelList where idnVarEmpresa does not contain UPDATED_IDN_VAR_EMPRESA
        defaultGrupoPapelShouldBeFound("idnVarEmpresa.doesNotContain=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByGrupoIsEqualToSomething() throws Exception {
        Grupo grupo;
        if (TestUtil.findAll(em, Grupo.class).isEmpty()) {
            grupoPapelRepository.saveAndFlush(grupoPapel);
            grupo = GrupoResourceIT.createEntity(em);
        } else {
            grupo = TestUtil.findAll(em, Grupo.class).get(0);
        }
        em.persist(grupo);
        em.flush();
        grupoPapel.setGrupo(grupo);
        grupoPapelRepository.saveAndFlush(grupoPapel);
        Long grupoId = grupo.getId();

        // Get all the grupoPapelList where grupo equals to grupoId
        defaultGrupoPapelShouldBeFound("grupoId.equals=" + grupoId);

        // Get all the grupoPapelList where grupo equals to (grupoId + 1)
        defaultGrupoPapelShouldNotBeFound("grupoId.equals=" + (grupoId + 1));
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByPapelIsEqualToSomething() throws Exception {
        Papel papel;
        if (TestUtil.findAll(em, Papel.class).isEmpty()) {
            grupoPapelRepository.saveAndFlush(grupoPapel);
            papel = PapelResourceIT.createEntity(em);
        } else {
            papel = TestUtil.findAll(em, Papel.class).get(0);
        }
        em.persist(papel);
        em.flush();
        grupoPapel.setPapel(papel);
        grupoPapelRepository.saveAndFlush(grupoPapel);
        Long papelId = papel.getId();

        // Get all the grupoPapelList where papel equals to papelId
        defaultGrupoPapelShouldBeFound("papelId.equals=" + papelId);

        // Get all the grupoPapelList where papel equals to (papelId + 1)
        defaultGrupoPapelShouldNotBeFound("papelId.equals=" + (papelId + 1));
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByEmpresaIsEqualToSomething() throws Exception {
        Empresa empresa;
        if (TestUtil.findAll(em, Empresa.class).isEmpty()) {
            grupoPapelRepository.saveAndFlush(grupoPapel);
            empresa = EmpresaResourceIT.createEntity(em);
        } else {
            empresa = TestUtil.findAll(em, Empresa.class).get(0);
        }
        em.persist(empresa);
        em.flush();
        grupoPapel.setEmpresa(empresa);
        grupoPapelRepository.saveAndFlush(grupoPapel);
        Long empresaId = empresa.getId();

        // Get all the grupoPapelList where empresa equals to empresaId
        defaultGrupoPapelShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the grupoPapelList where empresa equals to (empresaId + 1)
        defaultGrupoPapelShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllGrupoPapelsByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            grupoPapelRepository.saveAndFlush(grupoPapel);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        grupoPapel.setUsuario(usuario);
        grupoPapelRepository.saveAndFlush(grupoPapel);
        Long usuarioId = usuario.getId();

        // Get all the grupoPapelList where usuario equals to usuarioId
        defaultGrupoPapelShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the grupoPapelList where usuario equals to (usuarioId + 1)
        defaultGrupoPapelShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGrupoPapelShouldBeFound(String filter) throws Exception {
        restGrupoPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupoPapel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarGrupo").value(hasItem(DEFAULT_IDN_VAR_GRUPO)))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)));

        // Check, that the count call also returns 1
        restGrupoPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGrupoPapelShouldNotBeFound(String filter) throws Exception {
        restGrupoPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGrupoPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGrupoPapel() throws Exception {
        // Get the grupoPapel
        restGrupoPapelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGrupoPapel() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();

        // Update the grupoPapel
        GrupoPapel updatedGrupoPapel = grupoPapelRepository.findById(grupoPapel.getId()).get();
        // Disconnect from session so that the updates on updatedGrupoPapel are not directly saved in db
        em.detach(updatedGrupoPapel);
        updatedGrupoPapel
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(updatedGrupoPapel);

        restGrupoPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupoPapelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO))
            )
            .andExpect(status().isOk());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void putNonExistingGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();
        grupoPapel.setId(count.incrementAndGet());

        // Create the GrupoPapel
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupoPapelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();
        grupoPapel.setId(count.incrementAndGet());

        // Create the GrupoPapel
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();
        grupoPapel.setId(count.incrementAndGet());

        // Create the GrupoPapel
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoPapelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGrupoPapelWithPatch() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();

        // Update the grupoPapel using partial update
        GrupoPapel partialUpdatedGrupoPapel = new GrupoPapel();
        partialUpdatedGrupoPapel.setId(grupoPapel.getId());

        partialUpdatedGrupoPapel.idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        restGrupoPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupoPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupoPapel))
            )
            .andExpect(status().isOk());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void fullUpdateGrupoPapelWithPatch() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();

        // Update the grupoPapel using partial update
        GrupoPapel partialUpdatedGrupoPapel = new GrupoPapel();
        partialUpdatedGrupoPapel.setId(grupoPapel.getId());

        partialUpdatedGrupoPapel
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        restGrupoPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupoPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupoPapel))
            )
            .andExpect(status().isOk());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
        GrupoPapel testGrupoPapel = grupoPapelList.get(grupoPapelList.size() - 1);
        assertThat(testGrupoPapel.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupoPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testGrupoPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupoPapel.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void patchNonExistingGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();
        grupoPapel.setId(count.incrementAndGet());

        // Create the GrupoPapel
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grupoPapelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();
        grupoPapel.setId(count.incrementAndGet());

        // Create the GrupoPapel
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrupoPapel() throws Exception {
        int databaseSizeBeforeUpdate = grupoPapelRepository.findAll().size();
        grupoPapel.setId(count.incrementAndGet());

        // Create the GrupoPapel
        GrupoPapelDTO grupoPapelDTO = grupoPapelMapper.toDto(grupoPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoPapelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(grupoPapelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupoPapel in the database
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrupoPapel() throws Exception {
        // Initialize the database
        grupoPapelRepository.saveAndFlush(grupoPapel);

        int databaseSizeBeforeDelete = grupoPapelRepository.findAll().size();

        // Delete the grupoPapel
        restGrupoPapelMockMvc
            .perform(delete(ENTITY_API_URL_ID, grupoPapel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GrupoPapel> grupoPapelList = grupoPapelRepository.findAll();
        assertThat(grupoPapelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
