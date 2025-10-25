export default interface UserAccountDto {
    name: string;
    email: string;
    active: boolean;
    role: 'DRIVER' | 'PASSENGER';
}