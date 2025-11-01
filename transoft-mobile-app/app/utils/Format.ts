function formatStatus(status: 'CONFIRMADO' | 'NAO_VAI'): string {
        switch (status) {
            case 'CONFIRMADO':
                return 'Confirmado';
            case 'NAO_VAI':
                return 'Não Vai';
            default:
                return '';
        }
    }

function formatType(type: 'IDA' | 'VOLTA' | undefined): string {
    switch (type) {
        case 'IDA':
            return 'Ida';
        case 'VOLTA':
            return 'Volta';
        default:
            return '';
    }
}

function formatDayOfWeek(dateString: string): string {
    const weekDays = {
        0: 'Domingo',
        1: 'Segunda-feira',
        2: 'Terça-feira',
        3: 'Quarta-feira',
        4: 'Quinta-feira',
        5: 'Sexta-feira',
        6: 'Sábado'
    };

    const [day, month, year] = dateString.split('/').map(Number);
    
    const dateObj = new Date(year, month - 1, day);
    
    return weekDays[dateObj.getDay() as keyof typeof weekDays];
}

export { formatDayOfWeek, formatStatus, formatType };

