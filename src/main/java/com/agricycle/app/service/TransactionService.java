package com.agricycle.app.service;

import com.agricycle.app.domain.Transaction;
import com.agricycle.app.repository.TransactionRepository;
import com.agricycle.app.service.dto.TransactionDTO;
import com.agricycle.app.service.mapper.TransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionDTO save(TransactionDTO transactionDTO) {
        LOG.debug("Request to save Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     * Update a transaction.
     *
     * @param transactionDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionDTO update(TransactionDTO transactionDTO) {
        LOG.debug("Request to update Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     * Partially update a transaction.
     *
     * @param transactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransactionDTO> partialUpdate(TransactionDTO transactionDTO) {
        LOG.debug("Request to partially update Transaction : {}", transactionDTO);

        return transactionRepository
            .findById(transactionDTO.getId())
            .map(existingTransaction -> {
                transactionMapper.partialUpdate(existingTransaction, transactionDTO);

                return existingTransaction;
            })
            .map(transactionRepository::save)
            .map(transactionMapper::toDto);
    }

    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionDTO> findOne(Long id) {
        LOG.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id).map(transactionMapper::toDto);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }
}
