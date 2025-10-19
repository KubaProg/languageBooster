const fs = require('fs');
const path = require('path');

const envFile = `
export const environment = {
  production: true,
  supabaseUrl: '${process.env.NG_APP_SUPABASE_URL}',
  supabaseKey: '${process.env.NG_APP_SUPABASE_KEY}',
};
`;

const targetPath = path.join(__dirname, './src/environments/environment.prod.ts');

fs.writeFile(targetPath, envFile, (err) => {
  if (err) {
    console.error(err);
    throw err;
  } else {
    console.log(`Successfully generated environment.prod.ts`);
  }
});
