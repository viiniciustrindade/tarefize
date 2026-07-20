import type { ReactNode } from 'react'

export function StatCard({
  icon,
  label,
  value,
  accent,
}: {
  icon: ReactNode
  label: string
  value: number
  accent: string
}) {
  return (
    <div className="flex items-center gap-3 rounded-xl border border-border bg-card p-4">
      <span className={`flex h-10 w-10 items-center justify-center rounded-lg ${accent}`}>
        {icon}
      </span>
      <div>
        <p className="text-2xl font-bold leading-none">{value}</p>
        <p className="mt-1 text-xs text-muted-foreground">{label}</p>
      </div>
    </div>
  )
}
