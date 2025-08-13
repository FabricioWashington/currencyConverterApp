
/**
 * @publicApi
 */
export class FiltroDTO<T> {
    public page: number = 0;
    public pageSize: number = 10;
    public sortField?: string;
    public sortOrder?: string;
    public sidebar: boolean = false;
    public data?: T;

    public setPage($event: any) {
        if (!$event) {
            this.page = 0;
        } else {
            this.page = $event.first / $event.rows;
            this.pageSize = $event.rows;
            this.sortField = $event.sortField;
            this.sortOrder = $event.sortOrder > 0 ? 'asc' : 'desc';
        }
    }
}
