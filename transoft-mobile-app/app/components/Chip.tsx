import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

interface ChipProps {
  text: string;
  color?: 'success' | 'warning' | 'error' | 'default';
}

export default function Chip({ text, color = 'default' }: ChipProps) {
  return (
    <View style={[styles.chip, { backgroundColor: `${toColor(color)}40` }]}>
      <Text style={[styles.text, { color: color }]}>{text}</Text>
    </View>
  );
}

function toColor(color: 'success' | 'warning' | 'error' | 'default') {
  switch (color) {
    case 'success':
      return '#4CAF50';
    case 'warning':
      return '#FFC107';
    case 'error':
      return '#F44336';
    default:
      return '#2196F3';
  }
}

const styles = StyleSheet.create({
  chip: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
    alignSelf: 'flex-start',
  },
  text: {
    fontSize: 14,
    fontWeight: '500',
  },
});
