import { StyleSheet, View } from "react-native"

export default function Card( { children }: { children: React.ReactNode } ) {
    return (
        <View style={styles.container}>
            {children}
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        marginTop: 10, 
        padding: 10, 
        borderWidth: 1, 
        borderColor: '#ccc', 
        borderRadius: 5
    }
})