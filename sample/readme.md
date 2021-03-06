## kusor sample

### update: kusor(1) is obsolete, now you should use kusor2 - it is also in 'library' dir, but you should yourself update this sample project using new library (it's just a: copy the jar from build/jar in libs and update manifest file, removing useless novoda sections)

-----

just build the apk, and using

#Kusor

Kusor is Android component providing an efficient geolocalization engine.  

It is just a wrapper for some existing components and strategies, that is:

  -  [Reto Meier's Android Protips: A Deep Dive Into Location](https://code.google.com/p/android-protips-location/)
  -  [novoda's novocation](https://github.com/novoda/Novocation)
  
When I started to work with Android geolocation in 2010, it was not so fast nor efficient to give exact geoposition:  
surely most people know LocationListener issues: long time to retrieve gps location, or low accuracy for network position.  

  
But Reto Meier showed alternative ways to retrieve geoposition: using PendingIntent.
In his [blog](http://android-developers.blogspot.it/2011/06/deep-dive-into-location.html) there are useful tips, howto, and some code examples.  

Unfortunately, his code was useless for reusing, too much full of static variables and so on.  

So, with many endurance, I started to refactor that code, in order to obtain a stand-alone library.  

During work I found, casually, a similar project, [ignition location](https://github.com/mttkay/ignition/tree/master/ignition-location), which attempt my same target.  
Unfortunately (2), it allows to get a fresh location only within an Activity, not from Service: technically, it uses AOP and principal annotation @IgnitedLocation works only within an Activity.  
I tried to modify project, so that works also within Service, but my poor knowledge of AOP stopped me, with no interesting result.  

So, I reprised Meier's code refactoring, until, again casually, I found Novoda's novocation project.  
According to readme, it is heavily inspired to ignition location, but this time there is no AOP behind the scene: it is entirely pure OOP ;D. And, above all, it worked very well, and its code was almost as I would have done


So, the way was just one: some little refactor to novocation, et voilà ladies and gentlemen, this is Kusor.

####Main features are:
- Factory initializations and engine start/stop are wrapped in a single class
- It is dependency injection based, with 2 mandatory parameters handled by IoC container: RoboGuice, in provided example, but you could use your favourite one (Dagger? Needle? Your custom one?)
- new fresh/best/etc location is again broadcasted when kusor receives it, so other components interested to it could get using their broadcastreceiver if they can/want not get it using "getLocation" main method.

Check sample application, and you will see how it is more simpler than read here ;D  
  
Kusor is splitted in 2 jars: the main/core, providing business classes, and -roboguiced one, providing a RoboGuice based module with (abstract) template method you could use to binding annotations/classes to your custom values; example:
<pre>
	install( new PolarisModule() {
		@Override
		protected void bindLocationUpdater() {
			bind(LocationUpdater.class).to(KLocator.class);
		}
		@Override
		protected void bindLocationProvider() {
			bind(LocationProvider.class).to(KLocator.class);
		}
	});
	bind(KLocator.class).in(Singleton.class);
	install( new KusorModule(5*60*1000, 50) );
</pre>
Polaris is a top-interface for Kusor, providing interfaces and utils classes that Kusor uses3; see [here](https://github.com/k0smik0/Polaris).  

--

And oh, why this name? Because Kusor was the Phoenician god of navigation and inventions ;D
