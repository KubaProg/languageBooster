require('dotenv').config();
const fs = require('fs');
const path = require('path');

// For production environment
const envProdFile = `
export const environment = {
  production: true,
  apiUrl: '${process.env.NG_APP_API_URL || ''}',
  supabaseUrl: '${process.env.NG_APP_SUPABASE_URL || ''}',
  supabaseKey: '${process.env.NG_APP_SUPABASE_KEY || ''}',
};
`;

const targetPathProd = path.join(__dirname, './src/environments/environment.prod.ts');

fs.writeFile(targetPathProd, envProdFile, (err) => {
  if (err) {
    console.error(err);
    throw err;
  } else {
    console.log(`Successfully generated environment.prod.ts`);
  }
});

// For development environment (to avoid process.env errors during ng serve)
const envDevFile = `
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080', // Default for local development
  supabaseUrl: '${process.env.NG_APP_SUPABASE_URL || ''}', // Can be overridden by local .env or build system
  supabaseKey: '${process.env.NG_APP_SUPABASE_KEY || ''}', // Can be overridden by local .env or build system
};
`;

const targetPathDev = path.join(__dirname, './src/environments/environment.ts');

fs.writeFile(targetPathDev, envDevFile, (err) => {
  if (err) {
    console.error(err);
    throw err;
  } else {
    console.log(`Successfully generated environment.ts`);
  }
});
