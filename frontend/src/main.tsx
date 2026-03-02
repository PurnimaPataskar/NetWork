import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.scss'
import {createBrowserRouter, RouterProvider} from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <h1>Home</h1>
  },

  {
    path: "/login",
    element: <h1>Login</h1> 
  },

  {
    path: "/signup",
    element: "Signup"
  }

  ,
  {
    path: "/reset-password",
    element: "Reset Password"  
  },

  {
    path: "verify-email",
    element: "Verify Email"
  }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
