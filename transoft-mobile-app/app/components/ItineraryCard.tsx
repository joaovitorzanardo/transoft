import Ionicons from "@expo/vector-icons/Ionicons";
import { router } from "expo-router";
import { Button, Text, View } from "react-native";
import ItineraryView from "../views/ItineraryView";
import Card from "./Card";

interface ItineraryCardProps {
    itinerary: ItineraryView;
}

export default function ItineraryCard( { itinerary }: ItineraryCardProps ) {
    return (
        <Card>
            <View>
                <Text>{itinerary.type}</Text>
            </View>
            <View style={{display: 'flex', flexDirection: 'row', justifyContent: 'space-between'}}>
                <Text style={{fontWeight: 'bold'}}>{itinerary.routeName}</Text>
                <Text>{itinerary.status}</Text>
            </View>
            <Text>{itinerary.schoolName}</Text>
            <View style={{display: 'flex', flexDirection: 'row', justifyContent: 'flex-start', alignContent: 'center'}}>
                <Ionicons name='time' />
                <Text>{itinerary.startTime} - {itinerary.endTime}</Text>
            </View>
            <Button title="Detalhes" onPress={() => router.navigate(`/trips/${itinerary.id}`)}/>
        </Card>
    )
}