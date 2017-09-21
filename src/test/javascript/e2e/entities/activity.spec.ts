import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Activity e2e test', () => {

    let navBarPage: NavBarPage;
    let activityDialogPage: ActivityDialogPage;
    let activityComponentsPage: ActivityComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Activities', () => {
        navBarPage.goToEntity('activity');
        activityComponentsPage = new ActivityComponentsPage();
        expect(activityComponentsPage.getTitle()).toMatch(/mystaffApp.activity.home.title/);

    });

    it('should load create Activity dialog', () => {
        activityComponentsPage.clickOnCreateButton();
        activityDialogPage = new ActivityDialogPage();
        expect(activityDialogPage.getModalTitle()).toMatch(/mystaffApp.activity.home.createOrEditLabel/);
        activityDialogPage.close();
    });

    it('should create and save Activities', () => {
        activityComponentsPage.clickOnCreateButton();
        activityDialogPage.setNameInput('name');
        expect(activityDialogPage.getNameInput()).toMatch('name');
        activityDialogPage.setLogoInput(absolutePath);
        activityDialogPage.setDescriptionInput('description');
        expect(activityDialogPage.getDescriptionInput()).toMatch('description');
        activityDialogPage.setPriceInput('5');
        expect(activityDialogPage.getPriceInput()).toMatch('5');
        activityDialogPage.setDurationMinutesInput('5');
        expect(activityDialogPage.getDurationMinutesInput()).toMatch('5');
        activityDialogPage.setPreDurationMinutesInput('5');
        expect(activityDialogPage.getPreDurationMinutesInput()).toMatch('5');
        activityDialogPage.setPostDurationMinutesInput('5');
        expect(activityDialogPage.getPostDurationMinutesInput()).toMatch('5');
        activityDialogPage.getIsNrivateInput().isSelected().then(function (selected) {
            if (selected) {
                activityDialogPage.getIsNrivateInput().click();
                expect(activityDialogPage.getIsNrivateInput().isSelected()).toBeFalsy();
            } else {
                activityDialogPage.getIsNrivateInput().click();
                expect(activityDialogPage.getIsNrivateInput().isSelected()).toBeTruthy();
            }
        });
        activityDialogPage.setColorCodeInput('colorCode');
        expect(activityDialogPage.getColorCodeInput()).toMatch('colorCode');
        activityDialogPage.setCancellationTimeInput('5');
        expect(activityDialogPage.getCancellationTimeInput()).toMatch('5');
        // activityDialogPage.locationSelectLastOption();
        // activityDialogPage.categorySelectLastOption();
        activityDialogPage.save();
        expect(activityDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ActivityComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-activity div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ActivityDialogPage {
    modalTitle = element(by.css('h4#myActivityLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    logoInput = element(by.css('input#file_logo'));
    descriptionInput = element(by.css('textarea#field_description'));
    priceInput = element(by.css('input#field_price'));
    durationMinutesInput = element(by.css('input#field_durationMinutes'));
    preDurationMinutesInput = element(by.css('input#field_preDurationMinutes'));
    postDurationMinutesInput = element(by.css('input#field_postDurationMinutes'));
    isNrivateInput = element(by.css('input#field_isNrivate'));
    colorCodeInput = element(by.css('input#field_colorCode'));
    cancellationTimeInput = element(by.css('input#field_cancellationTime'));
    locationSelect = element(by.css('select#field_location'));
    categorySelect = element(by.css('select#field_category'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setLogoInput = function (logo) {
        this.logoInput.sendKeys(logo);
    }

    getLogoInput = function () {
        return this.logoInput.getAttribute('value');
    }

    setDescriptionInput = function (description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function () {
        return this.descriptionInput.getAttribute('value');
    }

    setPriceInput = function (price) {
        this.priceInput.sendKeys(price);
    }

    getPriceInput = function () {
        return this.priceInput.getAttribute('value');
    }

    setDurationMinutesInput = function (durationMinutes) {
        this.durationMinutesInput.sendKeys(durationMinutes);
    }

    getDurationMinutesInput = function () {
        return this.durationMinutesInput.getAttribute('value');
    }

    setPreDurationMinutesInput = function (preDurationMinutes) {
        this.preDurationMinutesInput.sendKeys(preDurationMinutes);
    }

    getPreDurationMinutesInput = function () {
        return this.preDurationMinutesInput.getAttribute('value');
    }

    setPostDurationMinutesInput = function (postDurationMinutes) {
        this.postDurationMinutesInput.sendKeys(postDurationMinutes);
    }

    getPostDurationMinutesInput = function () {
        return this.postDurationMinutesInput.getAttribute('value');
    }

    getIsNrivateInput = function () {
        return this.isNrivateInput;
    }
    setColorCodeInput = function (colorCode) {
        this.colorCodeInput.sendKeys(colorCode);
    }

    getColorCodeInput = function () {
        return this.colorCodeInput.getAttribute('value');
    }

    setCancellationTimeInput = function (cancellationTime) {
        this.cancellationTimeInput.sendKeys(cancellationTime);
    }

    getCancellationTimeInput = function () {
        return this.cancellationTimeInput.getAttribute('value');
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

    categorySelectLastOption = function () {
        this.categorySelect.all(by.tagName('option')).last().click();
    }

    categorySelectOption = function (option) {
        this.categorySelect.sendKeys(option);
    }

    getCategorySelect = function () {
        return this.categorySelect;
    }

    getCategorySelectedOption = function () {
        return this.categorySelect.element(by.css('option:checked')).getText();
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
