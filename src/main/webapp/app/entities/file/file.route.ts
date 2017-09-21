import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FileComponent } from './file.component';
import { FileDetailComponent } from './file-detail.component';
import { FilePopupComponent } from './file-dialog.component';
import { FileDeletePopupComponent } from './file-delete-dialog.component';

export const fileRoute: Routes = [
    {
        path: 'file',
        component: FileComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mystaffApp.file.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'file/:id',
        component: FileDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mystaffApp.file.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const filePopupRoute: Routes = [
    {
        path: 'file-new',
        component: FilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mystaffApp.file.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'file/:id/edit',
        component: FilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mystaffApp.file.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'file/:id/delete',
        component: FileDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'mystaffApp.file.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
