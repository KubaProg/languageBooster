import { Component, forwardRef, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';

export interface Language {
  code: string;
  name: string;
}

@Component({
  selector: 'app-language-selector',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './language-selector.component.html',
  styleUrls: ['./language-selector.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => LanguageSelectorComponent),
      multi: true
    }
  ]
})
export class LanguageSelectorComponent implements ControlValueAccessor {
  public supportedLanguages = signal<Language[]>([
    { code: 'pl', name: 'Polish' },
    { code: 'en', name: 'English' },
    { code: 'es', name: 'Spanish' },
    { code: 'de', name: 'German' },
  ]);

  public languageForm = new FormGroup({
    baseLang: new FormControl(''),
    targetLang: new FormControl('')
  });

  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  constructor() {
    this.languageForm.valueChanges.subscribe(value => {
      this.onChange(value);
      this.onTouched();
    });
  }

  public writeValue(value: any): void {
    if (value) {
      this.languageForm.setValue(value, { emitEvent: false });
    }
  }

  public registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  public registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  public setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.languageForm.disable() : this.languageForm.enable();
  }
}