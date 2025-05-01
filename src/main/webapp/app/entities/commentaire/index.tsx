import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Commentaire from './commentaire';
import CommentaireDetail from './commentaire-detail';
import CommentaireUpdate from './commentaire-update';
import CommentaireDeleteDialog from './commentaire-delete-dialog';

const CommentaireRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Commentaire />} />
    <Route path="new" element={<CommentaireUpdate />} />
    <Route path=":id">
      <Route index element={<CommentaireDetail />} />
      <Route path="edit" element={<CommentaireUpdate />} />
      <Route path="delete" element={<CommentaireDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommentaireRoutes;
