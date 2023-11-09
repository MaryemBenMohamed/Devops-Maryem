FROM node:16.16-alpine AS build

WORKDIR /dist/src/app

COPY package*.json ./

RUN npm install --force

COPY . .

RUN npm run build

EXPOSE 4200

CMD ["npm", "start"]
