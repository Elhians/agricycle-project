import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Entreprise from './entreprise';
import EntrepriseDetail from './entreprise-detail';
import EntrepriseUpdate from './entreprise-update';
import EntrepriseDeleteDialog from './entreprise-delete-dialog';

const EntrepriseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Entreprise />} />
    <Route path="new" element={<EntrepriseUpdate />} />
    <Route path=":id">
      <Route index element={<EntrepriseDetail />} />
      <Route path="edit" element={<EntrepriseUpdate />} />
      <Route path="delete" element={<EntrepriseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EntrepriseRoutes;
