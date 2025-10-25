import { useEffect, useState } from "react";
import { Text, View } from "react-native";
import ItineraryCard from "../components/ItineraryCard";
import { getNextItinerary } from "../services/itinerary.service";
import ItineraryView from "../views/ItineraryView";

interface ItineraryAccount {
    date: string;
    itinerary: ItineraryView[];
}

export default function HomeScreen() {
    const [loading, setLoading] = useState<boolean>(false);
    const [itinerary, setItinerary] = useState<ItineraryAccount | null>(null);

    const loadItineraries = async () => {
            if (loading) return;
    
            setLoading(true);
            try {
                const response = await getNextItinerary();
                
                if (response.status === 200) {
                    const newData = response.data;
                                        
                    setItinerary(newData);
                }
            } catch (error) {
                console.error('Error loading itineraries:', error);
            } finally {
                setLoading(false);
            }
        };
    
        useEffect(() => {
            loadItineraries();
        }, []);

    return (
        <View style={{ flex: 1, padding: 16 }}>
            <Text style={{ marginBottom: 16, fontSize: 24, fontWeight: 'bold' }}>Próxima Viagem</Text>
            {itinerary && itinerary.date && <Text>{itinerary?.date}</Text>}
            {itinerary && itinerary.itinerary.length > 0 && <ItineraryCard itinerary={itinerary.itinerary[0]}/>}
        </View>
    )
        
}