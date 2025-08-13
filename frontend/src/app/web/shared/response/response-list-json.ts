export class IResponseListJson<type> {
    public data!: type;
    public totalRegistros!: number; // pagination
}
