import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './contenu-educatif.reducer';

export const ContenuEducatif = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const contenuEducatifList = useAppSelector(state => state.contenuEducatif.entities);
  const loading = useAppSelector(state => state.contenuEducatif.loading);
  const links = useAppSelector(state => state.contenuEducatif.links);
  const updateSuccess = useAppSelector(state => state.contenuEducatif.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="contenu-educatif-heading" data-cy="ContenuEducatifHeading">
        <Translate contentKey="agriCycleApp.contenuEducatif.home.title">Contenu Educatifs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agriCycleApp.contenuEducatif.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/contenu-educatif/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agriCycleApp.contenuEducatif.home.createLabel">Create new Contenu Educatif</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={contenuEducatifList ? contenuEducatifList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {contenuEducatifList && contenuEducatifList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="agriCycleApp.contenuEducatif.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('titre')}>
                    <Translate contentKey="agriCycleApp.contenuEducatif.titre">Titre</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('titre')} />
                  </th>
                  <th className="hand" onClick={sort('description')}>
                    <Translate contentKey="agriCycleApp.contenuEducatif.description">Description</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                  </th>
                  <th className="hand" onClick={sort('typeMedia')}>
                    <Translate contentKey="agriCycleApp.contenuEducatif.typeMedia">Type Media</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('typeMedia')} />
                  </th>
                  <th className="hand" onClick={sort('url')}>
                    <Translate contentKey="agriCycleApp.contenuEducatif.url">Url</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('url')} />
                  </th>
                  <th className="hand" onClick={sort('datePublication')}>
                    <Translate contentKey="agriCycleApp.contenuEducatif.datePublication">Date Publication</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('datePublication')} />
                  </th>
                  <th>
                    <Translate contentKey="agriCycleApp.contenuEducatif.auteur">Auteur</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {contenuEducatifList.map((contenuEducatif, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/contenu-educatif/${contenuEducatif.id}`} color="link" size="sm">
                        {contenuEducatif.id}
                      </Button>
                    </td>
                    <td>{contenuEducatif.titre}</td>
                    <td>{contenuEducatif.description}</td>
                    <td>
                      <Translate contentKey={`agriCycleApp.TypeMedia.${contenuEducatif.typeMedia}`} />
                    </td>
                    <td>{contenuEducatif.url}</td>
                    <td>
                      {contenuEducatif.datePublication ? (
                        <TextFormat type="date" value={contenuEducatif.datePublication} format={APP_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {contenuEducatif.auteur ? (
                        <Link to={`/utilisateur/${contenuEducatif.auteur.id}`}>{contenuEducatif.auteur.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`/contenu-educatif/${contenuEducatif.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/contenu-educatif/${contenuEducatif.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/contenu-educatif/${contenuEducatif.id}/delete`)}
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
                <Translate contentKey="agriCycleApp.contenuEducatif.home.notFound">No Contenu Educatifs found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default ContenuEducatif;
