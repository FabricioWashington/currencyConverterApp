import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class CookieService {

    setItem(key: string, value: string, days: number = 30): void {
        let date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        let expires = "expires=" + date.toUTCString();
        let domain = ";domain=" + this.getDomain();
        document.cookie = key + "=" + value + ";" + expires + ";path=/" + domain;
    }

    getItem(key: string): string | null {
        let ret = null;
        let name = key + "=";
        let cookies = decodeURIComponent(document.cookie).split(';');
        for (let i = 0; i < cookies.length; i++) {
            let cookie = cookies[i].trim();
            if (cookie.indexOf(name) == 0) {
                ret = cookie.substring(name.length, cookie.length);
            }
        }
        return ret;
    }

    deleteItem(key: string): void {
        let domain = ";domain=" + this.getDomain();
        document.cookie = key + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC;" + ";path=/" + domain;
    }

    private getDomain() {
        var urlAtual = window.location.href;
        if (urlAtual.includes('localhost')) {
            return 'localhost';
        } else {
            const match = urlAtual.match(/(?:https?:\/\/)?(?:[^.@\n]+\.)?([^:\/\n]+\.[^:\/\n]+)/);
            return match && match[1] ? match[1] : '';
        }
    }
}
