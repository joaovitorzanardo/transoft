import { useState } from 'react';
import { Alert, Modal, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import AccountDto from '../model/AccountDto';

interface PersonalDataDialogProps {
  visible: boolean;
  onClose: () => void;
  onSave: (data: AccountDto) => void;
  initialData: {
    name: string;
    email: string;
    phoneNumber: {
      ddd: string;
      number: string;
    };
  };
}

export default function PersonalDataDialog({ visible, onClose, onSave, initialData }: PersonalDataDialogProps) {
  const [email, setEmail] = useState(initialData.email);
  const [name, setName] = useState(initialData.name);
  const [ddd, setDdd] = useState(initialData.phoneNumber.ddd);
  const [number, setNumber] = useState(initialData.phoneNumber.number);

  const handleSave = () => {
    Alert.alert(
      "Confirmação",
      "Tem certeza que deseja atualizar seus dados?",
      [
        {
          text: "Cancelar",
          style: "cancel"
        },
        {
          text: "Confirmar",
          onPress: () => {
            onSave({
              name,
              email,
              phoneNumber: {
                ddd,
                number
              }
            });
            onClose();
          }
        }
      ]
    );
  };

  return (
    <Modal visible={visible} transparent animationType="fade">
      <View style={styles.overlay}>
        <View style={styles.dialog}>
          <Text style={styles.title}>Editar Dados Pessoais</Text>
          
          <TextInput
            style={styles.input}
            placeholder="Nome"
            value={name}
            onChangeText={setName}
          />

        <TextInput
            style={styles.input}
            placeholder="Email"
            value={email}
            onChangeText={setEmail}
          />
          
          <View style={styles.phoneContainer}>
            <TextInput
              style={[styles.input, styles.dddInput]}
              placeholder="DDD"
              value={ddd}
              onChangeText={setDdd}
              keyboardType="number-pad"
              maxLength={2}
            />
            <TextInput
              style={[styles.input, styles.numberInput]}
              placeholder="Número"
              value={number}
              onChangeText={setNumber}
              keyboardType="number-pad"
              maxLength={11}
            />
          </View>

          <View style={styles.buttonContainer}>
            <TouchableOpacity style={styles.button} onPress={onClose}>
              <Text style={styles.buttonText}>Cancelar</Text>
            </TouchableOpacity>
            <TouchableOpacity style={[styles.button, styles.saveButton]} onPress={handleSave}>
              <Text style={styles.buttonSaveText}>Salvar</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  dialog: {
    width: '90%',
    backgroundColor: 'white',
    borderRadius: 12,
    padding: 20,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 20,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    marginBottom: 15,
    fontSize: 16,
    color: '#444',
    backgroundColor: '#f9f9f9',
  },
  phoneContainer: {
    flexDirection: 'row',
    gap: 10,
  },
  dddInput: {
    width: '20%',
  },
  numberInput: {
    flex: 1,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    gap: 10,
    marginTop: 10,
  },
  button: {
    padding: 12,
    borderRadius: 8,
    minWidth: 100,
    alignItems: 'center',
  },
  saveButton: {
    backgroundColor: '#007AFF',
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  buttonText: {
    color: '#007AFF',
    fontWeight: 'bold',
    fontSize: 16,
  },
  buttonSaveText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 16,
  },
});
