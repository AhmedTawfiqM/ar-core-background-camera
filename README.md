ARCoreBackgroundSession is ARCore tool that use camera in background to notify real-time updates about current locations including translations and rotations.

- complete code in just a single class ? pick up this gist
https://gist.github.com/AhmedTawfiqM/9b5376ce4c17904bcafd04b74755c7b1

Sample in Real World 

https://github.com/AhmedTawfiqM/ar-core-background-camera/assets/26546253/6aa1b4fa-89aa-4c36-9735-9a51345faff3

after import OpenCV SDK https://opencv.org/releases/
- u must resolve build errors by 2 steps 
- 1- delete setting.gradle file which had been generated by open cv import
- 2- migrate to java 18 and add
- kotlinOptions {
  jvmTarget = "1.8"
  }
- 3- 
    

