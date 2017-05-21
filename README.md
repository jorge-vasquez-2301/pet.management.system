# pet.management.system

<a href="#design">Design choices</a>
<br>
<a href="#requirements">Requirements</a>

**<a name="design"><h2>Design choices</h2></a>**
<ul>
</ul>
<li>An <b>MVC architecture</b> is used for the project, exposing a <b>RESTful API</b> so it can be used by the console application</li>
<li><b>Play framework was chosen</b>, because of its simplicity compared to frameworks based in Java EE</li>
<li>Play doesn't need and application server, it includes an <b>embedded HTTP server, based in Netty</b>, which supports non-blocking IO, this makes Play more efficient in terms of threads usage</li>
<li><b>SBT</b> is used as the build and dependencies management tool for this project, this is because Play framework has APIs for Java and Scala, so SBT is supported by Play by default</li>
<li><b>EBean</b> was the choice for ORM because of its great integration with the Play framework</li>
<li>For storing information about pets, an <b>H2 in-memory database</b> is used, because Play includes H2 driver libraries by default</li>
<li>For better performance, results for search request are cached, using <b>Play's cache API</b>, that is based in EHCache</li>
<li>For better performance, search methods in PetRESTController are <b>asynchronous</b>, database queries are executed in separate threads</li>
<li>Play's <b>evolution script</b> is used to create the PET table in the H2 database when the application starts</li>
<li><b>JUnit</b> was the chosen framework for testing</li>
<li>Searches are case insensitive</li>

**<a name="requirements"><h2>Requirements</h2></a>**
- JDK 8
- SBT
