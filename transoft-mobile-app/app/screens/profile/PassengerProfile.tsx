import { MaterialIcons } from "@expo/vector-icons";
import { useEffect, useState } from "react";
import { ActivityIndicator, Alert, Text, TouchableOpacity, View } from "react-native";
import AddressInfoCard from "../../components/AddressInfoCard";
import PersonalDataDialog from "../../components/PersonalDataDialog";
import PersonalInfoCard from "../../components/PersonalInfoCard";
import AccountDto from "../../model/AccountDto";
import PassengerPresenter from "../../model/PassengerPresenter";
import { getPassengerAccount, updatePassengerAccount } from "../../services/passenger.service";
import ProfileProps from "./ProfileProps";

export default function PassengerProfile({isDialogVisible, setIsDialogVisible, handleLogout}: ProfileProps) {
  const [loading, setLoading] = useState<boolean>(false);  
  const [passengerAccount, setPassengerAccount] = useState<PassengerPresenter | null>(null);
  
    const handleSavePersonalData = async (data: AccountDto) => {
      try {
        const response = await updatePassengerAccount({
          name: data.name,
          email: data.email,
          phoneNumber: data.phoneNumber
        });
  
        if (response.status === 200) {
          setPassengerAccount(prev => prev ? {...prev, ...data} : null);
          Alert.alert(
            "Sucesso",
            "Dados pessoais atualizados com sucesso!"
          );
        }
  
      } catch (error) {
        console.error('Error loading itineraries:', error);
        Alert.alert(
          "Erro",
          "Não foi possível atualizar os dados pessoais."
        );
      }
    };
  
    const loadPassengerAccountInfo = async () => {
      try {
        setLoading(true);
        const response = await getPassengerAccount();
          
        if (response.status === 200) {
          const newData = response.data;
          setPassengerAccount(newData);
        }
      } catch (error) {
          console.error('Error loading passenger account:', error);
      } finally {
        setLoading(false);
      }
    };
  
    useEffect(() => {
      loadPassengerAccountInfo();
    }, []);

    if (loading) {
      return (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <ActivityIndicator size="large" color="#0066FF" />
        </View>
      )
    }

    if (!passengerAccount) {
      return (
        <View>
          <Text style={{ fontSize: 16, fontWeight: 'bold' }}>Erro ao carregar passageiro</Text>
        </View>
      )
    }
  
    return (
      <>
        <View style={{ gap: 16 }}>
          <PersonalInfoCard account={{name: passengerAccount.name, email: passengerAccount.email, phoneNumber: passengerAccount.phoneNumber}} setIsDialogVisible={setIsDialogVisible}/>
          <AddressInfoCard address={passengerAccount.address} />
        </View>
  
        <TouchableOpacity
          onPress={handleLogout}
          style={{
            backgroundColor: '#ff4444',
            padding: 16,
            borderRadius: 8,
            marginTop: 20,
            alignItems: 'center',
            flexDirection: 'row',
            justifyContent: 'center',
            gap: 8
          }}
        >
        <MaterialIcons name="logout" size={24} color="white" />
          <Text style={{ color: 'white', fontSize: 16, fontWeight: 'bold' }}>Sair</Text>
        </TouchableOpacity>
  
        {passengerAccount && (
          <PersonalDataDialog
            visible={isDialogVisible}
            onClose={() => setIsDialogVisible(false)}
            onSave={handleSavePersonalData}
            initialData={{
              name: passengerAccount.name,
              email: passengerAccount.email,
              phoneNumber: passengerAccount.phoneNumber
            }}
          />
        )}
      </>
    )
  }