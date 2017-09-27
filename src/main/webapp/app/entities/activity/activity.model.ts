import { BaseEntity } from './../../shared';

export class Activity implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public logoContentType?: string,
        public logo?: any,
        public description?: any,
        public price?: number,
        public durationMinutes?: number,
        public preDurationMinutes?: number,
        public postDurationMinutes?: number,
        public isPrivate?: boolean,
        public colorCode?: string,
        public cancellationTime?: number,
        public locations?: BaseEntity[],
        public categories?: BaseEntity[],
    ) {
        this.isPrivate = false;
    }
}
