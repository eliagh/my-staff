import { BaseEntity, User } from './../../shared';

export class Appointment implements BaseEntity {
    constructor(
        public id?: number,
        public when?: any,
        public label?: string,
        public isRecurring?: boolean,
        public isFlexible?: boolean,
        public notes?: any,
        public reminder?: string,
        public customer?: BaseEntity,
        public activityBooked?: BaseEntity,
        public location?: BaseEntity,
        public provider?: User,
    ) {
        this.isRecurring = false;
        this.isFlexible = false;
    }
}
