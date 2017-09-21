import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Location e2e test', () => {

    let navBarPage: NavBarPage;
    let locationDialogPage: LocationDialogPage;
    let locationComponentsPage: LocationComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Locations', () => {
        navBarPage.goToEntity('location');
        locationComponentsPage = new LocationComponentsPage();
        expect(locationComponentsPage.getTitle()).toMatch(/mystaffApp.location.home.title/);

    });

    it('should load create Location dialog', () => {
        locationComponentsPage.clickOnCreateButton();
        locationDialogPage = new LocationDialogPage();
        expect(locationDialogPage.getModalTitle()).toMatch(/mystaffApp.location.home.createOrEditLabel/);
        locationDialogPage.close();
    });

   /* it('should create and save Locations', () => {
        locationComponentsPage.clickOnCreateButton();
        locationDialogPage.setNameInput('name');
        expect(locationDialogPage.getNameInput()).toMatch('name');
        locationDialogPage.setAddress1Input('address1');
        expect(locationDialogPage.getAddress1Input()).toMatch('address1');
        locationDialogPage.setAddress2Input('address2');
        expect(locationDialogPage.getAddress2Input()).toMatch('address2');
        locationDialogPage.setAddress3Input('address3');
        expect(locationDialogPage.getAddress3Input()).toMatch('address3');
        locationDialogPage.setNumberInput('5');
        expect(locationDialogPage.getNumberInput()).toMatch('5');
        locationDialogPage.setPostcodeInput('postcode');
        expect(locationDialogPage.getPostcodeInput()).toMatch('postcode');
        locationDialogPage.setCityInput('city');
        expect(locationDialogPage.getCityInput()).toMatch('city');
        locationDialogPage.setStateProvinceInput('stateProvince');
        expect(locationDialogPage.getStateProvinceInput()).toMatch('stateProvince');
        locationDialogPage.companySelectLastOption();
        locationDialogPage.save();
        expect(locationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class LocationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-location div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class LocationDialogPage {
    modalTitle = element(by.css('h4#myLocationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    address1Input = element(by.css('input#field_address1'));
    address2Input = element(by.css('input#field_address2'));
    address3Input = element(by.css('input#field_address3'));
    numberInput = element(by.css('input#field_number'));
    postcodeInput = element(by.css('input#field_postcode'));
    cityInput = element(by.css('input#field_city'));
    stateProvinceInput = element(by.css('input#field_stateProvince'));
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

    setAddress1Input = function (address1) {
        this.address1Input.sendKeys(address1);
    }

    getAddress1Input = function () {
        return this.address1Input.getAttribute('value');
    }

    setAddress2Input = function (address2) {
        this.address2Input.sendKeys(address2);
    }

    getAddress2Input = function () {
        return this.address2Input.getAttribute('value');
    }

    setAddress3Input = function (address3) {
        this.address3Input.sendKeys(address3);
    }

    getAddress3Input = function () {
        return this.address3Input.getAttribute('value');
    }

    setNumberInput = function (number) {
        this.numberInput.sendKeys(number);
    }

    getNumberInput = function () {
        return this.numberInput.getAttribute('value');
    }

    setPostcodeInput = function (postcode) {
        this.postcodeInput.sendKeys(postcode);
    }

    getPostcodeInput = function () {
        return this.postcodeInput.getAttribute('value');
    }

    setCityInput = function (city) {
        this.cityInput.sendKeys(city);
    }

    getCityInput = function () {
        return this.cityInput.getAttribute('value');
    }

    setStateProvinceInput = function (stateProvince) {
        this.stateProvinceInput.sendKeys(stateProvince);
    }

    getStateProvinceInput = function () {
        return this.stateProvinceInput.getAttribute('value');
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
