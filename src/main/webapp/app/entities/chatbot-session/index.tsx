import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ChatbotSession from './chatbot-session';
import ChatbotSessionDetail from './chatbot-session-detail';
import ChatbotSessionUpdate from './chatbot-session-update';
import ChatbotSessionDeleteDialog from './chatbot-session-delete-dialog';

const ChatbotSessionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ChatbotSession />} />
    <Route path="new" element={<ChatbotSessionUpdate />} />
    <Route path=":id">
      <Route index element={<ChatbotSessionDetail />} />
      <Route path="edit" element={<ChatbotSessionUpdate />} />
      <Route path="delete" element={<ChatbotSessionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChatbotSessionRoutes;
