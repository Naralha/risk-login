package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioGrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioGrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioGrupoMapper;
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
 * Integration tests for the {@link UsuarioGrupoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioGrupoResourceIT {

    private static final String DEFAULT_IDN_VAR_USUARIO_CADASTRADO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO_CADASTRADO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_GRUPO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuario-grupos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioGrupoRepository usuarioGrupoRepository;

    @Autowired
    private UsuarioGrupoMapper usuarioGrupoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioGrupoMockMvc;

    private UsuarioGrupo usuarioGrupo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioGrupo createEntity(EntityManager em) {
        UsuarioGrupo usuarioGrupo = new UsuarioGrupo()
            .idnVarUsuarioCadastrado(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(DEFAULT_IDN_VAR_GRUPO)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return usuarioGrupo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioGrupo createUpdatedEntity(EntityManager em) {
        UsuarioGrupo usuarioGrupo = new UsuarioGrupo()
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return usuarioGrupo;
    }

    @BeforeEach
    public void initTest() {
        usuarioGrupo = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuarioGrupo() throws Exception {
        int databaseSizeBeforeCreate = usuarioGrupoRepository.findAll().size();
        // Create the UsuarioGrupo
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);
        restUsuarioGrupoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeCreate + 1);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(DEFAULT_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void createUsuarioGrupoWithExistingId() throws Exception {
        // Create the UsuarioGrupo with an existing ID
        usuarioGrupo.setId(1L);
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        int databaseSizeBeforeCreate = usuarioGrupoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioGrupoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioCadastradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioGrupoRepository.findAll().size();
        // set the field null
        usuarioGrupo.setIdnVarUsuarioCadastrado(null);

        // Create the UsuarioGrupo, which fails.
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        restUsuarioGrupoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarGrupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioGrupoRepository.findAll().size();
        // set the field null
        usuarioGrupo.setIdnVarGrupo(null);

        // Create the UsuarioGrupo, which fails.
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        restUsuarioGrupoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioGrupoRepository.findAll().size();
        // set the field null
        usuarioGrupo.setIdnVarUsuario(null);

        // Create the UsuarioGrupo, which fails.
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        restUsuarioGrupoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarioGrupos() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList
        restUsuarioGrupoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioGrupo.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarUsuarioCadastrado").value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)))
            .andExpect(jsonPath("$.[*].idnVarGrupo").value(hasItem(DEFAULT_IDN_VAR_GRUPO)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));
    }

    @Test
    @Transactional
    void getUsuarioGrupo() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get the usuarioGrupo
        restUsuarioGrupoMockMvc
            .perform(get(ENTITY_API_URL_ID, usuarioGrupo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuarioGrupo.getId().intValue()))
            .andExpect(jsonPath("$.idnVarUsuarioCadastrado").value(DEFAULT_IDN_VAR_USUARIO_CADASTRADO))
            .andExpect(jsonPath("$.idnVarGrupo").value(DEFAULT_IDN_VAR_GRUPO))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    @Transactional
    void getUsuarioGruposByIdFiltering() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        Long id = usuarioGrupo.getId();

        defaultUsuarioGrupoShouldBeFound("id.equals=" + id);
        defaultUsuarioGrupoShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioGrupoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioGrupoShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioGrupoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioGrupoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioCadastradoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado equals to DEFAULT_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldBeFound("idnVarUsuarioCadastrado.equals=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado equals to UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuarioCadastrado.equals=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioCadastradoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado in DEFAULT_IDN_VAR_USUARIO_CADASTRADO or UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldBeFound(
            "idnVarUsuarioCadastrado.in=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO + "," + UPDATED_IDN_VAR_USUARIO_CADASTRADO
        );

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado equals to UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuarioCadastrado.in=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioCadastradoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado is not null
        defaultUsuarioGrupoShouldBeFound("idnVarUsuarioCadastrado.specified=true");

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado is null
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuarioCadastrado.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioCadastradoContainsSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado contains DEFAULT_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldBeFound("idnVarUsuarioCadastrado.contains=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado contains UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuarioCadastrado.contains=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioCadastradoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado does not contain DEFAULT_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuarioCadastrado.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO);

        // Get all the usuarioGrupoList where idnVarUsuarioCadastrado does not contain UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioGrupoShouldBeFound("idnVarUsuarioCadastrado.doesNotContain=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarGrupoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarGrupo equals to DEFAULT_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldBeFound("idnVarGrupo.equals=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the usuarioGrupoList where idnVarGrupo equals to UPDATED_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldNotBeFound("idnVarGrupo.equals=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarGrupoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarGrupo in DEFAULT_IDN_VAR_GRUPO or UPDATED_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldBeFound("idnVarGrupo.in=" + DEFAULT_IDN_VAR_GRUPO + "," + UPDATED_IDN_VAR_GRUPO);

        // Get all the usuarioGrupoList where idnVarGrupo equals to UPDATED_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldNotBeFound("idnVarGrupo.in=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarGrupoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarGrupo is not null
        defaultUsuarioGrupoShouldBeFound("idnVarGrupo.specified=true");

        // Get all the usuarioGrupoList where idnVarGrupo is null
        defaultUsuarioGrupoShouldNotBeFound("idnVarGrupo.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarGrupoContainsSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarGrupo contains DEFAULT_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldBeFound("idnVarGrupo.contains=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the usuarioGrupoList where idnVarGrupo contains UPDATED_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldNotBeFound("idnVarGrupo.contains=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarGrupoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarGrupo does not contain DEFAULT_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldNotBeFound("idnVarGrupo.doesNotContain=" + DEFAULT_IDN_VAR_GRUPO);

        // Get all the usuarioGrupoList where idnVarGrupo does not contain UPDATED_IDN_VAR_GRUPO
        defaultUsuarioGrupoShouldBeFound("idnVarGrupo.doesNotContain=" + UPDATED_IDN_VAR_GRUPO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioGrupoList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the usuarioGrupoList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuario is not null
        defaultUsuarioGrupoShouldBeFound("idnVarUsuario.specified=true");

        // Get all the usuarioGrupoList where idnVarUsuario is null
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioGrupoList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        // Get all the usuarioGrupoList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioGrupoList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultUsuarioGrupoShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByGrupoIsEqualToSomething() throws Exception {
        Grupo grupo;
        if (TestUtil.findAll(em, Grupo.class).isEmpty()) {
            usuarioGrupoRepository.saveAndFlush(usuarioGrupo);
            grupo = GrupoResourceIT.createEntity(em);
        } else {
            grupo = TestUtil.findAll(em, Grupo.class).get(0);
        }
        em.persist(grupo);
        em.flush();
        usuarioGrupo.setGrupo(grupo);
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);
        Long grupoId = grupo.getId();

        // Get all the usuarioGrupoList where grupo equals to grupoId
        defaultUsuarioGrupoShouldBeFound("grupoId.equals=" + grupoId);

        // Get all the usuarioGrupoList where grupo equals to (grupoId + 1)
        defaultUsuarioGrupoShouldNotBeFound("grupoId.equals=" + (grupoId + 1));
    }

    @Test
    @Transactional
    void getAllUsuarioGruposByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuarioGrupoRepository.saveAndFlush(usuarioGrupo);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        usuarioGrupo.setUsuario(usuario);
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);
        Long usuarioId = usuario.getId();

        // Get all the usuarioGrupoList where usuario equals to usuarioId
        defaultUsuarioGrupoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the usuarioGrupoList where usuario equals to (usuarioId + 1)
        defaultUsuarioGrupoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioGrupoShouldBeFound(String filter) throws Exception {
        restUsuarioGrupoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioGrupo.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarUsuarioCadastrado").value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)))
            .andExpect(jsonPath("$.[*].idnVarGrupo").value(hasItem(DEFAULT_IDN_VAR_GRUPO)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));

        // Check, that the count call also returns 1
        restUsuarioGrupoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioGrupoShouldNotBeFound(String filter) throws Exception {
        restUsuarioGrupoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioGrupoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuarioGrupo() throws Exception {
        // Get the usuarioGrupo
        restUsuarioGrupoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuarioGrupo() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();

        // Update the usuarioGrupo
        UsuarioGrupo updatedUsuarioGrupo = usuarioGrupoRepository.findById(usuarioGrupo.getId()).get();
        // Disconnect from session so that the updates on updatedUsuarioGrupo are not directly saved in db
        em.detach(updatedUsuarioGrupo);
        updatedUsuarioGrupo
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(updatedUsuarioGrupo);

        restUsuarioGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioGrupoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void putNonExistingUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // Create the UsuarioGrupo
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioGrupoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // Create the UsuarioGrupo
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioGrupoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // Create the UsuarioGrupo
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioGrupoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioGrupoWithPatch() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();

        // Update the usuarioGrupo using partial update
        UsuarioGrupo partialUpdatedUsuarioGrupo = new UsuarioGrupo();
        partialUpdatedUsuarioGrupo.setId(usuarioGrupo.getId());

        partialUpdatedUsuarioGrupo.idnVarGrupo(UPDATED_IDN_VAR_GRUPO).idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restUsuarioGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioGrupo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioGrupo))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioGrupoWithPatch() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();

        // Update the usuarioGrupo using partial update
        UsuarioGrupo partialUpdatedUsuarioGrupo = new UsuarioGrupo();
        partialUpdatedUsuarioGrupo.setId(usuarioGrupo.getId());

        partialUpdatedUsuarioGrupo
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarGrupo(UPDATED_IDN_VAR_GRUPO)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restUsuarioGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioGrupo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioGrupo))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
        UsuarioGrupo testUsuarioGrupo = usuarioGrupoList.get(usuarioGrupoList.size() - 1);
        assertThat(testUsuarioGrupo.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioGrupo.getIdnVarGrupo()).isEqualTo(UPDATED_IDN_VAR_GRUPO);
        assertThat(testUsuarioGrupo.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // Create the UsuarioGrupo
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioGrupoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // Create the UsuarioGrupo
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuarioGrupo() throws Exception {
        int databaseSizeBeforeUpdate = usuarioGrupoRepository.findAll().size();
        usuarioGrupo.setId(count.incrementAndGet());

        // Create the UsuarioGrupo
        UsuarioGrupoDTO usuarioGrupoDTO = usuarioGrupoMapper.toDto(usuarioGrupo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioGrupoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioGrupoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioGrupo in the database
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuarioGrupo() throws Exception {
        // Initialize the database
        usuarioGrupoRepository.saveAndFlush(usuarioGrupo);

        int databaseSizeBeforeDelete = usuarioGrupoRepository.findAll().size();

        // Delete the usuarioGrupo
        restUsuarioGrupoMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuarioGrupo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UsuarioGrupo> usuarioGrupoList = usuarioGrupoRepository.findAll();
        assertThat(usuarioGrupoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
