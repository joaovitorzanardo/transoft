import ItineraryCard from "@/app/components/ItineraryCard";
import { getItinerariesHistory } from "@/app/services/itinerary.service";
import { formatDayOfWeek } from "@/app/utils/Format";
import ItineraryView from "@/app/views/ItineraryView";
import { useEffect, useState } from "react";
import { RefreshControl, SectionList, Text, View } from "react-native";

interface ItineraryAccount {
    date: string;
    itinerary: ItineraryView[];
}

interface ItineraryList {
    title: string;
    data: ItineraryView[];
}

export default function HistoryItineraries() {
    const [loading, setLoading] = useState<boolean>(false);
    const [refreshing, setRefreshing] = useState<boolean>(false);
    const [itineraries, setItineraries] = useState<ItineraryList[]>([]);
    const PAGE_SIZE = 10;

    const loadItineraries = async (isRefreshing = false) => {
        if (loading) return;

        setLoading(true);
        setRefreshing(isRefreshing);
        try {
            const response = await getItinerariesHistory(0, PAGE_SIZE);
            
            if (response.status === 200) {
                const newData = response.data.map((itinerary: ItineraryAccount) => ({
                    title: itinerary.date,
                    data: itinerary.itinerary
                }));
                
                setItineraries(prev => isRefreshing ? newData : [...prev, ...newData]);
            }
        } catch (error) {
            console.error('Error loading itineraries:', error);
        } finally {
            setLoading(false);
            setRefreshing(false);
        }
    };

    useEffect(() => {
        loadItineraries();
    }, []);

    return (
        <View style={{ padding: 20 }}>
            <SectionList 
                sections={itineraries}
                refreshControl={
                    <RefreshControl
                        refreshing={refreshing}
                        onRefresh={() => loadItineraries(true)}
                    />
                }
                renderSectionHeader={({section: {title}}) => (
                    <Text>{formatDayOfWeek(title)}, {title}</Text>
                )}
                renderItem={({ item }) => (
                    <ItineraryCard itinerary={item} loading={loading}/>
                )}
                onEndReachedThreshold={0.5}
            />
        </View>
    );
}
