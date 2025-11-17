import { test, expect } from '@playwright/test';

test('should log in, navigate to a collection, and study cards', async ({ page }) => {
  await page.goto('http://localhost:4200/#/auth');

  // Login
  await expect(page.getByRole('heading', { name: 'Login' })).toBeVisible();
  await page.getByRole('textbox', { name: 'Email' }).fill('cf.jp@interia.pl');
  await page.getByRole('textbox', { name: 'Password' }).fill('dupa123');
  await page.getByRole('button', { name: 'Login' }).click();

  // After login, user is redirected. Assert the URL has changed.
  await expect(page).toHaveURL(/.*\/app/);

  // The app redirects to the 'Create New Collection' page by default.
  await expect(page.getByRole('heading', { name: 'Create New Collection' })).toBeVisible();

  // Navigate to the collections list.
  await page.getByRole('link', { name: 'My Collections' }).click();
  await expect(page.getByRole('heading', { name: 'My Collections' })).toBeVisible();

  // Click on a test collection. Using a partial match to avoid test failures on dynamic names.
  const collectionLink = page.getByRole('link', { name: /My E2E Test Collection/ }).first();
  await expect(collectionLink).toBeVisible();

  // Get the number of cards to review from the study page, which is more reliable
  await collectionLink.click();

  // We should be on the study page now.
  await expect(page).toHaveURL(/.*\/study/);
  const cardsRemainingElement = page.getByTestId('cards-remaining');
  await expect(cardsRemainingElement).toBeVisible();

  const cardsRemainingText = await cardsRemainingElement.textContent();
  const initialCardCount = cardsRemainingText ? parseInt(cardsRemainingText.split(' ')[0], 10) : 0;

  if (initialCardCount > 0) {
    // Click the 'Umiem' button to mark one card as known
    await page.getByRole('button', { name: 'Umiem', exact: true }).click();

    // Assert that card count has decreased or the study is finished
    if (initialCardCount > 1) {
      const newCardsRemainingElement = page.getByTestId('cards-remaining');
      await expect(newCardsRemainingElement).toHaveText(`${initialCardCount - 1} cards remaining`);
    } else {
      await expect(page.getByText('This collection has no cards to review.')).toBeVisible();
    }
  } else {
    await expect(page.getByText('This collection has no cards to review.')).toBeVisible();
  }
});