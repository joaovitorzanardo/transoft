import { Stack } from "expo-router";

export default function TripsLayout() {
  return (
    <Stack>
      <Stack.Screen name="index" options={{ headerShown: false }} />
      <Stack.Screen name="[id]" options={{ title: 'Detalhes da Rota' }} />
    </Stack>
  );
}