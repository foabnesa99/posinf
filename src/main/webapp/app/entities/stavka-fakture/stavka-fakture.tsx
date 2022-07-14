import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStavkaFakture } from 'app/shared/model/stavka-fakture.model';
import { getEntities } from './stavka-fakture.reducer';

export const StavkaFakture = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const stavkaFaktureList = useAppSelector(state => state.stavkaFakture.entities);
  const loading = useAppSelector(state => state.stavkaFakture.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="stavka-fakture-heading" data-cy="StavkaFaktureHeading">
        <Translate contentKey="myApp.stavkaFakture.home.title">Stavka Faktures</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.stavkaFakture.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/stavka-fakture/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.stavkaFakture.home.createLabel">Create new Stavka Fakture</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {stavkaFaktureList && stavkaFaktureList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.kolicina">Kolicina</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.jedinicnaCena">Jedinicna Cena</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.rabat">Rabat</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.procenatPDV">Procenat PDV</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.iznosPDV">Iznos PDV</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.ukupnaCena">Ukupna Cena</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.izlaznaFaktura">Izlazna Faktura</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.stavkaFakture.robaIliUsluga">Roba Ili Usluga</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {stavkaFaktureList.map((stavkaFakture, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/stavka-fakture/${stavkaFakture.id}`} color="link" size="sm">
                      {stavkaFakture.id}
                    </Button>
                  </td>
                  <td>{stavkaFakture.kolicina}</td>
                  <td>{stavkaFakture.jedinicnaCena}</td>
                  <td>{stavkaFakture.rabat}</td>
                  <td>{stavkaFakture.procenatPDV}</td>
                  <td>{stavkaFakture.iznosPDV}</td>
                  <td>{stavkaFakture.ukupnaCena}</td>
                  <td>
                    {stavkaFakture.izlaznaFaktura ? (
                      <Link to={`/izlazna-faktura/${stavkaFakture.izlaznaFaktura.id}`}>{stavkaFakture.izlaznaFaktura.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {stavkaFakture.robaIliUsluga ? (
                      <Link to={`/roba-ili-usluga/${stavkaFakture.robaIliUsluga.id}`}>{stavkaFakture.robaIliUsluga.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/stavka-fakture/${stavkaFakture.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/stavka-fakture/${stavkaFakture.id}/edit`}
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
                        to={`/stavka-fakture/${stavkaFakture.id}/delete`}
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
              <Translate contentKey="myApp.stavkaFakture.home.notFound">No Stavka Faktures found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default StavkaFakture;
