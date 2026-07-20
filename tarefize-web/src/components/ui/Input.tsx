import { ComponentProps } from "react";

interface InputProps extends ComponentProps<"input"> {
    label?: string;
    error?: string;
}

export function Input ({label, error, className="", ...props }: InputProps){
    return(
      <div className="flex flex-col gap-1.5 w-full">
            {label && (
                <label className="text-sm font-medium text-foreground">
                    {label}
                </label>
            )}

            <input {...props}
            className={`w-full rounded-lg border bg-background px-3 py-2.5 text-foreground shadow-sm outline-none transition-all duration-200 placeholder:text-muted-foreground focus:border-ring focus:ring-2 focus:ring-ring/30
            ${error ? "border-red-500 focus:border-red-500 focus:ring-red-200" : "border-input"}
            ${className}
            `}/>

            {error && (
                <span className="text-xs font-medium text-red-500">
                {error}
                </span>
            )}
        </div>
    );
}