import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StavkaFakture from './stavka-fakture';
import StavkaFaktureDetail from './stavka-fakture-detail';
import StavkaFaktureUpdate from './stavka-fakture-update';
import StavkaFaktureDeleteDialog from './stavka-fakture-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StavkaFaktureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StavkaFaktureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StavkaFaktureDetail} />
      <ErrorBoundaryRoute path={match.url} component={StavkaFakture} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={StavkaFaktureDeleteDialog} />
  </>
);

export default Routes;
