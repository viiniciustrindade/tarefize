import {
  Calendar,
  Circle,
  CircleCheckBig,
  Pencil,
  Trash2,
} from 'lucide-react'

import { PRIORITY_META, type Priority, type Task } from '@/types/task'

function formatDate(iso: string): string {
  if (!iso) return 'Sem data'
  const [y, m, d] = iso.split('-').map(Number)
  const date = new Date(y, m - 1, d)
  return date.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  })
}

function isOverdue(iso: string, completed: boolean): boolean {
  if (!iso || completed) return false
  const today = new Date('2026-07-13')
  const [y, m, d] = iso.split('-').map(Number)
  const date = new Date(y, m - 1, d)
  return date < today
}

export function TaskCard({
  task,
  onToggle,
  onEdit,
  onDelete,
  isCompleting = false,
}: {
  task: Task
  onToggle: () => void
  onEdit: () => void
  onDelete: () => void
  isCompleting?: boolean
}) {
  const meta = PRIORITY_META[task.priority as Priority]
  const overdue = isOverdue(task.dueDate ?? '', task.completed)

  return (
    <article
      className={`group relative flex flex-col overflow-hidden rounded-xl border bg-card transition hover:shadow-md ${
        task.completed ? 'border-border opacity-75' : 'border-border'
      }`}
    >
      <span className={`absolute left-0 top-0 h-full w-1 ${meta.bar}`} aria-hidden />

      <div className="flex flex-1 flex-col p-5 pl-6">
        <div className="flex items-start justify-between gap-3">
          <button
            type="button"
            onClick={onToggle}
            disabled={isCompleting}
            className={`mt-0.5 flex h-10 w-10 items-center justify-center rounded-full border transition disabled:cursor-not-allowed disabled:opacity-70 ${
              task.completed
                ? 'border-emerald-500 bg-emerald-500 text-white hover:bg-emerald-600'
                : 'border-border bg-background text-muted-foreground hover:border-primary hover:text-primary hover:bg-secondary/70'
            }`}
            aria-label={task.completed ? 'Marcar como pendente' : 'Concluir tarefa'}
            aria-pressed={task.completed}
            title={task.completed ? 'Concluída' : 'Pendente'}
          >
            {isCompleting ? (
              <span className="flex h-5 w-5 items-center justify-center rounded-full border-2 border-primary/20 border-t-primary animate-spin" />
            ) : task.completed ? (
              <CircleCheckBig className="h-5 w-5" />
            ) : (
              <Circle className="h-5 w-5" />
            )}
          </button>

          <div className="min-w-0 flex-1">
            <h3
              className={`text-base font-semibold leading-snug text-pretty ${
                task.completed ? 'text-muted-foreground line-through' : ''
              }`}
            >
              {task.taskName}
            </h3>
            <p className="mt-1 line-clamp-2 text-sm text-muted-foreground">
              {task.taskDescription}
            </p>
          </div>

          <span
            className={`inline-flex flex-shrink-0 items-center gap-1 rounded-full px-2 py-0.5 text-xs font-semibold ring-1 ring-inset ${meta.badge}`}
            title={`Prioridade ${task.priority} — ${meta.label}`}
          >
            <span className={`h-1.5 w-1.5 rounded-full ${meta.dot}`} />
            P{task.priority}
          </span>
        </div>

        <div className="mt-4 flex items-center justify-between border-t border-border pt-3">
          <div className="flex items-center gap-3 text-xs">
            <span
              className={`inline-flex items-center gap-1.5 ${
                overdue ? 'font-semibold text-red-600' : 'text-muted-foreground'
              }`}
            >
              <Calendar className="h-3.5 w-3.5" />
              {formatDate(task.dueDate ?? '')}
            </span>
            <span
              className={`inline-flex items-center gap-1 rounded-full px-2 py-0.5 font-medium ${
                task.completed
                  ? 'bg-emerald-50 text-emerald-700'
                  : 'bg-secondary text-secondary-foreground'
              }`}
            >
              {task.completed ? 'Concluída' : 'Pendente'}
            </span>
          </div>

          <div className="flex items-center gap-1 opacity-0 transition group-hover:opacity-100 focus-within:opacity-100">
            <button
              type="button"
              onClick={onEdit}
              aria-label="Editar tarefa"
              className="flex h-8 w-8 items-center justify-center rounded-lg text-muted-foreground transition hover:bg-secondary hover:text-foreground"
            >
              <Pencil className="h-4 w-4" />
            </button>
            <button
              type="button"
              onClick={onDelete}
              aria-label="Deletar tarefa"
              className="flex h-8 w-8 items-center justify-center rounded-lg text-muted-foreground transition hover:bg-red-50 hover:text-red-600"
            >
              <Trash2 className="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </article>
  )
}
