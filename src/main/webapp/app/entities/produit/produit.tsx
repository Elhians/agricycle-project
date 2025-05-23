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

import { getEntities, reset } from './produit.reducer';

export const Produit = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const produitList = useAppSelector(state => state.produit.entities);
  const loading = useAppSelector(state => state.produit.loading);
  const links = useAppSelector(state => state.produit.links);
  const updateSuccess = useAppSelector(state => state.produit.updateSuccess);

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
      <h2 id="produit-heading" data-cy="ProduitHeading">
        <Translate contentKey="agriCycleApp.produit.home.title">Produits</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agriCycleApp.produit.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/produit/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agriCycleApp.produit.home.createLabel">Create new Produit</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={produitList ? produitList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {produitList && produitList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="agriCycleApp.produit.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('nom')}>
                    <Translate contentKey="agriCycleApp.produit.nom">Nom</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('nom')} />
                  </th>
                  <th className="hand" onClick={sort('description')}>
                    <Translate contentKey="agriCycleApp.produit.description">Description</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                  </th>
                  <th className="hand" onClick={sort('prix')}>
                    <Translate contentKey="agriCycleApp.produit.prix">Prix</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('prix')} />
                  </th>
                  <th className="hand" onClick={sort('quantite')}>
                    <Translate contentKey="agriCycleApp.produit.quantite">Quantite</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('quantite')} />
                  </th>
                  <th className="hand" onClick={sort('type')}>
                    <Translate contentKey="agriCycleApp.produit.type">Type</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                  </th>
                  <th className="hand" onClick={sort('statut')}>
                    <Translate contentKey="agriCycleApp.produit.statut">Statut</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('statut')} />
                  </th>
                  <th className="hand" onClick={sort('imageUrl')}>
                    <Translate contentKey="agriCycleApp.produit.imageUrl">Image Url</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('imageUrl')} />
                  </th>
                  <th className="hand" onClick={sort('dateAjout')}>
                    <Translate contentKey="agriCycleApp.produit.dateAjout">Date Ajout</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('dateAjout')} />
                  </th>
                  <th>
                    <Translate contentKey="agriCycleApp.produit.vendeur">Vendeur</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="agriCycleApp.produit.acheteur">Acheteur</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {produitList.map((produit, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/produit/${produit.id}`} color="link" size="sm">
                        {produit.id}
                      </Button>
                    </td>
                    <td>{produit.nom}</td>
                    <td>{produit.description}</td>
                    <td>{produit.prix}</td>
                    <td>{produit.quantite}</td>
                    <td>
                      <Translate contentKey={`agriCycleApp.TypeProduit.${produit.type}`} />
                    </td>
                    <td>
                      <Translate contentKey={`agriCycleApp.StatutAnnonce.${produit.statut}`} />
                    </td>
                    <td>{produit.imageUrl}</td>
                    <td>{produit.dateAjout ? <TextFormat type="date" value={produit.dateAjout} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{produit.vendeur ? <Link to={`/utilisateur/${produit.vendeur.id}`}>{produit.vendeur.id}</Link> : ''}</td>
                    <td>{produit.acheteur ? <Link to={`/utilisateur/${produit.acheteur.id}`}>{produit.acheteur.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/produit/${produit.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/produit/${produit.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/produit/${produit.id}/delete`)}
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
                <Translate contentKey="agriCycleApp.produit.home.notFound">No Produits found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Produit;
