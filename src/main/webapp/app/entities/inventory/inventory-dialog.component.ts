import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Inventory } from './inventory.model';
import { InventoryPopupService } from './inventory-popup.service';
import { InventoryService } from './inventory.service';
import { Location, LocationService } from '../location';
import { Item, ItemService } from '../item';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-inventory-dialog',
    templateUrl: './inventory-dialog.component.html'
})
export class InventoryDialogComponent implements OnInit {

    inventory: Inventory;
    isSaving: boolean;

    locations: Location[];

    items: Item[];
    inventoryDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private inventoryService: InventoryService,
        private locationService: LocationService,
        private itemService: ItemService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.locationService
            .query({filter: 'inventory-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.inventory.location || !this.inventory.location.id) {
                    this.locations = res.json;
                } else {
                    this.locationService
                        .find(this.inventory.location.id)
                        .subscribe((subRes: Location) => {
                            this.locations = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.itemService
            .query({filter: 'inventory-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.inventory.item || !this.inventory.item.id) {
                    this.items = res.json;
                } else {
                    this.itemService
                        .find(this.inventory.item.id)
                        .subscribe((subRes: Item) => {
                            this.items = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.inventory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.inventoryService.update(this.inventory));
        } else {
            this.subscribeToSaveResponse(
                this.inventoryService.create(this.inventory));
        }
    }

    private subscribeToSaveResponse(result: Observable<Inventory>) {
        result.subscribe((res: Inventory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Inventory) {
        this.eventManager.broadcast({ name: 'inventoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackLocationById(index: number, item: Location) {
        return item.id;
    }

    trackItemById(index: number, item: Item) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-inventory-popup',
    template: ''
})
export class InventoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private inventoryPopupService: InventoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.inventoryPopupService
                    .open(InventoryDialogComponent as Component, params['id']);
            } else {
                this.inventoryPopupService
                    .open(InventoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
