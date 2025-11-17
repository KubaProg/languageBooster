# AI Rules for language-booster frontend 

## FRONTEND

### Guidelines for ANGULAR

#### ANGULAR_CODING_STANDARDS

- Use standalone components, directives, and pipes instead of NgModules
- Implement signals for state management instead of traditional RxJS-based approaches
- Use the new inject function instead of constructor injection
- Implement control flow with @if, @for, and @switch instead of *ngIf, *ngFor, etc.
- Leverage functional guards and resolvers instead of class-based ones
- Use the new deferrable views for improved loading states
- Implement OnPush change detection strategy for improved performance
- Use TypeScript decorators with explicit visibility modifiers (public, private)
- Leverage Angular CLI for schematics and code generation
- Implement proper lazy loading with loadComponent and loadChildren

### Guidelines for STYLING

#### TAILWIND

**Configuration & Theming**
- Implement the Tailwind configuration file (`tailwind.config.js`) for customizing theme, plugins, and variants.
- Define a consistent spacing scale in `theme.extend.spacing` to be used for padding, margins, and gaps. Prefer theme values (e.g., `p-4`) over arbitrary values to maintain design consistency.
- Establish a clear typographic scale in `theme.extend.fontSize` and a clean, sans-serif font family to ensure readable and consistent text.
- Define a subtle elevation system using custom shadow styles in `theme.extend.boxShadow` (e.g., `shadow-card`, `shadow-card-hover`) for consistent depth.
- Use the `theme()` function in CSS for accessing Tailwind theme values when writing custom CSS.

**Layout & Structure**
- Use the `@layer` directive to organize custom styles into `base`, `components`, and `utilities` layers.
- For card grids, use CSS Grid with responsive column definitions (e.g., `grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3`). For fluid grids, consider using arbitrary values for `grid-template-columns`.
- Use responsive variants (`sm:`, `md:`, `lg:`, etc.) for creating adaptive designs that work across all screen sizes.

**Component Styling & Interactivity**
- Leverage the `@apply` directive sparingly in component classes to reuse combinations of utilities for common UI elements. Prefer creating well-structured components.
- Use component extraction for repeated UI patterns instead of copying utility classes.
- Apply interactive styles using state variants like `hover:`, `focus:`, `active:`.
- Ensure keyboard focus is always visible by using `focus-visible:` to apply distinct outline, border, or shadow styles.
- Implement dark mode with the `dark:` variant where applicable.

**Development & Optimization**
- Implement Just-in-Time (JIT) mode for development efficiency and smaller CSS bundles.
- Use arbitrary values with square brackets (e.g., `w-[123px]`) only for precise, one-off designs that are not covered by the theme.

### Guidelines for ACCESSIBILITY

#### MOBILE_ACCESSIBILITY

- Ensure touch targets are at least 44 by 44 pixels for comfortable interaction on mobile devices
- Implement proper viewport configuration to support pinch-to-zoom and prevent scaling issues
- Design layouts that adapt to both portrait and landscape orientations without loss of content
- Support both touch and keyboard navigation for hybrid devices with {{input_methods}}
- Ensure interactive elements have sufficient spacing to prevent accidental activation
- Test with mobile screen readers like VoiceOver (iOS) and TalkBack (Android)
- Design forms that work efficiently with on-screen keyboards and autocomplete functionality
- Implement alternatives to complex gestures that require fine motor control
- Ensure content is accessible when device orientation is locked for users with fixed devices
- Provide alternatives to motion-based interactions for users with vestibular disorders

## TESTING FRONTEND

### Guidelines for UNIT

#### JEST

- Use Jest with TypeScript for type checking in tests
- Implement Testing Library for component testing instead of enzyme
- Use snapshot testing sparingly and only for stable UI components
- Leverage mock functions and spies for isolating units of code
- Implement test setup and teardown with beforeEach and afterEach
- Use describe blocks for organizing related tests
- Leverage expect assertions with specific matchers
- Implement code coverage reporting with meaningful targets
- Use mockResolvedValue and mockRejectedValue for async testing
- Leverage fake timers for testing time-dependent functionality


