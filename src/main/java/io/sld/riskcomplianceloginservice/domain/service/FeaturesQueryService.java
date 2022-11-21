package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import io.sld.riskcomplianceloginservice.domain.repository.FeaturesRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.FeaturesCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.FeaturesDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.FeaturesMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for executing complex queries for {@link Features} entities in the database.
 * The main input is a {@link FeaturesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FeaturesDTO} or a {@link Page} of {@link FeaturesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeaturesQueryService extends QueryService<Features> {

    private final Logger log = LoggerFactory.getLogger(FeaturesQueryService.class);

    private final FeaturesRepository featuresRepository;

    private final FeaturesMapper featuresMapper;

    public FeaturesQueryService(FeaturesRepository featuresRepository, FeaturesMapper featuresMapper) {
        this.featuresRepository = featuresRepository;
        this.featuresMapper = featuresMapper;
    }

    /**
     * Return a {@link List} of {@link FeaturesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FeaturesDTO> findByCriteria(FeaturesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Features> specification = createSpecification(criteria);
        return featuresMapper.toDto(featuresRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FeaturesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FeaturesDTO> findByCriteria(FeaturesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Features> specification = createSpecification(criteria);
        return featuresRepository.findAll(specification, page).map(featuresMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FeaturesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Features> specification = createSpecification(criteria);
        return featuresRepository.count(specification);
    }

    /**
     * Function to convert {@link FeaturesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Features> createSpecification(FeaturesCriteria criteria) {
        Specification<Features> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Features_.id));
            }
            if (criteria.getIdnVarFeatures() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarFeatures(), Features_.idnVarFeatures));
            }
            if (criteria.getnVarNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getnVarNome(), Features_.nVarNome));
            }
            if (criteria.getIdnVarApp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarApp(), Features_.idnVarApp));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), Features_.idnVarUsuario));
            }
            if (criteria.getPermissionsPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPermissionsPapelId(),
                            root -> root.join(Features_.permissionsPapels, JoinType.LEFT).get(PermissionsPapel_.id)
                        )
                    );
            }
            if (criteria.getAppId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppId(), root -> root.join(Features_.app, JoinType.LEFT).get(App_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Features_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
