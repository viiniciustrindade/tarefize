import type { Dispatch, FormEvent, SetStateAction } from 'react'

import { CircleCheckBig, Pencil, Plus, X } from 'lucide-react'

import { PRIORITY_META, type TaskFormState, type Priority } from '@/types/task'

export function TaskModal({
  form,
  setForm,
  onClose,
  onSubmit,
  isEditing,
  isSubmitting,
}: {
  form: TaskFormState
  setForm: Dispatch<SetStateAction<TaskFormState>>
  onClose: () => void
  onSubmit: (e: FormEvent) => void
  isEditing: boolean
  isSubmitting: boolean
}) {
  const meta = PRIORITY_META[form.priority]

  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center bg-foreground/40 p-0 backdrop-blur-sm sm:items-center sm:p-4">
      <div className="absolute inset-0" onClick={onClose} aria-hidden />

      <div className="relative z-10 w-full max-w-lg overflow-hidden rounded-t-2xl border border-border bg-card shadow-xl sm:rounded-2xl">
        <div className="flex items-center justify-between border-b border-border px-6 py-4">
          <div className="flex items-center gap-3">
            <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-primary text-primary-foreground">
              {isEditing ? <Pencil className="h-4 w-4" /> : <Plus className="h-4 w-4" />}
            </span>
            <h2 className="text-lg font-semibold">
              {isEditing ? 'Editar Tarefa' : 'Nova Tarefa'}
            </h2>
          </div>
          <button
            type="button"
            onClick={onClose}
            aria-label="Fechar"
            className="flex h-8 w-8 items-center justify-center rounded-lg text-muted-foreground transition hover:bg-secondary hover:text-foreground"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <form onSubmit={onSubmit} className="space-y-5 px-6 py-5">
          <div>
            <label htmlFor="title" className="mb-1.5 block text-sm font-medium">
              Título
            </label>
            <input
              id="taskName"
              type="text"
              value={form.taskName}
              onChange={(e) => setForm((f) => ({ ...f, taskName: e.target.value }))}
              placeholder="Ex.: Enviar relatório mensal"
              required
              autoFocus
              className="w-full rounded-lg border border-input bg-background px-3 py-2.5 text-sm outline-none transition focus:border-ring focus:ring-2 focus:ring-ring/30"
            />
          </div>

          <div>
            <label htmlFor="description" className="mb-1.5 block text-sm font-medium">
              Descrição
            </label>
            <textarea
              id="taskDescription"
              rows={3}
              value={form.taskDescription}
              onChange={(e) =>
                setForm((f) => ({ ...f, taskDescription: e.target.value }))
              }
              placeholder="Detalhes da tarefa..."
              className="w-full resize-none rounded-lg border border-input bg-background px-3 py-2.5 text-sm outline-none transition focus:border-ring focus:ring-2 focus:ring-ring/30"
            />
          </div>

          <div>
            <label htmlFor="dueDate" className="mb-1.5 block text-sm font-medium">
              Data de vencimento
            </label>
            <input
              id="dueDate"
              type="date"
              value={form.dueDate ?? ''}
              onChange={(e) => setForm((f) => ({ ...f, dueDate: e.target.value }))}
              className="w-full rounded-lg border border-input bg-background px-3 py-2.5 text-sm outline-none transition focus:border-ring focus:ring-2 focus:ring-ring/30"
            />
          </div>

          <div>
            <div className="mb-2 flex items-center justify-between">
              <label htmlFor="priority" className="text-sm font-medium">
                Prioridade
              </label>
              <span
                className={`inline-flex items-center gap-1 rounded-full px-2.5 py-0.5 text-xs font-semibold ring-1 ring-inset ${meta.badge}`}
              >
                <span className={`h-1.5 w-1.5 rounded-full ${meta.dot}`} />
                {form.priority} — {meta.label}
              </span>
            </div>
            <input
              id="priority"
              type="range"
              min={1}
              max={5}
              step={1}
              value={form.priority}
              onChange={(e) =>
                setForm((f) => ({
                  ...f,
                  priority: Number(e.target.value) as Priority,
                }))
              }
              className="w-full cursor-pointer accent-primary"
            />
            <div className="mt-1 flex justify-between px-0.5 text-[11px] text-muted-foreground">
              <span>1</span>
              <span>2</span>
              <span>3</span>
              <span>4</span>
              <span>5</span>
            </div>
          </div>

          <div className="flex items-center justify-end gap-2 border-t border-border pt-4">
            <button
              type="button"
              onClick={onClose}
              className="rounded-lg border border-input bg-background px-4 py-2.5 text-sm font-medium transition hover:bg-secondary"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="inline-flex min-w-[148px] items-center justify-center gap-2 rounded-lg bg-primary px-4 py-2.5 text-sm font-semibold text-primary-foreground transition hover:opacity-90 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-70"
            >
              {isSubmitting ? (
                <span className="h-4 w-4 animate-spin rounded-full border-2 border-primary-foreground/30 border-t-primary-foreground" />
              ) : (
                <CircleCheckBig className="h-4 w-4" />
              )}
              {isEditing ? 'Salvar alterações' : 'Criar tarefa'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
