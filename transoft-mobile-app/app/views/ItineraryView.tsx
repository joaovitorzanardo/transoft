export default interface ItineraryView {
    id: string;
    routeName: string;
    schoolName: string;
    type: 'IDA' | 'VOLTA';
    status: 'Agendado' | 'Em andamento' | 'Concluído' | 'Cancelado';
    startTime: string;
    endTime: string;
}