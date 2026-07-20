'use client';

import { Input } from '../../components/ui/Input';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/useAuth';
import { Sparkles } from 'lucide-react';
import { SyntheticEvent } from 'react';

export default function RegisterPage() {
    const router = useRouter();

    const {
        setName,
        setEmail,
        setPassword,
        loading,
        error,
        register,
    } = useAuth();

    async function handleRegister(event: SyntheticEvent) {
        event.preventDefault();

        if (await register()) {
            router.push('/dashboard');
        }
    }

    function handleNavigateToLogin() {
        router.push('/login');
    }

    return (
        <main className="flex min-h-screen items-center justify-center bg-[radial-gradient(circle_at_top,_rgba(59,130,246,0.14),_transparent_55%)] bg-background p-6 text-foreground">
            <div className="w-full max-w-md rounded-2xl border border-border bg-card/95 p-8 shadow-[0_24px_80px_-24px_rgba(15,23,42,0.45)] backdrop-blur">
                <div className="mb-6 flex items-center gap-3">
                    <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-primary text-primary-foreground">
                        <Sparkles className="h-5 w-5" />
                    </div>
                    <div>
                        <p className="text-sm font-semibold uppercase tracking-[0.24em] text-muted-foreground">Tarefize</p>
                        <h1 className="text-2xl font-bold tracking-tight">Crie sua conta</h1>
                    </div>
                </div>

                <p className="mb-6 text-sm leading-6 text-muted-foreground">
                    Cadastre-se para começar a gerenciar tarefas, acompanhar seu progresso e manter o foco.
                </p>

                {error && (
                    <div className="mb-4 rounded-lg border border-red-200 bg-red-50 p-3 text-sm font-medium text-red-600">
                        {error}
                    </div>
                )}

                <form onSubmit={handleRegister} className="flex flex-col gap-3">
                    <Input
                        onChange={(e) => setName(e.target.value)}
                        label="Nome"
                        type="text"
                        placeholder="Seu nome"
                        required
                    />

                    <Input
                        onChange={(e) => setEmail(e.target.value)}
                        label="Email"
                        type="email"
                        placeholder="voce@email.com"
                        required
                    />

                    <Input
                        onChange={(e) => setPassword(e.target.value)}
                        label="Senha"
                        type="password"
                        placeholder="••••••••"
                        required
                    />

                    <div className="mt-2 flex flex-col gap-2 sm:flex-row">
                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full cursor-pointer rounded-lg bg-primary px-4 py-2.5 font-semibold text-primary-foreground transition hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-80"
                        >
                            <span className="flex items-center justify-center gap-2">
                                {loading && (
                                    <span className="h-4 w-4 animate-spin rounded-full border-2 border-white/30 border-t-white" />
                                )}
                                {loading ? 'Registrando...' : 'Registrar'}
                            </span>
                        </button>

                        <button
                            type="button"
                            disabled={loading}
                            className="w-full cursor-pointer rounded-lg bg-secondary px-4 py-2.5 font-semibold text-secondary-foreground transition hover:bg-accent hover:text-foreground disabled:cursor-not-allowed disabled:opacity-80"
                            onClick={handleNavigateToLogin}
                        >
                            Fazer login
                        </button>
                    </div>
                </form>
            </div>
        </main>
    );
}
