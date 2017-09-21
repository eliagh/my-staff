/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { MystaffTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InventoryDetailComponent } from '../../../../../../main/webapp/app/entities/inventory/inventory-detail.component';
import { InventoryService } from '../../../../../../main/webapp/app/entities/inventory/inventory.service';
import { Inventory } from '../../../../../../main/webapp/app/entities/inventory/inventory.model';

describe('Component Tests', () => {

    describe('Inventory Management Detail Component', () => {
        let comp: InventoryDetailComponent;
        let fixture: ComponentFixture<InventoryDetailComponent>;
        let service: InventoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MystaffTestModule],
                declarations: [InventoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    InventoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(InventoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InventoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InventoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Inventory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.inventory).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
