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

### CompassActivity
Stegen i artikeln [Compass Tutorial (Java)](https://www.codespeedy.com/simple-compass-code-with-android-studio/) och 
[Add image to Android Studio](https://www.youtube.com/watch?v=Y7JTkXoN8OE&ab_channel=John%27sAndroidStudioTutorials) följdes för att skapa en simpel kompassfunktion.
Ändringar som sedan gjordes var:
* Sensor och eventlistner lades till för att kunna känna av ändringar i sensorn, [Android Developer Sensor Manager](https://developer.android.com/reference/android/hardware/SensorManager.html)
* En round metod skapades för att visa graden med två decimaler
* Intervall för N, E, S och W skapades och riktningen skrivs ut, vid norr är bokstaven röd och telefonen vibrerar för att ge haptisk feedback
* Behöll även constraints layout i XML filen

### AcceleromterActivity
Stegen i följdes. Sedan gjordes följande ändringar:
* TextView Som uppdaterar X, Y och Z axis samt som beskriver telefonens läge lades till
* Bakgrundsfärg lades till
* Bilder lades till
* MediaPlayer lades till för att ge auditiv feedback [Mediaplayer Tutorial](https://jmsliu.com/2499/play-mp3-in-android-tutorial-android-mediaplayer-example.html)
