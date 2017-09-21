import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MystaffCompanyModule } from './company/company.module';
import { MystaffLocationModule } from './location/location.module';
import { MystaffItemModule } from './item/item.module';
import { MystaffInventoryModule } from './inventory/inventory.module';
import { MystaffCategoryModule } from './category/category.module';
import { MystaffActivityModule } from './activity/activity.module';
import { MystaffAppointmentModule } from './appointment/appointment.module';
import { MystaffFileModule } from './file/file.module';
import { MystaffCustomerModule } from './customer/customer.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        MystaffCompanyModule,
        MystaffLocationModule,
        MystaffItemModule,
        MystaffInventoryModule,
        MystaffCategoryModule,
        MystaffActivityModule,
        MystaffAppointmentModule,
        MystaffFileModule,
        MystaffCustomerModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MystaffEntityModule {}
