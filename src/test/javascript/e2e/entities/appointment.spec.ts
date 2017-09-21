import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Appointment e2e test', () => {

    let navBarPage: NavBarPage;
    let appointmentDialogPage: AppointmentDialogPage;
    let appointmentComponentsPage: AppointmentComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Appointments', () => {
        navBarPage.goToEntity('appointment');
        appointmentComponentsPage = new AppointmentComponentsPage();
        expect(appointmentComponentsPage.getTitle()).toMatch(/mystaffApp.appointment.home.title/);

    });

    it('should load create Appointment dialog', () => {
        appointmentComponentsPage.clickOnCreateButton();
        appointmentDialogPage = new AppointmentDialogPage();
        expect(appointmentDialogPage.getModalTitle()).toMatch(/mystaffApp.appointment.home.createOrEditLabel/);
        appointmentDialogPage.close();
    });

   /* it('should create and save Appointments', () => {
        appointmentComponentsPage.clickOnCreateButton();
        appointmentDialogPage.setWhenInput(12310020012301);
        expect(appointmentDialogPage.getWhenInput()).toMatch('2001-12-31T02:30');
        appointmentDialogPage.setLabelInput('label');
        expect(appointmentDialogPage.getLabelInput()).toMatch('label');
        appointmentDialogPage.getIsRecurringInput().isSelected().then(function (selected) {
            if (selected) {
                appointmentDialogPage.getIsRecurringInput().click();
                expect(appointmentDialogPage.getIsRecurringInput().isSelected()).toBeFalsy();
            } else {
                appointmentDialogPage.getIsRecurringInput().click();
                expect(appointmentDialogPage.getIsRecurringInput().isSelected()).toBeTruthy();
            }
        });
        appointmentDialogPage.getIsFlexibleInput().isSelected().then(function (selected) {
            if (selected) {
                appointmentDialogPage.getIsFlexibleInput().click();
                expect(appointmentDialogPage.getIsFlexibleInput().isSelected()).toBeFalsy();
            } else {
                appointmentDialogPage.getIsFlexibleInput().click();
                expect(appointmentDialogPage.getIsFlexibleInput().isSelected()).toBeTruthy();
            }
        });
        appointmentDialogPage.setNotesInput('notes');
        expect(appointmentDialogPage.getNotesInput()).toMatch('notes');
        appointmentDialogPage.setReminderInput('reminder');
        expect(appointmentDialogPage.getReminderInput()).toMatch('reminder');
        appointmentDialogPage.customerSelectLastOption();
        appointmentDialogPage.activityBookedSelectLastOption();
        appointmentDialogPage.locationSelectLastOption();
        appointmentDialogPage.providerSelectLastOption();
        appointmentDialogPage.save();
        expect(appointmentDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AppointmentComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-appointment div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AppointmentDialogPage {
    modalTitle = element(by.css('h4#myAppointmentLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    whenInput = element(by.css('input#field_when'));
    labelInput = element(by.css('input#field_label'));
    isRecurringInput = element(by.css('input#field_isRecurring'));
    isFlexibleInput = element(by.css('input#field_isFlexible'));
    notesInput = element(by.css('textarea#field_notes'));
    reminderInput = element(by.css('input#field_reminder'));
    customerSelect = element(by.css('select#field_customer'));
    activityBookedSelect = element(by.css('select#field_activityBooked'));
    locationSelect = element(by.css('select#field_location'));
    providerSelect = element(by.css('select#field_provider'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setWhenInput = function (when) {
        this.whenInput.sendKeys(when);
    }

    getWhenInput = function () {
        return this.whenInput.getAttribute('value');
    }

    setLabelInput = function (label) {
        this.labelInput.sendKeys(label);
    }

    getLabelInput = function () {
        return this.labelInput.getAttribute('value');
    }

    getIsRecurringInput = function () {
        return this.isRecurringInput;
    }
    getIsFlexibleInput = function () {
        return this.isFlexibleInput;
    }
    setNotesInput = function (notes) {
        this.notesInput.sendKeys(notes);
    }

    getNotesInput = function () {
        return this.notesInput.getAttribute('value');
    }

    setReminderInput = function (reminder) {
        this.reminderInput.sendKeys(reminder);
    }

    getReminderInput = function () {
        return this.reminderInput.getAttribute('value');
    }

    customerSelectLastOption = function () {
        this.customerSelect.all(by.tagName('option')).last().click();
    }

    customerSelectOption = function (option) {
        this.customerSelect.sendKeys(option);
    }

    getCustomerSelect = function () {
        return this.customerSelect;
    }

    getCustomerSelectedOption = function () {
        return this.customerSelect.element(by.css('option:checked')).getText();
    }

    activityBookedSelectLastOption = function () {
        this.activityBookedSelect.all(by.tagName('option')).last().click();
    }

    activityBookedSelectOption = function (option) {
        this.activityBookedSelect.sendKeys(option);
    }

    getActivityBookedSelect = function () {
        return this.activityBookedSelect;
    }

    getActivityBookedSelectedOption = function () {
        return this.activityBookedSelect.element(by.css('option:checked')).getText();
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

    providerSelectLastOption = function () {
        this.providerSelect.all(by.tagName('option')).last().click();
    }

    providerSelectOption = function (option) {
        this.providerSelect.sendKeys(option);
    }

    getProviderSelect = function () {
        return this.providerSelect;
    }

    getProviderSelectedOption = function () {
        return this.providerSelect.element(by.css('option:checked')).getText();
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
