import Chip from "@/app/components/Chip";
import ItineraryPresenter from "@/app/model/ItineraryPresenter";
import PassengerItineraryPresenter from "@/app/model/PassengerItineraryPresenter";
import DriverItinerary from "@/app/screens/itinerary/DriverItinerary";
import { cancelItineraryForPassenger, confirmItineraryForPassenger, getItineraryById, getPassengersFromItinerary } from "@/app/services/itinerary.service";
import { formatStatus, formatType } from "@/app/utils/Format";
import { useAuth } from "@/src/contexts/AuthContext";
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useLocalSearchParams } from "expo-router";
import { useEffect, useState } from "react";
import { ActivityIndicator, Alert, FlatList, Text, TouchableOpacity, View } from "react-native";
import MapView, { Marker } from "react-native-maps";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

interface Passengers {
    id: string;
    name: string;
    email: string;
    status: 'CONFIRMADO' | 'NAO_VAI';
}

export default function TripDetailsScreen() {
    const { user } = useAuth();
    const [status, setStatus] = useState<'CONFIRMADO' | 'NAO_VAI' | null>(null);
    const [itinerary, setItinerary] = useState<ItineraryPresenter | null>(null);
    const [passengers, setPassengers] = useState<Passengers[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const { id } = useLocalSearchParams<{ id: string }>();

    function getItineraryStatusChip(status: 'AGENDADO' | 'EM_ANDAMENTO' | 'CANCELADO' | 'CONCLUIDO' | undefined) {
        switch (status) {
            case 'AGENDADO':
                return <Chip text="Agendado" color="default" />;
            case 'EM_ANDAMENTO':
                return <Chip text="Em Andamento" color="warning" />;
            case 'CANCELADO':
                return <Chip text="Cancelado" color="error" />;
            case 'CONCLUIDO':
                return <Chip text="Concluído" color="success" />;
            default:
                return '';
        }
    }

    useEffect(() => {
        async function loadItinerary() {
            try {
                const response = await getItineraryById(id);
                
                if (response.status === 200) {
                    const newData = response.data;
                    setItinerary(newData);
                }
            } catch (error) {
                console.error('Error loading itinerary:', error);
            }
        };

        async function loadPassengers() {
            try {
                const response = await getPassengersFromItinerary(id);
                
                if (response.status === 200) {
                    const status = response.data.filter((item: PassengerItineraryPresenter) => item.passenger.email === user?.email)[0]?.status;
                    setStatus(status);
                    const newData = response.data.map((item: PassengerItineraryPresenter) => {
                        return {
                            id: item.passenger.passengerId,
                            name: item.passenger.name,
                            email: item.passenger.email,
                            status: item.status
                        }
                    });
                    setPassengers(newData);
                }
            } catch (error) {
                console.error('Error loading itinerary:', error);
            }
        }
        setLoading(true);
        loadItinerary();
        loadPassengers();
        setLoading(false);
    }, [id]);

    const handleCancelItinerary = () => {
        Alert.alert('Confirma cancelamento', 'Tem certeza que deseja cancelar sua participação neste itinerário?', [
            {
                text: 'Não'
            },
            {
                text: 'Sim',
                onPress: async () => {
                    try {
                       const response = await cancelItineraryForPassenger(id);
                       
                       if (response.status === 200) {
                           setStatus("NAO_VAI");
                           setPassengers(passengers.map(passenger => 
                               passenger.email === user?.email 
                               ? { ...passenger, status: 'NAO_VAI' }
                               : passenger
                           ));
                           Alert.alert('Sucesso', 'Sua participação foi cancelada com sucesso.');
                       }
                    } catch (error) {
                        console.error('Error cancelling trip:', error);
                    }
                }
            }
        ]);
    }

    const handleConfirmItinerary = () => {
        Alert.alert('Confirma participação', 'Tem certeza que deseja confirmar sua participação neste itinerário?', [
            {
                text: 'Não'
            },
            {
                text: 'Sim',
                onPress: async () => {
                    try {
                       const response = await confirmItineraryForPassenger(id);
                       
                       if (response.status === 200) {
                           setStatus("CONFIRMADO");
                           setPassengers(passengers.map(passenger => 
                               passenger.email === user?.email
                               ? { ...passenger, status: 'CONFIRMADO' }
                               : passenger
                           ));
                           Alert.alert('Sucesso', 'Sua participação foi confirmada com sucesso.');
                       }
                    } catch (error) {
                        console.error('Error confirming trip:', error);
                    }
                }
            }
        ]);
    }

    if (user?.role === 'DRIVER') {
        return (
            <DriverItinerary />
        )
    }
    
    return (
        <SafeAreaProvider>
            <SafeAreaView style={{flex: 1, width: '100%', paddingTop: 20}}>
                {loading ? (
                    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
                        <ActivityIndicator size="large" color="#007AFF" />
                    </View>
                ) : (
                    <>
                        <View style={{flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingBottom: 20, marginLeft: 20, marginRight: 20}}>
                            <Text style={{ fontWeight: 'bold'}}>{itinerary?.route.name}</Text>
                            <Text>{getItineraryStatusChip(itinerary?.status)}</Text>
                        </View>

                        <View style={{ 
                            backgroundColor: 'white',
                            borderRadius: 12,
                            padding: 16,
                            marginHorizontal: 20,
                            marginBottom: 16,
                            shadowColor: "#000",
                            shadowOffset: { width: 0, height: 2 },
                            shadowOpacity: 0.1,
                            shadowRadius: 4,
                            elevation: 3
                        }}>
                            <View style={{ gap: 8 }}>
                                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                                    <MaterialIcons name="category" size={20} color="#666" />
                                    <Text style={{ fontSize: 16, color: '#444' }}>{formatType(itinerary?.type)}</Text>
                                </View>
                                <View style={{ flexDirection: 'row', gap: 8 }}>
                                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                                        <MaterialIcons name="schedule" size={20} color="#666" />
                                        <Text style={{ fontSize: 16, color: '#444' }}>{itinerary?.startTime} - {itinerary?.endTime}</Text>
                                    </View>
                                </View>
                                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                                    <MaterialIcons name="school" size={20} color="#666" />
                                    <Text style={{ fontSize: 16, color: '#444' }}>{itinerary?.route.school.name}</Text>
                                </View>
                                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                                    <MaterialIcons name="person" size={20} color="#666" />
                                    <Text style={{ fontSize: 16, color: '#444' }}>{itinerary?.driver.name}</Text>
                                </View>
                                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                                    <MaterialIcons name="directions-car" size={20} color="#666" />
                                    <Text style={{ fontSize: 16, color: '#444' }}>{itinerary?.vehicle.vehicleModel.automaker.name} - {itinerary?.vehicle.vehicleModel.modelName}</Text>
                                </View>
                                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                                    <MaterialIcons name="tag" size={20} color="#666" />
                                    <Text style={{ fontSize: 16, color: '#444' }}>Placa: {itinerary?.vehicle.plateNumber}</Text>
                                </View>
                            </View>
                        </View>

                        {itinerary?.status === 'EM_ANDAMENTO' && (
                            <MapView style={{height: '40%', width: '100%'}} 
                                provider="google"
                                initialRegion={{
                                  latitude: 37.78825,
                                  longitude: -122.4324,
                                  latitudeDelta: 0.0922,
                                  longitudeDelta: 0.0421,
                                }}
                            >
                                <Marker
                                    coordinate={{ latitude: 37.78825, longitude: -122.4324 }}
                                    title="My Marker"
                                    description="Here is a marker example"
                                />
                            </MapView>
                        )}
                        <TouchableOpacity 
                            style={{
                                backgroundColor: status === 'CONFIRMADO' ? '#ff4444' : '#4CAF50',
                                padding: 10,
                                borderRadius: 5,
                                marginHorizontal: 20,
                                marginVertical: 10,
                            }}
                            onPress={status === 'CONFIRMADO' ? handleCancelItinerary : handleConfirmItinerary}
                        >
                            <Text style={{
                                color: 'white',
                                textAlign: 'center',
                                fontSize: 16,
                                fontWeight: 'bold'
                            }}>
                                {status === 'CONFIRMADO' ? 'Não Vou' : 'Vou'}
                            </Text>
                        </TouchableOpacity>
                        <View style={{
                            backgroundColor: 'white',
                            borderRadius: 12,
                            padding: 16,
                            marginHorizontal: 20,
                            marginBottom: 16,
                            shadowColor: "#000",
                            shadowOffset: { width: 0, height: 2 },
                            shadowOpacity: 0.1,
                            shadowRadius: 4,
                            elevation: 3
                        }}>
                            <Text style={{
                                fontSize: 16,
                                fontWeight: 'bold',
                                color: '#444',
                                marginBottom: 12
                            }}>Passageiros</Text>
                            <FlatList 
                                data={passengers} 
                                renderItem={({item}) => (
                                    <View style={{
                                        flexDirection: 'row', 
                                        justifyContent: 'space-between', 
                                        alignItems: 'center', 
                                        paddingVertical: 12,
                                        borderBottomWidth: 1, 
                                        borderColor: '#eee'
                                    }}>
                                        <Text style={{ fontSize: 14, color: '#666' }}>{item.name}</Text>
                                        <Chip text={formatStatus(item.status)} color={item.status === 'CONFIRMADO' ? 'success' : 'error'} />
                                    </View>
                                )}
                                style={{ maxHeight: 200 }}
                            />
                        </View>
                    </>
                )}
            </SafeAreaView>
        </SafeAreaProvider>
    );
}

