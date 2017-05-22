# Pet management system

<ol>
    <li><a href="#design">Design choices</a></li>
    <li><a href="#requirements">Requirements</a></li>
    <li><a href="#instructions">Running instructions</a></li>
</ol>

**<a name="design"><h2>Design choices</h2></a>**
<ul>
    <li>An <b>MVC architecture</b> is used for the project, exposing a <b>RESTful API</b> so it can be used by the console application</li>
    <li><b>Play framework was chosen</b>, because of its simplicity compared to frameworks based in Java EE</li>
    <li>Play doesn't need and application server, it includes an <b>embedded HTTP server, based in Netty</b>, which supports non-blocking IO, this makes Play more efficient in terms of threads usage</li>
    <li><b>SBT</b> is used as the build and dependencies management tool for this project, this is because Play framework has APIs for Java and Scala, so SBT is supported by Play by default</li>
    <li><b>EBean</b> was the choice for ORM because of its great integration with the Play framework</li>
    <li>For storing information about pets, an <b>H2 in-memory database</b> is used, because Play includes H2 driver libraries by default</li>
    <li>For better performance, results for search request are cached, using <b>Play's cache API</b>, that is based in EHCache, this implies that data obtained in searches will not always be synchronized with the database, in a real application caching time must be tuned to achieve optimum balance</li>
    <li>For better performance, search methods in PetRESTController are <b>asynchronous</b>, database queries are executed in separate threads</li>
    <li>Play's <b>evolution script</b> is used to create the PET table in the H2 database when the application starts</li>
    <li>Routes are configured in app/conf/routes file</li>
    <li><b>JUnit</b> was the chosen framework for testing</li>
    <li>Searches are case insensitive</li>
</ul>

**<a name="requirements"><h2>Requirements</h2></a>**
<ul>
    <li>JDK 8, you can get it from <a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html" target="_blank">here</a></li>
    <li>SBT, you can find installation instructions for Windows, Linux or Mac <a href="http://www.scala-sbt.org/0.13/docs/Setup.html" target="_blank">here</a></li>
</ul>

**<a name="instructions"><h2>Running instructions</h2></a>**
<ul>
    <li>For running the application in development mode, just issue the command <code>sbt run</code> in the project's root directory, where the build.sbt file resides (if sbt is not included in system path, execute <code>/full/path/to/sbt run</code>)</li>
    <li>The first time the application runs, SBT will download automatically all the dependencies</li>
    <li>The first time the application receives a request, as the application is in development mode, the code is automatically compiled</li>
    <li>For running tests for the application, just issue the command <code>sbt test</code> in the project's root directory (if sbt is not included in system path, execute <code>/full/path/to/sbt test</code>)</li>
    <li>Once the application is started, it listens on port 9000 expecting for requests</li>
</ul>
