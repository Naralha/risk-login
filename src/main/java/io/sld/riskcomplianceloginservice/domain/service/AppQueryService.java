package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.App;
import io.sld.riskcomplianceloginservice.domain.repository.AppRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.AppCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.AppMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link App} entities in the database.
 * The main input is a {@link AppCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppDTO} or a {@link Page} of {@link AppDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppQueryService extends QueryService<App> {

    private final Logger log = LoggerFactory.getLogger(AppQueryService.class);

    private final AppRepository appRepository;

    private final AppMapper appMapper;

    public AppQueryService(AppRepository appRepository, AppMapper appMapper) {
        this.appRepository = appRepository;
        this.appMapper = appMapper;
    }

    /**
     * Return a {@link List} of {@link AppDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppDTO> findByCriteria(AppCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<App> specification = createSpecification(criteria);
        return appMapper.toDto(appRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppDTO> findByCriteria(AppCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<App> specification = createSpecification(criteria);
        return appRepository.findAll(specification, page).map(appMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<App> specification = createSpecification(criteria);
        return appRepository.count(specification);
    }

    /**
     * Function to convert {@link AppCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<App> createSpecification(AppCriteria criteria) {
        Specification<App> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), App_.id));
            }
            if (criteria.getIdnVarApp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarApp(), App_.idnVarApp));
            }
            if (criteria.getnVarNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getnVarNome(), App_.nVarNome));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), App_.idnVarUsuario));
            }
            if (criteria.getIdnVarEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarEmpresa(), App_.idnVarEmpresa));
            }
            if (criteria.getAppEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppEmpresaId(),
                            root -> root.join(App_.appEmpresas, JoinType.LEFT).get(AppEmpresa_.id)
                        )
                    );
            }
            if (criteria.getFeaturesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFeaturesId(), root -> root.join(App_.features, JoinType.LEFT).get(Features_.id))
                    );
            }
            if (criteria.getPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPapelId(), root -> root.join(App_.papels, JoinType.LEFT).get(Papel_.id))
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(App_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(App_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
