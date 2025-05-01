import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ContenuEducatif from './contenu-educatif';
import ContenuEducatifDetail from './contenu-educatif-detail';
import ContenuEducatifUpdate from './contenu-educatif-update';
import ContenuEducatifDeleteDialog from './contenu-educatif-delete-dialog';

const ContenuEducatifRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ContenuEducatif />} />
    <Route path="new" element={<ContenuEducatifUpdate />} />
    <Route path=":id">
      <Route index element={<ContenuEducatifDetail />} />
      <Route path="edit" element={<ContenuEducatifUpdate />} />
      <Route path="delete" element={<ContenuEducatifDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContenuEducatifRoutes;
