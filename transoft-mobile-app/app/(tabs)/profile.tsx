import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { router } from 'expo-router';
import React, { useEffect, useState } from "react";
import { Alert, Button, Text, View } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";
import Card from "../components/Card";
import PassengerPresenter from '../model/PassengerPresenter';
import { getPassengerAccount } from '../services/passenger.service';
import { storage } from '../utils/Storage';

export default function ProfileScreen() {
  
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

  const [loading, setLoading] = useState<boolean>(false);
      const [accountInfo, setAccountInfo] = useState<PassengerPresenter | null>(null);
  
      const loadUserAccountInfo = async () => {
              if (loading) return;
      
              setLoading(true);
              try {
                  const response = await getPassengerAccount();
                  
                  if (response.status === 200) {
                      const newData = response.data;
                                          
                      setAccountInfo(newData);
                  }
              } catch (error) {
                  console.error('Error loading itineraries:', error);
              } finally {
                  setLoading(false);
              }
          };
      
          useEffect(() => {
            loadUserAccountInfo();
          }, []);
  
  return (
    <SafeAreaProvider>
        <SafeAreaView style={{ padding: 20, display: 'flex', flexDirection: 'column', justifyContent: 'space-between', flex: 1 }}>
          <View>
            <Card>
              <View style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                <Text style={{fontWeight: 'bold'}}>Dados Pessoais</Text>
                <MaterialIcons name="edit" size={24} color="black" />
              </View>
              <Text>Nome: {accountInfo?.name}</Text>
              <Text>Email: {accountInfo?.email}</Text>
              <Text>Telefone: ({accountInfo?.phoneNumber.ddd}) {accountInfo?.phoneNumber.number}</Text>
            </Card>
            <Card>
              <View style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                <Text style={{fontWeight: 'bold'}}>Endereco</Text>
              </View>
              <Text>CEP: {accountInfo?.address.cep}</Text>
              <Text>Rua: {accountInfo?.address.street}</Text>
              <Text>Bairro: {accountInfo?.address.district}</Text>
              <Text>Numero: {accountInfo?.address.number}</Text>
              {accountInfo?.address.complement && <Text>Complemento: {accountInfo.address.complement}</Text>}
            </Card>
          </View>
          <View>
          <Button 
              title='Sair' 
              color='red' 
              onPress={handleLogout}
            />
          </View>
        </SafeAreaView>
    </SafeAreaProvider>
  );
}