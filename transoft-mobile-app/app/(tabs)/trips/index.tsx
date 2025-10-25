import ItineraryCard from "@/app/components/ItineraryCard";
import { getItineraries } from "@/app/services/itinerary.service";
import ItineraryView from "@/app/views/ItineraryView";
import { useEffect, useState } from "react";
import { ActivityIndicator, SectionList, Text } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

interface ItineraryAccount {
    date: string;
    itinerary: ItineraryView[];
}

interface ItineraryList {
    title: string;
    data: ItineraryView[];
}

export default function TripsScreen() {
    const [loading, setLoading] = useState<boolean>(false);
    const [itineraries, setItineraries] = useState<ItineraryList[]>([]);
    const [page, setPage] = useState<number>(0);
    const [hasMore, setHasMore] = useState<boolean>(true);
    const PAGE_SIZE = 10;

    const loadItineraries = async () => {
        if (loading || !hasMore) return;

        setLoading(true);
        try {
            const response = await getItineraries(page, PAGE_SIZE);
            
            if (response.status === 200) {
                const newData = response.data.map((itinerary: ItineraryAccount) => {
                    return {
                        title: itinerary.date,
                        data: itinerary.itinerary
                    }
                });
                
                if (newData.length < PAGE_SIZE) {
                    setHasMore(false);
                }
                
                setItineraries(prev => [...prev, ...newData]);
                setPage(prev => prev + 1);
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

    const renderFooter = () => {
        if (!loading) return null;
        
        return (
            <ActivityIndicator 
                size="large"
                color="#0000ff"
                style={{ padding: 20 }}
            />
        );
    };

    return (
        <SafeAreaProvider>
            <SafeAreaView style={{ padding: 20 }}>
                <SectionList 
                    sections={itineraries}
                    renderSectionHeader={({section: {title}}) => (
                        <Text>{title}</Text>
                    )}
                    renderItem={({ item }) => (
                        <ItineraryCard itinerary={item}/>
                    )}
                    onEndReached={loadItineraries}
                    onEndReachedThreshold={0.5}
                    ListFooterComponent={renderFooter}
                />
            </SafeAreaView>
        </SafeAreaProvider>
    );
}