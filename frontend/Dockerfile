# Stage 1: Build
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Runtime (SSR)
FROM node:20-alpine
WORKDIR /app
# Copy the built SSR server and browser assets
COPY --from=build /app/dist/frontend ./dist/frontend
EXPOSE 4000
CMD ["node", "dist/frontend/server/server.mjs"]
