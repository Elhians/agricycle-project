import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Signalement from './signalement';
import SignalementDetail from './signalement-detail';
import SignalementUpdate from './signalement-update';
import SignalementDeleteDialog from './signalement-delete-dialog';

const SignalementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Signalement />} />
    <Route path="new" element={<SignalementUpdate />} />
    <Route path=":id">
      <Route index element={<SignalementDetail />} />
      <Route path="edit" element={<SignalementUpdate />} />
      <Route path="delete" element={<SignalementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SignalementRoutes;
