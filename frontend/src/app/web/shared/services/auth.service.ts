// import { Injectable, Inject } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { BehaviorSubject, Observable } from 'rxjs';
// import { map } from 'rxjs/operators';
// import { SocialUser } from '@abacritt/angularx-social-login';
// import { ActivatedRouteSnapshot } from '@angular/router';
// import { CookieService } from './cookie.service';
// import { UserActionEnum } from '../../enum/user-action.enum';
// import { AuthAcesso, UserAuthDTO } from '../../models/userauth.model';
// import { LoginUpdate } from '../../models/login-update.model';

// @Injectable({ providedIn: 'root' })
// export class AuthService {
//     private currentUserSubject: BehaviorSubject<UserAuthDTO>;
//     public currentUser: Observable<UserAuthDTO>;

//     private currentAccessSubject: BehaviorSubject<AuthAcesso>;
//     public currentAccess: Observable<AuthAcesso>;

//     constructor(
//         private http: HttpClient,
//         private cookieService: CookieService,
//         @Inject('APP_ENVIRONMENT') private env?: any
//     ) {
//         this.currentUserSubject = new BehaviorSubject<UserAuthDTO>(
//             this.getUser()
//         );
//         this.currentAccessSubject = new BehaviorSubject<AuthAcesso>(
//             this.getAccess()
//         );
//         this.currentUser = this.currentUserSubject.asObservable();
//         this.currentAccess = this.currentAccessSubject.asObservable();
//     }

//     public get currentUserValue(): UserAuthDTO {
//         return this.currentUserSubject.getValue();
//     }

//     public get currentAccessValue(): AuthAcesso {
//         return this.currentAccessSubject.getValue();
//     }

//     isStart(user: UserAuthDTO, route: ActivatedRouteSnapshot) {
//         if (route.routeConfig && route.routeConfig.path === 'start') return false;
//         let logout = route.queryParams['r'] && route.queryParams['r'].search('login/logout');

//         if (!logout) {
//             if (!user.acessos?.length) return true;
//             if (user.usuario.ds_action == UserActionEnum.CONFIRM_EMAIL) return true;
//         }
//         return false;
//     }

//     get() {
//         return this.http.get<UserAuthDTO>(`${this.env.domain.api}/auth/get`);
//     }

//     getUser() {
//         const userStr = this.cookieService.getItem(this.env.userStorage);
//         return userStr ? JSON.parse(userStr) : {};
//     }

//     getAccess() {
//         const user = this.getUser();
//         return user?.acessos?.find((x: any) => x.in_atual);
//     }

//     login(login: import('../../models/login.model').Login) {
//         return this.http.post<UserAuthDTO>(`${this.env.domain.api}/auth`, login).pipe(map(user => {
//             if (user && user.token) {
//                 this.saveSessionStorage(user);
//             }
//             return user;
//         }));
//     }

//     loginWithToken(token: string) {
//         return this.http.post<UserAuthDTO>(`${this.env.domain.api}/authToken`, {
//             token: token
//         }).pipe(map(user => {
//             if (user && user.token) {
//                 this.saveSessionStorage(user);
//             }
//             return user;
//         }));
//     }

//     loginGoogle(socialUser: SocialUser) {
//         return this.http.post<UserAuthDTO>(`${this.env.domain.api}/auth/google`, socialUser).pipe(map(user => {
//             if (user && user.token) {
//                 this.saveSessionStorage(user);
//             }
//             return user;
//         }));
//     }

//     update(loginUpdate: LoginUpdate) {
//         return this.http.post<UserAuthDTO>(`${this.env.domain.api}/auth/update`, loginUpdate).pipe(map(user => {
//             if (user && user.token) {
//                 this.saveSessionStorage(user);
//             }
//             return user;
//         }));
//     }

//     loginLegado(user: UserAuthDTO, redirect?: string) {
//         redirect = redirect || this.env.domain.apl;
//         location.href = `${this.env.domain.erp}/login/auth?authorization=${user.login}&redirect=${redirect}`;
//     }

//     saveRefreshSession(user: UserAuthDTO) {
//         this.saveSessionStorage(user);
//         this.refreshSessionStorage(user);
//     }

//     saveSessionStorage(user: UserAuthDTO) {
//         this.cookieService.setItem(this.env.userStorage, JSON.stringify(user));
//     }

//     refreshSessionStorage(user: UserAuthDTO) {
//         this.currentUserSubject.next(user);
//         const atual = user.acessos.find((a: any) => a.in_atual);
//         this.currentAccessSubject.next(atual ? atual : {} as AuthAcesso);
//     }

//     setAccess(access: AuthAcesso) {
//         let user = this.getUser();
//         user.acessos.map((a: any) => {
//             a.in_atual = access.id_empresa == a.id_empresa
//         });
//         this.saveRefreshSession(user);
//     }

//     logout(noRedirect?: boolean) {
//         this.cookieService.deleteItem(this.env.userStorage);
//         this.currentUserSubject.next({} as UserAuthDTO);
//         if (!noRedirect) {
//             location.href = `${this.env.domain.erp}/login/logout?redirect=${this.env.domain.apl}`;
//         }
//     }

//     isFree() {
//         const user = this.getUser();
//         const acesso = user?.acessos?.find((x: any) => x.in_atual);
//         return !acesso || acesso.plano?.ds_plano === 'Free';
//     }
// }
