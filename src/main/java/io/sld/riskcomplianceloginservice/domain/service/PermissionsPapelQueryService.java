package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import io.sld.riskcomplianceloginservice.domain.repository.PermissionsPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.PermissionsPapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PermissionsPapelMapper;
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
 * Service for executing complex queries for {@link PermissionsPapel} entities in the database.
 * The main input is a {@link PermissionsPapelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PermissionsPapelDTO} or a {@link Page} of {@link PermissionsPapelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PermissionsPapelQueryService extends QueryService<PermissionsPapel> {

    private final Logger log = LoggerFactory.getLogger(PermissionsPapelQueryService.class);

    private final PermissionsPapelRepository permissionsPapelRepository;

    private final PermissionsPapelMapper permissionsPapelMapper;

    public PermissionsPapelQueryService(
        PermissionsPapelRepository permissionsPapelRepository,
        PermissionsPapelMapper permissionsPapelMapper
    ) {
        this.permissionsPapelRepository = permissionsPapelRepository;
        this.permissionsPapelMapper = permissionsPapelMapper;
    }

    /**
     * Return a {@link List} of {@link PermissionsPapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PermissionsPapelDTO> findByCriteria(PermissionsPapelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PermissionsPapel> specification = createSpecification(criteria);
        return permissionsPapelMapper.toDto(permissionsPapelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PermissionsPapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissionsPapelDTO> findByCriteria(PermissionsPapelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PermissionsPapel> specification = createSpecification(criteria);
        return permissionsPapelRepository.findAll(specification, page).map(permissionsPapelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PermissionsPapelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PermissionsPapel> specification = createSpecification(criteria);
        return permissionsPapelRepository.count(specification);
    }

    /**
     * Function to convert {@link PermissionsPapelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PermissionsPapel> createSpecification(PermissionsPapelCriteria criteria) {
        Specification<PermissionsPapel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PermissionsPapel_.id));
            }
            if (criteria.getIdnVarPermissions() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getIdnVarPermissions(), PermissionsPapel_.idnVarPermissions));
            }
            if (criteria.getIdnVarPapel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarPapel(), PermissionsPapel_.idnVarPapel));
            }
            if (criteria.getIdnVarFeatures() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarFeatures(), PermissionsPapel_.idnVarFeatures));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), PermissionsPapel_.idnVarUsuario));
            }
            if (criteria.getPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPapelId(), root -> root.join(PermissionsPapel_.papel, JoinType.LEFT).get(Papel_.id))
                    );
            }
            if (criteria.getPermissionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPermissionsId(),
                            root -> root.join(PermissionsPapel_.permissions, JoinType.LEFT).get(Permissions_.id)
                        )
                    );
            }
            if (criteria.getFeaturesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFeaturesId(),
                            root -> root.join(PermissionsPapel_.features, JoinType.LEFT).get(Features_.id)
                        )
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioId(),
                            root -> root.join(PermissionsPapel_.usuario, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
