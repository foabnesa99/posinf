import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IzlaznaFaktura from './izlazna-faktura';
import IzlaznaFakturaDetail from './izlazna-faktura-detail';
import IzlaznaFakturaUpdate from './izlazna-faktura-update';
import IzlaznaFakturaDeleteDialog from './izlazna-faktura-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IzlaznaFakturaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IzlaznaFakturaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IzlaznaFakturaDetail} />
      <ErrorBoundaryRoute path={match.url} component={IzlaznaFaktura} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={IzlaznaFakturaDeleteDialog} />
  </>
);

export default Routes;
