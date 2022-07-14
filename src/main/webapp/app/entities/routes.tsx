import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Kupac from './kupac';
import IzlaznaFaktura from './izlazna-faktura';
import RobaIliUsluga from './roba-ili-usluga';
import Cena from './cena';
import PoreskaKategorija from './poreska-kategorija';
import PoreskaStopa from './poreska-stopa';
import JedinicaMere from './jedinica-mere';
import PoslovnaGodina from './poslovna-godina';
import StavkaFakture from './stavka-fakture';
import Preduzece from './preduzece';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}kupac`} component={Kupac} />
        <ErrorBoundaryRoute path={`${match.url}izlazna-faktura`} component={IzlaznaFaktura} />
        <ErrorBoundaryRoute path={`${match.url}roba-ili-usluga`} component={RobaIliUsluga} />
        <ErrorBoundaryRoute path={`${match.url}cena`} component={Cena} />
        <ErrorBoundaryRoute path={`${match.url}poreska-kategorija`} component={PoreskaKategorija} />
        <ErrorBoundaryRoute path={`${match.url}poreska-stopa`} component={PoreskaStopa} />
        <ErrorBoundaryRoute path={`${match.url}jedinica-mere`} component={JedinicaMere} />
        <ErrorBoundaryRoute path={`${match.url}poslovna-godina`} component={PoslovnaGodina} />
        <ErrorBoundaryRoute path={`${match.url}stavka-fakture`} component={StavkaFakture} />
        <ErrorBoundaryRoute path={`${match.url}preduzece`} component={Preduzece} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
