import { defineStore } from 'pinia'


/*
EXAMPLE ON HOW TO USE
----
import { notificationStore } from '@/stores/notification.js'; 
const store = notificationStore();
store.newNotification("Login was successful",false,"is-success",3);
----


*/
// main is the name of the store. It is unique across your application
// and will appear in devtools
export const notificationStore = defineStore('main', {
  // a function that returns a fresh state
  state: () => ({
    visible: false,
    message: "Message!",
    bulmaClass: "is-danger",
    currentNotificationId: 0,
    showCloseButton: true,
    /*
    Current notification helps when we create more than one. 
    So the second one wont be closed by the firs'ts time
    */
  }),
  actions: {
    newNotification(message,showCloseButton, bulmaClass, timeToCloseSeconds) {
      this.visible = true;
      this.message = message;
      this.bulmaClass = bulmaClass;
      this.showCloseButton = showCloseButton;
      this.currentNotificationId++;
      const currentNotificationId = this.currentNotificationId;

      setTimeout(() => {

        if (currentNotificationId == this.currentNotificationId) {
          this.visible = false;
          console.log("End of timer and closing notification");
        } else {
          console.log("End of timer but different id");
        }
      }, timeToCloseSeconds * 1000);


    },
    hideNotification() {
      this.visible = false;
    }
  },

})