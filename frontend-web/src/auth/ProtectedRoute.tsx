import type { PropsWithChildren } from "react";
import type User from "../models/auth/User";
import { Navigate, Outlet } from "react-router";
import { getLoggedUser } from "../services/user.service";

type ProtectedRouteProps = PropsWithChildren & {
    allowedRoles?: User['role'];
};
  
export default function ProtectedRoute({
    allowedRoles,
}: ProtectedRouteProps) {
    console.log(allowedRoles);
    // const apiToken = sessionStorage.getItem("apiToken");

    // if (!apiToken) {
    //     return <Navigate to="/login" replace/>
    // }

    // const user = getUser(apiToken).then(user => user).catch(() => null);
  
    // if (user === null || user === undefined) {
    //     return <Navigate to="/login" replace/>
    // }
  
    // if (allowedRoles && !allowedRoles.includes(user.role)) {
    //   return <div>Permission denied</div>;
    // }
  
    return <Outlet />;
}