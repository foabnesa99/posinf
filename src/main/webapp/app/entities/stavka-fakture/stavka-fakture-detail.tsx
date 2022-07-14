import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stavka-fakture.reducer';

export const StavkaFaktureDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const stavkaFaktureEntity = useAppSelector(state => state.stavkaFakture.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stavkaFaktureDetailsHeading">
          <Translate contentKey="myApp.stavkaFakture.detail.title">StavkaFakture</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stavkaFaktureEntity.id}</dd>
          <dt>
            <span id="kolicina">
              <Translate contentKey="myApp.stavkaFakture.kolicina">Kolicina</Translate>
            </span>
          </dt>
          <dd>{stavkaFaktureEntity.kolicina}</dd>
          <dt>
            <span id="jedinicnaCena">
              <Translate contentKey="myApp.stavkaFakture.jedinicnaCena">Jedinicna Cena</Translate>
            </span>
          </dt>
          <dd>{stavkaFaktureEntity.jedinicnaCena}</dd>
          <dt>
            <span id="rabat">
              <Translate contentKey="myApp.stavkaFakture.rabat">Rabat</Translate>
            </span>
          </dt>
          <dd>{stavkaFaktureEntity.rabat}</dd>
          <dt>
            <span id="procenatPDV">
              <Translate contentKey="myApp.stavkaFakture.procenatPDV">Procenat PDV</Translate>
            </span>
          </dt>
          <dd>{stavkaFaktureEntity.procenatPDV}</dd>
          <dt>
            <span id="iznosPDV">
              <Translate contentKey="myApp.stavkaFakture.iznosPDV">Iznos PDV</Translate>
            </span>
          </dt>
          <dd>{stavkaFaktureEntity.iznosPDV}</dd>
          <dt>
            <span id="ukupnaCena">
              <Translate contentKey="myApp.stavkaFakture.ukupnaCena">Ukupna Cena</Translate>
            </span>
          </dt>
          <dd>{stavkaFaktureEntity.ukupnaCena}</dd>
          <dt>
            <Translate contentKey="myApp.stavkaFakture.izlaznaFaktura">Izlazna Faktura</Translate>
          </dt>
          <dd>{stavkaFaktureEntity.izlaznaFaktura ? stavkaFaktureEntity.izlaznaFaktura.id : ''}</dd>
          <dt>
            <Translate contentKey="myApp.stavkaFakture.robaIliUsluga">Roba Ili Usluga</Translate>
          </dt>
          <dd>{stavkaFaktureEntity.robaIliUsluga ? stavkaFaktureEntity.robaIliUsluga.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/stavka-fakture" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stavka-fakture/${stavkaFaktureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StavkaFaktureDetail;
