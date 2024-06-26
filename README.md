# Learning cards
Web app for learning using cards.

**You can try it out on [live server](https://www.learn.hajek.live/user/login)**

## Screenshots
<img src="https://github.com/matyash12/learning-cards/assets/93146910/1e221cdd-057f-41c3-a893-1039492ec759" width="300">
<img src="https://github.com/matyash12/learning-cards/assets/93146910/0c748bae-9140-417a-a29e-97b39b1db5d7" width="300">
<img src="https://github.com/matyash12/learning-cards/assets/93146910/258fd7b3-0e90-45ec-832e-99a348fb9358" width="300">



## Docs
### Running as example (Development)
**Not secure using hardcoded usernames and password** <br/>
You must have docker installed
For it to work on localhost it's best to disable cors restriction.
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
* Spring boot, security
* Docker
* Vue
* Bulma
* Jenkins
* Github actions
* Postman

## Note
* I had to learn many technologies on the way.
* Passwords are stored hashed using salt (Unique salt for each password).
* If account managment is slow try to adjust hashing algorithm (PasswordUtils.java)


## Development tip
* Disable Cross-Origin restrictions
