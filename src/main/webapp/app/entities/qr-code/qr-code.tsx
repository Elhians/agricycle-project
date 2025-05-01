import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './qr-code.reducer';

export const QRCode = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const qRCodeList = useAppSelector(state => state.qRCode.entities);
  const loading = useAppSelector(state => state.qRCode.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="qr-code-heading" data-cy="QRCodeHeading">
        <Translate contentKey="agriCycleApp.qRCode.home.title">QR Codes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agriCycleApp.qRCode.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/qr-code/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agriCycleApp.qRCode.home.createLabel">Create new QR Code</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {qRCodeList && qRCodeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="agriCycleApp.qRCode.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('valeur')}>
                  <Translate contentKey="agriCycleApp.qRCode.valeur">Valeur</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('valeur')} />
                </th>
                <th className="hand" onClick={sort('dateExpiration')}>
                  <Translate contentKey="agriCycleApp.qRCode.dateExpiration">Date Expiration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateExpiration')} />
                </th>
                <th>
                  <Translate contentKey="agriCycleApp.qRCode.transaction">Transaction</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {qRCodeList.map((qRCode, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/qr-code/${qRCode.id}`} color="link" size="sm">
                      {qRCode.id}
                    </Button>
                  </td>
                  <td>{qRCode.valeur}</td>
                  <td>
                    {qRCode.dateExpiration ? <TextFormat type="date" value={qRCode.dateExpiration} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{qRCode.transaction ? <Link to={`/transaction/${qRCode.transaction.id}`}>{qRCode.transaction.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/qr-code/${qRCode.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/qr-code/${qRCode.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/qr-code/${qRCode.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="agriCycleApp.qRCode.home.notFound">No QR Codes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default QRCode;
