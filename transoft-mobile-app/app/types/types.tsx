interface Trip {
    id: string;
    routeName: string;
    schoolName: string;
    type: 'Ida' | 'Volta';
    status: 'Programada' | 'Em andamento' | 'Concluída';
    time: string;
}

export { Trip };
