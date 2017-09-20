import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'findLanguageFromKey'})
export class FindLanguageFromKeyPipe implements PipeTransform {
    private languages: any = {
        'ar-ly': { name: 'العربية', rtl: true },
        'ca': { name: 'Català' },
        'da': { name: 'Dansk' },
        'nl': { name: 'Nederlands' },
        'en': { name: 'English' },
        'et': { name: 'Eesti' },
        'fr': { name: 'Français' },
        'gl': { name: 'Galego' },
        'de': { name: 'Deutsch' },
        'el': { name: 'Ελληνικά' },
        'hi': { name: 'हिंदी' },
        'hu': { name: 'Magyar' },
        'it': { name: 'Italiano' },
        'pl': { name: 'Polski' },
        'pt-br': { name: 'Português (Brasil)' },
        'pt-pt': { name: 'Português' },
        'ro': { name: 'Română' },
        'ru': { name: 'Русский' },
        'sk': { name: 'Slovenský' },
        'sr': { name: 'Srpski' },
        'es': { name: 'Español' },
        'sv': { name: 'Svenska' },
        'tr': { name: 'Türkçe' },
        'ua': { name: 'Українська' }
        // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
    };
    transform(lang: string): string {
        return this.languages[lang].name;
    }
    isRTL(lang: string): boolean {
        return this.languages[lang].rtl;
    }
}
