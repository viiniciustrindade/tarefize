import { NextRequest, NextResponse } from "next/server";

const publicRoutes = ['/login', '/register']

export function middleware(request: NextRequest){
    const token = request.cookies.get('tarefize_token')?.value;
    const { pathname } = request.nextUrl;

    const isPublicRoute = publicRoutes.some(route => pathname === route || pathname.startsWith(route + '/'));

    if (!isPublicRoute && !token) {
        const loginUrl = new URL('/login', request.url);
        return NextResponse.redirect(loginUrl);
    }

    if (isPublicRoute && token) {
        const dashboardUrl = new URL('/dashboard', request.url);
        return NextResponse.redirect(dashboardUrl);
    }

    return NextResponse.next();
}

export const config = {
  matcher: [
    '/((?!api|_next/static|_next/image|favicon.ico|.*\\.png$|.*\\.jpg$).*)',
  ],
};