import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.scss'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import { Login } from './features/authentication/pages/Login/Login';
import { Feed } from './features/feed/pages/Feed';
import { Signup } from './features/authentication/pages/Signup/Signup';
import { VerifyEmail } from './features/authentication/pages/VerifyEmail/VerifyEmail';
import { ResetPassword } from './features/authentication/pages/ResetPassword/ResetPassword';
const router = createBrowserRouter([
  {
    path: "/",
    element: <Feed />
  },

  {
    path: "/login",
    element: <Login />
  },

  {
    path: "/signup",
    element: <Signup />
  }

  ,
  {
    path: "/reset-password",
    element: <ResetPassword />
  },

  {
    path: "/verify-email",
    element: <VerifyEmail />
  }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
