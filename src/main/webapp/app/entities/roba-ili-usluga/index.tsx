import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RobaIliUsluga from './roba-ili-usluga';
import RobaIliUslugaDetail from './roba-ili-usluga-detail';
import RobaIliUslugaUpdate from './roba-ili-usluga-update';
import RobaIliUslugaDeleteDialog from './roba-ili-usluga-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RobaIliUslugaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RobaIliUslugaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RobaIliUslugaDetail} />
      <ErrorBoundaryRoute path={match.url} component={RobaIliUsluga} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RobaIliUslugaDeleteDialog} />
  </>
);

export default Routes;
