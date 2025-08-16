import FontAwesome5 from '@expo/vector-icons/FontAwesome5';
import FontAwesome6 from '@expo/vector-icons/FontAwesome6';
import { Tabs } from 'expo-router';
import React from 'react';

export default function TabLayout() {
  return (
    <Tabs>
      <Tabs.Screen
        name="index"
        options={{
          title: 'Inicio',
          tabBarIcon: () => <FontAwesome6 name='house' size={22}/>
        }}  
      />
      <Tabs.Screen
        name="trips"
        options={{
          title: 'Viagens',
          tabBarIcon: () => <FontAwesome6 name='route' size={22}/>
        }}  
      />
      <Tabs.Screen
        name="profile"
        options={{
          title: 'Perfil',
          tabBarIcon: () => <FontAwesome5 name='user-alt' size={22}/>
        }}
      />
    </Tabs>
  );
}
