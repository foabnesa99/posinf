import React, { useEffect, useCallback, useRef } from 'react';
import axios from "axios";
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { exportPdf, getEntity } from './izlazna-faktura.reducer';
import StavkaFakture from '../stavka-fakture/stavka-fakture';

export const IzlaznaFakturaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const izlaznaFakturaEntity = useAppSelector(state => state.izlaznaFaktura.entity);

  const downloadRef = useRef(null);

  const downloadFunkcija = useCallback(() => {
    fetch(`http://localhost:8080/api/izlazne-faktures/${izlaznaFakturaEntity.id}/export`)
      .then((response) => response.json())
      .then((data) => {
        const file = new Blob([JSON.stringify(data)], {
          type: "application/json",
        });
        downloadRef.current.href = URL.createObjectURL(file);
        downloadRef.current.download = `fakturaZaDan_${new Date().toLocaleDateString(
          "en-US"
        )}`;
        downloadRef.current.click();
      });
  }, [downloadRef]);

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="izlaznaFakturaDetailsHeading">
          <Translate contentKey="myApp.izlaznaFaktura.detail.title">IzlaznaFaktura</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{izlaznaFakturaEntity.id}</dd>
          <dt>
            <span id="brojFakture">
              <Translate contentKey="myApp.izlaznaFaktura.brojFakture">Broj Fakture</Translate>
            </span>
          </dt>
          <dd>{izlaznaFakturaEntity.brojFakture}</dd>
          <dt>
            <span id="datumIzdavanja">
              <Translate contentKey="myApp.izlaznaFaktura.datumIzdavanja">Datum Izdavanja</Translate>
            </span>
          </dt>
          <dd>
            {izlaznaFakturaEntity.datumIzdavanja ? (
              <TextFormat value={izlaznaFakturaEntity.datumIzdavanja} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="datumValute">
              <Translate contentKey="myApp.izlaznaFaktura.datumValute">Datum Valute</Translate>
            </span>
          </dt>
          <dd>
            {izlaznaFakturaEntity.datumValute ? (
              <TextFormat value={izlaznaFakturaEntity.datumValute} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="ukupnaOsnovica">
              <Translate contentKey="myApp.izlaznaFaktura.ukupnaOsnovica">Ukupna Osnovica</Translate>
            </span>
          </dt>
          <dd>{izlaznaFakturaEntity.ukupnaOsnovica}</dd>
          <dt>
            <span id="ukupanRabat">
              <Translate contentKey="myApp.izlaznaFaktura.ukupanRabat">Ukupan Rabat</Translate>
            </span>
          </dt>
          <dd>{izlaznaFakturaEntity.ukupanRabat}</dd>
          <dt>
            <span id="ukupanIznos">
              <Translate contentKey="myApp.izlaznaFaktura.ukupanIznos">Ukupan Iznos</Translate>
            </span>
          </dt>
          <dd>{izlaznaFakturaEntity.ukupanIznos}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="myApp.izlaznaFaktura.status">Status</Translate>
            </span>
          </dt>
          <dd>{izlaznaFakturaEntity.status}</dd>
          <dt>
            <Translate contentKey="myApp.izlaznaFaktura.kupac">Kupac</Translate>
          </dt>
          <dd>{izlaznaFakturaEntity.kupac ? izlaznaFakturaEntity.kupac.id : ''}</dd>
          <dt>
            <Translate contentKey="myApp.izlaznaFaktura.preduzece">Preduzece</Translate>
          </dt>
          <dd>{izlaznaFakturaEntity.preduzece ? izlaznaFakturaEntity.preduzece.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/izlazna-faktura" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/izlazna-faktura/${izlaznaFakturaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
        <Button onClick={downloadFunkcija}>
                <a ref={downloadRef}>Download</a>
          </Button>

          <a href="http://localhost:8080/api/izlazne-faktures/11/export">SKINI OVDE</a>
      </Col>
    </Row>
  );
};

export default IzlaznaFakturaDetail;
