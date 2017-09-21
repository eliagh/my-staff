import { BaseEntity } from './../../shared';

export class Location implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public address1?: string,
        public address2?: string,
        public address3?: string,
        public number?: number,
        public postcode?: string,
        public city?: string,
        public stateProvince?: string,
        public company?: BaseEntity,
    ) {
    }
}
