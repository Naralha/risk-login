package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import io.sld.riskcomplianceloginservice.domain.repository.PapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.PapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.PapelMapper;
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
 * Service for executing complex queries for {@link Papel} entities in the database.
 * The main input is a {@link PapelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PapelDTO} or a {@link Page} of {@link PapelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PapelQueryService extends QueryService<Papel> {

    private final Logger log = LoggerFactory.getLogger(PapelQueryService.class);

    private final PapelRepository papelRepository;

    private final PapelMapper papelMapper;

    public PapelQueryService(PapelRepository papelRepository, PapelMapper papelMapper) {
        this.papelRepository = papelRepository;
        this.papelMapper = papelMapper;
    }

    /**
     * Return a {@link List} of {@link PapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PapelDTO> findByCriteria(PapelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Papel> specification = createSpecification(criteria);
        return papelMapper.toDto(papelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PapelDTO> findByCriteria(PapelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Papel> specification = createSpecification(criteria);
        return papelRepository.findAll(specification, page).map(papelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PapelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Papel> specification = createSpecification(criteria);
        return papelRepository.count(specification);
    }

    /**
     * Function to convert {@link PapelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Papel> createSpecification(PapelCriteria criteria) {
        Specification<Papel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Papel_.id));
            }
            if (criteria.getIdnVarPapel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarPapel(), Papel_.idnVarPapel));
            }
            if (criteria.getnVarNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getnVarNome(), Papel_.nVarNome));
            }
            if (criteria.getIdnVarApp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarApp(), Papel_.idnVarApp));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), Papel_.idnVarUsuario));
            }
            if (criteria.getGrupoPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGrupoPapelId(),
                            root -> root.join(Papel_.grupoPapels, JoinType.LEFT).get(GrupoPapel_.id)
                        )
                    );
            }
            if (criteria.getPermissionsPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPermissionsPapelId(),
                            root -> root.join(Papel_.permissionsPapels, JoinType.LEFT).get(PermissionsPapel_.id)
                        )
                    );
            }
            if (criteria.getUsuarioPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioPapelId(),
                            root -> root.join(Papel_.usuarioPapels, JoinType.LEFT).get(UsuarioPapel_.id)
                        )
                    );
            }
            if (criteria.getAppId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getAppId(), root -> root.join(Papel_.app, JoinType.LEFT).get(App_.id)));
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Papel_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
