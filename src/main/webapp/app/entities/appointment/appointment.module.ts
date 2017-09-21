import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MystaffSharedModule } from '../../shared';
import { MystaffAdminModule } from '../../admin/admin.module';
import {
    AppointmentService,
    AppointmentPopupService,
    AppointmentComponent,
    AppointmentDetailComponent,
    AppointmentDialogComponent,
    AppointmentPopupComponent,
    AppointmentDeletePopupComponent,
    AppointmentDeleteDialogComponent,
    appointmentRoute,
    appointmentPopupRoute,
} from './';

const ENTITY_STATES = [
    ...appointmentRoute,
    ...appointmentPopupRoute,
];

@NgModule({
    imports: [
        MystaffSharedModule,
        MystaffAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AppointmentComponent,
        AppointmentDetailComponent,
        AppointmentDialogComponent,
        AppointmentDeleteDialogComponent,
        AppointmentPopupComponent,
        AppointmentDeletePopupComponent,
    ],
    entryComponents: [
        AppointmentComponent,
        AppointmentDialogComponent,
        AppointmentPopupComponent,
        AppointmentDeleteDialogComponent,
        AppointmentDeletePopupComponent,
    ],
    providers: [
        AppointmentService,
        AppointmentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MystaffAppointmentModule {}
