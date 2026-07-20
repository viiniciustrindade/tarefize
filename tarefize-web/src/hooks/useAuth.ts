import { authService } from "@/services/auth";

import type { LoginRequest, RegisterRequest } from "@/types/auth";
import { User } from "@/types/user";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface TokenPayload {
    email?: string;
    sub?: string;
    [key: string]: unknown;
}

function parseJwt(token: string): TokenPayload | null {
    try {
        const base64Url = token.split('.')[1]
        if (!base64Url) return null

        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
        const paddedBase64 = base64.padEnd(base64.length + (4 - (base64.length % 4)) % 4, '=')

        const jsonPayload = decodeURIComponent(
            atob(paddedBase64)
                .split('')
                .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
                .join('')
        )

        return JSON.parse(jsonPayload)
    } catch {
        return null
    }
}

export function useAuth(){
    const router = useRouter();
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [userEmail, setUserEmail] = useState(() => authService.getStoredUserEmail());
    const [userName, setUserName] = useState(() => authService.getStoredUserName());
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    function syncUserFromToken() {
        const token = authService.getStoredToken()
        if (!token) {
            setUserEmail("")
            return
        }
        const payload = parseJwt(token);
        const emailFromToken = payload?.email?.toString() ?? payload?.sub?.toString() ?? ""

        setUserEmail(emailFromToken)
    }

    function hydrateUserFromStorage() {
        const storedName = authService.getStoredUserName()
        if (storedName) {
            setUserName(storedName)
        }
    }

    async function login(): Promise<boolean> {
        setLoading(true);
        setError("");

        const credentials: LoginRequest = {
            email: email,
            password: password
        };

        try{
            await authService.login(credentials);
            const user: User = await authService.findProfile(email);
            const nextUserName = user.nome?.trim() || email;

            authService.setStoredUserName(nextUserName);
            setUserName(nextUserName);
            syncUserFromToken();

            return true;
        }catch(err: any){
            setError(err.message || "Erro ao conectar com o servidor");
            return false;
        } finally{
            setLoading(false);
        }
    }

    async function register(): Promise<boolean> {
        setLoading(true);
        setError("");

        const credentials: RegisterRequest = {
            name: name,
            email: email,
            password: password,
        };

        try {
            await authService.register(credentials);
            const nextUserName = credentials.name.trim() || credentials.email;
            authService.setStoredUserName(nextUserName);
            setUserName(nextUserName);
            syncUserFromToken();

            return true;
        } catch (err: any) {
            setError(err.message || "Erro ao conectar com o servidor");
            return false;
        } finally {
            setLoading(false);
        }
    }

    function logout() {
        authService.logout();
        setUserName("");
        setUserEmail("");
        router.push("/login");
    }

    useEffect(() => {
        syncUserFromToken()
        hydrateUserFromStorage()
    }, [])

    return {
        setName,
        setEmail,
        setPassword,
        loading,
        error,
        userEmail,
        userName,
        login,
        register,
        logout,
    }
}
