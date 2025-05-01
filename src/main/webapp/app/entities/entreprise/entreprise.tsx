import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './entreprise.reducer';

export const Entreprise = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const entrepriseList = useAppSelector(state => state.entreprise.entities);
  const loading = useAppSelector(state => state.entreprise.loading);

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
      <h2 id="entreprise-heading" data-cy="EntrepriseHeading">
        <Translate contentKey="agriCycleApp.entreprise.home.title">Entreprises</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agriCycleApp.entreprise.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/entreprise/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agriCycleApp.entreprise.home.createLabel">Create new Entreprise</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {entrepriseList && entrepriseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="agriCycleApp.entreprise.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nomEntreprise')}>
                  <Translate contentKey="agriCycleApp.entreprise.nomEntreprise">Nom Entreprise</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nomEntreprise')} />
                </th>
                <th className="hand" onClick={sort('typeActivite')}>
                  <Translate contentKey="agriCycleApp.entreprise.typeActivite">Type Activite</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('typeActivite')} />
                </th>
                <th className="hand" onClick={sort('licence')}>
                  <Translate contentKey="agriCycleApp.entreprise.licence">Licence</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('licence')} />
                </th>
                <th className="hand" onClick={sort('adressePhysique')}>
                  <Translate contentKey="agriCycleApp.entreprise.adressePhysique">Adresse Physique</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('adressePhysique')} />
                </th>
                <th>
                  <Translate contentKey="agriCycleApp.entreprise.utilisateur">Utilisateur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {entrepriseList.map((entreprise, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/entreprise/${entreprise.id}`} color="link" size="sm">
                      {entreprise.id}
                    </Button>
                  </td>
                  <td>{entreprise.nomEntreprise}</td>
                  <td>{entreprise.typeActivite}</td>
                  <td>{entreprise.licence}</td>
                  <td>{entreprise.adressePhysique}</td>
                  <td>
                    {entreprise.utilisateur ? (
                      <Link to={`/utilisateur/${entreprise.utilisateur.id}`}>{entreprise.utilisateur.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/entreprise/${entreprise.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/entreprise/${entreprise.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/entreprise/${entreprise.id}/delete`)}
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
              <Translate contentKey="agriCycleApp.entreprise.home.notFound">No Entreprises found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Entreprise;
