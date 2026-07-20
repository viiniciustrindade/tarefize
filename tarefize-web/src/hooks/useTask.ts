import { useEffect, useMemo, useState } from 'react'

import { taskService } from '@/services/task'
import type { Priority, StatusFilter, Task, TaskFormState, TaskRequest } from '@/types/task'

const createEmptyForm = (): TaskFormState => ({
  taskName: '',
  taskDescription: '',
  priority: 3,
  dueDate: '',
})

export function useTask() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [isLoadingTasks, setIsLoadingTasks] = useState(true)
  const [statusFilter, setStatusFilter] = useState<StatusFilter>('all')
  const [priorityFilter, setPriorityFilter] = useState<Priority | 'all'>('all')
  const [search, setSearch] = useState('')
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)
  const [totalItems, setTotalItems] = useState(0)
  const [pageSize] = useState(6)

  const [isModalOpen, setIsModalOpen] = useState(false)
  const [editingId, setEditingId] = useState<number | null>(null)
  const [form, setForm] = useState<TaskFormState>(createEmptyForm)
  const [isSubmittingTask, setIsSubmittingTask] = useState(false)
  const [completingTaskId, setCompletingTaskId] = useState<number | null>(null)
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)
  const [taskToDelete, setTaskToDelete] = useState<Task | null>(null)

  const [isProfileOpen, setIsProfileOpen] = useState(false)

  async function loadTasksPage(page = currentPage) {
    try {
      setIsLoadingTasks(true)
      const loadedPage = await taskService.loadTasks(page, pageSize)
      setTasks(loadedPage.tasks)
      setTotalPages(Math.max(loadedPage.totalPages, 1))
      setTotalItems(loadedPage.totalElements)
    } catch (error) {
      console.error(error)
    } finally {
      setIsLoadingTasks(false)
    }
  }

  useEffect(() => {
    void loadTasksPage(currentPage)
  }, [currentPage, pageSize])

  const filteredTasks = useMemo(() => {
    return tasks.filter((task) => {
      const matchStatus =
        statusFilter === 'all'
          ? true
          : statusFilter === 'done'
            ? task.completed
            : !task.completed
      const matchPriority =
        priorityFilter === 'all' ? true : task.priority === priorityFilter
      const matchSearch =
        search.trim() === ''
          ? true
          : (task.taskName + ' ' + task.taskDescription)
              .toLowerCase()
              .includes(search.toLowerCase())

      return matchStatus && matchPriority && matchSearch
    })
  }, [priorityFilter, search, statusFilter, tasks])

  const stats = useMemo(() => {
    const total = tasks.length
    const done = tasks.filter((task) => task.completed).length
    const pending = total - done
    const critical = tasks.filter((task) => task.priority >= 4 && !task.completed).length

    return { total, done, pending, critical }
  }, [tasks])

  async function toggleComplete(id: number) {
    setCompletingTaskId(id)

    try {
      await taskService.completeTask(id)
      await loadTasksPage(currentPage)
    } catch (error) {
      console.error(error)
    } finally {
      setCompletingTaskId(null)
    }
  }

  async function deleteTask(id: number) {
    try {
      await taskService.deleteTask(id)
      setTasks((prevTasks) => prevTasks.filter((task) => task.id !== id))
    } catch (error) {
      console.error(error)
    }
  }

  function openDeleteConfirm(task: Task) {
    setTaskToDelete(task)
    setIsDeleteModalOpen(true)
  }

  function closeDeleteConfirm() {
    setIsDeleteModalOpen(false)
    setTaskToDelete(null)
  }

  async function confirmDelete() {
    if (!taskToDelete) return

    await deleteTask(taskToDelete.id)
    closeDeleteConfirm()
  }

  function openCreate() {
    setEditingId(null)
    setForm(createEmptyForm())
    setIsModalOpen(true)
  }

  function openEdit(task: Task) {
    setEditingId(task.id)
    setForm({
      taskName: task.taskName,
      taskDescription: task.taskDescription,
      priority: task.priority,
      dueDate: '',
    })
    setIsModalOpen(true)
  }

  function closeModal() {
    setIsModalOpen(false)
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!form.taskName.trim()) return

    const payload: TaskRequest = {
      taskName: form.taskName.trim(),
      taskDescription: form.taskDescription,
      priority: form.priority,
    }

    try {
      setIsSubmittingTask(true)
      if (editingId) {
        const updatedTask = await taskService.updateTask(editingId, payload)
        setTasks((prevTasks) =>
          prevTasks.map((task) => (task.id === editingId ? updatedTask : task)),
        )
      } else {
        await taskService.createTask(payload)
        setCurrentPage(0)
        await loadTasksPage(0)
      }
    } catch (error) {
      console.error(error)
      return
    } finally {
      setIsSubmittingTask(false)
    }

    setIsModalOpen(false)
    setEditingId(null)
    setForm(createEmptyForm())
  }

  return {
    tasks,
    isLoadingTasks,
    currentPage,
    setCurrentPage,
    totalPages,
    totalItems,
    pageSize,
    statusFilter,
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
  }
}
