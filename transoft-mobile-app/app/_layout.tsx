import { Stack } from "expo-router";
import { useEffect, useState } from "react";
import { storage } from "./utils/Storage";

export default function RootLayout() {
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    checkToken();
  }, []);

  const checkToken = async () => {
    const token = await storage.getToken();
    setIsAuthenticated(!!token);
    setIsLoading(false);
  };

  if (isLoading) {
    return null; // Or a loading screen
  }

  return (
    <Stack screenOptions={{ headerShown: false }}>
      {!isAuthenticated ? (
        <Stack.Screen name="login" />
      ) : (
        <Stack.Screen name="(tabs)" />
      )}
    </Stack>
  );
}
