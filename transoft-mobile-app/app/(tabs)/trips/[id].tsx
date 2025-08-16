import { useLocalSearchParams } from "expo-router";
import { Alert, Button, FlatList, Text, View } from "react-native";
import MapView, { Marker } from "react-native-maps";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

export default function TripDetailsScreen() {
    const { id } = useLocalSearchParams<{ id: string }>();

    const DATA = [
        {
            id: '1',
            name: 'Joao Vitor Zanardo',
            status: 'Confirmado',
        },
        {
            id: '2',
            name: 'Pedro Henrique',
            status: 'Nao volta',
        },
        {
            id: '3',
            name: 'Babu',
            status: 'Confirmado',
        }
    ]
    
    return (
        <SafeAreaProvider>
            <SafeAreaView style={{flex: 1, width: '100%', paddingTop: 20}}>
                <View style={{flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingBottom: 20, marginLeft: 20, marginRight: 20}}>
                    <Text style={{ fontWeight: 'bold'}}>Rota {id}</Text>
                    <Text>Em andamento</Text>
                </View>
                <MapView style={{height: '40%', width: '100%'}} 
                    provider="google"
                    initialRegion={{
                      latitude: 37.78825,
                      longitude: -122.4324,
                      latitudeDelta: 0.0922,
                      longitudeDelta: 0.0421,
                    }}
                />
                 <Marker
                    coordinate={{ latitude: 37.78825, longitude: -122.4324 }}
                    title="My Marker"
                    description="Here is a marker example"
                />
                <Button title='Nao vou' color='red' onPress={() => Alert.alert('Confirma nao ir?', 'Tem certeza que deseja nao ir?', [
                    {
                        text: 'Não'
                    },
                    {
                        text: 'Sim'
                    }
                ])}/>
                <View>
                    <Text style={{fontWeight: 'bold'}}>Passageiros</Text>
                    <FlatList data={DATA} renderItem={({item}) => (
                        <View style={{flexDirection: 'row', justifyContent: 'space-between', padding: 10, borderBottomWidth: 1, borderColor: '#ccc'}}>
                            <Text>{item.name}</Text>
                            <Text>{item.status}</Text>
                        </View>
                    )} />
                </View>
            </SafeAreaView>
        </SafeAreaProvider>
        
    );
}