![](https://img.shields.io/badge/Java_17-yellow?style=for-the-badge)
![](https://img.shields.io/badge/Angular_15-yellow?style=for-the-badge)
![](https://img.shields.io/badge/Spring_boot_3-blueviolet?style=for-the-badge)
![](https://img.shields.io/badge/Spring_Security_6-blue?style=for-the-badge)

![](https://img.shields.io/badge/MongoDB_7.0.5-red?style=for-the-badge)
![](https://img.shields.io/badge/Docker-blue?style=for-the-badge)
![](https://img.shields.io/badge/Apache_Kafka-green?style=for-the-badge)
![](https://img.shields.io/badge/(JWT)_Json_web_token-greenyellow?style=for-the-badge)

# The structure of the project
- **_dev-deployment_** : a project for installing the tools needed to build and launch the entire application. It contains the script to create an image of Kafka and also the script (developed with Jinja) to initialize the `Mongo` DataBase with the collections used in this project.
- **_httpRequests_** : a directory that contains examples of http requests that you can use to test the different APIs developed in the tasksBack project.
- **_integration-tests_** : a project for integration testing (testing with `cucumber`) to test the APIs of the tasksBack project.
- **_taskFront_** : The frontend project with `Angular`.
- **_tasksBack_** : The backend project with `Java` and `SpringBoot`.

# Technologies Used
## BACKEND :
- JAVA 17
- Spring Boot 3.x.x
- Spring Data
- Spring Security 6.x.x
- JWT (Json Web Token)
- Apache KAFKA
- Docker
- Spring Test
- Junit 5 and Mockito 5
- Maven
## FRONTEND :
- Angular 15
- TypeScript
- BootStrap
- Karma
- Jasmine
## DataBase :
- MongoDB 7.0.5
## Prerequisites :
- Intellij or Eclipse
- VsCode
- Java 17 or higher 
- Angular 15 or higher

# Steps to run the project
- Install `MongoDB` on your machine, follow the next [steps](https://osz-technology.blogspot.com/2023/03/how-to-install-mongodb-on-ubuntu-2204.html)
- Create and initialize the `Mongo` DataBase by running the next command :
```sh
    $ ./dev-deployment/docker/mongo/initialize_mongo_db.sh
```
you can change the details about the DataBase name and the user credentials by modifying the file `dev-deployment/docker/mongo/init-mongodb/01_initialize_database_user.js`
- Install `Docker` on your machine.
- Configure the **SMTP GMAIL Server** on your project repository by adding this section to your application.yml file :

```
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: <Login User to SMTP server> (email Address)
    password: <Login password to SMTP server>
    properties:
      mail:
        smtp:
          auth: true
          starttls: true
```

- Install `zookeeper` and `Kafka` by running the next command :
```sh
    $ ./dev-deployment/docker/kafka/install_kafka.sh
```
- Run Backend **tasksBack** and make sure there is no Error.
- Run Frontend **tasksFront**.

## How To Build And Run : TasksBack (backend)
- Go to the project : **TasksProject/tasksBack**.
- Compile the project (without unit tests) : 
```sh
    $ mvn clean install -DskipTests
``` 
- Run the project :
```sh
    $ mvn spring-boot:run
``` 

## How To Build And Run : TaskFront (frontend)
- Go to the project : **TasksProject/taskFront**.
- Install the necessary dependencies with the command:
```sh
    $ npm install
```  
- If everything installed properly, run the next command to start the application (FrontEnd) :
```sh
    $ npm run start:task
```
you can go to this file **taskFront/package.json** to change the command to start the taskFront project.
- If you want to run the unit tests you can use the following command :
```sh
    $ npm run test:task 
```
you can go to this file **taskFront/package.json** to change the command to test the taskFront project.
- Open browser and go to http://localhost:4200
- port 4200 is default, you can change with command --port to change the port.


## IDE configuration

### VSCode

#### Recommended Extensions for VSCode

- "ms-vscode.vscode-typescript-tslint-plugin" : for more information: // TsLinst, https://marketplace.visualstudio.com/items?itemName=ms-vscode.
- "esbenp.prettier-vscode" : for more information: // Code formater Prettier, https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode
- "sibiraj-s.vscode-scss-formatter" : for more information: // SCSS Formatter, https://marketplace.visualstudio.com/items?itemName=sibiraj-s.vscode-scss-formatter
- "msjsdiag.debugger-for-chrome": for more information: // Debbuger for Chrome, https://marketplace.visualstudio.com/items?itemName=msjsdiag.debugger-for-chrome
- "firefox-devtools.vscode-firefox-debug" : for more information: // Debbuger for Firefox, https://marketplace.visualstudio.com/items?itemName=firefox-devtools.vscode-firefox-debug


#### VSCode settings

Bellow, an example to manage code format for Visual studio code using `settings.json` file:

```json
{
  "editor.formatOnSave": true,
  "[typescript]": {
    "editor.codeActionsOnSave": {
      "source.fixAll.tslint": "explicit",
      "source.organizeImports": "explicit"
    },
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
  "[html]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
  "[scss]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
  "search.exclude": {
    "**/node_modules": true,
    "**/dist": true
  },

  "editor.snippetSuggestions": "top",
  "editor.formatOnPaste": true,
  "typescript.updateImportsOnFileMove.enabled": "always",
  "files.autoSave": "onFocusChange",
  "prettier.singleQuote": true,
  "javascript.preferences.quoteStyle": "single",
  "typescript.preferences.quoteStyle": "single",
  "breadcrumbs.enabled": true,
  "javascript.format.insertSpaceAfterKeywordsInControlFlowStatements": false,
  "typescript.format.insertSpaceAfterKeywordsInControlFlowStatements": false,
  "[xml]": {
    "editor.defaultFormatter": "fabianlauer.vs-code-xml-format"
  },
  "[yaml]": {
    "editor.quickSuggestions": {
      "other": true,
      "comments": false,
      "strings": true
    }
  },
  "[json]": {
    "editor.quickSuggestions": {
      "strings": true
    },
    "editor.suggest.insertMode": "replace"
  },
  "json.schemas": [],
  "typescript.npm": "",
  "[css]": {
    "editor.defaultFormatter": "sibiraj-s.vscode-scss-formatter"
  },
  "editor.codeActionsOnSave": {},
  "rest-client.defaultHeaders": {
    "User-Agent": "vscode-restclient"
  }
}
```

To check that all your FrontEnd files are formatted :

```sh
    $ npm run prettier:check 
```

To format all your FrontEnd files you can run this command :

```sh
    $ npm run prettier:fix 
```