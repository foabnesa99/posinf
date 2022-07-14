import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './poreska-kategorija.reducer';

export const PoreskaKategorijaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const poreskaKategorijaEntity = useAppSelector(state => state.poreskaKategorija.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="poreskaKategorijaDetailsHeading">
          <Translate contentKey="myApp.poreskaKategorija.detail.title">PoreskaKategorija</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{poreskaKategorijaEntity.id}</dd>
          <dt>
            <span id="naziv">
              <Translate contentKey="myApp.poreskaKategorija.naziv">Naziv</Translate>
            </span>
          </dt>
          <dd>{poreskaKategorijaEntity.naziv}</dd>
        </dl>
        <Button tag={Link} to="/poreska-kategorija" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/poreska-kategorija/${poreskaKategorijaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PoreskaKategorijaDetail;
