"use client"

import {
  Bell,
  Search,
  Plus,
  ChevronDown,
  ListTodo,
  Clock,
  CircleCheckBig,
  Flag,
  LogOut,
  Settings,
  User,
  Sparkles,
} from 'lucide-react'

import { EmptyState } from '@/components/empty-state'
import { StatCard } from '@/components/stat-card'
import { TaskCard } from '@/components/task-card'
import { TaskModal } from '@/components/task-modal'
import { useAuth } from '@/hooks/useAuth'
import { useTask } from '@/hooks/useTask'
import { PRIORITY_META, type Priority, type StatusFilter } from '@/types/task'

const STATUS_OPTIONS: Array<{ value: StatusFilter; label: string }> = [
  { value: 'all', label: 'Todas' },
  { value: 'pending', label: 'Pendentes' },
  { value: 'done', label: 'Concluídas' },
]

export default function TarefizeDashboard() {
  const { logout, userEmail, userName } = useAuth()
  const {
    statusFilter,
    isLoadingTasks,
    currentPage,
    setCurrentPage,
    totalPages,
    totalItems,
    setStatusFilter,
    priorityFilter,
    setPriorityFilter,
    search,
    setSearch,
    isModalOpen,
    editingId,
    isSubmittingTask,
    completingTaskId,
    form,
    setForm,
    isProfileOpen,
    setIsProfileOpen,
    filteredTasks,
    stats,
    toggleComplete,
    deleteTask,
    openDeleteConfirm,
    closeDeleteConfirm,
    confirmDelete,
    taskToDelete,
    isDeleteModalOpen,
    openCreate,
    openEdit,
    closeModal,
    handleSubmit,
  } = useTask()

  return (
    <>
      <div className="min-h-screen bg-background text-foreground">
        <header className="sticky top-0 z-30 border-b border-border bg-card/80 backdrop-blur">
          <div className="mx-auto flex h-16 max-w-6xl items-center justify-between gap-4 px-4 sm:px-6">
            <div className="flex items-center gap-2">
              <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary text-primary-foreground">
                <Sparkles className="h-5 w-5" />
              </div>
              <span className="text-lg font-bold tracking-tight">Tarefize</span>
            </div>

            <div className="relative hidden max-w-md flex-1 md:block">
              <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Buscar tarefas..."
                className="w-full rounded-lg border border-input bg-background py-2 pl-9 pr-3 text-sm outline-none transition focus:border-ring focus:ring-2 focus:ring-ring/30"
              />
            </div>

            <div className="flex items-center gap-2 sm:gap-3">
              <button
                type="button"
                aria-label="Notificações"
                className="relative flex h-10 w-10 items-center justify-center rounded-lg text-muted-foreground transition hover:bg-secondary hover:text-foreground"
              >
                <Bell className="h-5 w-5" />
                <span className="absolute right-2 top-2 flex h-2.5 w-2.5">
                  <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-primary opacity-75" />
                  <span className="relative inline-flex h-2.5 w-2.5 rounded-full bg-primary ring-2 ring-card" />
                </span>
              </button>

              <div className="relative">
                <button
                  type="button"
                  onClick={() => setIsProfileOpen((v) => !v)}
                  className="flex items-center gap-2 rounded-lg p-1 pr-2 transition hover:bg-secondary"
                >
                  <span className="flex h-9 w-9 items-center justify-center rounded-full bg-accent text-sm font-semibold text-accent-foreground">
                    ML
                  </span>
                  <ChevronDown
                    className={`hidden h-4 w-4 text-muted-foreground transition-transform sm:block ${
                      isProfileOpen ? 'rotate-180' : ''
                    }`}
                  />
                </button>

                {isProfileOpen && (
                  <>
                    <div
                      className="fixed inset-0 z-10"
                      onClick={() => setIsProfileOpen(false)}
                    />
                    <div className="absolute right-0 z-20 mt-2 w-56 overflow-hidden rounded-xl border border-border bg-popover text-popover-foreground shadow-lg">
                      <div className="border-b border-border px-4 py-3">
                        <p className="text-sm font-semibold">{userName || 'Usuário'}</p>
                        <p className="truncate text-xs text-muted-foreground">
                          {userEmail || 'Sem email'}
                        </p>
                      </div>
                      <nav className="p-1">
                        <button className="flex w-full items-center gap-2 rounded-lg px-3 py-2 text-sm transition hover:bg-secondary">
                          <User className="h-4 w-4 text-muted-foreground" />
                          Meu perfil
                        </button>
                        <button className="flex w-full items-center gap-2 rounded-lg px-3 py-2 text-sm transition hover:bg-secondary">
                          <Settings className="h-4 w-4 text-muted-foreground" />
                          Configurações
                        </button>
                        <button
                          type="button"
                          onClick={() => {
                            setIsProfileOpen(false)
                            logout()
                          }}
                          className="flex w-full items-center gap-2 rounded-lg px-3 py-2 text-sm text-red-600 transition hover:bg-red-50"
                        >
                          <LogOut className="h-4 w-4" />
                          Sair
                        </button>
                      </nav>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>
        </header>

        <main className="mx-auto max-w-6xl px-4 py-8 sm:px-6">
          <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <h1 className="text-2xl font-bold tracking-tight text-balance sm:text-3xl">
                Minhas Tarefas
              </h1>
              <p className="mt-1 text-sm text-muted-foreground">
                Organize seu dia e mantenha o foco no que importa.
              </p>
            </div>
            <button
              type="button"
              onClick={openCreate}
              className="inline-flex items-center justify-center gap-2 rounded-lg bg-primary px-4 py-2.5 text-sm font-semibold text-primary-foreground shadow-sm transition hover:opacity-90 active:scale-[0.98]"
            >
              <Plus className="h-4 w-4" />
              Nova Tarefa
            </button>
          </div>

          <div className="mt-6 grid grid-cols-2 gap-3 lg:grid-cols-4">
            <StatCard
              icon={<ListTodo className="h-5 w-5" />}
              label="Total"
              value={stats.total}
              accent="bg-accent text-accent-foreground"
            />
            <StatCard
              icon={<Clock className="h-5 w-5" />}
              label="Pendentes"
              value={stats.pending}
              accent="bg-amber-50 text-amber-700"
            />
            <StatCard
              icon={<CircleCheckBig className="h-5 w-5" />}
              label="Concluídas"
              value={stats.done}
              accent="bg-emerald-50 text-emerald-700"
            />
            <StatCard
              icon={<Flag className="h-5 w-5" />}
              label="Urgentes"
              value={stats.critical}
              accent="bg-red-50 text-red-700"
            />
          </div>

          <div className="mt-8 flex flex-col gap-4 border-b border-border pb-5 lg:flex-row lg:items-center lg:justify-between">
            <div className="flex flex-wrap items-center gap-1.5">
              {STATUS_OPTIONS.map(({ value, label }) => (
                <button
                  key={value}
                  type="button"
                  onClick={() => setStatusFilter(value)}
                  className={`rounded-lg px-3.5 py-2 text-sm font-medium transition ${
                    statusFilter === value
                      ? 'bg-primary text-primary-foreground shadow-sm'
                      : 'bg-secondary text-secondary-foreground hover:bg-accent'
                  }`}
                >
                  {label}
                </button>
              ))}
            </div>

            <div className="flex items-center gap-2">
              <Flag className="h-4 w-4 text-muted-foreground" />
              <div className="flex flex-wrap items-center gap-1.5">
                <button
                  type="button"
                  onClick={() => setPriorityFilter('all')}
                  className={`rounded-lg px-3 py-1.5 text-xs font-semibold transition ${
                    priorityFilter === 'all'
                      ? 'bg-foreground text-background'
                      : 'bg-secondary text-secondary-foreground hover:bg-accent'
                  }`}
                >
                  Todas
                </button>
                {([1, 2, 3, 4, 5] as Priority[]).map((p) => (
                  <button
                    key={p}
                    type="button"
                    onClick={() => setPriorityFilter(p)}
                    title={PRIORITY_META[p].label}
                    className={`flex h-8 w-8 items-center justify-center rounded-lg text-xs font-bold transition ring-1 ring-inset ${
                      priorityFilter === p
                        ? PRIORITY_META[p].badge + ' scale-105'
                        : 'bg-secondary text-secondary-foreground ring-transparent hover:bg-accent'
                    }`}
                  >
                    {p}
                  </button>
                ))}
              </div>
            </div>
          </div>

          {isLoadingTasks ? (
            <div className="mt-10 flex flex-col items-center justify-center gap-3 rounded-2xl border border-border bg-card/70 py-16">
              <div className="flex h-12 w-12 items-center justify-center rounded-full border-4 border-primary/20 border-t-primary animate-spin" />
              <p className="text-sm font-medium text-muted-foreground">Carregando tarefas...</p>
            </div>
          ) : filteredTasks.length === 0 ? (
            <EmptyState onCreate={openCreate} />
          ) : (
            <>
              <div className="mt-6 grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-3">
                {filteredTasks.map((task) => (
                  <TaskCard
                    key={task.id}
                    task={task}
                    onToggle={() => toggleComplete(task.id)}
                    onEdit={() => openEdit(task)}
                    onDelete={() => openDeleteConfirm(task)}
                    isCompleting={completingTaskId === task.id}
                  />
                ))}
              </div>

              {totalPages > 1 && (
                <div className="mt-6 flex flex-col gap-3 rounded-2xl border border-border bg-card/70 px-4 py-4 sm:flex-row sm:items-center sm:justify-between">
                  <p className="text-sm text-muted-foreground">
                    Página {currentPage + 1} de {totalPages} • {totalItems} tarefa(s) no total
                  </p>

                  <div className="flex items-center gap-2">
                    <button
                      type="button"
                      onClick={() => setCurrentPage((page) => Math.max(page - 1, 0))}
                      disabled={currentPage === 0 || isLoadingTasks}
                      className="rounded-lg border border-input bg-background px-3.5 py-2 text-sm font-medium transition hover:bg-secondary disabled:cursor-not-allowed disabled:opacity-60"
                    >
                      Anterior
                    </button>

                    {Array.from({ length: totalPages }, (_, index) => index + 1).map((page) => {
                      const isActive = page - 1 === currentPage

                      return (
                        <button
                          key={page}
                          type="button"
                          onClick={() => setCurrentPage(page - 1)}
                          disabled={isLoadingTasks}
                          className={`h-9 w-9 rounded-lg text-sm font-semibold transition ${
                            isActive
                              ? 'bg-primary text-primary-foreground shadow-sm'
                              : 'border border-input bg-background text-foreground hover:bg-secondary'
                          }`}
                        >
                          {page}
                        </button>
                      )
                    })}

                    <button
                      type="button"
                      onClick={() => setCurrentPage((page) => Math.min(page + 1, totalPages - 1))}
                      disabled={currentPage >= totalPages - 1 || isLoadingTasks}
                      className="rounded-lg border border-input bg-background px-3.5 py-2 text-sm font-medium transition hover:bg-secondary disabled:cursor-not-allowed disabled:opacity-60"
                    >
                      Próxima
                    </button>
                  </div>
                </div>
              )}
            </>
          )}
        </main>
      </div>

      {isModalOpen && (
        <TaskModal
          form={form}
          setForm={setForm}
          onClose={closeModal}
          onSubmit={handleSubmit}
          isEditing={Boolean(editingId)}
          isSubmitting={isSubmittingTask}
        />
      )}

      {isDeleteModalOpen && taskToDelete && (
        <div className="fixed inset-0 z-[60] flex items-center justify-center bg-foreground/40 p-4 backdrop-blur-sm">
          <div className="w-full max-w-md rounded-2xl border border-border bg-card shadow-xl">
            <div className="border-b border-border px-6 py-4">
              <h3 className="text-lg font-semibold">Excluir tarefa</h3>
              <p className="mt-1 text-sm text-muted-foreground">
                Tem certeza que deseja remover esta tarefa? Esta ação não pode ser desfeita.
              </p>
            </div>

            <div className="px-6 py-5">
              <div className="rounded-xl border border-border bg-secondary/40 p-4">
                <p className="text-sm font-medium">{taskToDelete.taskName}</p>
                <p className="mt-1 line-clamp-2 text-sm text-muted-foreground">
                  {taskToDelete.taskDescription || 'Sem descrição'}
                </p>
              </div>
            </div>

            <div className="flex items-center justify-end gap-2 border-t border-border px-6 py-4">
              <button
                type="button"
                onClick={closeDeleteConfirm}
                className="rounded-lg border border-input bg-background px-4 py-2.5 text-sm font-medium transition hover:bg-secondary"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={confirmDelete}
                className="rounded-lg bg-red-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-red-700"
              >
                Excluir
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  )
}
