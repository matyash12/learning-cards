# Learning cards
Personal showcase of coding abilities and the creation of a sophisticated learning tool. This application is designed for self-paced learning through interactive learning cards. Each card features a hidden side and a visible side. Assess your knowledge by rating yourself on a scale of 1-5 according to your familiarity with the content. Cards you find challenging will be prioritized for more frequent review, optimizing your learning experience.

**You can try it out on [test server](https://www.learn.hajek.live/user/login)**

## Technologies used:
* Spring java
* Docker
* Vue
* Jenkins
* Github actions
* Postman

## Docs
### Running as example
**Not secure using hardcoded usernames and password** <br/>
You must have docker installed
```
git clone https://github.com/matyash12/learning-cards.git
chmod +x build.sh
./build.sh -example
```
### Running using Jenkins
1. Create new `Pipeline`
2. Set pipeline definition to `Pipeline script from SCM`
3. Set SCM to `Git`
4. Set repository url to `https://github.com/matyash12/learning-cards`
5. Set Branches to build to `main`
6. Add .env to credentials as `LEARNING_CARDS_ENV_FILE` (copy and change .env.example)
7. Add application.properties to credentials as `LEARNING_CARDS_APPLICATION_PROPERTIES` (copy and change backend/src/main/resources/application_example.properties)
8. Add manifest.json to credentials as `LEARNING_CARDS_MANIFESTJSON` (just copy frontend/src/manifest.json)
9. Click run

### API
[Postman public docs](https://elements.getpostman.com/redirect?entityId=23011773-bbce6d9f-fcb5-4de3-9357-f43d438b60d9&entityType=collection)

### Jenkins



## Things to edit or create to run
.env
frontend/manifest.json - set start_url
backend/src/main/resources/application.properties
frontend/src/helpers.js - when running in development. Eg. change api address

