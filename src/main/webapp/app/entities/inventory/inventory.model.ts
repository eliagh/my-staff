import { BaseEntity } from './../../shared';

export class Inventory implements BaseEntity {
    constructor(
        public id?: number,
        public quantity?: number,
        public inventoryDate?: any,
        public location?: BaseEntity,
        public item?: BaseEntity,
    ) {
    }
}
