import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Appointment } from './appointment.model';
import { AppointmentPopupService } from './appointment-popup.service';
import { AppointmentService } from './appointment.service';

@Component({
    selector: 'jhi-appointment-delete-dialog',
    templateUrl: './appointment-delete-dialog.component.html'
})
export class AppointmentDeleteDialogComponent {

    appointment: Appointment;

    constructor(
        private appointmentService: AppointmentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.appointmentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'appointmentListModification',
                content: 'Deleted an appointment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-appointment-delete-popup',
    template: ''
})
export class AppointmentDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appointmentPopupService: AppointmentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.appointmentPopupService
                .open(AppointmentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
