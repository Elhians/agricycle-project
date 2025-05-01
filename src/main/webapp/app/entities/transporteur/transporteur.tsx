import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './transporteur.reducer';

export const Transporteur = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const transporteurList = useAppSelector(state => state.transporteur.entities);
  const loading = useAppSelector(state => state.transporteur.loading);

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
      <h2 id="transporteur-heading" data-cy="TransporteurHeading">
        <Translate contentKey="agriCycleApp.transporteur.home.title">Transporteurs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agriCycleApp.transporteur.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/transporteur/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agriCycleApp.transporteur.home.createLabel">Create new Transporteur</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {transporteurList && transporteurList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="agriCycleApp.transporteur.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('zoneCouverture')}>
                  <Translate contentKey="agriCycleApp.transporteur.zoneCouverture">Zone Couverture</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('zoneCouverture')} />
                </th>
                <th className="hand" onClick={sort('typeVehicule')}>
                  <Translate contentKey="agriCycleApp.transporteur.typeVehicule">Type Vehicule</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('typeVehicule')} />
                </th>
                <th className="hand" onClick={sort('capacite')}>
                  <Translate contentKey="agriCycleApp.transporteur.capacite">Capacite</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('capacite')} />
                </th>
                <th>
                  <Translate contentKey="agriCycleApp.transporteur.utilisateur">Utilisateur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {transporteurList.map((transporteur, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/transporteur/${transporteur.id}`} color="link" size="sm">
                      {transporteur.id}
                    </Button>
                  </td>
                  <td>{transporteur.zoneCouverture}</td>
                  <td>
                    <Translate contentKey={`agriCycleApp.TypeVehicule.${transporteur.typeVehicule}`} />
                  </td>
                  <td>{transporteur.capacite}</td>
                  <td>
                    {transporteur.utilisateur ? (
                      <Link to={`/utilisateur/${transporteur.utilisateur.id}`}>{transporteur.utilisateur.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/transporteur/${transporteur.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/transporteur/${transporteur.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/transporteur/${transporteur.id}/delete`)}
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
              <Translate contentKey="agriCycleApp.transporteur.home.notFound">No Transporteurs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Transporteur;
