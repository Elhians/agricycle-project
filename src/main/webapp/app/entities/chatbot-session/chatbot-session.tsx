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

import { getEntities } from './chatbot-session.reducer';

export const ChatbotSession = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const chatbotSessionList = useAppSelector(state => state.chatbotSession.entities);
  const loading = useAppSelector(state => state.chatbotSession.loading);

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
      <h2 id="chatbot-session-heading" data-cy="ChatbotSessionHeading">
        <Translate contentKey="agriCycleApp.chatbotSession.home.title">Chatbot Sessions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agriCycleApp.chatbotSession.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/chatbot-session/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agriCycleApp.chatbotSession.home.createLabel">Create new Chatbot Session</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {chatbotSessionList && chatbotSessionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="agriCycleApp.chatbotSession.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dateDebut')}>
                  <Translate contentKey="agriCycleApp.chatbotSession.dateDebut">Date Debut</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateDebut')} />
                </th>
                <th className="hand" onClick={sort('dateFin')}>
                  <Translate contentKey="agriCycleApp.chatbotSession.dateFin">Date Fin</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateFin')} />
                </th>
                <th>
                  <Translate contentKey="agriCycleApp.chatbotSession.utilisateur">Utilisateur</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {chatbotSessionList.map((chatbotSession, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/chatbot-session/${chatbotSession.id}`} color="link" size="sm">
                      {chatbotSession.id}
                    </Button>
                  </td>
                  <td>
                    {chatbotSession.dateDebut ? <TextFormat type="date" value={chatbotSession.dateDebut} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {chatbotSession.dateFin ? <TextFormat type="date" value={chatbotSession.dateFin} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {chatbotSession.utilisateur ? (
                      <Link to={`/utilisateur/${chatbotSession.utilisateur.id}`}>{chatbotSession.utilisateur.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/chatbot-session/${chatbotSession.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/chatbot-session/${chatbotSession.id}/edit`}
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
                        onClick={() => (window.location.href = `/chatbot-session/${chatbotSession.id}/delete`)}
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
              <Translate contentKey="agriCycleApp.chatbotSession.home.notFound">No Chatbot Sessions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ChatbotSession;
