import CardTrip from '@/app/components/CardTrip';
import { Trip } from '@/app/types/types';
import { SectionList, Text } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

interface TripListItem {
    title: string;
    data: Trip[];
}

const data: TripListItem[] = [
    {
        title: 'Segunda-feira, 18 de Agosto',
        data: [
            {
                id: '1',
                routeName: 'Rota URI Campus II - Noturno',
                schoolName: 'URI Erechim',
                type: 'Ida',
                time: '18:00 - 19:15',
                status: 'Em andamento'                
            },
            {
                id: '2',
                routeName: 'Rota URI Campus II - Noturno',
                schoolName: 'URI Erechim',
                type: 'Volta',
                time: '22:30 - 23:15',
                status: 'Programada',
            },
        ],
    },
    {
        title: 'Terça-feira, 19 de Agosto',
        data: [
            {
                id: '3',
                routeName: 'Rota URI Campus II - Noturno',
                schoolName: 'URI Erechim',
                type: 'Ida',
                time: '18:00 - 19:15',
                status: 'Programada',
            },
            {
                id: '4',
                routeName: 'Rota URI Campus II - Noturno',
                schoolName: 'URI Erechim',
                type: 'Volta',
                time: '22:30 - 23:15',
                status: 'Programada',
            },
        ],
    },
    {
        title: 'Quarta-feira, 20 de Agosto',
        data: [
            {
                id: '5',
                routeName: 'Rota URI Campus II - Noturno',
                schoolName: 'URI Erechim',
                type: 'Ida',
                time: '18:00 - 19:15',
                status: 'Programada',
            },
            {
                id: '6',
                routeName: 'Rota URI Campus II - Noturno',
                schoolName: 'URI Erechim',
                type: 'Volta',
                time: '22:30 - 23:15',
                status: 'Programada',
            },
        ],
    }
]

export default function TripsScreen() {
    return (
        <SafeAreaProvider>
            <SafeAreaView style={{ padding: 20 }}>
                <SectionList 
                    sections={data}
                    renderSectionHeader={({section: {title}}) => (
                        <Text>{title}</Text>
                    )}
                    renderItem={({ item }) => (
                        <CardTrip trip={item}/>
                    )}
                />
            </SafeAreaView>
        </SafeAreaProvider>
    )
}