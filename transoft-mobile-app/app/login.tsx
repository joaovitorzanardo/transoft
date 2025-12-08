import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { useState } from 'react';
import { Alert, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../src/contexts/AuthContext';
import { login } from './services/login.service';
import { storage } from './utils/Storage';

export default function LoginScreen() {
  const { setUser } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordVisible, setPasswordVisible] = useState(false);

  const handleLogin = async () => {
    try {
      const response = await login({ email, password });
      await storage.setToken(response.data.token);
      await storage.setRefreshToken(response.data.refreshToken);

      setUser(response.data.user);

      if (!response.data.user.active) {
        router.replace('/first-access');
        return;
      }

      router.replace('/(tabs)');
    } catch (error: any) {
      showAlert(error.response?.data?.message || 'Ocorreu um erro inesperado. Por favor, tente novamente.');
    }
  };

  const showAlert = (message: string) => {
    Alert.alert('Erro ao fazer login', message, [
      {
        text: 'Ok'
      }
    ]);
  }

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={styles.settingsButton}
        onPress={() => router.push('/settings')}
      >
        <Ionicons name="settings-outline" size={28} color="#007AFF" />
      </TouchableOpacity>

      <Text style={styles.title}>Transoft</Text>

      <TextInput
        style={styles.input}
        placeholder="Email"
        value={email}
        onChangeText={setEmail}
        keyboardType="email-address"
        autoCapitalize="none"
      />

      <View style={styles.passwordContainer}>
        <TextInput
          style={styles.passwordInput}
          placeholder="Password"
          value={password}
          onChangeText={setPassword}
          secureTextEntry={!passwordVisible}
        />
        <TouchableOpacity onPress={() => setPasswordVisible(!passwordVisible)}>
          <Ionicons
            name={passwordVisible ? 'eye-off' : 'eye'}
            size={24}
            color="gray"
          />
        </TouchableOpacity>
      </View>

      <TouchableOpacity style={styles.button} onPress={handleLogin}>
        <Text style={styles.buttonText}>Login</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 20,
    backgroundColor: '#fff',
  },
  settingsButton: {
    position: 'absolute',
    top: 50,
    right: 20,
    zIndex: 1,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 30,
    textAlign: 'center',
  },
  input: {
    height: 50,
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    paddingHorizontal: 15,
    marginBottom: 15,
    fontSize: 16,
  },
  passwordContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    paddingHorizontal: 15,
    marginBottom: 15,
  },
  passwordInput: {
    flex: 1,
    height: 50,
    fontSize: 16,
  },
  button: {
    backgroundColor: '#007AFF',
    height: 50,
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 15,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});