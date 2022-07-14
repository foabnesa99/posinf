import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './roba-ili-usluga.reducer';

export const RobaIliUslugaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const robaIliUslugaEntity = useAppSelector(state => state.robaIliUsluga.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="robaIliUslugaDetailsHeading">
          <Translate contentKey="myApp.robaIliUsluga.detail.title">RobaIliUsluga</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{robaIliUslugaEntity.id}</dd>
          <dt>
            <span id="naziv">
              <Translate contentKey="myApp.robaIliUsluga.naziv">Naziv</Translate>
            </span>
          </dt>
          <dd>{robaIliUslugaEntity.naziv}</dd>
          <dt>
            <span id="opis">
              <Translate contentKey="myApp.robaIliUsluga.opis">Opis</Translate>
            </span>
          </dt>
          <dd>{robaIliUslugaEntity.opis}</dd>
          <dt>
            <Translate contentKey="myApp.robaIliUsluga.jedinicaMere">Jedinica Mere</Translate>
          </dt>
          <dd>{robaIliUslugaEntity.jedinicaMere ? robaIliUslugaEntity.jedinicaMere.id : ''}</dd>
          <dt>
            <Translate contentKey="myApp.robaIliUsluga.poreskaKategorija">Poreska Kategorija</Translate>
          </dt>
          <dd>{robaIliUslugaEntity.poreskaKategorija ? robaIliUslugaEntity.poreskaKategorija.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/roba-ili-usluga" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/roba-ili-usluga/${robaIliUslugaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RobaIliUslugaDetail;
