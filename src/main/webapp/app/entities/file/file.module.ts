import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MystaffSharedModule } from '../../shared';
import {
    FileService,
    FilePopupService,
    FileComponent,
    FileDetailComponent,
    FileDialogComponent,
    FilePopupComponent,
    FileDeletePopupComponent,
    FileDeleteDialogComponent,
    fileRoute,
    filePopupRoute,
} from './';

const ENTITY_STATES = [
    ...fileRoute,
    ...filePopupRoute,
];

@NgModule({
    imports: [
        MystaffSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        FileComponent,
        FileDetailComponent,
        FileDialogComponent,
        FileDeleteDialogComponent,
        FilePopupComponent,
        FileDeletePopupComponent,
    ],
    entryComponents: [
        FileComponent,
        FileDialogComponent,
        FilePopupComponent,
        FileDeleteDialogComponent,
        FileDeletePopupComponent,
    ],
    providers: [
        FileService,
        FilePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MystaffFileModule {}
