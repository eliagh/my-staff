import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MystaffSharedModule } from '../../shared';
import {
    InventoryService,
    InventoryPopupService,
    InventoryComponent,
    InventoryDetailComponent,
    InventoryDialogComponent,
    InventoryPopupComponent,
    InventoryDeletePopupComponent,
    InventoryDeleteDialogComponent,
    inventoryRoute,
    inventoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...inventoryRoute,
    ...inventoryPopupRoute,
];

@NgModule({
    imports: [
        MystaffSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        InventoryComponent,
        InventoryDetailComponent,
        InventoryDialogComponent,
        InventoryDeleteDialogComponent,
        InventoryPopupComponent,
        InventoryDeletePopupComponent,
    ],
    entryComponents: [
        InventoryComponent,
        InventoryDialogComponent,
        InventoryPopupComponent,
        InventoryDeleteDialogComponent,
        InventoryDeletePopupComponent,
    ],
    providers: [
        InventoryService,
        InventoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MystaffInventoryModule {}
