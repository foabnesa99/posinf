import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cena.reducer';

export const CenaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cenaEntity = useAppSelector(state => state.cena.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cenaDetailsHeading">
          <Translate contentKey="myApp.cena.detail.title">Cena</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cenaEntity.id}</dd>
          <dt>
            <span id="datumVazenja">
              <Translate contentKey="myApp.cena.datumVazenja">Datum Vazenja</Translate>
            </span>
          </dt>
          <dd>
            {cenaEntity.datumVazenja ? <TextFormat value={cenaEntity.datumVazenja} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="iznosCene">
              <Translate contentKey="myApp.cena.iznosCene">Iznos Cene</Translate>
            </span>
          </dt>
          <dd>{cenaEntity.iznosCene}</dd>
          <dt>
            <Translate contentKey="myApp.cena.robaIliUsluga">Roba Ili Usluga</Translate>
          </dt>
          <dd>{cenaEntity.robaIliUsluga ? cenaEntity.robaIliUsluga.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/cena" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cena/${cenaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CenaDetail;
