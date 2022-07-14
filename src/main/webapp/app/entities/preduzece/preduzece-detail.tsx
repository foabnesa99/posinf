import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './preduzece.reducer';

export const PreduzeceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const preduzeceEntity = useAppSelector(state => state.preduzece.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="preduzeceDetailsHeading">
          <Translate contentKey="myApp.preduzece.detail.title">Preduzece</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{preduzeceEntity.id}</dd>
          <dt>
            <span id="pib">
              <Translate contentKey="myApp.preduzece.pib">Pib</Translate>
            </span>
          </dt>
          <dd>{preduzeceEntity.pib}</dd>
          <dt>
            <span id="naziv">
              <Translate contentKey="myApp.preduzece.naziv">Naziv</Translate>
            </span>
          </dt>
          <dd>{preduzeceEntity.naziv}</dd>
          <dt>
            <span id="adresa">
              <Translate contentKey="myApp.preduzece.adresa">Adresa</Translate>
            </span>
          </dt>
          <dd>{preduzeceEntity.adresa}</dd>
        </dl>
        <Button tag={Link} to="/preduzece" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/preduzece/${preduzeceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PreduzeceDetail;
