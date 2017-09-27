import { BaseEntity } from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public firstName?: string,
        public midleName?: string,
        public lastName?: string,
        public login?: string,
        public passwordHash?: string,
        public phone?: string,
        public email?: string,
        public imageUrl?: string,
        public activated?: boolean,
        public langKey?: string,
        public activationKey?: string,
        public resetKey?: string,
        public createdDate?: any,
        public resetDate?: any,
        public lastModifiedBy?: string,
        public lastModifiedDate?: any,
        public companies?: BaseEntity[],
    ) {
        this.activated = false;
    }
}
