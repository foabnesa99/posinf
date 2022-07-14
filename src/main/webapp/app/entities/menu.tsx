import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/kupac">
        <Translate contentKey="global.menu.entities.kupac" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/izlazna-faktura">
        <Translate contentKey="global.menu.entities.izlaznaFaktura" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/roba-ili-usluga">
        <Translate contentKey="global.menu.entities.robaIliUsluga" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cena">
        <Translate contentKey="global.menu.entities.cena" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/poreska-kategorija">
        <Translate contentKey="global.menu.entities.poreskaKategorija" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/poreska-stopa">
        <Translate contentKey="global.menu.entities.poreskaStopa" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/jedinica-mere">
        <Translate contentKey="global.menu.entities.jedinicaMere" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/poslovna-godina">
        <Translate contentKey="global.menu.entities.poslovnaGodina" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stavka-fakture">
        <Translate contentKey="global.menu.entities.stavkaFakture" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/preduzece">
        <Translate contentKey="global.menu.entities.preduzece" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
