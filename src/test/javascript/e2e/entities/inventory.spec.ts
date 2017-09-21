import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Inventory e2e test', () => {

    let navBarPage: NavBarPage;
    let inventoryDialogPage: InventoryDialogPage;
    let inventoryComponentsPage: InventoryComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Inventories', () => {
        navBarPage.goToEntity('inventory');
        inventoryComponentsPage = new InventoryComponentsPage();
        expect(inventoryComponentsPage.getTitle()).toMatch(/mystaffApp.inventory.home.title/);

    });

    it('should load create Inventory dialog', () => {
        inventoryComponentsPage.clickOnCreateButton();
        inventoryDialogPage = new InventoryDialogPage();
        expect(inventoryDialogPage.getModalTitle()).toMatch(/mystaffApp.inventory.home.createOrEditLabel/);
        inventoryDialogPage.close();
    });

   /* it('should create and save Inventories', () => {
        inventoryComponentsPage.clickOnCreateButton();
        inventoryDialogPage.setQuantityInput('5');
        expect(inventoryDialogPage.getQuantityInput()).toMatch('5');
        inventoryDialogPage.setInventoryDateInput('2000-12-31');
        expect(inventoryDialogPage.getInventoryDateInput()).toMatch('2000-12-31');
        inventoryDialogPage.locationSelectLastOption();
        inventoryDialogPage.itemSelectLastOption();
        inventoryDialogPage.save();
        expect(inventoryDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class InventoryComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-inventory div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class InventoryDialogPage {
    modalTitle = element(by.css('h4#myInventoryLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    quantityInput = element(by.css('input#field_quantity'));
    inventoryDateInput = element(by.css('input#field_inventoryDate'));
    locationSelect = element(by.css('select#field_location'));
    itemSelect = element(by.css('select#field_item'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setQuantityInput = function (quantity) {
        this.quantityInput.sendKeys(quantity);
    }

    getQuantityInput = function () {
        return this.quantityInput.getAttribute('value');
    }

    setInventoryDateInput = function (inventoryDate) {
        this.inventoryDateInput.sendKeys(inventoryDate);
    }

    getInventoryDateInput = function () {
        return this.inventoryDateInput.getAttribute('value');
    }

    locationSelectLastOption = function () {
        this.locationSelect.all(by.tagName('option')).last().click();
    }

    locationSelectOption = function (option) {
        this.locationSelect.sendKeys(option);
    }

    getLocationSelect = function () {
        return this.locationSelect;
    }

    getLocationSelectedOption = function () {
        return this.locationSelect.element(by.css('option:checked')).getText();
    }

    itemSelectLastOption = function () {
        this.itemSelect.all(by.tagName('option')).last().click();
    }

    itemSelectOption = function (option) {
        this.itemSelect.sendKeys(option);
    }

    getItemSelect = function () {
        return this.itemSelect;
    }

    getItemSelectedOption = function () {
        return this.itemSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
