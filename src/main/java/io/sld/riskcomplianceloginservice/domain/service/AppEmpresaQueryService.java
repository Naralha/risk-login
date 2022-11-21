package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import io.sld.riskcomplianceloginservice.domain.repository.AppEmpresaRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.AppEmpresaCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.AppEmpresaDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.AppEmpresaMapper;
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
 * Service for executing complex queries for {@link AppEmpresa} entities in the database.
 * The main input is a {@link AppEmpresaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppEmpresaDTO} or a {@link Page} of {@link AppEmpresaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppEmpresaQueryService extends QueryService<AppEmpresa> {

    private final Logger log = LoggerFactory.getLogger(AppEmpresaQueryService.class);

    private final AppEmpresaRepository appEmpresaRepository;

    private final AppEmpresaMapper appEmpresaMapper;

    public AppEmpresaQueryService(AppEmpresaRepository appEmpresaRepository, AppEmpresaMapper appEmpresaMapper) {
        this.appEmpresaRepository = appEmpresaRepository;
        this.appEmpresaMapper = appEmpresaMapper;
    }

    /**
     * Return a {@link List} of {@link AppEmpresaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppEmpresaDTO> findByCriteria(AppEmpresaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppEmpresa> specification = createSpecification(criteria);
        return appEmpresaMapper.toDto(appEmpresaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppEmpresaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppEmpresaDTO> findByCriteria(AppEmpresaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppEmpresa> specification = createSpecification(criteria);
        return appEmpresaRepository.findAll(specification, page).map(appEmpresaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppEmpresaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppEmpresa> specification = createSpecification(criteria);
        return appEmpresaRepository.count(specification);
    }

    /**
     * Function to convert {@link AppEmpresaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppEmpresa> createSpecification(AppEmpresaCriteria criteria) {
        Specification<AppEmpresa> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppEmpresa_.id));
            }
            if (criteria.getIdnVarApp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarApp(), AppEmpresa_.idnVarApp));
            }
            if (criteria.getIdnVarEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarEmpresa(), AppEmpresa_.idnVarEmpresa));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), AppEmpresa_.idnVarUsuario));
            }
            if (criteria.getAppId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppId(), root -> root.join(AppEmpresa_.app, JoinType.LEFT).get(App_.id))
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(AppEmpresa_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(AppEmpresa_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
