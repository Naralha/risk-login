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
import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioRepository;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioMapper;
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
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_IDN_VAR_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_IDN_VAR_USUARIO_CADASTRO = "AAAAAAAAAA";
    private static final String UPDATED_IDN_VAR_USUARIO_CADASTRO = "BBBBBBBBBB";

    private static final String DEFAULT_N_VAR_SENHA = "AAAAAAAAAA";
    private static final String UPDATED_N_VAR_SENHA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .idnVarUsuario(DEFAULT_IDN_VAR_USUARIO)
            .nVarNome(DEFAULT_N_VAR_NOME)
            .idnVarEmpresa(DEFAULT_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(DEFAULT_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(DEFAULT_N_VAR_SENHA);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(UPDATED_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(UPDATED_N_VAR_SENHA);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(DEFAULT_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(DEFAULT_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(DEFAULT_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdnVarUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setIdnVarUsuario(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setnVarNome(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checknVarSenhaIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setnVarSenha(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)))
            .andExpect(jsonPath("$.[*].idnVarUsuarioCadastro").value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].nVarSenha").value(hasItem(DEFAULT_N_VAR_SENHA)));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.idnVarUsuario").value(DEFAULT_IDN_VAR_USUARIO))
            .andExpect(jsonPath("$.nVarNome").value(DEFAULT_N_VAR_NOME))
            .andExpect(jsonPath("$.idnVarEmpresa").value(DEFAULT_IDN_VAR_EMPRESA))
            .andExpect(jsonPath("$.idnVarUsuarioCadastro").value(DEFAULT_IDN_VAR_USUARIO_CADASTRO))
            .andExpect(jsonPath("$.nVarSenha").value(DEFAULT_N_VAR_SENHA));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioShouldBeFound("id.equals=" + id);
        defaultUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuario equals to DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioShouldBeFound("idnVarUsuario.equals=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultUsuarioShouldNotBeFound("idnVarUsuario.equals=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuario in DEFAULT_IDN_VAR_USUARIO or UPDATED_IDN_VAR_USUARIO
        defaultUsuarioShouldBeFound("idnVarUsuario.in=" + DEFAULT_IDN_VAR_USUARIO + "," + UPDATED_IDN_VAR_USUARIO);

        // Get all the usuarioList where idnVarUsuario equals to UPDATED_IDN_VAR_USUARIO
        defaultUsuarioShouldNotBeFound("idnVarUsuario.in=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuario is not null
        defaultUsuarioShouldBeFound("idnVarUsuario.specified=true");

        // Get all the usuarioList where idnVarUsuario is null
        defaultUsuarioShouldNotBeFound("idnVarUsuario.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuario contains DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioShouldBeFound("idnVarUsuario.contains=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioList where idnVarUsuario contains UPDATED_IDN_VAR_USUARIO
        defaultUsuarioShouldNotBeFound("idnVarUsuario.contains=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuario does not contain DEFAULT_IDN_VAR_USUARIO
        defaultUsuarioShouldNotBeFound("idnVarUsuario.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO);

        // Get all the usuarioList where idnVarUsuario does not contain UPDATED_IDN_VAR_USUARIO
        defaultUsuarioShouldBeFound("idnVarUsuario.doesNotContain=" + UPDATED_IDN_VAR_USUARIO);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarNome equals to DEFAULT_N_VAR_NOME
        defaultUsuarioShouldBeFound("nVarNome.equals=" + DEFAULT_N_VAR_NOME);

        // Get all the usuarioList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultUsuarioShouldNotBeFound("nVarNome.equals=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarNomeIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarNome in DEFAULT_N_VAR_NOME or UPDATED_N_VAR_NOME
        defaultUsuarioShouldBeFound("nVarNome.in=" + DEFAULT_N_VAR_NOME + "," + UPDATED_N_VAR_NOME);

        // Get all the usuarioList where nVarNome equals to UPDATED_N_VAR_NOME
        defaultUsuarioShouldNotBeFound("nVarNome.in=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarNome is not null
        defaultUsuarioShouldBeFound("nVarNome.specified=true");

        // Get all the usuarioList where nVarNome is null
        defaultUsuarioShouldNotBeFound("nVarNome.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarNomeContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarNome contains DEFAULT_N_VAR_NOME
        defaultUsuarioShouldBeFound("nVarNome.contains=" + DEFAULT_N_VAR_NOME);

        // Get all the usuarioList where nVarNome contains UPDATED_N_VAR_NOME
        defaultUsuarioShouldNotBeFound("nVarNome.contains=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarNomeNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarNome does not contain DEFAULT_N_VAR_NOME
        defaultUsuarioShouldNotBeFound("nVarNome.doesNotContain=" + DEFAULT_N_VAR_NOME);

        // Get all the usuarioList where nVarNome does not contain UPDATED_N_VAR_NOME
        defaultUsuarioShouldBeFound("nVarNome.doesNotContain=" + UPDATED_N_VAR_NOME);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarEmpresa equals to DEFAULT_IDN_VAR_EMPRESA
        defaultUsuarioShouldBeFound("idnVarEmpresa.equals=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the usuarioList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultUsuarioShouldNotBeFound("idnVarEmpresa.equals=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarEmpresaIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarEmpresa in DEFAULT_IDN_VAR_EMPRESA or UPDATED_IDN_VAR_EMPRESA
        defaultUsuarioShouldBeFound("idnVarEmpresa.in=" + DEFAULT_IDN_VAR_EMPRESA + "," + UPDATED_IDN_VAR_EMPRESA);

        // Get all the usuarioList where idnVarEmpresa equals to UPDATED_IDN_VAR_EMPRESA
        defaultUsuarioShouldNotBeFound("idnVarEmpresa.in=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarEmpresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarEmpresa is not null
        defaultUsuarioShouldBeFound("idnVarEmpresa.specified=true");

        // Get all the usuarioList where idnVarEmpresa is null
        defaultUsuarioShouldNotBeFound("idnVarEmpresa.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarEmpresaContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarEmpresa contains DEFAULT_IDN_VAR_EMPRESA
        defaultUsuarioShouldBeFound("idnVarEmpresa.contains=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the usuarioList where idnVarEmpresa contains UPDATED_IDN_VAR_EMPRESA
        defaultUsuarioShouldNotBeFound("idnVarEmpresa.contains=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarEmpresaNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarEmpresa does not contain DEFAULT_IDN_VAR_EMPRESA
        defaultUsuarioShouldNotBeFound("idnVarEmpresa.doesNotContain=" + DEFAULT_IDN_VAR_EMPRESA);

        // Get all the usuarioList where idnVarEmpresa does not contain UPDATED_IDN_VAR_EMPRESA
        defaultUsuarioShouldBeFound("idnVarEmpresa.doesNotContain=" + UPDATED_IDN_VAR_EMPRESA);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioCadastroIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuarioCadastro equals to DEFAULT_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldBeFound("idnVarUsuarioCadastro.equals=" + DEFAULT_IDN_VAR_USUARIO_CADASTRO);

        // Get all the usuarioList where idnVarUsuarioCadastro equals to UPDATED_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldNotBeFound("idnVarUsuarioCadastro.equals=" + UPDATED_IDN_VAR_USUARIO_CADASTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioCadastroIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuarioCadastro in DEFAULT_IDN_VAR_USUARIO_CADASTRO or UPDATED_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldBeFound(
            "idnVarUsuarioCadastro.in=" + DEFAULT_IDN_VAR_USUARIO_CADASTRO + "," + UPDATED_IDN_VAR_USUARIO_CADASTRO
        );

        // Get all the usuarioList where idnVarUsuarioCadastro equals to UPDATED_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldNotBeFound("idnVarUsuarioCadastro.in=" + UPDATED_IDN_VAR_USUARIO_CADASTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioCadastroIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuarioCadastro is not null
        defaultUsuarioShouldBeFound("idnVarUsuarioCadastro.specified=true");

        // Get all the usuarioList where idnVarUsuarioCadastro is null
        defaultUsuarioShouldNotBeFound("idnVarUsuarioCadastro.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioCadastroContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuarioCadastro contains DEFAULT_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldBeFound("idnVarUsuarioCadastro.contains=" + DEFAULT_IDN_VAR_USUARIO_CADASTRO);

        // Get all the usuarioList where idnVarUsuarioCadastro contains UPDATED_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldNotBeFound("idnVarUsuarioCadastro.contains=" + UPDATED_IDN_VAR_USUARIO_CADASTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByIdnVarUsuarioCadastroNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where idnVarUsuarioCadastro does not contain DEFAULT_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldNotBeFound("idnVarUsuarioCadastro.doesNotContain=" + DEFAULT_IDN_VAR_USUARIO_CADASTRO);

        // Get all the usuarioList where idnVarUsuarioCadastro does not contain UPDATED_IDN_VAR_USUARIO_CADASTRO
        defaultUsuarioShouldBeFound("idnVarUsuarioCadastro.doesNotContain=" + UPDATED_IDN_VAR_USUARIO_CADASTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarSenhaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarSenha equals to DEFAULT_N_VAR_SENHA
        defaultUsuarioShouldBeFound("nVarSenha.equals=" + DEFAULT_N_VAR_SENHA);

        // Get all the usuarioList where nVarSenha equals to UPDATED_N_VAR_SENHA
        defaultUsuarioShouldNotBeFound("nVarSenha.equals=" + UPDATED_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarSenhaIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarSenha in DEFAULT_N_VAR_SENHA or UPDATED_N_VAR_SENHA
        defaultUsuarioShouldBeFound("nVarSenha.in=" + DEFAULT_N_VAR_SENHA + "," + UPDATED_N_VAR_SENHA);

        // Get all the usuarioList where nVarSenha equals to UPDATED_N_VAR_SENHA
        defaultUsuarioShouldNotBeFound("nVarSenha.in=" + UPDATED_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarSenhaIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarSenha is not null
        defaultUsuarioShouldBeFound("nVarSenha.specified=true");

        // Get all the usuarioList where nVarSenha is null
        defaultUsuarioShouldNotBeFound("nVarSenha.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarSenhaContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarSenha contains DEFAULT_N_VAR_SENHA
        defaultUsuarioShouldBeFound("nVarSenha.contains=" + DEFAULT_N_VAR_SENHA);

        // Get all the usuarioList where nVarSenha contains UPDATED_N_VAR_SENHA
        defaultUsuarioShouldNotBeFound("nVarSenha.contains=" + UPDATED_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void getAllUsuariosBynVarSenhaNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nVarSenha does not contain DEFAULT_N_VAR_SENHA
        defaultUsuarioShouldNotBeFound("nVarSenha.doesNotContain=" + DEFAULT_N_VAR_SENHA);

        // Get all the usuarioList where nVarSenha does not contain UPDATED_N_VAR_SENHA
        defaultUsuarioShouldBeFound("nVarSenha.doesNotContain=" + UPDATED_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void getAllUsuariosByAppIsEqualToSomething() throws Exception {
        App app;
        if (TestUtil.findAll(em, App.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            app = AppResourceIT.createEntity(em);
        } else {
            app = TestUtil.findAll(em, App.class).get(0);
        }
        em.persist(app);
        em.flush();
        usuario.addApp(app);
        usuarioRepository.saveAndFlush(usuario);
        Long appId = app.getId();

        // Get all the usuarioList where app equals to appId
        defaultUsuarioShouldBeFound("appId.equals=" + appId);

        // Get all the usuarioList where app equals to (appId + 1)
        defaultUsuarioShouldNotBeFound("appId.equals=" + (appId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByAppEmpresaIsEqualToSomething() throws Exception {
        AppEmpresa appEmpresa;
        if (TestUtil.findAll(em, AppEmpresa.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            appEmpresa = AppEmpresaResourceIT.createEntity(em);
        } else {
            appEmpresa = TestUtil.findAll(em, AppEmpresa.class).get(0);
        }
        em.persist(appEmpresa);
        em.flush();
        usuario.addAppEmpresa(appEmpresa);
        usuarioRepository.saveAndFlush(usuario);
        Long appEmpresaId = appEmpresa.getId();

        // Get all the usuarioList where appEmpresa equals to appEmpresaId
        defaultUsuarioShouldBeFound("appEmpresaId.equals=" + appEmpresaId);

        // Get all the usuarioList where appEmpresa equals to (appEmpresaId + 1)
        defaultUsuarioShouldNotBeFound("appEmpresaId.equals=" + (appEmpresaId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByFeaturesIsEqualToSomething() throws Exception {
        Features features;
        if (TestUtil.findAll(em, Features.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            features = FeaturesResourceIT.createEntity(em);
        } else {
            features = TestUtil.findAll(em, Features.class).get(0);
        }
        em.persist(features);
        em.flush();
        usuario.addFeatures(features);
        usuarioRepository.saveAndFlush(usuario);
        Long featuresId = features.getId();

        // Get all the usuarioList where features equals to featuresId
        defaultUsuarioShouldBeFound("featuresId.equals=" + featuresId);

        // Get all the usuarioList where features equals to (featuresId + 1)
        defaultUsuarioShouldNotBeFound("featuresId.equals=" + (featuresId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByGrupoIsEqualToSomething() throws Exception {
        Grupo grupo;
        if (TestUtil.findAll(em, Grupo.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            grupo = GrupoResourceIT.createEntity(em);
        } else {
            grupo = TestUtil.findAll(em, Grupo.class).get(0);
        }
        em.persist(grupo);
        em.flush();
        usuario.addGrupo(grupo);
        usuarioRepository.saveAndFlush(usuario);
        Long grupoId = grupo.getId();

        // Get all the usuarioList where grupo equals to grupoId
        defaultUsuarioShouldBeFound("grupoId.equals=" + grupoId);

        // Get all the usuarioList where grupo equals to (grupoId + 1)
        defaultUsuarioShouldNotBeFound("grupoId.equals=" + (grupoId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByGrupoPapelIsEqualToSomething() throws Exception {
        GrupoPapel grupoPapel;
        if (TestUtil.findAll(em, GrupoPapel.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            grupoPapel = GrupoPapelResourceIT.createEntity(em);
        } else {
            grupoPapel = TestUtil.findAll(em, GrupoPapel.class).get(0);
        }
        em.persist(grupoPapel);
        em.flush();
        usuario.addGrupoPapel(grupoPapel);
        usuarioRepository.saveAndFlush(usuario);
        Long grupoPapelId = grupoPapel.getId();

        // Get all the usuarioList where grupoPapel equals to grupoPapelId
        defaultUsuarioShouldBeFound("grupoPapelId.equals=" + grupoPapelId);

        // Get all the usuarioList where grupoPapel equals to (grupoPapelId + 1)
        defaultUsuarioShouldNotBeFound("grupoPapelId.equals=" + (grupoPapelId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByPermissionsIsEqualToSomething() throws Exception {
        Permissions permissions;
        if (TestUtil.findAll(em, Permissions.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            permissions = PermissionsResourceIT.createEntity(em);
        } else {
            permissions = TestUtil.findAll(em, Permissions.class).get(0);
        }
        em.persist(permissions);
        em.flush();
        usuario.addPermissions(permissions);
        usuarioRepository.saveAndFlush(usuario);
        Long permissionsId = permissions.getId();

        // Get all the usuarioList where permissions equals to permissionsId
        defaultUsuarioShouldBeFound("permissionsId.equals=" + permissionsId);

        // Get all the usuarioList where permissions equals to (permissionsId + 1)
        defaultUsuarioShouldNotBeFound("permissionsId.equals=" + (permissionsId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByPermissionsPapelIsEqualToSomething() throws Exception {
        PermissionsPapel permissionsPapel;
        if (TestUtil.findAll(em, PermissionsPapel.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            permissionsPapel = PermissionsPapelResourceIT.createEntity(em);
        } else {
            permissionsPapel = TestUtil.findAll(em, PermissionsPapel.class).get(0);
        }
        em.persist(permissionsPapel);
        em.flush();
        usuario.addPermissionsPapel(permissionsPapel);
        usuarioRepository.saveAndFlush(usuario);
        Long permissionsPapelId = permissionsPapel.getId();

        // Get all the usuarioList where permissionsPapel equals to permissionsPapelId
        defaultUsuarioShouldBeFound("permissionsPapelId.equals=" + permissionsPapelId);

        // Get all the usuarioList where permissionsPapel equals to (permissionsPapelId + 1)
        defaultUsuarioShouldNotBeFound("permissionsPapelId.equals=" + (permissionsPapelId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByPapelIsEqualToSomething() throws Exception {
        Papel papel;
        if (TestUtil.findAll(em, Papel.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            papel = PapelResourceIT.createEntity(em);
        } else {
            papel = TestUtil.findAll(em, Papel.class).get(0);
        }
        em.persist(papel);
        em.flush();
        usuario.addPapel(papel);
        usuarioRepository.saveAndFlush(usuario);
        Long papelId = papel.getId();

        // Get all the usuarioList where papel equals to papelId
        defaultUsuarioShouldBeFound("papelId.equals=" + papelId);

        // Get all the usuarioList where papel equals to (papelId + 1)
        defaultUsuarioShouldNotBeFound("papelId.equals=" + (papelId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByUsuarioGrupoIsEqualToSomething() throws Exception {
        UsuarioGrupo usuarioGrupo;
        if (TestUtil.findAll(em, UsuarioGrupo.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            usuarioGrupo = UsuarioGrupoResourceIT.createEntity(em);
        } else {
            usuarioGrupo = TestUtil.findAll(em, UsuarioGrupo.class).get(0);
        }
        em.persist(usuarioGrupo);
        em.flush();
        usuario.addUsuarioGrupo(usuarioGrupo);
        usuarioRepository.saveAndFlush(usuario);
        Long usuarioGrupoId = usuarioGrupo.getId();

        // Get all the usuarioList where usuarioGrupo equals to usuarioGrupoId
        defaultUsuarioShouldBeFound("usuarioGrupoId.equals=" + usuarioGrupoId);

        // Get all the usuarioList where usuarioGrupo equals to (usuarioGrupoId + 1)
        defaultUsuarioShouldNotBeFound("usuarioGrupoId.equals=" + (usuarioGrupoId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByUsuarioPapelIsEqualToSomething() throws Exception {
        UsuarioPapel usuarioPapel;
        if (TestUtil.findAll(em, UsuarioPapel.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            usuarioPapel = UsuarioPapelResourceIT.createEntity(em);
        } else {
            usuarioPapel = TestUtil.findAll(em, UsuarioPapel.class).get(0);
        }
        em.persist(usuarioPapel);
        em.flush();
        usuario.addUsuarioPapel(usuarioPapel);
        usuarioRepository.saveAndFlush(usuario);
        Long usuarioPapelId = usuarioPapel.getId();

        // Get all the usuarioList where usuarioPapel equals to usuarioPapelId
        defaultUsuarioShouldBeFound("usuarioPapelId.equals=" + usuarioPapelId);

        // Get all the usuarioList where usuarioPapel equals to (usuarioPapelId + 1)
        defaultUsuarioShouldNotBeFound("usuarioPapelId.equals=" + (usuarioPapelId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByEmpresaIsEqualToSomething() throws Exception {
        Empresa empresa;
        if (TestUtil.findAll(em, Empresa.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            empresa = EmpresaResourceIT.createEntity(em);
        } else {
            empresa = TestUtil.findAll(em, Empresa.class).get(0);
        }
        em.persist(empresa);
        em.flush();
        usuario.setEmpresa(empresa);
        usuarioRepository.saveAndFlush(usuario);
        Long empresaId = empresa.getId();

        // Get all the usuarioList where empresa equals to empresaId
        defaultUsuarioShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the usuarioList where empresa equals to (empresaId + 1)
        defaultUsuarioShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].idnVarUsuario").value(hasItem(DEFAULT_IDN_VAR_USUARIO)))
            .andExpect(jsonPath("$.[*].nVarNome").value(hasItem(DEFAULT_N_VAR_NOME)))
            .andExpect(jsonPath("$.[*].idnVarEmpresa").value(hasItem(DEFAULT_IDN_VAR_EMPRESA)))
            .andExpect(jsonPath("$.[*].idnVarUsuarioCadastro").value(hasItem(DEFAULT_IDN_VAR_USUARIO_CADASTRO)))
            .andExpect(jsonPath("$.[*].nVarSenha").value(hasItem(DEFAULT_N_VAR_SENHA)));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(UPDATED_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(UPDATED_N_VAR_SENHA);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(updatedUsuario);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(UPDATED_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.idnVarUsuario(UPDATED_IDN_VAR_USUARIO).nVarNome(UPDATED_N_VAR_NOME).nVarSenha(UPDATED_N_VAR_SENHA);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(DEFAULT_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(DEFAULT_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(UPDATED_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .idnVarUsuario(UPDATED_IDN_VAR_USUARIO)
            .nVarNome(UPDATED_N_VAR_NOME)
            .idnVarEmpresa(UPDATED_IDN_VAR_EMPRESA)
            .idnVarUsuarioCadastro(UPDATED_IDN_VAR_USUARIO_CADASTRO)
            .nVarSenha(UPDATED_N_VAR_SENHA);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getIdnVarUsuario()).isEqualTo(UPDATED_IDN_VAR_USUARIO);
        assertThat(testUsuario.getnVarNome()).isEqualTo(UPDATED_N_VAR_NOME);
        assertThat(testUsuario.getIdnVarEmpresa()).isEqualTo(UPDATED_IDN_VAR_EMPRESA);
        assertThat(testUsuario.getIdnVarUsuarioCadastro()).isEqualTo(UPDATED_IDN_VAR_USUARIO_CADASTRO);
        assertThat(testUsuario.getnVarSenha()).isEqualTo(UPDATED_N_VAR_SENHA);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
