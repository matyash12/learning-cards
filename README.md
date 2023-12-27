# Learning cards
Web app for learning using cards.

**You can try it out on [live server](https://www.learn.hajek.live/user/login)**

## Docs
### Running as example (Development)
**Not secure using hardcoded usernames and password** <br/>
You must have docker installed
```
git clone https://github.com/matyash12/learning-cards.git
cd learning-cards
chmod +x build.sh
./build.sh -example
```
### Running using Jenkins (Production)
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
#### Documentation
*Note: autogerated* </br>
[Docs](https://documenter.getpostman.com/view/23011773/2s9YkuXxkm)
#### Postman
You can view and test api on Postman
[Postman public docs](https://elements.getpostman.com/redirect?entityId=23011773-bbce6d9f-fcb5-4de3-9357-f43d438b60d9&entityType=collection)

### PWA
*Progressive Web App*
#### Chrome
Open app and in search bar click on <img width="28" alt="Screenshot 2023-12-27 at 15 57 05" src="https://github.com/matyash12/learning-cards/assets/93146910/b94bba76-0394-4dfb-aa97-85a4c2a28d13">.
#### IOS
1. Click share
2. Scroll down and click `Add to Home Screen`

## Technologies used:
* Spring java
* Docker
* Vue
* Bulma
* Jenkins
* Github actions
* Postman


## Development tip
* Disable Cross-Origin restrictions
