import { useAuth } from '@/src/contexts/AuthContext';
import { router } from 'expo-router';
import React, { useState } from "react";
import { Alert } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";
import DriverProfile from '../screens/profile/DriverProfile';
import PassengerProfile from '../screens/profile/PassengerProfile';
import { storage } from '../utils/Storage';

export default function ProfileScreen() {
  const { user, setUser } = useAuth();
  const [isDialogVisible, setIsDialogVisible] = useState<boolean>(false);
  
  const handleLogout = async () => {
    Alert.alert(
      "Confirmação",
      "Tem certeza que deseja sair?",
      [
        {
          text: "Cancelar",
          style: "cancel"
        },
        {
          text: "Sair",
          style: "destructive",
          onPress: async () => {
            try {
              setUser(null);
              await storage.removeToken();
              router.replace('/login');
            } catch (error) {
              console.error('Error logging out:', error);
            }
          }
        }
      ]
    );
  };

  if (!user) {
    return;
  }
 
  return (
    <SafeAreaProvider>
        <SafeAreaView style={{ 
          padding: 20, 
          display: 'flex', 
          flexDirection: 'column', 
          justifyContent: 'space-between', 
          flex: 1,
          backgroundColor: '#f5f5f5' 
        }}>
          {
            user.role === 'DRIVER' ? (
              <DriverProfile 
                isDialogVisible={isDialogVisible}
                setIsDialogVisible={setIsDialogVisible}
                handleLogout={handleLogout}
              />
            ) : (
              <PassengerProfile
                isDialogVisible={isDialogVisible}
                setIsDialogVisible={setIsDialogVisible}
                handleLogout={handleLogout}
              />
            )
          }
        </SafeAreaView>
    </SafeAreaProvider>
  );
}