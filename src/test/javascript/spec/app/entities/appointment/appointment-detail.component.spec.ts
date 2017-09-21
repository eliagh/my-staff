/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { MystaffTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AppointmentDetailComponent } from '../../../../../../main/webapp/app/entities/appointment/appointment-detail.component';
import { AppointmentService } from '../../../../../../main/webapp/app/entities/appointment/appointment.service';
import { Appointment } from '../../../../../../main/webapp/app/entities/appointment/appointment.model';

describe('Component Tests', () => {

    describe('Appointment Management Detail Component', () => {
        let comp: AppointmentDetailComponent;
        let fixture: ComponentFixture<AppointmentDetailComponent>;
        let service: AppointmentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MystaffTestModule],
                declarations: [AppointmentDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AppointmentService,
                    JhiEventManager
                ]
            }).overrideTemplate(AppointmentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AppointmentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppointmentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Appointment(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.appointment).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
