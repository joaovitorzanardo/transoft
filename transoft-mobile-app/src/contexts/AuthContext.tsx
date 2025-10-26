import UserAccountDto from '@/app/model/UserAccountDto';
import React, { createContext, ReactNode, useContext, useState } from 'react';

interface AuthContextData {
  user: UserAccountDto | null;
  setUser: (user: UserAccountDto | null) => void;
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<UserAccountDto | null>(null);

  return (
    <AuthContext.Provider value={{ user, setUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  
  return context;
};
