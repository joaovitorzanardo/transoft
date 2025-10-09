export default interface User {
    name: string;
    email: string;
    isActive: boolean;
    role: 'MANAGER' | 'DRIVER' | 'PASSENGER' | 'SYS_ADMIN'
}