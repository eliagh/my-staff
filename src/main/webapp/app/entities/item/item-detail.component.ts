import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Item } from './item.model';
import { ItemService } from './item.service';

@Component({
    selector: 'jhi-item-detail',
    templateUrl: './item-detail.component.html'
})
export class ItemDetailComponent implements OnInit, OnDestroy {

    item: Item;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private itemService: ItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInItems();
    }

    load(id) {
        this.itemService.find(id).subscribe((item) => {
            this.item = item;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'itemListModification',
            (response) => this.load(this.item.id)
        );
    }
}
