import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Customer e2e test', () => {

    let navBarPage: NavBarPage;
    let customerDialogPage: CustomerDialogPage;
    let customerComponentsPage: CustomerComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Customers', () => {
        navBarPage.goToEntity('customer');
        customerComponentsPage = new CustomerComponentsPage();
        expect(customerComponentsPage.getTitle()).toMatch(/mystaffApp.customer.home.title/);

    });

    it('should load create Customer dialog', () => {
        customerComponentsPage.clickOnCreateButton();
        customerDialogPage = new CustomerDialogPage();
        expect(customerDialogPage.getModalTitle()).toMatch(/mystaffApp.customer.home.createOrEditLabel/);
        customerDialogPage.close();
    });

   /* it('should create and save Customers', () => {
        customerComponentsPage.clickOnCreateButton();
        customerDialogPage.setFirstNameInput('firstName');
        expect(customerDialogPage.getFirstNameInput()).toMatch('firstName');
        customerDialogPage.setMidleNameInput('midleName');
        expect(customerDialogPage.getMidleNameInput()).toMatch('midleName');
        customerDialogPage.setLastNameInput('lastName');
        expect(customerDialogPage.getLastNameInput()).toMatch('lastName');
        customerDialogPage.setLoginInput('login');
        expect(customerDialogPage.getLoginInput()).toMatch('login');
        customerDialogPage.setPasswordHashInput('passwordHash');
        expect(customerDialogPage.getPasswordHashInput()).toMatch('passwordHash');
        customerDialogPage.setPhoneInput('phone');
        expect(customerDialogPage.getPhoneInput()).toMatch('phone');
        customerDialogPage.setEmailInput('email');
        expect(customerDialogPage.getEmailInput()).toMatch('email');
        customerDialogPage.setImageUrlInput('imageUrl');
        expect(customerDialogPage.getImageUrlInput()).toMatch('imageUrl');
        customerDialogPage.getActivatedInput().isSelected().then(function (selected) {
            if (selected) {
                customerDialogPage.getActivatedInput().click();
                expect(customerDialogPage.getActivatedInput().isSelected()).toBeFalsy();
            } else {
                customerDialogPage.getActivatedInput().click();
                expect(customerDialogPage.getActivatedInput().isSelected()).toBeTruthy();
            }
        });
        customerDialogPage.setLangKeyInput('langKey');
        expect(customerDialogPage.getLangKeyInput()).toMatch('langKey');
        customerDialogPage.setActivationKeyInput('activationKey');
        expect(customerDialogPage.getActivationKeyInput()).toMatch('activationKey');
        customerDialogPage.setResetKeyInput('resetKey');
        expect(customerDialogPage.getResetKeyInput()).toMatch('resetKey');
        customerDialogPage.setCreatedDateInput(12310020012301);
        expect(customerDialogPage.getCreatedDateInput()).toMatch('2001-12-31T02:30');
        customerDialogPage.setResetDateInput(12310020012301);
        expect(customerDialogPage.getResetDateInput()).toMatch('2001-12-31T02:30');
        customerDialogPage.setLastModifiedByInput('lastModifiedBy');
        expect(customerDialogPage.getLastModifiedByInput()).toMatch('lastModifiedBy');
        customerDialogPage.setLastModifiedDateInput(12310020012301);
        expect(customerDialogPage.getLastModifiedDateInput()).toMatch('2001-12-31T02:30');
        // customerDialogPage.companySelectLastOption();
        customerDialogPage.save();
        expect(customerDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CustomerComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-customer div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CustomerDialogPage {
    modalTitle = element(by.css('h4#myCustomerLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    firstNameInput = element(by.css('input#field_firstName'));
    midleNameInput = element(by.css('input#field_midleName'));
    lastNameInput = element(by.css('input#field_lastName'));
    loginInput = element(by.css('input#field_login'));
    passwordHashInput = element(by.css('input#field_passwordHash'));
    phoneInput = element(by.css('input#field_phone'));
    emailInput = element(by.css('input#field_email'));
    imageUrlInput = element(by.css('input#field_imageUrl'));
    activatedInput = element(by.css('input#field_activated'));
    langKeyInput = element(by.css('input#field_langKey'));
    activationKeyInput = element(by.css('input#field_activationKey'));
    resetKeyInput = element(by.css('input#field_resetKey'));
    createdDateInput = element(by.css('input#field_createdDate'));
    resetDateInput = element(by.css('input#field_resetDate'));
    lastModifiedByInput = element(by.css('input#field_lastModifiedBy'));
    lastModifiedDateInput = element(by.css('input#field_lastModifiedDate'));
    companySelect = element(by.css('select#field_company'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFirstNameInput = function (firstName) {
        this.firstNameInput.sendKeys(firstName);
    }

    getFirstNameInput = function () {
        return this.firstNameInput.getAttribute('value');
    }

    setMidleNameInput = function (midleName) {
        this.midleNameInput.sendKeys(midleName);
    }

    getMidleNameInput = function () {
        return this.midleNameInput.getAttribute('value');
    }

    setLastNameInput = function (lastName) {
        this.lastNameInput.sendKeys(lastName);
    }

    getLastNameInput = function () {
        return this.lastNameInput.getAttribute('value');
    }

    setLoginInput = function (login) {
        this.loginInput.sendKeys(login);
    }

    getLoginInput = function () {
        return this.loginInput.getAttribute('value');
    }

    setPasswordHashInput = function (passwordHash) {
        this.passwordHashInput.sendKeys(passwordHash);
    }

    getPasswordHashInput = function () {
        return this.passwordHashInput.getAttribute('value');
    }

    setPhoneInput = function (phone) {
        this.phoneInput.sendKeys(phone);
    }

    getPhoneInput = function () {
        return this.phoneInput.getAttribute('value');
    }

    setEmailInput = function (email) {
        this.emailInput.sendKeys(email);
    }

    getEmailInput = function () {
        return this.emailInput.getAttribute('value');
    }

    setImageUrlInput = function (imageUrl) {
        this.imageUrlInput.sendKeys(imageUrl);
    }

    getImageUrlInput = function () {
        return this.imageUrlInput.getAttribute('value');
    }

    getActivatedInput = function () {
        return this.activatedInput;
    }
    setLangKeyInput = function (langKey) {
        this.langKeyInput.sendKeys(langKey);
    }

    getLangKeyInput = function () {
        return this.langKeyInput.getAttribute('value');
    }

    setActivationKeyInput = function (activationKey) {
        this.activationKeyInput.sendKeys(activationKey);
    }

    getActivationKeyInput = function () {
        return this.activationKeyInput.getAttribute('value');
    }

    setResetKeyInput = function (resetKey) {
        this.resetKeyInput.sendKeys(resetKey);
    }

    getResetKeyInput = function () {
        return this.resetKeyInput.getAttribute('value');
    }

    setCreatedDateInput = function (createdDate) {
        this.createdDateInput.sendKeys(createdDate);
    }

    getCreatedDateInput = function () {
        return this.createdDateInput.getAttribute('value');
    }

    setResetDateInput = function (resetDate) {
        this.resetDateInput.sendKeys(resetDate);
    }

    getResetDateInput = function () {
        return this.resetDateInput.getAttribute('value');
    }

    setLastModifiedByInput = function (lastModifiedBy) {
        this.lastModifiedByInput.sendKeys(lastModifiedBy);
    }

    getLastModifiedByInput = function () {
        return this.lastModifiedByInput.getAttribute('value');
    }

    setLastModifiedDateInput = function (lastModifiedDate) {
        this.lastModifiedDateInput.sendKeys(lastModifiedDate);
    }

    getLastModifiedDateInput = function () {
        return this.lastModifiedDateInput.getAttribute('value');
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
