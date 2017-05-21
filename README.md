# pet.management.system

**<h2>Design choices</h2>**
<ul>
</ul>
<li>An MVC architecture is used for the project, exposing a RESTful API so it can be used by the console application</li>
<li>Play framework was chosen, because of its simplicity compared to frameworks based in Java EE/li>
<li>Play doesn't need and application server, it includes an embedded HTTP server, based in Netty, which supports non-blocking IO, this makes Play more efficient in terms of threads usage</li>
<li>SBT is used as the build and dependencies management tool for this project, this is because Play framework has APIs for Java and Scala, so SBT is supported by Play by default</li>
<li>EBean was the choice for ORM because of its great integration with the Play framework</li>
<li>For storing information about pets, an H2 in-memory database is used, because Play includes H2 driver libraries by default</li>
<li>For better performance, results for search request are cached, using Play's cache API, that is based in EHCache</li>
<li>For better performance, search methods in PetRESTController are asynchronous, database queries are executed in separate threads</li>
<li>Play's evolution script is used to create the PET table in the H2 database when the application starts</li>
<li>Searches are case insensitive</li>
<li>JUnit was the chosen framework for testing</li>

**<h2>Requirements</h2>**
- JDK 8
- SBT
