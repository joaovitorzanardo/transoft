/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, type PropsWithChildren } from "react";
import type LoginDto from "../models/auth/LoginDto";
import { login } from "../services/login.service";

interface AuthContext {
  handleLogin: (email: string, password: string) => Promise<void>;
}

const AuthContext = createContext<AuthContext | undefined>(undefined);

type AuthProviderProps = PropsWithChildren;

export default function AuthProvider({ children }: AuthProviderProps) {
  async function handleLogin(email: string, password: string) {

    const loginDto: LoginDto = {
      email: email,
      password: password
    }

    await login(loginDto);
  }

  return (
    <AuthContext.Provider
      value={{
        handleLogin
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