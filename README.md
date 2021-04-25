# AndroidTestApp
First app in android studio

## Compass & Accelerometer App
Individuell uppgift i kursen MAMN01. Appen består av tre aktiviteter:
1. MainActivity
2. CompassActivity
3. AccelerometerActivity

### MainActivity
Projektet inledes med att stegen i [Android Developer: "Build your first app"](https://developer.android.com/training/basics/firstapp/index.html) följdes. 
* Två knappar till de följande aktiviteterna lades till
* Livscykeln i de två aktiviteterna Compass och Accelerator kompletterades med en onPause och onResume för att motverka att de kördes i bakgrundem. [Android Developer: "Activity Lifecycle"](https://developer.android.com/guide/components/activities/activity-lifecycle)
* OnBackPressed metod lades även till i båda aktiviteterna, [Stackoverflow artikel](https://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm-that-the-user-wishes-to-exit-an-android-activity)

### CompassActivity
Stegen i artikeln [Compass Tutorial (Java)](https://www.codespeedy.com/simple-compass-code-with-android-studio/) och 
[Add image to Android Studio](https://www.youtube.com/watch?v=Y7JTkXoN8OE&ab_channel=John%27sAndroidStudioTutorials) följdes för att skapa en simpel kompassfunktion.
Ändringar som sedan gjordes var:
* Sensor och eventlistner lades till för att kunna känna av ändringar i sensorn, [Android Developer Sensor Manager](https://developer.android.com/reference/android/hardware/SensorManager.html)
* En round metod skapades för att visa graden med två decimaler
* Intervall för N, E, S och W skapades och riktningen skrivs ut, vid norr är bokstaven röd och telefonen vibrerar för att ge haptisk feedback, Mediaplayer lades till för att ge auditiv feedback (två olika lägen beroende på hur nära norr man är)
* Behöll även constraints layout i XML filen
* La till lowpass, [Applying Lowpass Filter article](https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings)

### AcceleromterActivity
Stegen i följdes. Sedan gjordes följande ändringar:
* TextView Som uppdaterar X, Y och Z axis samt som beskriver telefonens läge lades till
* Bakgrundsfärg lades till
* Bilder lades till
* MediaPlayer lades till för att ge auditiv feedback [Mediaplayer Tutorial](https://jmsliu.com/2499/play-mp3-in-android-tutorial-android-mediaplayer-example.html)
* En nedräkning lades till de respektive mediaplayer lägena, [Android Developer: "CountDownTimer"](https://developer.android.com/reference/android/os/CountDownTimer.html)
* La till lowpass, [Applying Lowpass Filter article](https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings)
