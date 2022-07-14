import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPoslovnaGodina } from 'app/shared/model/poslovna-godina.model';
import { getEntities } from './poslovna-godina.reducer';

export const PoslovnaGodina = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const poslovnaGodinaList = useAppSelector(state => state.poslovnaGodina.entities);
  const loading = useAppSelector(state => state.poslovnaGodina.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="poslovna-godina-heading" data-cy="PoslovnaGodinaHeading">
        <Translate contentKey="myApp.poslovnaGodina.home.title">Poslovna Godinas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.poslovnaGodina.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/poslovna-godina/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.poslovnaGodina.home.createLabel">Create new Poslovna Godina</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {poslovnaGodinaList && poslovnaGodinaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.poslovnaGodina.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.poslovnaGodina.godina">Godina</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.poslovnaGodina.zakljucena">Zakljucena</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.poslovnaGodina.preduzece">Preduzece</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {poslovnaGodinaList.map((poslovnaGodina, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/poslovna-godina/${poslovnaGodina.id}`} color="link" size="sm">
                      {poslovnaGodina.id}
                    </Button>
                  </td>
                  <td>{poslovnaGodina.godina}</td>
                  <td>{poslovnaGodina.zakljucena ? 'true' : 'false'}</td>
                  <td>
                    {poslovnaGodina.preduzece ? (
                      <Link to={`/preduzece/${poslovnaGodina.preduzece.id}`}>{poslovnaGodina.preduzece.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/poslovna-godina/${poslovnaGodina.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/poslovna-godina/${poslovnaGodina.id}/edit`}
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
                        to={`/poslovna-godina/${poslovnaGodina.id}/delete`}
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
              <Translate contentKey="myApp.poslovnaGodina.home.notFound">No Poslovna Godinas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PoslovnaGodina;
