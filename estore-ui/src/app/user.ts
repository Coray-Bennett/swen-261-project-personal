export interface User {
    index: number;
    username: string;
    status: boolean;
    cart: number[][]
    passwordHash: string;
}