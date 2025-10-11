import React, { createContext, useContext, type PropsWithChildren } from "react";
import type LoginDto from "../models/auth/LoginDto";
import { login } from "../services/login.service";

interface AuthContext {
    token?: string | null;
    handleLogin: (email: string, password: string) => Promise<void>;
    handleLogout: () => void;
}

const AuthContext = createContext<AuthContext | undefined>(undefined);

type AuthProviderProps = PropsWithChildren;

export default function AuthProvider({ children } : AuthProviderProps) {
    const [token, setToken] = React.useState<string | null>();
    async function handleLogin(email: string, password: string) {
      try {
        const loginDto: LoginDto = {
          email: email,
          password: password
        }

        const response = await login(loginDto);
  
        const { token } = response.data;
  
        setToken(token);
        sessionStorage.setItem("apiToken", token);
      } catch {
        sessionStorage.removeItem("apiToken");
        setToken(null);          
      }
    }

    function handleLogout() {
        sessionStorage.removeItem("apiToken");
        setToken(null);
    }


    return (
        <AuthContext.Provider
          value={{
            token,
            handleLogin,
            handleLogout,
          }}
        >
          {children}
        </AuthContext.Provider>
      );
}

export function useAuth() {
    const context = useContext(AuthContext);
  
    if (context === undefined) {
      throw new Error('useAuth must be used inside of a AuthProvider');
    }
  
    return context;
}