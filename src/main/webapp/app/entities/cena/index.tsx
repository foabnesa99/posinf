import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cena from './cena';
import CenaDetail from './cena-detail';
import CenaUpdate from './cena-update';
import CenaDeleteDialog from './cena-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CenaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CenaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CenaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cena} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CenaDeleteDialog} />
  </>
);

export default Routes;
