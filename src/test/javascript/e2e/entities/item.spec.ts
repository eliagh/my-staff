import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Item e2e test', () => {

    let navBarPage: NavBarPage;
    let itemDialogPage: ItemDialogPage;
    let itemComponentsPage: ItemComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Items', () => {
        navBarPage.goToEntity('item');
        itemComponentsPage = new ItemComponentsPage();
        expect(itemComponentsPage.getTitle()).toMatch(/mystaffApp.item.home.title/);

    });

    it('should load create Item dialog', () => {
        itemComponentsPage.clickOnCreateButton();
        itemDialogPage = new ItemDialogPage();
        expect(itemDialogPage.getModalTitle()).toMatch(/mystaffApp.item.home.createOrEditLabel/);
        itemDialogPage.close();
    });

   /* it('should create and save Items', () => {
        itemComponentsPage.clickOnCreateButton();
        itemDialogPage.setNameInput('name');
        expect(itemDialogPage.getNameInput()).toMatch('name');
        itemDialogPage.setPictureUrlInput('pictureUrl');
        expect(itemDialogPage.getPictureUrlInput()).toMatch('pictureUrl');
        itemDialogPage.setPricePerUnitInput('5');
        expect(itemDialogPage.getPricePerUnitInput()).toMatch('5');
        itemDialogPage.setUnitInput('unit');
        expect(itemDialogPage.getUnitInput()).toMatch('unit');
        itemDialogPage.setCodeInput('code');
        expect(itemDialogPage.getCodeInput()).toMatch('code');
        itemDialogPage.setDescriptionInput('description');
        expect(itemDialogPage.getDescriptionInput()).toMatch('description');
        itemDialogPage.getShowInShopInput().isSelected().then(function (selected) {
            if (selected) {
                itemDialogPage.getShowInShopInput().click();
                expect(itemDialogPage.getShowInShopInput().isSelected()).toBeFalsy();
            } else {
                itemDialogPage.getShowInShopInput().click();
                expect(itemDialogPage.getShowInShopInput().isSelected()).toBeTruthy();
            }
        });
        itemDialogPage.companySelectLastOption();
        itemDialogPage.save();
        expect(itemDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ItemComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-item div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ItemDialogPage {
    modalTitle = element(by.css('h4#myItemLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    pictureUrlInput = element(by.css('input#field_pictureUrl'));
    pricePerUnitInput = element(by.css('input#field_pricePerUnit'));
    unitInput = element(by.css('input#field_unit'));
    codeInput = element(by.css('textarea#field_code'));
    descriptionInput = element(by.css('textarea#field_description'));
    showInShopInput = element(by.css('input#field_showInShop'));
    companySelect = element(by.css('select#field_company'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setPictureUrlInput = function (pictureUrl) {
        this.pictureUrlInput.sendKeys(pictureUrl);
    }

    getPictureUrlInput = function () {
        return this.pictureUrlInput.getAttribute('value');
    }

    setPricePerUnitInput = function (pricePerUnit) {
        this.pricePerUnitInput.sendKeys(pricePerUnit);
    }

    getPricePerUnitInput = function () {
        return this.pricePerUnitInput.getAttribute('value');
    }

    setUnitInput = function (unit) {
        this.unitInput.sendKeys(unit);
    }

    getUnitInput = function () {
        return this.unitInput.getAttribute('value');
    }

    setCodeInput = function (code) {
        this.codeInput.sendKeys(code);
    }

    getCodeInput = function () {
        return this.codeInput.getAttribute('value');
    }

    setDescriptionInput = function (description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function () {
        return this.descriptionInput.getAttribute('value');
    }

    getShowInShopInput = function () {
        return this.showInShopInput;
    }
    companySelectLastOption = function () {
        this.companySelect.all(by.tagName('option')).last().click();
    }

    companySelectOption = function (option) {
        this.companySelect.sendKeys(option);
    }

    getCompanySelect = function () {
        return this.companySelect;
    }

    getCompanySelectedOption = function () {
        return this.companySelect.element(by.css('option:checked')).getText();
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
