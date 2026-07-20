import { NextRequest, NextResponse } from "next/server";

const publicRoutes = ['/login', '/register'];

export function middleware(request: NextRequest) {
    const token = request.cookies.get('tarefize_token')?.value;
    const { pathname } = request.nextUrl;

    
    const normalizedPathname = pathname.endsWith('/') && pathname !== '/' 
        ? pathname.slice(0, -1) 
        : pathname;

    const isPublicRoute = publicRoutes.includes(normalizedPathname);

    if (normalizedPathname === '/') {
        if (token) {
            return NextResponse.redirect(new URL('/dashboard', request.url));
        }
        return NextResponse.next();
    }

    if (!isPublicRoute && !token) {
        return NextResponse.redirect(new URL('/login', request.url));
    }

    if (isPublicRoute && token) {
        return NextResponse.redirect(new URL('/dashboard', request.url));
    }

    return NextResponse.next();
}

export const config = {
  matcher: [
    
    '/((?!api|_next/static|_next/image|favicon.ico|.*\\.png$|.*\\.jpg$).*)',
  ],
};