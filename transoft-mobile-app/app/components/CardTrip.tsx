import Ionicons from "@expo/vector-icons/Ionicons";
import { router } from "expo-router";
import { Button, Text, View } from "react-native";
import { Trip } from "../types/types";
import Card from "./Card";

interface CardTripProps {
    trip: Trip;
}

export default function CardTrip( { trip }: CardTripProps ) {
    return (
        <Card>
            <View style={{display: 'flex', flexDirection: 'row', justifyContent: 'space-between'}}>
                <Text style={{fontWeight: 'bold'}}>{trip.routeName}</Text>
                <Text>{trip.status}</Text>
            </View>
            <Text>{trip.schoolName}</Text>
            <View style={{display: 'flex', flexDirection: 'row', justifyContent: 'flex-start', alignContent: 'center'}}>
                <Ionicons name='time' />
                <Text>{trip.time}</Text>
            </View>
            <Button title="Detalhes" onPress={() => router.navigate(`/trips/${trip.id}`)}/>
        </Card>
    )
}