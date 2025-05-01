package com.agricycle.app.service;

import com.agricycle.app.domain.QRCode;
import com.agricycle.app.repository.QRCodeRepository;
import com.agricycle.app.service.dto.QRCodeDTO;
import com.agricycle.app.service.mapper.QRCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.QRCode}.
 */
@Service
@Transactional
public class QRCodeService {

    private static final Logger LOG = LoggerFactory.getLogger(QRCodeService.class);

    private final QRCodeRepository qRCodeRepository;

    private final QRCodeMapper qRCodeMapper;

    public QRCodeService(QRCodeRepository qRCodeRepository, QRCodeMapper qRCodeMapper) {
        this.qRCodeRepository = qRCodeRepository;
        this.qRCodeMapper = qRCodeMapper;
    }

    /**
     * Save a qRCode.
     *
     * @param qRCodeDTO the entity to save.
     * @return the persisted entity.
     */
    public QRCodeDTO save(QRCodeDTO qRCodeDTO) {
        LOG.debug("Request to save QRCode : {}", qRCodeDTO);
        QRCode qRCode = qRCodeMapper.toEntity(qRCodeDTO);
        qRCode = qRCodeRepository.save(qRCode);
        return qRCodeMapper.toDto(qRCode);
    }

    /**
     * Update a qRCode.
     *
     * @param qRCodeDTO the entity to save.
     * @return the persisted entity.
     */
    public QRCodeDTO update(QRCodeDTO qRCodeDTO) {
        LOG.debug("Request to update QRCode : {}", qRCodeDTO);
        QRCode qRCode = qRCodeMapper.toEntity(qRCodeDTO);
        qRCode = qRCodeRepository.save(qRCode);
        return qRCodeMapper.toDto(qRCode);
    }

    /**
     * Partially update a qRCode.
     *
     * @param qRCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QRCodeDTO> partialUpdate(QRCodeDTO qRCodeDTO) {
        LOG.debug("Request to partially update QRCode : {}", qRCodeDTO);

        return qRCodeRepository
            .findById(qRCodeDTO.getId())
            .map(existingQRCode -> {
                qRCodeMapper.partialUpdate(existingQRCode, qRCodeDTO);

                return existingQRCode;
            })
            .map(qRCodeRepository::save)
            .map(qRCodeMapper::toDto);
    }

    /**
     * Get one qRCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QRCodeDTO> findOne(Long id) {
        LOG.debug("Request to get QRCode : {}", id);
        return qRCodeRepository.findById(id).map(qRCodeMapper::toDto);
    }

    /**
     * Delete the qRCode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QRCode : {}", id);
        qRCodeRepository.deleteById(id);
    }
}
