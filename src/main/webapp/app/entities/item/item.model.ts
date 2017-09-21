import { BaseEntity } from './../../shared';

export class Item implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public pictureUrl?: string,
        public pricePerUnit?: number,
        public unit?: string,
        public code?: any,
        public description?: any,
        public showInShop?: boolean,
        public company?: BaseEntity,
    ) {
        this.showInShop = false;
    }
}
