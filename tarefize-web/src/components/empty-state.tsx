import { ListTodo, Plus } from 'lucide-react'

export function EmptyState({ onCreate }: { onCreate: () => void }) {
  return (
    <div className="mt-10 flex flex-col items-center justify-center rounded-2xl border border-dashed border-border bg-card/50 px-6 py-16 text-center">
      <span className="flex h-14 w-14 items-center justify-center rounded-2xl bg-accent text-accent-foreground">
        <ListTodo className="h-7 w-7" />
      </span>
      <h3 className="mt-4 text-lg font-semibold">Nenhuma tarefa encontrada</h3>
      <p className="mt-1 max-w-sm text-sm text-muted-foreground">
        Ajuste os filtros ou crie uma nova tarefa para começar a organizar o seu dia.
      </p>
      <button
        type="button"
        onClick={onCreate}
        className="mt-5 inline-flex items-center gap-2 rounded-lg bg-primary px-4 py-2.5 text-sm font-semibold text-primary-foreground transition hover:opacity-90"
      >
        <Plus className="h-4 w-4" />
        Nova Tarefa
      </button>
    </div>
  )
}
