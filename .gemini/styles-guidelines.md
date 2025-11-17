# Card UI Style Guidelines for Flashcard App
*(Based on “Card UI design: fundamentals and examples” from Justinmind)* :contentReference[oaicite:0]{index=0}

## 1. Goals & Design Philosophy

**Goal:** Create a modern, minimal card-based UI for a Duolingo-like flashcard app with:

- Clear, scannable content
- Strong focus on the learning unit (flashcard / deck)
- No decorative images — only essential UI, text, icons, and subtle styling
- Easy responsiveness (desktop → tablet → mobile)

**Why cards?**

- Cards present content in small, digestible chunks, helping users scan and understand quickly. :contentReference[oaicite:1]{index=1}
- Card UIs are now a staple in modern web and app design because they’re intuitive, minimalist, and responsive. :contentReference[oaicite:2]{index=2}

For our app, **each card = one focused learning object**:
- One deck
- One flashcard
- One progress summary

---

## 2. Card UX Principles

Translate Justinmind’s principles directly into implementation rules for the dev. :contentReference[oaicite:3]{index=3}

### 2.1 One Focus per Card

- Each card should encapsulate **one idea / entity**:
    - Deck card: deck name, language, small meta, primary action (Start / Continue).
    - Flashcard card: front = term/sentence, back = translation/explanation.
    - Progress card: high-level stats (streak, mastered cards, etc.).
- Avoid mixing multiple concepts in one card (e.g., no multiple decks per card).

**Dev rule:**  
Design card components so they accept **one main object** (Deck, Card, Progress) and never display lists inside a single card.

---

### 2.2 Clear Information Hierarchy

Justinmind emphasizes putting the most important info first: titles / main message → supporting details. :contentReference[oaicite:4]{index=4}

For our app:

**Deck card hierarchy:**

1. **Primary:** Deck title (e.g., “Spanish A1 – Basics”)
2. **Secondary:** Short description or main category (e.g., “Food & Drinks”)
3. **Tertiary meta:** number of cards, last studied, difficulty tag, language flag/icon
4. **CTA:** “Continue” / “Start” button

**Flashcard (in-session) hierarchy:**

1. **Primary:** Main term or sentence (large font, centered)
2. **Secondary:** Hint / example sentence / part of speech
3. **Actions:** Flip, “I know this”, “I don’t know this”, progress indicator

**Dev rule:**  
For each card component, define **slots or sections**:
- `header` (title)
- `body` (core learning content)
- `meta` (small labels/secondary info)
- `actions` (buttons)

And enforce consistent ordering in templates.

---

### 2.3 Grid System & Layout

Justinmind recommends using a grid system for consistency in spacing, margins, and alignment. :contentReference[oaicite:5]{index=5}

**Deck list screen (grid):**

- Desktop: 3–4 deck cards per row
- Tablet: 2 per row
- Mobile: 1 per row
- Consistent **card width** and **gaps** between cards (e.g., `gap: 1.5rem`)

**Flashcard in-session:**

- A **single large card** centered horizontally, with max width (e.g., 480–600px).
- Vertical spacing around the card (top and bottom margins) to emphasize focus.

**Dev rule:**  
Implement a **layout container**:

- Use CSS Grid or Flexbox for deck listing:
    - `grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));`
- Ensure consistent padding inside cards and spacing between them.

---

### 2.4 Spacing & Alignment

Justinmind stresses giving cards space to “breathe” and keeping alignment clean. :contentReference[oaicite:6]{index=6}

**Inside cards:**

- Horizontal padding: 16–24px
- Vertical padding: 16–24px
- Vertical rhythm:
    - Title → 8–12px → subtitle
    - Subtitle → 12–16px → meta / actions

**Between cards:**

- Use consistent `gap` in the grid, e.g., `1.5rem` or `2rem`.
- Align content to a baseline: left-aligned text, consistent max line lengths.

**Dev rule:**  
Define spacing tokens in CSS (or SCSS / Tailwind config):

- `--space-xs: 4px;`
- `--space-sm: 8px;`
- `--space-md: 12px;`
- `--space-lg: 16px;`
- `--space-xl: 24px;`

And use them only (no arbitrary spacing).

---

### 2.5 Interactivity & Clickability

Justinmind suggests making interactive cards “feel” interactive via hover effects and using the full card as a link where appropriate. :contentReference[oaicite:7]{index=7}

**Deck cards:**

- The **entire card** is clickable (navigates to deck / session).
- Hover (desktop):
    - Slight shadow increase
    - Subtle scale (e.g., `scale(1.01)`)
    - Cursor: pointer
- Active / pressed:
    - Slightly darker background or smaller scale back to `1.0`
- Focus (keyboard):
    - Clear focus outline (e.g., border or outline with accent color).

**Flashcard in-session:**

- Card itself can be clickable to “flip” (in addition to flip button).
- Flip animation: simple transition (rotate Y or fade/swap text) with ~150–250ms duration.

**Dev rule:**  
Define interactive states in CSS:

- `.card` (base)
- `.card:hover` (hover state)
- `.card:active` (active state)
- `.card:focus-visible` (keyboard focus outline)

And ensure **hover styles only apply** on devices that support hover (use media query if needed).

---

### 2.6 Light Shadows & Depth

Justinmind recommends using light shadowing to create depth and distinguish cards from the background. :contentReference[oaicite:8]{index=8}

**Base style:**

- Background: solid, light (e.g., white or very light grey)
- Border radius: 8–16px (modern look)
- Shadow: subtle (e.g., `0 4px 12px rgba(0,0,0,0.06)`)

**Hover style:**

- Slightly stronger shadow: `0 6px 18px rgba(0,0,0,0.10)`
- Ensure no “heavy” glowing or strongly blurred shadows; keep it clean.

**Dev rule:**  
Define a small elevation system:

- `--elevation-1: 0 4px 12px rgba(0,0,0,0.06);`
- `--elevation-2: 0 6px 18px rgba(0,0,0,0.10);`

And apply via a `.card` and `.card--hover` state.

---

### 2.7 Simple, Readable Typography

Justinmind emphasizes simple, easy-to-read fonts and avoiding overly decorative typefaces. :contentReference[oaicite:9]{index=9}

**Font choice:**

- Use one clean sans-serif family (e.g., Inter, Roboto, System UI).
- Font weights:
    - Title: `600`–`700`
    - Subtitle/body: `400`–`500`
    - Meta: `400` with smaller size

**Sizes (example):**

- Deck title: 18–20px
- Flashcard main term: 28–36px (large, prominent)
- Secondary text: 14–16px
- Meta: 12–14px

**Dev rule:**  
Define a typographic scale in CSS:

- `--font-size-xs: 12px;`
- `--font-size-sm: 14px;`
- `--font-size-md: 16px;`
- `--font-size-lg: 20px;`
- `--font-size-xl: 28px;`
- `--font-size-2xl: 34px;`

Apply consistently across card components.

---

### 2.8 Responsiveness

Justinmind highlights card responsiveness: rearranging from multiple columns to single column on mobile while maintaining hierarchy. :contentReference[oaicite:10]{index=10}

**Deck overview:**

- Desktop: 3–4 columns
- Tablet: 2 columns
- Mobile: 1 column

**Flashcard session:**

- Card width:
    - Desktop: max-width ~480–600px
    - Tablet/mobile: `width: 100%; max-width: 100%; margin: 0 1rem;`
- Maintain comfortable vertical spacing so card doesn’t touch screen edges.

**Dev rule:**  
Use media queries or responsive grid:

```css
.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: var(--space-xl);
}
