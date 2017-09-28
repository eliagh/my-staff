import { BaseEntity } from './../../shared';

export const enum FileType {
    'AVATAR',
    'PRODUCT_PICTURE',
    'REPORT'
}

export class File implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public url?: string,
        public fileType?: FileType,
        public fileContentType?: string,
        public file?: any,
        public company?: BaseEntity,
    ) {
    }
}
