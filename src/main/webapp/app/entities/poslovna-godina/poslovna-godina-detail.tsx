import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './poslovna-godina.reducer';

export const PoslovnaGodinaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const poslovnaGodinaEntity = useAppSelector(state => state.poslovnaGodina.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="poslovnaGodinaDetailsHeading">
          <Translate contentKey="myApp.poslovnaGodina.detail.title">PoslovnaGodina</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{poslovnaGodinaEntity.id}</dd>
          <dt>
            <span id="godina">
              <Translate contentKey="myApp.poslovnaGodina.godina">Godina</Translate>
            </span>
          </dt>
          <dd>{poslovnaGodinaEntity.godina}</dd>
          <dt>
            <span id="zakljucena">
              <Translate contentKey="myApp.poslovnaGodina.zakljucena">Zakljucena</Translate>
            </span>
          </dt>
          <dd>{poslovnaGodinaEntity.zakljucena ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="myApp.poslovnaGodina.preduzece">Preduzece</Translate>
          </dt>
          <dd>{poslovnaGodinaEntity.preduzece ? poslovnaGodinaEntity.preduzece.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/poslovna-godina" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/poslovna-godina/${poslovnaGodinaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PoslovnaGodinaDetail;
