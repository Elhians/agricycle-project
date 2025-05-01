package com.agricycle.app.service.mapper;

import com.agricycle.app.domain.QRCode;
import com.agricycle.app.domain.Transaction;
import com.agricycle.app.service.dto.QRCodeDTO;
import com.agricycle.app.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QRCode} and its DTO {@link QRCodeDTO}.
 */
@Mapper(componentModel = "spring")
public interface QRCodeMapper extends EntityMapper<QRCodeDTO, QRCode> {
    @Mapping(target = "transaction", source = "transaction", qualifiedByName = "transactionId")
    QRCodeDTO toDto(QRCode s);

    @Named("transactionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransactionDTO toDtoTransactionId(Transaction transaction);
}
