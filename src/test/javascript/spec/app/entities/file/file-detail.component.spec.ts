/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { MystaffTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FileDetailComponent } from '../../../../../../main/webapp/app/entities/file/file-detail.component';
import { FileService } from '../../../../../../main/webapp/app/entities/file/file.service';
import { File } from '../../../../../../main/webapp/app/entities/file/file.model';

describe('Component Tests', () => {

    describe('File Management Detail Component', () => {
        let comp: FileDetailComponent;
        let fixture: ComponentFixture<FileDetailComponent>;
        let service: FileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MystaffTestModule],
                declarations: [FileDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FileService,
                    JhiEventManager
                ]
            }).overrideTemplate(FileDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FileDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new File(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.file).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
