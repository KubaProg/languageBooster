import { test, expect } from '@playwright/test';

test('should have LanguageBooster as title', async ({ page }) => {
  await page.goto('/');
  await expect(page).toHaveTitle("Frontend");
});
