import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIzlaznaFaktura } from 'app/shared/model/izlazna-faktura.model';
import { getEntities } from './izlazna-faktura.reducer';

export const IzlaznaFaktura = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const izlaznaFakturaList = useAppSelector(state => state.izlaznaFaktura.entities);
  const loading = useAppSelector(state => state.izlaznaFaktura.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="izlazna-faktura-heading" data-cy="IzlaznaFakturaHeading">
        <Translate contentKey="myApp.izlaznaFaktura.home.title">Izlazna Fakturas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.izlaznaFaktura.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/izlazna-faktura/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.izlaznaFaktura.home.createLabel">Create new Izlazna Faktura</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {izlaznaFakturaList && izlaznaFakturaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.brojFakture">Broj Fakture</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.datumIzdavanja">Datum Izdavanja</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.datumValute">Datum Valute</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.ukupnaOsnovica">Ukupna Osnovica</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.ukupanRabat">Ukupan Rabat</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.ukupanIznos">Ukupan Iznos</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.kupac">Kupac</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.izlaznaFaktura.preduzece">Preduzece</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {izlaznaFakturaList.map((izlaznaFaktura, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/izlazna-faktura/${izlaznaFaktura.id}`} color="link" size="sm">
                      {izlaznaFaktura.id}
                    </Button>
                  </td>
                  <td>{izlaznaFaktura.brojFakture}</td>
                  <td>
                    {izlaznaFaktura.datumIzdavanja ? (
                      <TextFormat type="date" value={izlaznaFaktura.datumIzdavanja} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {izlaznaFaktura.datumValute ? (
                      <TextFormat type="date" value={izlaznaFaktura.datumValute} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{izlaznaFaktura.ukupnaOsnovica}</td>
                  <td>{izlaznaFaktura.ukupanRabat}</td>
                  <td>{izlaznaFaktura.ukupanIznos}</td>
                  <td>
                    <Translate contentKey={`myApp.StatusFakture.${izlaznaFaktura.status}`} />
                  </td>
                  <td>{izlaznaFaktura.kupac ? <Link to={`/kupac/${izlaznaFaktura.kupac.id}`}>{izlaznaFaktura.kupac.id}</Link> : ''}</td>
                  <td>
                    {izlaznaFaktura.preduzece ? (
                      <Link to={`/preduzece/${izlaznaFaktura.preduzece.id}`}>{izlaznaFaktura.preduzece.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/izlazna-faktura/${izlaznaFaktura.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/izlazna-faktura/${izlaznaFaktura.id}/edit`}
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
                        tag={Link}
                        to={`/izlazna-faktura/${izlaznaFaktura.id}/delete`}
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
              <Translate contentKey="myApp.izlaznaFaktura.home.notFound">No Izlazna Fakturas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default IzlaznaFaktura;
