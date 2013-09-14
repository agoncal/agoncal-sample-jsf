# Sample - JSF - Video

## Purpose of this sample

The purpose of this sample is to show how you can upload a video with JSF and watch it with HTML 5

## Compile and package

Being Maven centric, you can compile and package it with `mvn clean compile`, `mvn clean package` or `mvn clean install`. The `package` and `install` phase will automatically trigger the unit tests. Once you have your war file, you can deploy it.

## Deploy the sample

This sample has been tested with GlassFish 4.0 in several modes :

* GlassFish runtime : [download GlassFish](http://glassfish.java.net/public/downloadsindex.html), install it, start GlassFish (typing `asadmin start-`domain) and once the application is packaged deploy it (using the admin console or the command line `asadmin deploy target/sampleJSFVideo.war`)
* GlassFish embedded : use the [GlassFish Maven Plugin](http://maven-glassfish-plugin.java.net/) by running `mvn clean package embedded-glassfish:run`

## Execute the sample

Once deployed go to [http://localhost:8080/sampleJSFVideo](http://localhost:8080/sampleJSFVideo) and you'll see a web page where you can:

* Watch the list of videos
* Edit a video and upload a file
* Visualise the video

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>
