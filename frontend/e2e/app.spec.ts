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
  await expect(page.getByRole('heading', { name: 'Create New Collection' })).toBeVisible();
});

// Run tests in this file serially to avoid parallel execution issues with shared state.
test.describe.serial('Collection and Card Management', () => {
  test.beforeEach(async ({ page }) => {
    // Arrange: Log in before each test
    await page.goto('/auth');
    await page.locator('input[formControlName="email"]').fill(environment.e2eUsername);
    await page.locator('input[formControlName="password"]').fill(environment.e2ePassword);
    await page.locator('button[type="submit"]').click();

    // After login, we are redirected to /app which redirects to /app/collections/new
    // Wait for the generation form to be ready
    await expect(page.getByRole('heading', { name: 'Create New Collection' })).toBeVisible();
  });

  test('should create a new collection from text and see it in the list', async ({ page }) => {
    const collectionName = `My E2E Test Collection ${Date.now()}`;

    // Act: Create a new collection. We are already on the generation form page.
    await page.getByLabel('Collection Name').fill(collectionName);
    await page.getByLabel('Paste your text here').fill('This is a test sentence for my new collection.');
    await page.getByLabel('Base Language').selectOption({ label: 'English' });
    await page.getByLabel('Target Language').selectOption({ label: 'Polish' });
    await page.getByLabel('Number of Cards to Generate').fill('1');
    await page.getByRole('button', { name: 'Generate' }).click();

    // Assert: Verify the new collection is in the list
    await expect(page).toHaveURL(/.*\/app\/collections/);

    // Wait for the list to be displayed and check for the new collection
    await expect(page.getByRole('heading', { name: 'My Collections' })).toBeVisible();
    await expect(page.getByText(collectionName)).toBeVisible();
  });

});
