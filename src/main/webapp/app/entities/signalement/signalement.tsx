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

import { getEntities } from './signalement.reducer';

export const Signalement = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const signalementList = useAppSelector(state => state.signalement.entities);
  const loading = useAppSelector(state => state.signalement.loading);

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
      <h2 id="signalement-heading" data-cy="SignalementHeading">
        <Translate contentKey="agriCycleApp.signalement.home.title">Signalements</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agriCycleApp.signalement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/signalement/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agriCycleApp.signalement.home.createLabel">Create new Signalement</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {signalementList && signalementList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="agriCycleApp.signalement.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('raison')}>
                  <Translate contentKey="agriCycleApp.signalement.raison">Raison</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('raison')} />
                </th>
                <th className="hand" onClick={sort('cible')}>
                  <Translate contentKey="agriCycleApp.signalement.cible">Cible</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cible')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="agriCycleApp.signalement.date">Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th>
                  <Translate contentKey="agriCycleApp.signalement.auteur">Auteur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="agriCycleApp.signalement.ciblePost">Cible Post</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {signalementList.map((signalement, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/signalement/${signalement.id}`} color="link" size="sm">
                      {signalement.id}
                    </Button>
                  </td>
                  <td>{signalement.raison}</td>
                  <td>
                    <Translate contentKey={`agriCycleApp.Cible.${signalement.cible}`} />
                  </td>
                  <td>{signalement.date ? <TextFormat type="date" value={signalement.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{signalement.auteur ? <Link to={`/utilisateur/${signalement.auteur.id}`}>{signalement.auteur.id}</Link> : ''}</td>
                  <td>{signalement.ciblePost ? <Link to={`/post/${signalement.ciblePost.id}`}>{signalement.ciblePost.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/signalement/${signalement.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/signalement/${signalement.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/signalement/${signalement.id}/delete`)}
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
              <Translate contentKey="agriCycleApp.signalement.home.notFound">No Signalements found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Signalement;
