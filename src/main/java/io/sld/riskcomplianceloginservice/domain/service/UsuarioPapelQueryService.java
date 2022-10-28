package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.Papel_;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel_;
import io.sld.riskcomplianceloginservice.domain.entity.Usuario_;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.UsuarioPapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioPapelMapper;
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
 * Service for executing complex queries for {@link UsuarioPapel} entities in the database.
 * The main input is a {@link UsuarioPapelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsuarioPapelDTO} or a {@link Page} of {@link UsuarioPapelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioPapelQueryService extends QueryService<UsuarioPapel> {

    private final Logger log = LoggerFactory.getLogger(UsuarioPapelQueryService.class);

    private final UsuarioPapelRepository usuarioPapelRepository;

    private final UsuarioPapelMapper usuarioPapelMapper;

    public UsuarioPapelQueryService(UsuarioPapelRepository usuarioPapelRepository, UsuarioPapelMapper usuarioPapelMapper) {
        this.usuarioPapelRepository = usuarioPapelRepository;
        this.usuarioPapelMapper = usuarioPapelMapper;
    }

    /**
     * Return a {@link List} of {@link UsuarioPapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioPapelDTO> findByCriteria(UsuarioPapelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UsuarioPapel> specification = createSpecification(criteria);
        return usuarioPapelMapper.toDto(usuarioPapelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsuarioPapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioPapelDTO> findByCriteria(UsuarioPapelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UsuarioPapel> specification = createSpecification(criteria);
        return usuarioPapelRepository.findAll(specification, page).map(usuarioPapelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioPapelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UsuarioPapel> specification = createSpecification(criteria);
        return usuarioPapelRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioPapelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UsuarioPapel> createSpecification(UsuarioPapelCriteria criteria) {
        Specification<UsuarioPapel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UsuarioPapel_.id));
            }
            if (criteria.getIdnVarUsuarioCadastrado() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getIdnVarUsuarioCadastrado(), UsuarioPapel_.idnVarUsuarioCadastrado)
                    );
            }
            if (criteria.getIdnVarPapel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarPapel(), UsuarioPapel_.idnVarPapel));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), UsuarioPapel_.idnVarUsuario));
            }
            if (criteria.getPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPapelId(), root -> root.join(UsuarioPapel_.papel, JoinType.LEFT).get(Papel_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioId(),
                            root -> root.join(UsuarioPapel_.usuario, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
