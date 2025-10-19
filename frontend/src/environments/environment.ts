export const environment = {
  production: false,
  supabaseUrl: process.env["NG_APP_SUPABASE_URL"] ?? '',
  supabaseKey: process.env["NG_APP_SUPABASE_KEY"] ?? ''
};
