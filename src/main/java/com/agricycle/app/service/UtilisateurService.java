package com.agricycle.app.service;

import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.UtilisateurRepository;
import com.agricycle.app.service.dto.UtilisateurDTO;
import com.agricycle.app.service.mapper.UtilisateurMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurService {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    /**
     * Save a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilisateurDTO save(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to save Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDto(utilisateur);
    }

    /**
     * Update a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilisateurDTO update(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to update Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDto(utilisateur);
    }

    /**
     * Partially update a utilisateur.
     *
     * @param utilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtilisateurDTO> partialUpdate(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to partially update Utilisateur : {}", utilisateurDTO);

        return utilisateurRepository
            .findById(utilisateurDTO.getId())
            .map(existingUtilisateur -> {
                utilisateurMapper.partialUpdate(existingUtilisateur, utilisateurDTO);

                return existingUtilisateur;
            })
            .map(utilisateurRepository::save)
            .map(utilisateurMapper::toDto);
    }

    /**
     *  Get all the utilisateurs where Agriculteur is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereAgriculteurIsNull() {
        LOG.debug("Request to get all utilisateurs where Agriculteur is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getAgriculteur() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Commercant is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereCommercantIsNull() {
        LOG.debug("Request to get all utilisateurs where Commercant is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getCommercant() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Transporteur is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereTransporteurIsNull() {
        LOG.debug("Request to get all utilisateurs where Transporteur is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getTransporteur() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Consommateur is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereConsommateurIsNull() {
        LOG.debug("Request to get all utilisateurs where Consommateur is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getConsommateur() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Organisation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereOrganisationIsNull() {
        LOG.debug("Request to get all utilisateurs where Organisation is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getOrganisation() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Entreprise is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereEntrepriseIsNull() {
        LOG.debug("Request to get all utilisateurs where Entreprise is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getEntreprise() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one utilisateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtilisateurDTO> findOne(Long id) {
        LOG.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findById(id).map(utilisateurMapper::toDto);
    }

    /**
     * Delete the utilisateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
    }
}
