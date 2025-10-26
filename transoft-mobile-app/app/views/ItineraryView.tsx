export default interface ItineraryView {
    id: string;
    routeName: string;
    schoolName: string;
    type: 'IDA' | 'VOLTA';
    status: 'AGENDADO' | 'EM_ANDAMENTO' | 'CONCLUIDO' | 'CANCELADO';
    startTime: string;
    endTime: string;
}