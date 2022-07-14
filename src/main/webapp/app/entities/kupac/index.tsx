import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Kupac from './kupac';
import KupacDetail from './kupac-detail';
import KupacUpdate from './kupac-update';
import KupacDeleteDialog from './kupac-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={KupacUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={KupacUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={KupacDetail} />
      <ErrorBoundaryRoute path={match.url} component={Kupac} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={KupacDeleteDialog} />
  </>
);

export default Routes;
