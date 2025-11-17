/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        // Apple HIG inspired colors (using CSS variables for flexibility)
        'system-blue': 'rgb(var(--color-system-blue) / <alpha-value>)',
        'system-gray': 'rgb(var(--color-system-gray) / <alpha-value>)',
        'system-red': 'rgb(var(--color-system-red) / <alpha-value>)',

        'label-primary': 'rgb(var(--color-primary-label) / <alpha-value>)',
        'label-secondary': 'rgb(var(--color-secondary-label) / <alpha-value>)',

        'background-primary': 'rgb(var(--color-primary-background) / <alpha-value>)',
        'background-secondary': 'rgb(var(--color-secondary-background) / <alpha-value>)',

        'separator': 'rgb(var(--color-separator) / <alpha-value>)',
        'fill-tertiary': 'rgb(var(--color-tertiary-fill) / <alpha-value>)',
      },
      fontFamily: {
        sans: ['-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', 'Noto Sans', 'sans-serif', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji'],
        // For SF Pro Text, you would typically rely on -apple-system or include it via a font-face if self-hosting
      },
      borderRadius: {
        'none': '0',
        'sm': '0.25rem', // 4px
        'md': '0.375rem', // 6px
        'lg': '0.625rem', // 10px (common for HIG buttons/cards)
        'xl': '0.75rem', // 12px
        '2xl': '1rem', // 16px
        '3xl': '1.25rem', // 20px
        'full': '9999px',
      },
      boxShadow: {
        // HIG often uses very subtle or no shadows, relying on background contrast
        'sm': '0 1px 0 rgba(0, 0, 0, 0.04)',
        'md': '0 1px 2px rgba(0, 0, 0, 0.08)',
        'lg': '0 2px 4px rgba(0, 0, 0, 0.12)',
        'card': '0 4px 12px rgba(0,0,0,0.06)',
        'card-hover': '0 6px 18px rgba(0,0,0,0.10)',
      }
    },
  },
  plugins: [],
}
