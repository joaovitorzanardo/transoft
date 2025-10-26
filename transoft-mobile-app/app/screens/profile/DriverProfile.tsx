import { MaterialIcons } from "@expo/vector-icons";
import { useEffect, useState } from "react";
import { ActivityIndicator, Alert, Text, TouchableOpacity, View } from "react-native";
import PersonalDataDialog from "../../components/PersonalDataDialog";
import PersonalInfoCard from "../../components/PersonalInfoCard";
import AccountDto from "../../model/AccountDto";
import DriverPresenter from "../../model/DriverPresenter";
import { getDriverAccount, updateDriverAccount } from "../../services/driver.service";
import ProfileProps from "./ProfileProps";

export default function DriverProfile({isDialogVisible, setIsDialogVisible, handleLogout}: ProfileProps) {
    const [loading, setLoading] = useState<boolean>(false);
    const [driverAccount, setDriverAccount] = useState<DriverPresenter | null>(null);
  
    const handleSavePersonalData = async (data: AccountDto) => {
      try {
        const response = await updateDriverAccount({
          name: data.name,
          email: data.email,
          phoneNumber: data.phoneNumber
        });
  
        if (response.status === 200) {
          setDriverAccount(prev => prev ? {...prev, ...data} : null);
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
  
    const loadDriverAccountInfo = async () => {
        try {
            setLoading(true);
            const response = await getDriverAccount();
            
            if (response.status === 200) {
                const newData = response.data;
                setDriverAccount(newData);
            }
        } catch (error) {
            console.error('Error loading driver account:', error);
        } finally {
            setLoading(false);
        }
    };
      
    useEffect(() => {
        loadDriverAccountInfo();
    }, []);

    if (loading) {
        return (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <ActivityIndicator size="large" color="#0066FF" />
            </View>
        )
    }

    if (!driverAccount) {
        return (
            <View>
                <Text style={{ fontSize: 16, fontWeight: 'bold' }}>Erro ao carregar motorista</Text>
            </View>
        )
    }
  
    return (
      <>
        <View style={{ gap: 16 }}>
          <PersonalInfoCard account={{name: driverAccount.name, email: driverAccount.email, phoneNumber: driverAccount.phoneNumber}} setIsDialogVisible={setIsDialogVisible}/>
        </View>
  
        <TouchableOpacity
          onPress={handleLogout}
          style={{
            backgroundColor: '#ff4444',
            padding: 16,
            borderRadius: 8,
            alignItems: 'center',
            flexDirection: 'row',
            justifyContent: 'center',
            gap: 8
          }}
        >
          <MaterialIcons name="logout" size={24} color="white" />
          <Text style={{ color: 'white', fontSize: 16, fontWeight: 'bold' }}>Sair</Text>
        </TouchableOpacity>
  
        {driverAccount && (
          <PersonalDataDialog
            visible={isDialogVisible}
            onClose={() => setIsDialogVisible(false)}
            onSave={handleSavePersonalData}
            initialData={{
              name: driverAccount.name,
              email: driverAccount.email,
              phoneNumber: driverAccount.phoneNumber
            }}
          />
        )}
      </>
    )
}