import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { File } from './file.model';
import { FilePopupService } from './file-popup.service';
import { FileService } from './file.service';

@Component({
    selector: 'jhi-file-delete-dialog',
    templateUrl: './file-delete-dialog.component.html'
})
export class FileDeleteDialogComponent {

    file: File;

    constructor(
        private fileService: FileService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.fileService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'fileListModification',
                content: 'Deleted an file'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-file-delete-popup',
    template: ''
})
export class FileDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private filePopupService: FilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.filePopupService
                .open(FileDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
