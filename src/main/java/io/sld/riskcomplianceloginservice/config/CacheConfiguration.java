package io.sld.riskcomplianceloginservice.config;

import java.time.Duration;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, Empresa.class.getName());
            createCache(cm, Empresa.class.getName() + ".grupoPapels");
            createCache(cm, Empresa.class.getName() + ".usuarios");
            createCache(cm, Empresa.class.getName() + ".grupos");
            createCache(cm, Empresa.class.getName() + ".apps");
            createCache(cm, Empresa.class.getName() + ".appEmpresas");
            createCache(cm, Usuario.class.getName());
            createCache(cm, Usuario.class.getName() + ".apps");
            createCache(cm, Usuario.class.getName() + ".appEmpresas");
            createCache(cm, Usuario.class.getName() + ".features");
            createCache(cm, Usuario.class.getName() + ".grupos");
            createCache(cm, Usuario.class.getName() + ".grupoPapels");
            createCache(cm, Usuario.class.getName() + ".permissions");
            createCache(cm, Usuario.class.getName() + ".permissionsPapels");
            createCache(cm, Usuario.class.getName() + ".papels");
            createCache(cm, Usuario.class.getName() + ".usuarioGrupos");
            createCache(cm, Usuario.class.getName() + ".usuarioPapels");
            createCache(cm, Grupo.class.getName());
            createCache(cm, Grupo.class.getName() + ".grupoPapels");
            createCache(cm, Grupo.class.getName() + ".usuarioGrupos");
            createCache(cm, GrupoPapel.class.getName());
            createCache(cm, App.class.getName());
            createCache(cm, App.class.getName() + ".appEmpresas");
            createCache(cm, App.class.getName() + ".features");
            createCache(cm, App.class.getName() + ".papels");
            createCache(cm, AppEmpresa.class.getName());
            createCache(cm, Features.class.getName());
            createCache(cm, Features.class.getName() + ".permissionsPapels");
            createCache(cm, Permissions.class.getName());
            createCache(cm, Permissions.class.getName() + ".permissionsPapels");
            createCache(cm, PermissionsPapel.class.getName());
            createCache(cm, Papel.class.getName());
            createCache(cm, Papel.class.getName() + ".grupoPapels");
            createCache(cm, Papel.class.getName() + ".permissionsPapels");
            createCache(cm, Papel.class.getName() + ".usuarioPapels");
            createCache(cm, UsuarioGrupo.class.getName());
            createCache(cm, UsuarioPapel.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
