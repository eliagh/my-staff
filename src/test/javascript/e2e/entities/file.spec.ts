import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('File e2e test', () => {

    let navBarPage: NavBarPage;
    let fileDialogPage: FileDialogPage;
    let fileComponentsPage: FileComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Files', () => {
        navBarPage.goToEntity('file');
        fileComponentsPage = new FileComponentsPage();
        expect(fileComponentsPage.getTitle()).toMatch(/mystaffApp.file.home.title/);

    });

    it('should load create File dialog', () => {
        fileComponentsPage.clickOnCreateButton();
        fileDialogPage = new FileDialogPage();
        expect(fileDialogPage.getModalTitle()).toMatch(/mystaffApp.file.home.createOrEditLabel/);
        fileDialogPage.close();
    });

   /* it('should create and save Files', () => {
        fileComponentsPage.clickOnCreateButton();
        fileDialogPage.setNameInput('name');
        expect(fileDialogPage.getNameInput()).toMatch('name');
        fileDialogPage.setUrlInput('url');
        expect(fileDialogPage.getUrlInput()).toMatch('url');
        fileDialogPage.fileTypeSelectLastOption();
        fileDialogPage.setFileInput(absolutePath);
        fileDialogPage.companySelectLastOption();
        fileDialogPage.save();
        expect(fileDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class FileComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-file div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class FileDialogPage {
    modalTitle = element(by.css('h4#myFileLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    urlInput = element(by.css('input#field_url'));
    fileTypeSelect = element(by.css('select#field_fileType'));
    fileInput = element(by.css('input#file_file'));
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

    setUrlInput = function (url) {
        this.urlInput.sendKeys(url);
    }

    getUrlInput = function () {
        return this.urlInput.getAttribute('value');
    }

    setFileTypeSelect = function (fileType) {
        this.fileTypeSelect.sendKeys(fileType);
    }

    getFileTypeSelect = function () {
        return this.fileTypeSelect.element(by.css('option:checked')).getText();
    }

    fileTypeSelectLastOption = function () {
        this.fileTypeSelect.all(by.tagName('option')).last().click();
    }
    setFileInput = function (file) {
        this.fileInput.sendKeys(file);
    }

    getFileInput = function () {
        return this.fileInput.getAttribute('value');
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
