import { MaterialIcons } from "@expo/vector-icons";
import { Text, View } from "react-native";
import AddressDto from "../model/AddressDto";
import Card from "./Card";

interface AddressInfoProps {
    address: AddressDto;
}

export default function AddressInfoCard({ address }: AddressInfoProps) {
    return (
        <Card>
            <View style={{ 
              display: 'flex', 
              flexDirection: 'row', 
              justifyContent: 'space-between',
              alignItems: 'center',
              marginBottom: 16
            }}>
              <Text style={{ fontSize: 18, fontWeight: 'bold', color: '#333' }}>Endereço</Text>
            </View>
            <View style={{ gap: 8 }}>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <MaterialIcons name="location-on" size={20} color="#666" />
                <Text style={{ fontSize: 16, color: '#444' }}>CEP: {address.cep}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <MaterialIcons name="home" size={20} color="#666" />
                <Text style={{ fontSize: 16, color: '#444' }}>Rua: {address.street}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <MaterialIcons name="location-city" size={20} color="#666" />
                <Text style={{ fontSize: 16, color: '#444' }}>Bairro: {address.district}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                <MaterialIcons name="tag" size={20} color="#666" />
                <Text style={{ fontSize: 16, color: '#444' }}>Número: {address.number}</Text>
              </View>
              {address.complement && (
                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                  <MaterialIcons name="info" size={20} color="#666" />
                  <Text style={{ fontSize: 16, color: '#444' }}>Complemento: {address.complement}</Text>
                </View>
              )}
            </View>
        </Card>
    )
}