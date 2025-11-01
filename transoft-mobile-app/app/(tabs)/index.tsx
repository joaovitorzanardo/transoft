import { useEffect, useState } from "react";
import { RefreshControl, ScrollView, Text, View } from "react-native";
import ItineraryCard from "../components/ItineraryCard";
import { getNextItinerary, getOngoingItinerary } from "../services/itinerary.service";
import { formatDayOfWeek } from "../utils/Format";
import ItineraryView from "../views/ItineraryView";

interface ItineraryAccount {
    date: string;
    itinerary: ItineraryView[];
}

export default function HomeScreen() {
    const [loading, setLoading] = useState<boolean>(false);
    const [refreshing, setRefreshing] = useState<boolean>(false);
    const [itinerary, setItinerary] = useState<ItineraryAccount | null>(null);
    const [ongoingItinerary, setOngoingItinerary] = useState<ItineraryAccount | null>(null);

    const loadItineraries = async () => {
        if (loading) return;

        setLoading(true);
        try {
            const ongoingResponse = await getOngoingItinerary();
            const nextResponse = await getNextItinerary();
            
            if (ongoingResponse.status === 200) {
                setOngoingItinerary(ongoingResponse.data);
            } else if (ongoingResponse.status === 204) {
                setOngoingItinerary(null);
            }
            
            if (nextResponse.status === 200) {
                setItinerary(nextResponse.data);
            } else if (nextResponse.status === 204) {
                setItinerary(null);
            }
        } catch (error) {
            console.error('Error loading itineraries:', error);
        } finally {
            setLoading(false);
            setRefreshing(false);
        }
    };

    const onRefresh = () => {
        setRefreshing(true);
        loadItineraries();
    };

    useEffect(() => {
        loadItineraries();
    }, []);

    return (
        <ScrollView 
            style={{ flex: 1 }}
            refreshControl={
                <RefreshControl
                    refreshing={refreshing}
                    onRefresh={onRefresh}
                />
            }
        >
            <View style={{ padding: 16 }}>
                {ongoingItinerary && (
                    <>
                        <Text style={{ marginBottom: 16, fontSize: 24, fontWeight: 'bold' }}>Viagem em Andamento</Text>
                        {ongoingItinerary.date && <Text>{formatDayOfWeek(ongoingItinerary.date)}, {ongoingItinerary.date}</Text>}
                        {ongoingItinerary.itinerary.length > 0 && <ItineraryCard itinerary={ongoingItinerary.itinerary[0]}/>}
                    </>
                )}
                {itinerary && (
                    <View style={{ marginTop: ongoingItinerary ? 24 : 0 }}>
                        <Text style={{ marginBottom: 16, fontSize: 24, fontWeight: 'bold' }}>Próxima Viagem</Text>
                        {itinerary.date && <Text>{formatDayOfWeek(itinerary.date)}, {itinerary.date}</Text>}
                        {itinerary.itinerary.length > 0 && <ItineraryCard itinerary={itinerary.itinerary[0]}/>}
                    </View>
                )}
            </View>
        </ScrollView>
    )
}