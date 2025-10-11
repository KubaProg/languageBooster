import { Component, forwardRef, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { NgClass } from '@angular/common';

export type SourceType = 'text' | 'file';

@Component({
  selector: 'app-source-input',
  standalone: true,
  imports: [NgClass],
  templateUrl: './source-input.component.html',
  styleUrls: ['./source-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SourceInputComponent),
      multi: true
    }
  ]
})
export class SourceInputComponent implements ControlValueAccessor {
  public activeTab = signal<SourceType>('text');
  public fileName = signal<string | null>(null);
  public value: { type: SourceType, value: string | File } | null = null;

  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  public writeValue(obj: any): void {
    // This can be used to set the initial value if needed
  }

  public registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  public registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  public setDisabledState?(isDisabled: boolean): void {
    // TODO: Implement disabled state for inputs
  }

  public selectTab(tab: SourceType): void {
    this.activeTab.set(tab);
    this.fileName.set(null);
    this.updateValue(null);
  }

  public onTextChange(event: Event): void {
    const value = (event.target as HTMLTextAreaElement).value;
    this.updateValue({ type: 'text', value });
  }

  public onFileChange(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.fileName.set(file.name);
      this.updateValue({ type: 'file', value: file });
    } else {
      this.fileName.set(null);
      this.updateValue(null);
    }
  }

  private updateValue(value: { type: SourceType, value: string | File } | null): void {
    this.value = value;
    this.onChange(this.value);
    this.onTouched();
  }
}