import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export const languageMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const baseLang = control.get('baseLang')?.value;
  const targetLang = control.get('targetLang')?.value;

  return baseLang && targetLang && baseLang === targetLang ? { languageMatch: true } : null;
};
