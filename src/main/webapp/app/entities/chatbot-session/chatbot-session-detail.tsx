import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chatbot-session.reducer';

export const ChatbotSessionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chatbotSessionEntity = useAppSelector(state => state.chatbotSession.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatbotSessionDetailsHeading">
          <Translate contentKey="agriCycleApp.chatbotSession.detail.title">ChatbotSession</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chatbotSessionEntity.id}</dd>
          <dt>
            <span id="dateDebut">
              <Translate contentKey="agriCycleApp.chatbotSession.dateDebut">Date Debut</Translate>
            </span>
          </dt>
          <dd>
            {chatbotSessionEntity.dateDebut ? (
              <TextFormat value={chatbotSessionEntity.dateDebut} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateFin">
              <Translate contentKey="agriCycleApp.chatbotSession.dateFin">Date Fin</Translate>
            </span>
          </dt>
          <dd>
            {chatbotSessionEntity.dateFin ? <TextFormat value={chatbotSessionEntity.dateFin} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="agriCycleApp.chatbotSession.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{chatbotSessionEntity.utilisateur ? chatbotSessionEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/chatbot-session" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chatbot-session/${chatbotSessionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChatbotSessionDetail;
