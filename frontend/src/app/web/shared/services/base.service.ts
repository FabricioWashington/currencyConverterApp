import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IResponseListJson } from '../response/response-list-json';
import { IResponseJson } from '../response/response-json';
import { FiltroDTO } from '../generic/filtro.dto';

@Injectable({ providedIn: 'root' })
export class BaseService<Type> {

    public api: string;

    constructor(
        public http: HttpClient,
        @Inject('APP_ENVIRONMENT') private env: any
    ) {
        this.httpClient = http;
        this.api = this.env.api;
    }

    public httpClient: HttpClient;
    public endpoint: string = '';

    public get apiUrl(): string {
        return `${this.api}${this.endpoint}`;
    }

    findAll(options?: object): Observable<IResponseListJson<Type[]>> {
        return this.http.get<IResponseListJson<Type[]>>(`${this.apiUrl}`, options)
    }

    search(filtro: FiltroDTO<Type>, options?: object) {
        return this.http.post<IResponseListJson<Type[]>>(`${this.apiUrl}`, filtro, options)
    }

    get(id: number, options?: object): Observable<Type> {
        return this.http.get<Type>(`${this.apiUrl}/get?id=${id}`, options)
    }

    create(model: Type, options?: object): Observable<IResponseJson<Type>> {
        return this.http.post<IResponseJson<Type>>(`${this.apiUrl}/create`, model, options);
    }

    update(id: number, model: Type, options?: object): Observable<IResponseJson<Type>> {
        return this.http.post<IResponseJson<Type>>(`${this.apiUrl}/update?id=${id}`, model, options);
    }

    delete(id: number, options?: object): Observable<IResponseJson<Type>> {
        return this.http.post<IResponseJson<Type>>(`${this.apiUrl}/delete?id=${id}`, {}, options);
    }
}
