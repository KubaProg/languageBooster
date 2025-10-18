import { test, expect } from '@playwright/test';
import { environment } from '../src/environments/environment.staging';

test('should have LanguageBooster as title', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle("Frontend");
});

test('should log in and redirect to the app', async ({ page }) => {
  // Navigate to the auth page
  await page.goto('/auth');

  // Fill in the credentials
  await page.locator('input[formControlName="email"]').fill(environment.e2eUsername);
  await page.locator('input[formControlName="password"]').fill(environment.e2ePassword);

  // Click the login button
  await page.locator('button[type="submit"]').click();

  // Wait for navigation and assert the new URL
  await expect(page).toHaveURL(/.*\/app/);
});