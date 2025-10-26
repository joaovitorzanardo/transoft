import { MaterialIcons } from "@expo/vector-icons";
import { router } from "expo-router";
import { ActivityIndicator, Button, Text, View } from "react-native";
import { formatType } from "../utils/Format";
import ItineraryView from "../views/ItineraryView";
import Card from "./Card";
import Chip from "./Chip";

interface ItineraryCardProps {
    itinerary: ItineraryView;
    loading?: boolean;
}

export default function ItineraryCard({ itinerary, loading = false }: ItineraryCardProps) {
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

    if (loading) {
        return (
            <Card>
                <View style={{ padding: 16, alignItems: 'center', justifyContent: 'center', height: 150 }}>
                    <ActivityIndicator size="large" color="#007AFF" />
                </View>
            </Card>
        )
    }
    
    return (
        <Card>
            <View>
                <Text>{formatType(itinerary.type)}</Text>
            </View>
            <View style={{display: 'flex', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center'}}>
                <Text style={{fontWeight: 'bold'}}>{itinerary.routeName}</Text>
                {getItineraryStatusChip(itinerary.status)}
            </View>
            <Text>{itinerary.schoolName}</Text>
            <View style={{display: 'flex', flexDirection: 'row', justifyContent: 'flex-start', alignItems: 'center'}}>
                <MaterialIcons name="schedule" size={15} color="#666" />
                <Text>{itinerary.startTime} - {itinerary.endTime}</Text>
            </View>
            <Button title="Detalhes" onPress={() => router.navigate(`/itineraries/${itinerary.id}`)}/>
        </Card>
    )
}