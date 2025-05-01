package com.agricycle.app.service;

import com.agricycle.app.domain.Entreprise;
import com.agricycle.app.repository.EntrepriseRepository;
import com.agricycle.app.service.dto.EntrepriseDTO;
import com.agricycle.app.service.mapper.EntrepriseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Entreprise}.
 */
@Service
@Transactional
public class EntrepriseService {

    private static final Logger LOG = LoggerFactory.getLogger(EntrepriseService.class);

    private final EntrepriseRepository entrepriseRepository;

    private final EntrepriseMapper entrepriseMapper;

    public EntrepriseService(EntrepriseRepository entrepriseRepository, EntrepriseMapper entrepriseMapper) {
        this.entrepriseRepository = entrepriseRepository;
        this.entrepriseMapper = entrepriseMapper;
    }

    /**
     * Save a entreprise.
     *
     * @param entrepriseDTO the entity to save.
     * @return the persisted entity.
     */
    public EntrepriseDTO save(EntrepriseDTO entrepriseDTO) {
        LOG.debug("Request to save Entreprise : {}", entrepriseDTO);
        Entreprise entreprise = entrepriseMapper.toEntity(entrepriseDTO);
        entreprise = entrepriseRepository.save(entreprise);
        return entrepriseMapper.toDto(entreprise);
    }

    /**
     * Update a entreprise.
     *
     * @param entrepriseDTO the entity to save.
     * @return the persisted entity.
     */
    public EntrepriseDTO update(EntrepriseDTO entrepriseDTO) {
        LOG.debug("Request to update Entreprise : {}", entrepriseDTO);
        Entreprise entreprise = entrepriseMapper.toEntity(entrepriseDTO);
        entreprise = entrepriseRepository.save(entreprise);
        return entrepriseMapper.toDto(entreprise);
    }

    /**
     * Partially update a entreprise.
     *
     * @param entrepriseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EntrepriseDTO> partialUpdate(EntrepriseDTO entrepriseDTO) {
        LOG.debug("Request to partially update Entreprise : {}", entrepriseDTO);

        return entrepriseRepository
            .findById(entrepriseDTO.getId())
            .map(existingEntreprise -> {
                entrepriseMapper.partialUpdate(existingEntreprise, entrepriseDTO);

                return existingEntreprise;
            })
            .map(entrepriseRepository::save)
            .map(entrepriseMapper::toDto);
    }

    /**
     * Get one entreprise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntrepriseDTO> findOne(Long id) {
        LOG.debug("Request to get Entreprise : {}", id);
        return entrepriseRepository.findById(id).map(entrepriseMapper::toDto);
    }

    /**
     * Delete the entreprise by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Entreprise : {}", id);
        entrepriseRepository.deleteById(id);
    }
}
