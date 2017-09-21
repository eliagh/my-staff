import { BaseEntity } from './../../shared';

export class Company implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public logoContentType?: string,
        public logo?: any,
        public sector?: string,
        public thema?: string,
    ) {
    }
}
