import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { useEffect, useState } from 'react';
import { ActivityIndicator, Alert, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { initializeAxiosInstance } from './services/axios-instance';
import { storage } from './utils/Storage';


export default function SettingsScreen() {
    const [host, setHost] = useState('');
    const [isSaving, setIsSaving] = useState(false);
    const [isTesting, setIsTesting] = useState(false);

    useEffect(() => {
        loadHost();
    }, []);

    const loadHost = async () => {
        const savedHost = await storage.getHost();
        if (savedHost) {
            setHost(savedHost);
        }
    };

    const validateHost = (hostString: string): boolean => {
        const hostPattern = /^(\d{1,3}\.){3}\d{1,3}:\d{1,5}$/;

        if (!hostPattern.test(hostString)) {
            return false;
        }

        const [ip, port] = hostString.split(':');
        const octets = ip.split('.');

        for (const octet of octets) {
            const num = parseInt(octet, 10);
            if (num < 0 || num > 255) {
                return false;
            }
        }

        const portNum = parseInt(port, 10);
        if (portNum < 1 || portNum > 65535) {
            return false;
        }

        return true;
    };

    const testConnection = async () => {
        if (!host.trim()) {
            Alert.alert('Erro', 'Por favor, insira um host válido.');
            return;
        }

        if (!validateHost(host.trim())) {
            Alert.alert('Erro', 'Host inválido. Use o formato: 0.0.0.0:80');
            return;
        }

        setIsTesting(true);
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 5000);

        try {
            const response = await fetch(`http://${host.trim()}/actuator/health`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                signal: controller.signal,
            });

            clearTimeout(timeoutId);

            if (response.ok) {
                Alert.alert('Sucesso', 'Conexão estabelecida com sucesso!');
            } else {
                Alert.alert('Erro', 'Erro ao conectar com o servidor.');
            }
        } catch (error) {
            clearTimeout(timeoutId);
            if (error instanceof Error && error.name === 'AbortError') {
                Alert.alert('Erro', 'Tempo de conexão esgotado. Verifique o host e tente novamente.');
            } else {
                Alert.alert('Erro', 'Não foi possível conectar ao servidor. Verifique o host e tente novamente.');
            }
        } finally {
            setIsTesting(false);
        }
    };


    const handleSave = async () => {
        if (!host.trim()) {
            Alert.alert('Erro', 'Por favor, insira um host válido.');
            return;
        }

        if (!validateHost(host.trim())) {
            Alert.alert('Erro', 'Host inválido. Use o formato: 0.0.0.0:80\nExemplo: 192.168.0.1:8080');
            return;
        }

        setIsSaving(true);
        try {
            await storage.setHost(host.trim());

            await initializeAxiosInstance();

            Alert.alert('Sucesso', 'Host salvo com sucesso!', [
                {
                    text: 'Ok',
                    onPress: () => router.back()
                }
            ]);
        } catch (error) {
            console.error('Error saving host:', error);
            Alert.alert('Erro', 'Não foi possível salvar o host.');
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <TouchableOpacity onPress={() => router.back()}>
                    <Ionicons name="arrow-back" size={28} color="#007AFF" />
                </TouchableOpacity>
                <Text style={styles.title}>Configurações</Text>
                <View style={{ width: 28 }} />
            </View>

            <View style={styles.content}>
                <Text style={styles.label}>Host do Servidor</Text>
                <TextInput
                    style={styles.input}
                    placeholder="192.168.0.1:8080"
                    value={host}
                    onChangeText={setHost}
                    keyboardType="url"
                    autoCapitalize="none"
                    autoCorrect={false}
                />
                <Text style={styles.hint}>Formato: 0.0.0.0:porta</Text>

                <TouchableOpacity
                    style={[styles.button, styles.testButton]}
                    onPress={testConnection}
                    disabled={isTesting}
                >
                    {isTesting ? (
                        <ActivityIndicator color="#fff" />
                    ) : (
                        <Text style={styles.buttonText}>Testar Conexão</Text>
                    )}
                </TouchableOpacity>

                <TouchableOpacity
                    style={styles.button}
                    onPress={handleSave}
                    disabled={isSaving}
                >
                    {isSaving ? (
                        <ActivityIndicator color="#fff" />
                    ) : (
                        <Text style={styles.buttonText}>Salvar</Text>
                    )}
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    header: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        paddingHorizontal: 20,
        paddingTop: 50,
        paddingBottom: 20,
        borderBottomWidth: 1,
        borderBottomColor: '#ddd',
    },
    title: {
        fontSize: 20,
        fontWeight: 'bold',
    },
    content: {
        padding: 20,
    },
    label: {
        fontSize: 16,
        fontWeight: '600',
        marginBottom: 10,
        color: '#333',
    },
    input: {
        height: 50,
        borderWidth: 1,
        borderColor: '#ddd',
        borderRadius: 8,
        paddingHorizontal: 15,
        fontSize: 16,
        marginBottom: 20,
    },
    hint: {
        fontSize: 12,
        color: '#666',
        marginTop: -15,
        marginBottom: 20,
    },
    testButton: {
        backgroundColor: '#34C759',
        marginBottom: 10,
    },
    button: {
        backgroundColor: '#007AFF',
        height: 50,
        borderRadius: 8,
        justifyContent: 'center',
        alignItems: 'center',
    },
    buttonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: 'bold',
    },
});
