import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import React from "react";
import { Button, Text, View } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";
import Card from "../components/Card";

export default function ProfileScreen() {
  return (
    <SafeAreaProvider>
        <SafeAreaView style={{ padding: 20, display: 'flex', flexDirection: 'column', justifyContent: 'space-between', flex: 1 }}>
          <View>
            <Card>
              <View style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                <Text style={{fontWeight: 'bold'}}>Dados Pessoais</Text>
                <MaterialIcons name="edit" size={24} color="black" />
              </View>
              <Text>Nome: Joao Vitor Zanardo</Text>
              <Text>Email: joaovizan@hotmail.com</Text>
              <Text>Telefone: (54) 99203-1028</Text>
            </Card>
            <Card>
              <View style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                <Text style={{fontWeight: 'bold'}}>Endereco</Text>
                <MaterialIcons name="edit" size={24} color="black" />
              </View>
              <Text>CEP: 99700-036</Text>
              <Text>Rua: Av. Germano Hoffmman</Text>
              <Text>Bairro: Centro</Text>
              <Text>Numero: 155</Text>
              <Text>Complemento: Ap. 101</Text>
            </Card>
            <Card>
              <View style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                <Text style={{fontWeight: 'bold'}}>Plano</Text>
              </View>
              <Text>Valor: R$ 240,00</Text>
            </Card>
          </View>
          <View>
            <Button title='Sair' color='red'/>
          </View>
        </SafeAreaView>
    </SafeAreaProvider>
  );
}