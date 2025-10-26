import { MaterialIcons } from "@expo/vector-icons";
import { Text, TouchableOpacity, View } from "react-native";
import AccountDto from "../model/AccountDto";
import Card from "./Card";

interface PersonalInfoCardProps {
    account: AccountDto;
    setIsDialogVisible: (visible: boolean) => void;
}

export default function PersonalInfoCard({account, setIsDialogVisible}: PersonalInfoCardProps) {
    return (
        <Card>
            <View style={{ 
            display: 'flex', 
            flexDirection: 'row', 
            justifyContent: 'space-between', 
            alignItems: 'center',
            marginBottom: 16
            }}>
            <Text style={{ fontSize: 18, fontWeight: 'bold', color: '#333' }}>Dados Pessoais</Text>
            <TouchableOpacity 
                onPress={() => setIsDialogVisible(true)}
                style={{
                padding: 8,
                borderRadius: 20,
                backgroundColor: '#f0f0f0'
                }}
            >
                <MaterialIcons name="edit" size={20} color="#666" />
            </TouchableOpacity>
            </View>
            <View style={{ gap: 8 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <MaterialIcons name="person" size={20} color="#666" />
                <Text style={{ fontSize: 16, color: '#444' }}>{account.name}</Text>
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <MaterialIcons name="email" size={20} color="#666" />
                <Text style={{ fontSize: 16, color: '#444' }}>{account.email}</Text>
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <MaterialIcons name="phone" size={20} color="#666" />
                <Text style={{ fontSize: 16, color: '#444' }}>({account.phoneNumber.ddd}) {account.phoneNumber.number}</Text>
            </View>
            </View>
        </Card>
    )
}