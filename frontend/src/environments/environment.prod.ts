export const environment = {
  production: true,

  supabaseUrl: import.meta.env['NG_APP_SUPABASE_URL'] ?? '',
  supabaseKey: import.meta.env['NG_APP_SUPABASE_KEY'] ?? '',
};