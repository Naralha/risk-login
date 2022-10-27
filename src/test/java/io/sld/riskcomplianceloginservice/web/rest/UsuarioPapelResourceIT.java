package io.sld.riskcomplianceloginservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.sld.riskcomplianceloginservice.IntegrationTest;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioPapelMapper;
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
 * Integration tests for the {@link UsuarioPapelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioPapelResourceIT {

    private static final String DEFAULT_IDN_VAR_USUARIO_CADASTRADO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO_CADASTRADO = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_PAPEL = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_PAPEL = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuario-papels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioPapelRepository usuarioPapelRepository;

    @Autowired
    private UsuarioPapelMapper usuarioPapelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioPapelMockMvc;

    private UsuarioPapel usuarioPapel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioPapel createEntity(EntityManager em) {
        UsuarioPapel usuarioPapel = new UsuarioPapel()
            .idnVarUsuarioCadastrado(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(DEFAULT_IDN_VAR_PAPEL)
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO);
        return usuarioPapel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UsuarioPapel createUpdatedEntity(EntityManager em) {
        UsuarioPapel usuarioPapel = new UsuarioPapel()
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        return usuarioPapel;
    }

    @BeforeEach
    public void initTest() {
        usuarioPapel = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuarioPapel() throws Exception {
        int databaseSizeBeforeCreate = usuarioPapelRepository.findAll().size();
        // Create the UsuarioPapel
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);
        restUsuarioPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeCreate + 1);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(DEFAULT_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void createUsuarioPapelWithExistingId() throws Exception {
        // Create the UsuarioPapel with an existing ID
        usuarioPapel.setId(1L);
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        int databaseSizeBeforeCreate = usuarioPapelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioCadastradoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioPapelRepository.findAll().size();
        // set the field null
        usuarioPapel.setIdnVarUsuarioCadastrado(null);

        // Create the UsuarioPapel, which fails.
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        restUsuarioPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarPapelIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioPapelRepository.findAll().size();
        // set the field null
        usuarioPapel.setIdnVarPapel(null);

        // Create the UsuarioPapel, which fails.
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        restUsuarioPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioPapelRepository.findAll().size();
        // set the field null
        usuarioPapel.setIdnVarUsuario(null);

        // Create the UsuarioPapel, which fails.
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        restUsuarioPapelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarioPapels() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList
        restUsuarioPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioPapel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarUsuarioCadastrado").value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));
    }

    @Test
    @Transactional
    void getUsuarioPapel() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get the usuarioPapel
        restUsuarioPapelMockMvc
            .perform(get(ENTITY_API_URL_ID, usuarioPapel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuarioPapel.getId().intValue()))
            .andExpect(jsonPath("$.idnVarUsuarioCadastrado").value(DEFAULT_IDN_VAR_USUARIO_CADASTRADO))
            .andExpect(jsonPath("$.idnVarPapel").value(DEFAULT_IDN_VAR_PAPEL))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO));
    }

    @Test
    @Transactional
    void getUsuarioPapelsByIdFiltering() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        Long id = usuarioPapel.getId();

        defaultUsuarioPapelShouldBeFound("id.equals=" + id);
        defaultUsuarioPapelShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioPapelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioPapelShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioPapelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioPapelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioCadastradoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado equals to DEFAULT_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldBeFound("idnVarUsuarioCadastrado.equals=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado equals to UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuarioCadastrado.equals=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioCadastradoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado in DEFAULT_IDN_VAR_USUARIO_CADASTRADO or UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldBeFound(
            "idnVarUsuarioCadastrado.in=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO + "," + UPDATED_IDN_VAR_USUARIO_CADASTRADO
        );

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado equals to UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuarioCadastrado.in=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioCadastradoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado is not null
        defaultUsuarioPapelShouldBeFound("idnVarUsuarioCadastrado.specified=true");

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado is null
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuarioCadastrado.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioCadastradoContainsSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado contains DEFAULT_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldBeFound("idnVarUsuarioCadastrado.contains=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado contains UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuarioCadastrado.contains=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioCadastradoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado does not contain DEFAULT_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuarioCadastrado.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO_CADASTRADO);

        // Get all the usuarioPapelList where idnVarUsuarioCadastrado does not contain UPDATED_IDN_VAR_USUARIO_CADASTRADO
        defaultUsuarioPapelShouldBeFound("idnVarUsuarioCadastrado.doesNotContain=" + UPDATED_IDN_VAR_USUARIO_CADASTRADO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarPapelIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarPapel equals to DEFAULT_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldBeFound("idnVarPapel.equals=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the usuarioPapelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldNotBeFound("idnVarPapel.equals=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarPapelIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarPapel in DEFAULT_IDN_VAR_PAPEL or UPDATED_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldBeFound("idnVarPapel.in=" + DEFAULT_IDN_VAR_PAPEL + "," + UPDATED_IDN_VAR_PAPEL);

        // Get all the usuarioPapelList where idnVarPapel equals to UPDATED_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldNotBeFound("idnVarPapel.in=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarPapelIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarPapel is not null
        defaultUsuarioPapelShouldBeFound("idnVarPapel.specified=true");

        // Get all the usuarioPapelList where idnVarPapel is null
        defaultUsuarioPapelShouldNotBeFound("idnVarPapel.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarPapelContainsSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarPapel contains DEFAULT_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldBeFound("idnVarPapel.contains=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the usuarioPapelList where idnVarPapel contains UPDATED_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldNotBeFound("idnVarPapel.contains=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarPapelNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarPapel does not contain DEFAULT_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldNotBeFound("idnVarPapel.doesNotContain=" + DEFAULT_IDN_VAR_PAPEL);

        // Get all the usuarioPapelList where idnVarPapel does not contain UPDATED_IDN_VAR_PAPEL
        defaultUsuarioPapelShouldBeFound("idnVarPapel.doesNotContain=" + UPDATED_IDN_VAR_PAPEL);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioPapelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the usuarioPapelList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuario is not null
        defaultUsuarioPapelShouldBeFound("idnVarUsuario.specified=true");

        // Get all the usuarioPapelList where idnVarUsuario is null
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioPapelList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        // Get all the usuarioPapelList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioPapelList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultUsuarioPapelShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByPapelIsEqualToSomething() throws Exception {
        Papel papel;
        if (TestUtil.findAll(em, Papel.class).isEmpty()) {
            usuarioPapelRepository.saveAndFlush(usuarioPapel);
            papel = PapelResourceIT.createEntity(em);
        } else {
            papel = TestUtil.findAll(em, Papel.class).get(0);
        }
        em.persist(papel);
        em.flush();
        usuarioPapel.setPapel(papel);
        usuarioPapelRepository.saveAndFlush(usuarioPapel);
        Long papelId = papel.getId();

        // Get all the usuarioPapelList where papel equals to papelId
        defaultUsuarioPapelShouldBeFound("papelId.equals=" + papelId);

        // Get all the usuarioPapelList where papel equals to (papelId + 1)
        defaultUsuarioPapelShouldNotBeFound("papelId.equals=" + (papelId + 1));
    }

    @Test
    @Transactional
    void getAllUsuarioPapelsByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuarioPapelRepository.saveAndFlush(usuarioPapel);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        usuarioPapel.setUsuario(usuario);
        usuarioPapelRepository.saveAndFlush(usuarioPapel);
        Long usuarioId = usuario.getId();

        // Get all the usuarioPapelList where usuario equals to usuarioId
        defaultUsuarioPapelShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the usuarioPapelList where usuario equals to (usuarioId + 1)
        defaultUsuarioPapelShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioPapelShouldBeFound(String filter) throws Exception {
        restUsuarioPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuarioPapel.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarUsuarioCadastrado").value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRADO)))
            .andExpect(jsonPath("$.[*].idnVarPapel").value(hasItem(DEFAULT_IDN_VAR_PAPEL)))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)));

        // Check, that the count call also returns 1
        restUsuarioPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioPapelShouldNotBeFound(String filter) throws Exception {
        restUsuarioPapelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioPapelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuarioPapel() throws Exception {
        // Get the usuarioPapel
        restUsuarioPapelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuarioPapel() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();

        // Update the usuarioPapel
        UsuarioPapel updatedUsuarioPapel = usuarioPapelRepository.findById(usuarioPapel.getId()).get();
        // Disconnect from session so that the updates on updatedUsuarioPapel are not directly saved in db
        em.detach(updatedUsuarioPapel);
        updatedUsuarioPapel
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(updatedUsuarioPapel);

        restUsuarioPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioPapelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void putNonExistingUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();
        usuarioPapel.setId(count.incrementAndGet());

        // Create the UsuarioPapel
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioPapelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();
        usuarioPapel.setId(count.incrementAndGet());

        // Create the UsuarioPapel
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioPapelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();
        usuarioPapel.setId(count.incrementAndGet());

        // Create the UsuarioPapel
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioPapelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioPapelWithPatch() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();

        // Update the usuarioPapel using partial update
        UsuarioPapel partialUpdatedUsuarioPapel = new UsuarioPapel();
        partialUpdatedUsuarioPapel.setId(usuarioPapel.getId());

        partialUpdatedUsuarioPapel.idnVarPapel(UPDATED_IDN_VAR_PAPEL);

        restUsuarioPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioPapel))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioPapelWithPatch() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();

        // Update the usuarioPapel using partial update
        UsuarioPapel partialUpdatedUsuarioPapel = new UsuarioPapel();
        partialUpdatedUsuarioPapel.setId(usuarioPapel.getId());

        partialUpdatedUsuarioPapel
            .idnVarUsuarioCadastrado(UPDATED_IDN_VAR_USUARIO_CADASTRADO)
            .idnVarPapel(UPDATED_IDN_VAR_PAPEL)
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO);

        restUsuarioPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuarioPapel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuarioPapel))
            )
            .andExpect(status().isOk());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
        UsuarioPapel testUsuarioPapel = usuarioPapelList.get(usuarioPapelList.size() - 1);
        assertThat(testUsuarioPapel.getIdnVarUsuarioCadastrado()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRADO);
        assertThat(testUsuarioPapel.getIdnVarPapel()).isEqualTo(UPDATED_IDN_VAR_PAPEL);
        assertThat(testUsuarioPapel.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();
        usuarioPapel.setId(count.incrementAndGet());

        // Create the UsuarioPapel
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioPapelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();
        usuarioPapel.setId(count.incrementAndGet());

        // Create the UsuarioPapel
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioPapelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuarioPapel() throws Exception {
        int databaseSizeBeforeUpdate = usuarioPapelRepository.findAll().size();
        usuarioPapel.setId(count.incrementAndGet());

        // Create the UsuarioPapel
        UsuarioPapelDTO usuarioPapelDTO = usuarioPapelMapper.toDto(usuarioPapel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioPapelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioPapelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UsuarioPapel in the database
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuarioPapel() throws Exception {
        // Initialize the database
        usuarioPapelRepository.saveAndFlush(usuarioPapel);

        int databaseSizeBeforeDelete = usuarioPapelRepository.findAll().size();

        // Delete the usuarioPapel
        restUsuarioPapelMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuarioPapel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UsuarioPapel> usuarioPapelList = usuarioPapelRepository.findAll();
        assertThat(usuarioPapelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
