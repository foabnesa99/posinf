import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './kupac.reducer';

export const KupacDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const kupacEntity = useAppSelector(state => state.kupac.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="kupacDetailsHeading">
          <Translate contentKey="myApp.kupac.detail.title">Kupac</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{kupacEntity.id}</dd>
          <dt>
            <span id="pib">
              <Translate contentKey="myApp.kupac.pib">Pib</Translate>
            </span>
          </dt>
          <dd>{kupacEntity.pib}</dd>
          <dt>
            <span id="mib">
              <Translate contentKey="myApp.kupac.mib">Mib</Translate>
            </span>
          </dt>
          <dd>{kupacEntity.mib}</dd>
          <dt>
            <span id="naziv">
              <Translate contentKey="myApp.kupac.naziv">Naziv</Translate>
            </span>
          </dt>
          <dd>{kupacEntity.naziv}</dd>
          <dt>
            <span id="adresa">
              <Translate contentKey="myApp.kupac.adresa">Adresa</Translate>
            </span>
          </dt>
          <dd>{kupacEntity.adresa}</dd>
          <dt>
            <span id="telefon">
              <Translate contentKey="myApp.kupac.telefon">Telefon</Translate>
            </span>
          </dt>
          <dd>{kupacEntity.telefon}</dd>
        </dl>
        <Button tag={Link} to="/kupac" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/kupac/${kupacEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default KupacDetail;
