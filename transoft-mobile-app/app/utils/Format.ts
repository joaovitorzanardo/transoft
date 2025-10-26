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

export { formatStatus, formatType };
