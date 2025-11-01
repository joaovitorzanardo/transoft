import { useState } from 'react';
import { Dimensions } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";
import { SceneMap, TabBar, TabView } from 'react-native-tab-view';
import HistoryItineraries from './HistoryItineraries';
import NextItineraries from './NextItineraries';

export default function TripsScreen() {
    const [index, setIndex] = useState(0);
    const [routes] = useState([
        { key: 'next', title: 'Próximos' },
        { key: 'history', title: 'Histórico' },
    ]);

    const renderScene = SceneMap({
        next: NextItineraries,
        history: HistoryItineraries,
    });

    return (
        <SafeAreaProvider>
            <SafeAreaView style={{ flex: 1 }}>
                <TabView
                    navigationState={{ index, routes }}
                    renderScene={renderScene}
                    onIndexChange={setIndex}
                    initialLayout={{ width: Dimensions.get('window').width }}
                    renderTabBar={props => (
                        <TabBar
                            {...props}
                            style={{ backgroundColor: '#fff' }}
                            indicatorStyle={{ backgroundColor: '#000' }}
                            activeColor="#000"
                            inactiveColor="#000"
                        />
                    )}
                />
            </SafeAreaView>
        </SafeAreaProvider>
    );
}