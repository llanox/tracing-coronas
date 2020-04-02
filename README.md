# What's Tracing Coronas?

Sometimes It's hard to meet social distancing or physical distancing, so if we need to go out to do something vital, then TracingCoronas allows us to keep a trace of people who had close contact with you. So, if any of these persons get symptoms of COVID-19, health care authorities can know if you or someone else was exposed to that infected person and take control of that situation, isolating the affected people and slowing the spread of the COVID-19.

# How to Run the App

 1. Clone the repo
```
git clone git@github.com:googlesamples/android-architecture.git
```
 2. Open the project with Android Studio 4.+
 3. Create a Firestore database. See [here](https://firebase.google.com/docs/firestore/quickstart#create)
 4. After that process of database creation and configuration, you get a generated file google-services.json that you need to put into the app folder.
 5. Build and Deploy.

# How to it works?

Tracing Coronas has 3 screens: Registration, Permissions, and Monitoring.

Registration: It stores basic info about the person who's using the app, so the authorities can reach him/her if there's a possibility of infection on the person.

![Registration Screen](/screenshots/registration.jpeg)


Permissions: Here, the user must grant permission to get access to the Bluetooth hardware and also enable the location services.

![Granting Permissions Screen](/screenshots/permissions.jpeg)


Monitoring: The app is scanning and identifying devices running that same application, then it can collect information to know how close were these people with that app around you.

![Monitoring Screen](/screenshots/monitoring.jpeg)

Also, in the monitoring screen, there's a debug window to see on the fly the information that's collected by the application. You can enable/disable with a long touch on the screen title.

![Monitoring Screen - Debug Window](/screenshots/debug_monitoring.jpeg)



# TODO

- [ ] Finish up the documentation
- [ ] Test on a wide range of devices.
- [ ] Figure out how to make a query in the firestore database to find people who were close to a specific person.
- [ ] The app is sending much information to the server, and that's quickly exceeding the free quota of the firebase database. Is out there any database with a bigger free quota?


### License


```
MIT License

Copyright (c) 2020 Juan Gabriel Gutierrez

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
