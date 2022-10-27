package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.Empresa;
import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoMapper;
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
 * Integration tests for the {@link GrupoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GrupoResourceIT {

    private static final String DEFAULT_IDN_VAR_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_GRUPO = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grupos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private GrupoMapper grupoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGrupoMockMvc;

    private Grupo grupo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupo createEntity(EntityManager em) {
        Grupo grupo = new Grupo()
            .idnVarGrupo(DEFAULT_IDN_VAR_GRUPO)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA);
        return grupo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grupo createUpdatedEntity(EntityManager em) {
        Grupo grupo = new Grupo()
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        return grupo;
    }

    @BeforeEach
    public void initTest() {
        grupo = createEntity(em);
    }

    @Test
    @Transactional
    void createGrupo() throws Exception {
        int databaseSizeBeforeCreate = grupoRepository.findAll().size();
        // Create the Grupo
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);
        restGrupoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isCreated());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeCreate + 1);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void createGrupoWithExistingId() throws Exception {
        // Create the Grupo with an existing ID
        grupo.setId(1L);
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        int databaseSizeBeforeCreate = grupoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGrupoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarGrupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().size();
        // set the field null
        grupo.setIdnVarGrupo(null);

        // Create the Grupo, which fails.
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        restGrupoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isBadRequest());

        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().size();
        // set the field null
        grupo.setnVarNome(null);

        // Create the Grupo, which fails.
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        restGrupoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isBadRequest());

        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().size();
        // set the field null
        grupo.setIdnVarUsuario(null);

        // Create the Grupo, which fails.
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        restGrupoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isBadRequest());

        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarEmpresaIsRequired() throws Exception {
        int databaseSizeBeforeTest = grupoRepository.findAll().size();
        // set the field null
        grupo.setIdnVarEmpresa(null);

        // Create the Grupo, which fails.
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        restGrupoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isBadRequest());

        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGrupos() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupo.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarGrupo").value(hasItem(DEFAULT_IDN_VAR_GRUPO)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)));
    }

    @Test
    @Transactional
    void getGrupo() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get the grupo
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL_ID, grupo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grupo.getId().intValue()))
            .andExpect(jsonPath("$.idnVarGrupo").value(DEFAULT_IDN_VAR_GRUPO))
            .andExpect(jsonPath("$.nVarNome").value(DEFAULT_N_VAR_NOME))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO))
            .andExpect(jsonPath("$.idnVarEmpresa").value(DEFAULT_IDN_VAR_EMPRESA));
    }

    @Test
    @Transactional
    void getGruposByIdFiltering() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        Long id = grupo.getId();

        defaultGrupoShouldBeFound("id.equals=" + id);
        defaultGrupoShouldNotBeFound("id.notEquals=" + id);

        defaultGrupoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGrupoShouldNotBeFound("id.greaterThan=" + id);

        defaultGrupoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGrupoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarGrupoIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarGrupo equals to DEFAULT_IDN_VAR_GRUPO
        defaultGrupoShouldBeFound("idnVarGrupo.equals=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the grupoList where idnVarGrupo equals to UPDATED_IDN_VAR_GRUPO
        defaultGrupoShouldNotBeFound("idnVarGrupo.equals=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarGrupoIsInShouldWork() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarGrupo in DEFAULT_IDN_VAR_GRUPO or UPDATED_IDN_VAR_GRUPO
        defaultGrupoShouldBeFound("idnVarGrupo.in=" + DEFAULT_IDN_VAR_GRUPO + "," + UPDATED_IDN_VAR_GRUPO);

        // Get all the grupoList where idnVarGrupo equals to UPDATED_IDN_VAR_GRUPO
        defaultGrupoShouldNotBeFound("idnVarGrupo.in=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarGrupoIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarGrupo is not null
        defaultGrupoShouldBeFound("idnVarGrupo.specified=true");

        // Get all the grupoList where idnVarGrupo is null
        defaultGrupoShouldNotBeFound("idnVarGrupo.specified=false");
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarGrupoContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarGrupo contains DEFAULT_IDN_VAR_GRUPO
        defaultGrupoShouldBeFound("idnVarGrupo.contains=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the grupoList where idnVarGrupo contains UPDATED_IDN_VAR_GRUPO
        defaultGrupoShouldNotBeFound("idnVarGrupo.contains=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarGrupoNotContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarGrupo does not contain DEFAULT_IDN_VAR_GRUPO
        defaultGrupoShouldNotBeFound("idnVarGrupo.doesNotContain=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the grupoList where idnVarGrupo does not contain UPDATED_IDN_VAR_GRUPO
        defaultGrupoShouldBeFound("idnVarGrupo.doesNotContain=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllGruposBynVarNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where nVarNome equals to DEFAULT_N_VAR_NOME
        defaultGrupoShouldBeFound("nVarNome.equals=" + DEFAULT_N_VAR_NOME);

        // Get all the grupoList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultGrupoShouldNotBeFound("nVarNome.equals=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllGruposBynVarNomeIsInShouldWork() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where nVarNome in DEFAULT_N_VAR_NOME or UPDATED_N_VAR_NOME
        defaultGrupoShouldBeFound("nVarNome.in=" + DEFAULT_N_VAR_NOME + "," + UPDATED_N_VAR_NOME);

        // Get all the grupoList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultGrupoShouldNotBeFound("nVarNome.in=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllGruposBynVarNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where nVarNome is not null
        defaultGrupoShouldBeFound("nVarNome.specified=true");

        // Get all the grupoList where nVarNome is null
        defaultGrupoShouldNotBeFound("nVarNome.specified=false");
    }

    @Test
    @Transactional
    void getAllGruposBynVarNomeContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where nVarNome contains DEFAULT_N_VAR_NOME
        defaultGrupoShouldBeFound("nVarNome.contains=" + DEFAULT_N_VAR_NOME);

        // Get all the grupoList where nVarNome contains UPDATED_N_VAR_NOME
        defaultGrupoShouldNotBeFound("nVarNome.contains=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllGruposBynVarNomeNotContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where nVarNome does not contain DEFAULT_N_VAR_NOME
        defaultGrupoShouldNotBeFound("nVarNome.doesNotContain=" + DEFAULT_N_VAR_NOME);

        // Get all the grupoList where nVarNome does not contain UPDATED_N_VAR_NOME
        defaultGrupoShouldBeFound("nVarNome.doesNotContain=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultGrupoShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the grupoList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultGrupoShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultGrupoShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the grupoList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultGrupoShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarUsuario is not null
        defaultGrupoShouldBeFound("idnVarUsuario.specified=true");

        // Get all the grupoList where idnVarUsuario is null
        defaultGrupoShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultGrupoShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the grupoList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultGrupoShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultGrupoShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the grupoList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultGrupoShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarEmpresa equals to DEFAULT_IDN_VAR_EMPRESA
        defaultGrupoShouldBeFound("idnVarEmpresa.equals=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the grupoList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultGrupoShouldNotBeFound("idnVarEmpresa.equals=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarEmpresaIsInShouldWork() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarEmpresa in DEFAULT_IDN_VAR_EMPRESA or UPDATED_IDN_VAR_EMPRESA
        defaultGrupoShouldBeFound("idnVarEmpresa.in=" + DEFAULT_IDN_VAR_EMPRESA + "," + UPDATED_IDN_VAR_EMPRESA);

        // Get all the grupoList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultGrupoShouldNotBeFound("idnVarEmpresa.in=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarEmpresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarEmpresa is not null
        defaultGrupoShouldBeFound("idnVarEmpresa.specified=true");

        // Get all the grupoList where idnVarEmpresa is null
        defaultGrupoShouldNotBeFound("idnVarEmpresa.specified=false");
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarEmpresaContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarEmpresa contains DEFAULT_IDN_VAR_EMPRESA
        defaultGrupoShouldBeFound("idnVarEmpresa.contains=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the grupoList where idnVarEmpresa contains UPDATED_IDN_VAR_EMPRESA
        defaultGrupoShouldNotBeFound("idnVarEmpresa.contains=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGruposByIdnVarEmpresaNotContainsSomething() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        // Get all the grupoList where idnVarEmpresa does not contain DEFAULT_IDN_VAR_EMPRESA
        defaultGrupoShouldNotBeFound("idnVarEmpresa.doesNotContain=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the grupoList where idnVarEmpresa does not contain UPDATED_IDN_VAR_EMPRESA
        defaultGrupoShouldBeFound("idnVarEmpresa.doesNotContain=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllGruposByGrupoPapelIsEqualToSomething() throws Exception {
        GrupoPapel grupoPapel;
        if (TestUtil.findAll(em, GrupoPapel.class).isEmpty()) {
            grupoRepository.saveAndFlush(grupo);
            grupoPapel = GrupoPapelResourceIT.createEntity(em);
        } else {
            grupoPapel = TestUtil.findAll(em, GrupoPapel.class).get(0);
        }
        em.persist(grupoPapel);
        em.flush();
        grupo.addGrupoPapel(grupoPapel);
        grupoRepository.saveAndFlush(grupo);
        Long grupoPapelId = grupoPapel.getId();

        // Get all the grupoList where grupoPapel equals to grupoPapelId
        defaultGrupoShouldBeFound("grupoPapelId.equals=" + grupoPapelId);

        // Get all the grupoList where grupoPapel equals to (grupoPapelId + 1)
        defaultGrupoShouldNotBeFound("grupoPapelId.equals=" + (grupoPapelId + 1));
    }

    @Test
    @Transactional
    void getAllGruposByUsuarioGrupoIsEqualToSomething() throws Exception {
        UsuarioGrupo usuarioGrupo;
        if (TestUtil.findAll(em, UsuarioGrupo.class).isEmpty()) {
            grupoRepository.saveAndFlush(grupo);
            usuarioGrupo = UsuarioGrupoResourceIT.createEntity(em);
        } else {
            usuarioGrupo = TestUtil.findAll(em, UsuarioGrupo.class).get(0);
        }
        em.persist(usuarioGrupo);
        em.flush();
        grupo.addUsuarioGrupo(usuarioGrupo);
        grupoRepository.saveAndFlush(grupo);
        Long usuarioGrupoId = usuarioGrupo.getId();

        // Get all the grupoList where usuarioGrupo equals to usuarioGrupoId
        defaultGrupoShouldBeFound("usuarioGrupoId.equals=" + usuarioGrupoId);

        // Get all the grupoList where usuarioGrupo equals to (usuarioGrupoId + 1)
        defaultGrupoShouldNotBeFound("usuarioGrupoId.equals=" + (usuarioGrupoId + 1));
    }

    @Test
    @Transactional
    void getAllGruposByEmpresaIsEqualToSomething() throws Exception {
        Empresa empresa;
        if (TestUtil.findAll(em, Empresa.class).isEmpty()) {
            grupoRepository.saveAndFlush(grupo);
            empresa = EmpresaResourceIT.createEntity(em);
        } else {
            empresa = TestUtil.findAll(em, Empresa.class).get(0);
        }
        em.persist(empresa);
        em.flush();
        grupo.setEmpresa(empresa);
        grupoRepository.saveAndFlush(grupo);
        Long empresaId = empresa.getId();

        // Get all the grupoList where empresa equals to empresaId
        defaultGrupoShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the grupoList where empresa equals to (empresaId + 1)
        defaultGrupoShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    @Test
    @Transactional
    void getAllGruposByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            grupoRepository.saveAndFlush(grupo);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        grupo.setUsuario(usuario);
        grupoRepository.saveAndFlush(grupo);
        Long usuarioId = usuario.getId();

        // Get all the grupoList where usuario equals to usuarioId
        defaultGrupoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the grupoList where usuario equals to (usuarioId + 1)
        defaultGrupoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGrupoShouldBeFound(String filter) throws Exception {
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupo.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarGrupo").value(hasItem(DEFAULT_IDN_VAR_GRUPO)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)));

        // Check, that the count call also returns 1
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGrupoShouldNotBeFound(String filter) throws Exception {
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGrupoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGrupo() throws Exception {
        // Get the grupo
        restGrupoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGrupo() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();

        // Update the grupo
        Grupo updatedGrupo = grupoRepository.findById(grupo.getId()).get();
        // Disconnect from session so that the updates on updatedGrupo are not directly saved in db
        em.detach(updatedGrupo);
        updatedGrupo
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);
        GrupoDTO grupoDTO = grupoMapper.toDto(updatedGrupo);

        restGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void putNonExistingGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();
        grupo.setId(count.incrementAndGet());

        // Create the Grupo
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();
        grupo.setId(count.incrementAndGet());

        // Create the Grupo
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();
        grupo.setId(count.incrementAndGet());

        // Create the Grupo
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGrupoWithPatch() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();

        // Update the grupo using partial update
        Grupo partialUpdatedGrupo = new Grupo();
        partialUpdatedGrupo.setId(grupo.getId());

        partialUpdatedGrupo.idnVarGrupo(UPDATED_IDN_VAR_GRUPO).idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupo))
            )
            .andExpect(status().isOk());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void fullUpdateGrupoWithPatch() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();

        // Update the grupo using partial update
        Grupo partialUpdatedGrupo = new Grupo();
        partialUpdatedGrupo.setId(grupo.getId());

        partialUpdatedGrupo
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA);

        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrupo))
            )
            .andExpect(status().isOk());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
        Grupo testGrupo = grupoList.get(grupoList.size() - 1);
        assertThat(testGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testGrupo.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testGrupo.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void patchNonExistingGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();
        grupo.setId(count.incrementAndGet());

        // Create the Grupo
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grupoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();
        grupo.setId(count.incrementAndGet());

        // Create the Grupo
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrupo() throws Exception {
        int databaseSizeBeforeUpdate = grupoRepository.findAll().size();
        grupo.setId(count.incrementAndGet());

        // Create the Grupo
        GrupoDTO grupoDTO = grupoMapper.toDto(grupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(grupoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grupo in the database
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrupo() throws Exception {
        // Initialize the database
        grupoRepository.saveAndFlush(grupo);

        int databaseSizeBeforeDelete = grupoRepository.findAll().size();

        // Delete the grupo
        restGrupoMockMvc
            .perform(delete(ENTITY_API_URL_ID, grupo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Grupo> grupoList = grupoRepository.findAll();
        assertThat(grupoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
